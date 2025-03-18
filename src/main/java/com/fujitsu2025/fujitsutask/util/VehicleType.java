package com.fujitsu2025.fujitsutask.util;

/**
 * Enum for allowed vehicle types.
 */
public enum VehicleType {
    CAR, SCOOTER, BIKE;

    /**
     * Converts a string to VehicleType.
     *
     * @param vehicleType - User input vehicle type.
     * @return - Corresponding VehicleType.
     * @throws IllegalArgumentException if the vehicle type is invalid.
     */
    public static VehicleType fromString(String vehicleType) {
        try {
            return VehicleType.valueOf(vehicleType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid vehicle type. Allowed values: Car, Scooter, Bike");
        }
    }
}

