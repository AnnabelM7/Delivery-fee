package com.fujitsu2025.fujitsutask.service;

import com.fujitsu2025.fujitsutask.model.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

// Service for calculating the delivery fee based on weather data and vehicle type

@Service
public class DeliveryFeeService {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryFeeService.class);


    /**
     * calculates the total delivery fee based on the city, weather data, and vehicle type
     *
     * @param city        - where the delivery is taking place
     * @param weatherData - weather data for the city
     * @param vehicleType - what vehicle used for delivery
     * @return total delivery fee
     */
    public double calculateDeliveryFee(String city, WeatherData weatherData, String vehicleType) {
        city=capitalizeName(city);
        vehicleType=capitalizeName(vehicleType);
        logger.info("Calculating delivery fee for city: {}, vehicle type: {}", city, vehicleType);


        double baseFee = getBaseFee(city, vehicleType);
        double extraFee = calculateExtraFees(weatherData, vehicleType);

        double totalFee = baseFee + extraFee;

        logger.info("Base fee: {}, Extra fee: {}, Total fee: {}", baseFee, extraFee, totalFee);

        return totalFee; //total fee
    }

    /**
     * Base fee for the specified city and vehicle type
     *
     * @param city        - where the delivery is taking place
     * @param vehicleType - what vehicle used for delivery
     * @return base fee rot city and vehicle type
     */
    private double getBaseFee(String city, String vehicleType) {
        logger.debug("Getting base fee for city: {}, vehicle type: {}", city, vehicleType);

        Map<String, Map<String, Double>> baseFees = Map.of("Tallinn", Map.of("Car", 4.0, "Scooter", 3.5, "Bike", 3.0), "Tartu", Map.of("Car", 3.5, "Scooter", 3.0, "Bike", 2.5), "Pärnu", Map.of("Car", 3.0, "Scooter", 2.5, "Bike", 2.0));

        double fee = baseFees.getOrDefault(city, Map.of()).getOrDefault(vehicleType, 0.0);
        logger.debug("Base fee found: {}", fee);

        return fee;
    }

    /**
     * Calculates extra fees based on weather conditions
     *
     * @param weatherData - weather data for the city
     * @param vehicleType - what vehicle used for delivery
     * @return extra fee based on weather conditions
     */
    private double calculateExtraFees(WeatherData weatherData, String vehicleType) {
        logger.debug("Calculating extra fees for vehicle type: {}", vehicleType);

        double atef = 0;
        double wsef = 0;
        double wpef = 0;

        // Extra fee based on air temperature (atef)
        if ((vehicleType.equals("Scooter")) || (vehicleType.equals("Bike"))) {
            if (weatherData.getAirTemperature() < -10) {
                atef += 1.0;
            } else if (weatherData.getAirTemperature() < 0) {
                atef += 0.5;
            }
        }
        // Extra fee based on wind speed (wsef)
        if (vehicleType.equals("Bike")) {
            if (weatherData.getWindSpeed() > 20) {
                throw new IllegalArgumentException("Usage of selected vehicle type is forbidden");
            }
            if (weatherData.getWindSpeed() >= 10) {
                wsef += 0.5;
            }
        }
        //Extra fee based on weather phenomenon (wpef)
        if (vehicleType.equals("Scooter") || vehicleType.equals("Bike")) {
            if (weatherData.getWeatherPhenomenon().toLowerCase().contains("snow") || weatherData.getWeatherPhenomenon().toLowerCase().contains("sleet")) {
                wpef += 1.0;
            }
            if (weatherData.getWeatherPhenomenon().toLowerCase().contains("rain")) {
                wpef += 0.5;
            }
            if (weatherData.getWeatherPhenomenon().matches("(?i).*glaze.*|.*hail.*|.*thunder.*")) {
                throw new IllegalArgumentException("Usage of selected vehicle type is forbidden");
            }
        }

        double totalExtraFee = atef + wpef + wsef;
        logger.debug("Total extra fee calculated: {}", totalExtraFee);

        return totalExtraFee;
    }

    /**
     * Capitalizes the first letter of a city name and converts the rest to lowercase.
     *
     * @param city The name of the city.
     * @return The city name with the first letter capitalized and the rest in lowercase.
     */
    private String capitalizeName(String city) {
        if (city == null || city.isEmpty()) {
            return city;
        }
        return city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase();
    }

}
