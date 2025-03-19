package com.fujitsu2025.fujitsutask;

import com.fujitsu2025.fujitsutask.util.VehicleType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VehicleTypeTest {
    @Test
    void testFromString_validValues() {
        assertEquals(VehicleType.CAR, VehicleType.fromString("car"));
        assertEquals(VehicleType.SCOOTER, VehicleType.fromString("scooter"));
        assertEquals(VehicleType.BIKE, VehicleType.fromString("bike"));

        // Testige suurtähtede ja väiketähtede korral
        assertEquals(VehicleType.CAR, VehicleType.fromString("CAR"));
        assertEquals(VehicleType.SCOOTER, VehicleType.fromString("SCOOTER"));
        assertEquals(VehicleType.BIKE, VehicleType.fromString("BIKE"));
    }

    @Test
    void testFromString_invalidValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> VehicleType.fromString("plane"));
        assertEquals("Invalid vehicle type. Allowed values: Car, Scooter, Bike", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> VehicleType.fromString("bus"));
        assertEquals("Invalid vehicle type. Allowed values: Car, Scooter, Bike", exception.getMessage());
    }
}
