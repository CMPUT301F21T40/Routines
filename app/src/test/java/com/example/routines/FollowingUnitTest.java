package com.example.routines;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;


public class FollowingUnitTest {
    HashMap<String, String> dataAccepted;
    HashMap<String, String> dataDeclined;
    String status;

    @BeforeEach
    public void createNotification(){ // create a new mock habit list for every test
        dataAccepted = new HashMap<>();
        dataAccepted.put("Sender Name", "john123");
        dataAccepted.put("Sender", "123");
        dataAccepted.put("Receiver Name", "emma565");
        dataAccepted.put("Receiver", "565");
        dataAccepted.put("Status", "accepted");

        dataDeclined = new HashMap<>();
        dataDeclined.put("Sender Name", "john123");
        dataDeclined.put("Sender", "123");
        dataDeclined.put("Receiver Name", "emma565");
        dataDeclined.put("Receiver", "565");
        dataDeclined.put("Status", "pending");
    }

    @Test
    public void checkFollowingPass() {
        status = dataAccepted.get("Status");
        assertEquals("accepted", status);
    }

    @Test
    public void checkFollowingFail() {
        status = dataDeclined.get("Status");
        assertEquals("pending", status);
    }


}
