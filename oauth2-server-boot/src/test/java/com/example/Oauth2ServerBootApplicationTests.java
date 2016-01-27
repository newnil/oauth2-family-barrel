package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import com.newnil.cas.oauth2.provider.Oauth2ServerBootApplication;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Oauth2ServerBootApplication.class)
@WebAppConfiguration
public class Oauth2ServerBootApplicationTests {

	@Test
	public void contextLoads() {
	}

}
