package com.driver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="Cab")
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

    public int getCabId() {
        return CabId;
    }

    public void setCabId(int cabId) {
        CabId = cabId;
    }

    public int getPerKMHr() {
        return perKMHr;
    }

    public void setPerKMHr(int perKMHr) {
        this.perKMHr = perKMHr;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}