package com.example.cinemaapp.model;

import java.util.concurrent.locks.ReentrantLock;

public class Seat {
    private String seatId;            // Seat identifier, e.g., "A1"
    private boolean isBooked;         // Booking status flag
    private final ReentrantLock lock; // Exclusive lock for this specific seat instance

    public Seat(String seatId) {
        this.seatId = seatId;
        this.isBooked = false;
        this.lock = new ReentrantLock();
    }

    /**
     * Core thread-safe method for seat booking.
     * Prevents race conditions during concurrent booking requests.
     */
    public boolean bookSeat(String userName) {
        // 1. Acquire the lock.
        // If another thread holds the lock, the current thread will block here.
        lock.lock();
        try {
            // 2. Check seat availability
            if (!isBooked) {
                // Simulate network latency or database write time.
                // Critical: This is where race conditions would occur without synchronization.
                Thread.sleep(50);

                // Booking successful, update state
                isBooked = true;
                System.out.println(userName + " successfully booked seat: " + seatId);
                return true;
            } else {
                System.out.println(userName + " failed. Seat " + seatId + " is already taken.");
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } finally {
            // 3. Release the lock.
            // MUST be placed in a finally block to ensure the lock is released
            // even if an exception is thrown, preventing potential deadlocks.
            lock.unlock();
        }
    }

    public String getSeatId() {
        return seatId;
    }
}
