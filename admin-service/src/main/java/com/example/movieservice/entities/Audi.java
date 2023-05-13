package com.example.movieservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audi")
public class Audi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audi_id")
    private Long audiId;

    @Column(name = "audi_name")
    private String audiName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_hall_id")
    private Cinema cinema;

    @OneToMany(mappedBy = "audi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Show> shows;

}