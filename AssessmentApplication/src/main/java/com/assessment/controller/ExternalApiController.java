package com.assessment.controller;

import com.assessment.service.ExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExternalApiController {

    @Autowired
    private ExternalApiService externalApiService;

    @GetMapping("/api/external/users")
    public String getExternalUsers() {
        return externalApiService.getRandomUser();
    }
}
