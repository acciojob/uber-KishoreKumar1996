package com.driver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="Cab")
@Data
@AllArgsConstructor
public class Cab {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int CabId;

    private int perKMHr;
    private boolean seatBooked;

    public boolean isSeatBooked() {
        return seatBooked;
    }

    public void setSeatBooked(boolean seatBooked) {
        this.seatBooked = seatBooked;
    }



    public Cab() {
    }

    @OneToOne(mappedBy = "cab",cascade = CascadeType.ALL)
    private Driver driver;
}