package com.bayztracker.utils;

public enum Status {
    NEW, TRIGGERED, ACKED, CANCELLED;

    public static boolean existsByName(String name) {
        for (Status status : Status.values()) {
            if (status.name().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}