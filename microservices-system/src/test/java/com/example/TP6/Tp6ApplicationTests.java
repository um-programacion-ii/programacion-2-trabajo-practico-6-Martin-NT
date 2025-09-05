package com.example.TP6;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class Tp6ApplicationTests {

	@Test
	void contextLoads() {
	}

}
