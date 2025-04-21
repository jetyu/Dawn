package com.dawn;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.dawn.mapper")
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
