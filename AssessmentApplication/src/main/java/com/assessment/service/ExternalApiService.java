package com.assessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    @Autowired
    private RestTemplate restTemplate;

    public String getRandomUser() {
        String url = "https://randomuser.me/api/";
        return restTemplate.getForObject(url, String.class);
    }
}
