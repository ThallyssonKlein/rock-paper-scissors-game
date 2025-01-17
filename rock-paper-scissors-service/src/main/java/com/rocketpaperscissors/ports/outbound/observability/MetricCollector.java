package com.rocketpaperscissors.ports.outbound.observability;

import com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import com.timgroup.statsd.StatsDClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MetricCollector {

    @Value("${spring.application.name}")
    private String applicationPrefix;

    @Value("${datadog.agent.host}")
    private String datadogAgentHost;

    @Value("${datadog.agent.port}")
    private int datadogAgentPort;

    private StatsDClient statsDClient;

    @PostConstruct
    public void init() {
        this.statsDClient = new NonBlockingStatsDClientBuilder()
                .prefix(this.applicationPrefix)
                .hostname(this.datadogAgentHost)
                .port(this.datadogAgentPort)
                .build();
    }

    public void incrementMetric(String metricName) {
        this.statsDClient.count(metricName, 1);
    }

    public void incrementMetric(String metricName, Map<String, String> tags) {
        String[] tagsArray = tags.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .toArray(String[]::new);
        this.statsDClient.count(metricName, 1, tagsArray);
    }

    public void incrementMetricWithTags(String metricName, String... tags) {
        if (tags.length % 2 != 0) {
            throw new IllegalArgumentException("Odd number of arguments. Please provide key-value pairs.");
        }

        Map<String, String> tagsMap = new HashMap<>();
        for (int i = 0; i < tags.length; i += 2) {
            tagsMap.put(tags[i], tags[i + 1]);
        }

        incrementMetric(metricName, tagsMap);
    }
}