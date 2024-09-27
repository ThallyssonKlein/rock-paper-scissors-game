package com.srmasset.ports.inbound.http.api.v1.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsControllerApiV1 {

    @GetMapping("/{userId}")
    public void getStatistics() {}
}
