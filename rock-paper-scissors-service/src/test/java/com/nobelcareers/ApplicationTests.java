package com.nobelcareers;

import com.nobelcareers.ports.outbound.observability.MetricCollector;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {"spring.profiles.active=test"})
class ApplicationTests {

	@MockBean
	private MetricCollector metricCollector;

	@Test
	void contextLoads() {
	}

}
