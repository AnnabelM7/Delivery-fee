package com.fujitsu2025.fujitsutask;

import com.fujitsu2025.fujitsutask.controller.DeliveryFeeController;
import com.fujitsu2025.fujitsutask.model.WeatherData;
import com.fujitsu2025.fujitsutask.repository.WeatherDataRepository;
import com.fujitsu2025.fujitsutask.service.DeliveryFeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DeliveryFeeControllerTest {

    private DeliveryFeeController deliveryFeeController;
    private WeatherDataRepository weatherDataRepository;
    private DeliveryFeeService deliveryFeeService;

    @BeforeEach
    void setUp() {
        weatherDataRepository = Mockito.mock(WeatherDataRepository.class);
        deliveryFeeService = Mockito.mock(DeliveryFeeService.class);
        deliveryFeeController = new DeliveryFeeController(weatherDataRepository, deliveryFeeService);
    }

    @Test
    void testGetDeliveryFee_validData() {
        // Kehtiv sisend
        String city = "Tallinn";
        String vehicleType = "car";

        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(10.0);

        List<WeatherData> weatherDataList = Arrays.asList(weatherData);
        when(weatherDataRepository.findTopByStationNameOrderByTimestampDesc("Tallinn-Harku")).thenReturn(weatherDataList);
        when(deliveryFeeService.calculateDeliveryFee(city, weatherData, vehicleType)).thenReturn(5.0);

        ResponseEntity<?> response = deliveryFeeController.getDeliveryFee(city, vehicleType);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"fee\": 5.0}", response.getBody());
    }

    @Test
    void testGetDeliveryFee_invalidCity() {
        String city = "InvalidCity";
        String vehicleType = "car";

        ResponseEntity<?> response = deliveryFeeController.getDeliveryFee(city, vehicleType);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"error\": \"Invalid city\"}", response.getBody());
    }

    @Test
    void testGetDeliveryFee_invalidVehicleType() {
        String city = "Tallinn";
        String vehicleType = "invalidType";

        ResponseEntity<?> response = deliveryFeeController.getDeliveryFee(city, vehicleType);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"error\": \"Invalid vehicle type. Allowed values: Car, Scooter, Bike\"}", response.getBody());
    }
}
