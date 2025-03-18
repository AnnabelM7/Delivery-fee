package com.fujitsu2025.fujitsutask.controller;

import com.fujitsu2025.fujitsutask.model.WeatherData;
import com.fujitsu2025.fujitsutask.repository.WeatherDataRepository;
import com.fujitsu2025.fujitsutask.service.DeliveryFeeService;
import com.fujitsu2025.fujitsutask.util.VehicleType;
import com.fujitsu2025.fujitsutask.util.WeatherStationMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-fee")
public class DeliveryFeeController {

    private final WeatherDataRepository weatherDataRepository; // gets weather data
    private final DeliveryFeeService deliveryFeeService; // calculates

    public DeliveryFeeController(WeatherDataRepository weatherDataRepository, DeliveryFeeService deliveryFeeService) {
        this.weatherDataRepository = weatherDataRepository;
        this.deliveryFeeService = deliveryFeeService;
    }

    /**
     * Calculates delivery fee with weather data and vehicle type
     *
     * @param city        - city for which the delivery fee is calculated
     * @param vehicleType - type of vehicle affecting the delivery fee
     * @return ResponseEntity - returning fee or error messaeg
     */
    @GetMapping
    public ResponseEntity<?> getDeliveryFee(@RequestParam String city, @RequestParam String vehicleType) {
        // Validate city input
        String weatherStation = validateCity(city);
        if (weatherStation == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Invalid city\"}");
        }
        // Validate the vehicle type
        try {
            VehicleType.fromString(vehicleType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + e.getMessage() + "\"}");
        }


        // Latest weather data for city
        List<WeatherData> latestWeather = weatherDataRepository.findTopByStationNameOrderByTimestampDesc(weatherStation);
        if (latestWeather.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Weather data not available\"}");
        }

        try {
            double fee = deliveryFeeService.calculateDeliveryFee(city, latestWeather.get(0), vehicleType); // Kasutame teenust
            return ResponseEntity.ok("{\"fee\": " + fee + "}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }


    /**
     * Validates the city and maps it to a weather station.
     *
     * @param city - city name
     * @return - corresponding weather station name or null if invalid
     */
    private String validateCity(String city) {
        try {
            return WeatherStationMapper.getWeatherStationForCity(city);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
