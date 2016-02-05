package io.pivotal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"test"})
@SpringApplicationConfiguration(classes = WeatherbusWeatherApplication.class)
public class WeatherbusWeatherApplicationTests {

	@Test
	public void contextLoads() {
	}

}
