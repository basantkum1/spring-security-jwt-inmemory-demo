package com.jwt.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Service responsible for handling geolocation-based validations.
 * It uses an external API to determine the origin country of an IP address.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CountryLocationService {

    // Injected RestTemplate to make external HTTP calls
    private final RestTemplate restTemplate;

    // List of countries strictly prohibited from signing up
    private final List<String> RESTRICTED_COUNTRIES = List.of("Syria", "Afghanistan", "Iran");

    /**
     * Checks if the provided IP address belongs to a restricted country.
     *
     * @param ip The IP address of the incoming HTTP request.
     * @return true if the country is restricted, false otherwise.
     */
    public boolean isRestricted(String ip) {

        // Bypass the check for local development/testing environments
        if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
            return false;
        }

        try {
            // Call free IP-API service to retrieve location details
            String url = "http://ip-api.com/json/" + ip;
            Map response = restTemplate.getForObject(url, Map.class);

            // Verify the API call was successful before extracting data
            if (response != null && "success".equals(response.get("status"))) {
                String country = (String) response.get("country");

                // Check if the resolved country exists in our restricted list
                return RESTRICTED_COUNTRIES.contains(country);
            }
        } catch (Exception e) {
            log.error("3rd party Country location API failed for IP {}: {}", ip, e.getMessage());
        }
        return false;
    }
}