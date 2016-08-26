package com.autumncode.passenger.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class Passenger extends BaseEntity {
    String name;
    @OneToMany(mappedBy ="passenger",fetch = FetchType.LAZY)
    List<Trip> trips;
}
