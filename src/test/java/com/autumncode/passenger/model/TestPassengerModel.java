package com.autumncode.passenger.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;

public class TestPassengerModel {
    private SessionFactory factory = null;

    @BeforeClass
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    void populateModel() {
        Object[][] model={
                {"Joe", new Object[]{"Atlanta", "Boston", "Philadelphia", "New York"}},
                {"Tracy", new Object[]{"Paris", "New York", "Charlotte", "Detroit", "Boston"}},
                {"Justin", new Object[]{"Denver", "New York"}},
        };
        try (Session session = factory.openSession()) {
            Transaction tx=session.beginTransaction();
            for(Object[] data:model) {
                Passenger passenger=new Passenger();
                passenger.setName(data[0].toString());
                session.save(passenger);
                for(Object destination:(Object[])data[1]) {
                    Trip t=new Trip();
                    t.setDestination(destination.toString());
                    t.setPassenger(passenger);
                    session.save(t);
                }
            }
            for(String unattached:new String[]{"Los Angeles", "Des Moines", "Raleigh"}) {
                Trip t=new Trip();
                t.setDestination(unattached);
                session.save(t);
            }
            tx.commit();
        }
    }
    @Test
    public void testModel() {
        populateModel();
        try (Session session = factory.openSession()) {
            Transaction tx=session.beginTransaction();
            Query<Trip> tripQuery=session.createQuery("select t from Trip t where t.passenger is not null", Trip.class);
            for(Trip t:tripQuery.list()) {
                System.out.println(t);
            }
            Query<Object[]> sumQuery=session.createQuery("select t.destination, count(t) as tcount from Trip t " +
                    "where t.passenger is not null " +
                    "group by t.destination " +
                    "order by tcount desc", Object[].class);
            sumQuery.setMaxResults(2);
            for(Object[] r:sumQuery.list()) {
                System.out.println(Arrays.toString(r));
            }
            tx.commit();
        }
    }
}
