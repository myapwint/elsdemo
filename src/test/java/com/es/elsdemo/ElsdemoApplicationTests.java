package com.es.elsdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "app.elasticsearch.create-index-on-startup=false")
class ElsdemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
