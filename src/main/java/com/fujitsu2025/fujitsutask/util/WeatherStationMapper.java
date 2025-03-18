package com.fujitsu2025.fujitsutask.util;

public class WeatherStationMapper {
    /**
     * Utility class to map city names to corresponding weather station names
     *
     * @param city - what user inputs
     * @return - corresponding weather station name
     * @throws IllegalArgumentException if the city is invalid
     */

    public static String getWeatherStationForCity(String city) {
        return switch (city.toLowerCase()) {
            case "tallinn" -> "Tallinn-Harku";
            case "tartu" -> "Tartu-Tõravere";
            case "pärnu" -> "Pärnu";
            default -> throw new IllegalArgumentException("Invalid city");
        };
    }
}