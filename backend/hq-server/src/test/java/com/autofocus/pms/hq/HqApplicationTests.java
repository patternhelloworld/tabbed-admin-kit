package com.autofocus.pms.hq;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HqApplicationTests {

	@Test
	public void contextLoads() {

	}

	private Boolean checkProfileValidation(String profile) {
		String[] profiles = new String[]  {"local", "alpha", "production"};
		return Arrays.asList(profiles).contains(profile);

	}
}
