package com.fujitsu2025.fujitsutask.repository;

import com.fujitsu2025.fujitsutask.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    /**
     * Retrieves the most recent weather data for a specific station, ordered by timestamp
     *
     * @param stationName - the name of the weather station
     * @return list of WeatherData (OrderByTimestampDesc)
     */
    List<WeatherData> findTopByStationNameOrderByTimestampDesc(String stationName);
}
