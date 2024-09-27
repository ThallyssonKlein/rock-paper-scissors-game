package com.srmasset;

import com.srmasset.ports.outbound.observability.MetricCollector;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(properties = {"spring.profiles.active=test"})
class ApplicationTests {

	@MockBean
	private MetricCollector metricCollector;

	@Test
	void contextLoads() {
	}

}
