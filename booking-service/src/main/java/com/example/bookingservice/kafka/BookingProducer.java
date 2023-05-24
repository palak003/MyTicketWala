package com.example.bookingservice.kafka;

import com.example.movieservice.entities.Booking;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class BookingProducer {

    private static final Logger LOGGER= LoggerFactory.getLogger(BookingProducer.class);
    private NewTopic topic;

    private KafkaTemplate<String, Booking> kafkaTemplate;

    public BookingProducer(NewTopic topic, KafkaTemplate<String, Booking> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

   
  @KafkaListener(topics="${spring.kafka.topic1.name}",groupId="${spring.kafka.consumer.group-id}")
    public void consume(SeatEvent seatEvent){
        LOGGER.info(String.format("Payment is=?%s",seatEvent));
        if(Objects.equals(seatEvent.getMessage(), "success")){
            Optional<Seat> seat=seatRepository.findById(seatEvent.getSeat().getSeatId());
                    seat.get().setStatus(SeatStatus.BOOKED);
            seatRepository.save(seat.get());
        }
        else{
            //run the thread only if the seat is still reserved and not booked
            startTaskAfter5Min(seatEvent);
        }
        LOGGER.info("A mail has been sent saying that the booking of seat is %s",seatEvent.getMessage());
        /*if pass
        seat book => send success notification (midst timer)
        if fail
        send failure notification (send mail)

}
 public void startTaskAfter5Min(SeatEvent seatEvent) {

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();

        // Start the thread after 5 minutes
        scheduler.schedule(() -> {
            // Your task code goes here
            Optional<Seat> seat=seatRepository.findById(seatEvent.getSeat().getSeatId());
           if(seat.get().getStatus()!=SeatStatus.BOOKED) {
               seat.get().setStatus(SeatStatus.AVAILABLE);
               seatRepository.save(seat.get());
           }
      LOGGER.info("this task has been called");
        }, new PeriodicTrigger(Duration.ofMinutes(1).toMillis()));

        // Sleep the main thread for some time to let the background task run
        try {
            Thread.sleep(90000); // 1 minute
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Shut down the scheduler when the task is completed
        scheduler.shutdown();
    

    }

    
}

