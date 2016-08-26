package com.autumncode.passenger.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Trip extends BaseEntity {
    @Getter
    @Setter
    String destination;
    @ManyToOne
            @Getter @Setter
    Passenger passenger;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Trip{");
        sb.append("id=").append(id).append(',');
        sb.append("destination='").append(destination).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
