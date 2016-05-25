package xyz.ansidev.simple_crud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import xyz.ansidev.simple_crud.SimpleCrudApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SimpleCrudApplication.class)
@WebAppConfiguration
public class SimpleCrudApplicationTests {

	@Test
	public void contextLoads() {
	}

}
