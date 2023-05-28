package com.example.mockpaymentservice.kafka;

import com.example.movieservice.entities.Booking;
import com.example.movieservice.entities.BookingStatus;
import com.example.movieservice.repository.BookingRepository;
import com.example.movieservice.repository.PriceRepository;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.apache.kafka.clients.admin.NewTopic;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/v1/api")
public class BookingConsumer {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RazorpayClient razorpayClient;
    @Autowired
    private PriceRepository priceRepository;

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(50);
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

    private static final Logger LOGGER= LoggerFactory.getLogger(BookingConsumer.class);
    private NewTopic topic;
    private KafkaTemplate<String, Booking> kafkaTemplate;

    public BookingConsumer(NewTopic topic, KafkaTemplate<String, Booking> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/rePayment")
    public void rePayment(@RequestBody Booking booking) throws RazorpayException, JSONException {
        paymentMethod(booking);
    }

    @KafkaListener(topics="${spring.kafka.topic.name}",groupId="${spring.kafka.consumer.group-id}")
    public void consume(@Payload Booking booking) throws RazorpayException, JSONException {
        LOGGER.info(String.format("Proceed the payment for the seat=> %s", booking.toString()));
        startBgThread(booking);
        paymentMethod(booking);

    }
    private void paymentMethod(Booking booking) throws RazorpayException, JSONException{
        boolean result=razorpayPayment(booking);
        booking.setPaymentStatus(result);
        bookingRepository.save(booking);
        if(result){
            booking.getSeat().setStatus(BookingStatus.BOOKED);
            bookingRepository.save(booking);
            stopBgThread(booking.getSeat().getId());
        }
        Message<Booking> message= MessageBuilder.withPayload(booking).setHeader(KafkaHeaders.TOPIC,topic.name()).build();
        kafkaTemplate.send(message);
    }

    private void startBgThread(Booking booking) {
        ScheduledFuture<?> scheduledFuture = executorService.schedule(() -> {
            if (booking.getSeat().getStatus()!=BookingStatus.BOOKED) {
                booking.getSeat().setStatus(BookingStatus.AVAILABLE);
                bookingRepository.save(booking);
            }
        }, 10, TimeUnit.MINUTES);
        scheduledTasks.put(booking.getSeat().getId(), scheduledFuture);
    }

    private boolean razorpayPayment(Booking booking) throws JSONException{
        List<Booking> bookingList=booking.getUser().getBookings();
        double totalAmount = 0.0;
        for (Booking b : bookingList) {
            totalAmount+=priceRepository.findByShowIdAndSeatTypeId(b.getShow().getShowId(),b.getSeat().getSeatType().getSeatTypeId()).getPrice();
        }
        JSONObject options = new JSONObject();
        options.put("amount", totalAmount * 100);
        options.put("currency", "INR");
        options.put("receipt", booking.getId());
        RazorpayOrder order = razorpayClient.Orders.create(options);
        String paymentId = order.get("id");
        Payment payment = razorpayClient.Payments.fetch(paymentId);
        int paymentAmount = payment.get("amount");
        String paymentCurrency = payment.get("currency");
        return paymentAmount == totalAmount * 100 && paymentCurrency.equalsIgnoreCase("INR");
        return false;

    }

    private void stopBgThread(Long id) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(id);
        if (scheduledFuture != null) {
            boolean cancelled = scheduledFuture.cancel(true);
            if (cancelled) {
                scheduledTasks.remove(id);
            }
        }
    }

}