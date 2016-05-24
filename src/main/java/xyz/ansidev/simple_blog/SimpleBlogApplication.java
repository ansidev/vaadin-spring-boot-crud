package xyz.ansidev.simple_blog;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.javafaker.Faker;

import xyz.ansidev.simple_blog.constant.AppConstant;
import xyz.ansidev.simple_blog.entity.User;
import xyz.ansidev.simple_blog.repository.UserRepository;
import xyz.ansidev.simple_blog.util.UserUtils;

@SpringBootApplication
public class SimpleBlogApplication {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleBlogApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SimpleBlogApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner loadData(UserRepository repository) {
//		LOG.info("loadData()");
//		return (args) -> {
//
//			Faker faker = new Faker(Locale.US);
//
//			for (int i = 0; i < 10000; i++) {
//				User user = new User();
//				user.setFirstName(faker.name().firstName());
//				user.setLastName(faker.name().lastName());
//				user.setUsername(UserUtils.getUserName(user.getFirstName(), user.getLastName()));
//				String username = user.getUsername();
//
//				Integer numberOfDuplicateUsername = repository.findByUsernameStartsWithIgnoreCase(username).size();
//
//				if (numberOfDuplicateUsername > 0) {
//					user.setUsername(username + numberOfDuplicateUsername);
//				}
//
//				user.setEmail(username + "@" + faker.internet().domainName());
//
//				// Save user
//				repository.save(user);
//			}
//		};
//	}
}
