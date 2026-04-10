package com.example.cinemaapp;

import com.example.cinemaapp.model.Seat;
import org.junit.Test;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrencyTest {

    @Test
    public void testConcurrentSeatBooking() throws InterruptedException {
        // 1. Initialize the shared resource: Seat A1.
        Seat targetSeat = new Seat("A1");

        // 2. Create a Thread Pool to simulate multiple concurrent users.
        int numberOfUsers = 5;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfUsers);

        System.out.println("--- Starting JUnit concurrent booking test for Seat " + targetSeat.getSeatId() + " ---");

        // 3. Simulate multiple users initiating booking requests simultaneously.
        for (int i = 1; i <= numberOfUsers; i++) {
            final String userName = "User-" + i;
            executor.submit(() -> {
                System.out.println(userName + " is attempting to book...");
                targetSeat.bookSeat(userName);
            });
        }

        // 4. Gracefully shut down the executor service.
        executor.shutdown();

        // Block until all tasks have completed execution
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("--- Test completed ---");
    }
}