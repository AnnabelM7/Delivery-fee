package com.fujitsu2025.fujitsutask;

import com.fujitsu2025.fujitsutask.model.WeatherData;
import com.fujitsu2025.fujitsutask.service.DeliveryFeeService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeliveryFeeServiceTest {

    private final DeliveryFeeService service = new DeliveryFeeService();

    @Test
    void testCalculateDeliveryFee_TallinnCar_NormalWeather() {
        WeatherData weather = new WeatherData();
        weather.setAirTemperature(5.0);
        weather.setWindSpeed(2.0);
        weather.setWeatherPhenomenon("Clear");

        double result = service.calculateDeliveryFee("Tallinn", weather, "Car");
        assertEquals(4.0, result, "Tallinna auto baastasu peaks olema 4.0 €");
    }

    @Test
    void testCalculateDeliveryFee_TartuBike_ColdWeather() {
        WeatherData weather = new WeatherData();
        weather.setAirTemperature(-5.0);
        weather.setWindSpeed(5.0);
        weather.setWeatherPhenomenon("Light snow");

        double result = service.calculateDeliveryFee("Tartu", weather, "Bike");
        assertEquals(2.5 + 0.5 + 1.0, result, "Tartu rattaga ja külma ilmaga peaks tasu olema 4.0 €");
    }

    @Test
    void testCalculateDeliveryFee_PärnuScooter_HeavyRain() {
        WeatherData weather = new WeatherData();
        weather.setAirTemperature(10.0);
        weather.setWindSpeed(3.0);
        weather.setWeatherPhenomenon("Heavy rain");

        double result = service.calculateDeliveryFee("Pärnu", weather, "Scooter");
        assertEquals(2.5 + 0.5, result, "Pärnus tõukerattaga vihmaga peaks tasu olema 3.0 €");
    }

    @Test
    void testCalculateDeliveryFee_Bike_StrongWind_ThrowsException() {
        WeatherData weather = new WeatherData();
        weather.setAirTemperature(5.0);
        weather.setWindSpeed(21.0);
        weather.setWeatherPhenomenon("Clear");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.calculateDeliveryFee("Tallinn", weather, "Bike");
        });
        assertEquals("Usage of selected vehicle type is forbidden", exception.getMessage());
    }

    @Test
    void testCalculateDeliveryFee_Scooter_Thunderstorm_ThrowsException() {
        WeatherData weather = new WeatherData();
        weather.setAirTemperature(15.0);
        weather.setWindSpeed(3.0);
        weather.setWeatherPhenomenon("Thunderstorm");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.calculateDeliveryFee("Tartu", weather, "Scooter");
        });
        assertEquals("Usage of selected vehicle type is forbidden", exception.getMessage());
    }

    @Test
    void testCalculateDeliveryFee_Bike_Exactly10msWind() {
        WeatherData weather = new WeatherData();
        weather.setAirTemperature(10.0);
        weather.setWindSpeed(10.0);
        weather.setWeatherPhenomenon("Clear");

        double result = service.calculateDeliveryFee("Tallinn", weather, "Bike");
        assertEquals(3.0 + 0.5, result, "Tallinnas rattaga täpselt 10m/s tuulega peaks tasu olema 3.5 €");
    }

    @Test
    void testCalculateDeliveryFee_Bike_WindSpeedJustUnderLimit() {
        WeatherData weather = new WeatherData();
        weather.setAirTemperature(10.0);
        weather.setWindSpeed(19.9);
        weather.setWeatherPhenomenon("Clear");

        double result = service.calculateDeliveryFee("Tallinn", weather, "Bike");
        assertEquals(3.0 + 0.5, result, "Tallinnas rattaga 19.9m/s tuulega peaks tasu olema 3.5 €");
    }

    @Test
    void testCalculateDeliveryFee_Scooter_ExactlyMinus10Degrees() {
        WeatherData weather = new WeatherData();
        weather.setAirTemperature(-10.0);
        weather.setWindSpeed(3.0);
        weather.setWeatherPhenomenon("Clear");

        double result = service.calculateDeliveryFee("Tartu", weather, "Scooter");
        assertEquals(3.0 + 0.5, result, "Tartus tõukerattaga -10°C peaks tasu olema 3.5 €");
    }

    @Test
    void testCalculateDeliveryFee_Scooter_Glaze_ThrowsException() {
        WeatherData weather = new WeatherData();
        weather.setAirTemperature(5.0);
        weather.setWindSpeed(2.0);
        weather.setWeatherPhenomenon("Glaze");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.calculateDeliveryFee("Pärnu", weather, "Scooter");
        });
        assertEquals("Usage of selected vehicle type is forbidden", exception.getMessage());
    }

    @Test
    void testCalculateDeliveryFee_Bike_Hail_ThrowsException() {
        WeatherData weather = new WeatherData();
        weather.setAirTemperature(10.0);
        weather.setWindSpeed(5.0);
        weather.setWeatherPhenomenon("Hail");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.calculateDeliveryFee("Tallinn", weather, "Bike");
        });
        assertEquals("Usage of selected vehicle type is forbidden", exception.getMessage());
    }

    @Test
    void testCalculateDeliveryFee_Bike_Sleet() {
        WeatherData weather = new WeatherData();
        weather.setAirTemperature(5.0);
        weather.setWindSpeed(3.0);
        weather.setWeatherPhenomenon("Sleet");

        double result = service.calculateDeliveryFee("Tallinn", weather, "Bike");
        assertEquals(3.0 + 1.0, result, "Tallinnas rattaga lörtsiga peaks tasu olema 4.0 €");
    }

}
