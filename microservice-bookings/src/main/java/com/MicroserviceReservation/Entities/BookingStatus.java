package com.MicroserviceReservation.Entities;

public enum BookingStatus {
    Confirmed(1), // Representa "Completed"
    Pending(2),   // Representa "Pending"
    Cancelled(3); // Representa "Cancelled"

    private final int value;

    BookingStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BookingStatus fromValue(int value) {
        for (BookingStatus status : BookingStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }
}
