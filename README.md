# Food Delivery Fee Calculator

## Description
This project is a trial task for a Fujitsu internship.


The task involves creating a functionality to calculate delivery fees for food couriers using a combination of regional base fees and weather conditions data (temperature, wind speed, and weather phenomena). The application collects and stores weather data, calculates the fee, and provides a REST interface for requesting the fee based on input parameters such as city, vehicle type, and weather data.

## Core Modules
1. **Database**: A table to store weather data.
2. **CronJob**: A scheduled task that fetches weather data from the Estonian Environment Agency's API at configurable intervals.
3. **Delivery fee calculation**: A service that calculates the delivery fee based on the business rules for regional base fee and extra fees related to weather conditions.
4. **REST API**: A REST interface that allows the user to request the calculated delivery fee based on city, vehicle type, and weather data.

## Project Overview
This project was completed in about 20 hours and I found the difficulty to be moderate. While I managed to implement the core features, I encountered some challenges with improving the system's logic. There were also some complexities around calculating the extra fees based on weather conditions.

The implementation of the parseAndStoreWeatherData function in the WeatherService file caused some difficulties, particularly with parsing the XML data correctly.

ChatGPT was used to assist in generating the implementation of the parseAndStoreWeatherData function in the WeatherService file. It helped design the logic for parsing XML weather data and storing it in the database. Additionally, ChatGPT provided support in generating and improving tests for the application.