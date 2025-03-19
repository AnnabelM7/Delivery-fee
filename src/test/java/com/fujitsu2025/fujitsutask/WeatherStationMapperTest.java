package com.fujitsu2025.fujitsutask;

import com.fujitsu2025.fujitsutask.util.WeatherStationMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeatherStationMapperTest {
    @Test
    void testGetWeatherStationForCity_validCity() {
        assertEquals("Tallinn-Harku", WeatherStationMapper.getWeatherStationForCity("Tallinn"));
        assertEquals("Tartu-Tõravere", WeatherStationMapper.getWeatherStationForCity("Tartu"));
        assertEquals("Pärnu", WeatherStationMapper.getWeatherStationForCity("Pärnu"));
    }

    @Test
    void testGetWeatherStationForCity_invalidCity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> WeatherStationMapper.getWeatherStationForCity("Narva"));
        assertEquals("Invalid city", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> WeatherStationMapper.getWeatherStationForCity("Rakvere"));
        assertEquals("Invalid city", exception.getMessage());
    }

    @Test
    void testGetWeatherStationForCity_caseInsensitive() {
        assertEquals("Tallinn-Harku", WeatherStationMapper.getWeatherStationForCity("tallinn"));
        assertEquals("Tartu-Tõravere", WeatherStationMapper.getWeatherStationForCity("TARTU"));
        assertEquals("Pärnu", WeatherStationMapper.getWeatherStationForCity("pärnu"));
    }
}
