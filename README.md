# Food Delivery Fee Calculator

## Description
This project is a trial task for a Fujitsu internship.


The task involves creating a functionality to calculate delivery fees for food couriers using a combination of regional base fees and weather conditions data (temperature, wind speed, and weather phenomena). The application collects and stores weather data, calculates the fee, and provides a REST interface for requesting the fee based on input parameters such as city, vehicle type, and weather data.

## Core Modules
1. **Database**: A table to store weather data.
2. **CronJob**: A scheduled task that fetches weather data from the Estonian Environment Agency's API at configurable intervals.
3. **Delivery fee calculation**: A service that calculates the delivery fee based on the business rules for regional base fee and extra fees related to weather conditions.
4. **REST API**: A REST interface that allows the user to request the calculated delivery fee based on city and vehicle type.

##  API documentation
### **Endpoint**: `/api/delivery-fee`

**Method**: `GET`

**Description**:  
Calculates the delivery fee based on the city, vehicle type, and the most recent weather data. If the input is invalid or weather data is unavailable, an appropriate error message is returned.

---

### **Request Parameters**

| Parameter     | Type     | Description                                        | Required | Example                    |
|---------------|----------|----------------------------------------------------|----------|----------------------------|
| `city`        | `String` | The city for which the delivery fee is calculated. | **Yes**  | `Tallinn`,`Tartu`, `Pärnu` |
| `vehicleType` | `String` | The type of vehicle that affects the delivery fee. | **Yes**  | `Car`, `Scooter`, `Bike`   |

---

### **Example**
Request (using curl):
```json
curl -X GET "http://localhost:8080/api/delivery-fee?city=Tallinn&vehicleType=Car"
```


### **Response**

#### **Success Response (200 OK)**

**Content**:  
If the inputs are valid and weather data is available, the delivery fee is returned.

**Example**:
```json
{
  "fee": 7.5
}
```
#### **Error Response (400 Bad Request)**
**Content**:    
If the input parameters are invalid (e.g., an unsupported vehicle type or missing city), an error message is returned.

**Example**:
```json
{
  "error": "Invalid vehicle type. Allowed values: Car, Scooter, Bike"
}
```

## Project Overview
This project was completed in about 20 hours and I found the difficulty to be moderate. While I managed to implement the core features, I encountered some challenges with improving the system's logic. There were also some complexities around calculating the extra fees based on weather conditions.

The implementation of the parseAndStoreWeatherData function in the WeatherService file caused some difficulties, particularly with parsing the XML data correctly.

ChatGPT was used to assist in generating the implementation of the parseAndStoreWeatherData function in the WeatherService file. It helped design the logic for parsing XML weather data and storing it in the database. Additionally, ChatGPT provided support in generating and improving tests for the application.

## Future Improvements
If I had more time, I would have improved the following aspects of the project:
* **Database structure:** Instead of hardcoding base fees for each city and vehicle type directly in the code, I would have designed a more flexible database structure. This would involve adding tables for cities, vehicles, and their corresponding base fees. This would allow for easier modification and addition of new cities, vehicle types, and pricing details without changing the code.
* **API documentation and tests:** While I did create basic tests, I would have spent more time improving the test coverage for different scenarios, especially edge cases. Additionally, enhancing the REST API documentation with more detailed examples would have been a useful improvement.