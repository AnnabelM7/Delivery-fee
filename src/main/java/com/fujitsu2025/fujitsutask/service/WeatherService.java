package com.fujitsu2025.fujitsutask.service;

import com.fujitsu2025.fujitsutask.model.WeatherData;
import com.fujitsu2025.fujitsutask.repository.WeatherDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

// fetches weather data from an API

@Service
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final WeatherDataRepository repository;
    private final RestTemplate restTemplate;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    /**
     * Constructor
     *
     * @param repository   - to store the weather data into the database
     * @param restTemplate - used for making HTTP requests
     */
    public WeatherService(WeatherDataRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 15 * * * ?")
    // The default configuration to run the CronJob is once every hour, 15 minutes after a full hour (HH:15:00).
    public void fetchWeatherData() {
        try {
            logger.info("Starting to fetch weather data...");

            //GET request
            ResponseEntity<String> response = restTemplate.getForEntity(weatherApiUrl, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Successfully fetched weather data from the API.");
                parseAndStoreWeatherData(response.getBody());
            } else {
                logger.error("Unexpected status code from the weather data API: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Unknown error while processing weather data: {}", e.getMessage(), e);
        }
    }

    /**
     * Used: <a href="https://www.baeldung.com/java-xerces-dom-parsing">...</a>
     * Also, ChatGPT was used to assist in generating and fixing this implementation
     *
     * @param body the XML response body as a String
     */
    private void parseAndStoreWeatherData(String body) {
        // Parse the XML data
        try {
            // XML Parsing Setup
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));
            //... ensure that the document hierarchy isn’t affected by any extra white spaces or new lines within nodes.
            document.getDocumentElement().normalize();
            // NodeList of all station elements in the XML
            NodeList stationNodes = document.getElementsByTagName("station");

            // Iterate through stations and save only the selected ones
            for (int i = 0; i < stationNodes.getLength(); i++) {
                Node node = stationNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element stationElement = (Element) node;
                    String stationName = stationElement.getElementsByTagName("name").item(0).getTextContent();

                    // Get the timestamp from the <observations> element (UNIX timestamp in seconds)
                    String timestampString = document.getDocumentElement().getAttribute("timestamp");
                    long timestamp = Long.parseLong(timestampString);

                    // Check if the station name matches the required ones
                    if ("Tallinn-Harku".equals(stationName) || "Tartu-Tõravere".equals(stationName) || "Pärnu".equals(stationName)) {
                        // Create WeatherData object and map the fields
                        WeatherData weatherData = new WeatherData();
                        weatherData.setStationName(stationName);
                        weatherData.setWmoCode(stationElement.getElementsByTagName("wmocode").item(0).getTextContent());
                        weatherData.setAirTemperature(Double.parseDouble(stationElement.getElementsByTagName("airtemperature").item(0).getTextContent()));
                        weatherData.setWindSpeed(Double.parseDouble(stationElement.getElementsByTagName("windspeed").item(0).getTextContent()));
                        weatherData.setWeatherPhenomenon(stationElement.getElementsByTagName("phenomenon").item(0).getTextContent());
                        weatherData.setTimestamp(timestamp);

                        // Save the WeatherData object to the database
                        repository.save(weatherData);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error parsing or storing weather data: {}", e.getMessage(), e);
        }
    }
}