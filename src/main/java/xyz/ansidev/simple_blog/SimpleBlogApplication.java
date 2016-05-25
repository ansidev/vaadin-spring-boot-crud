package xyz.ansidev.simple_blog;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.javafaker.Faker;

import xyz.ansidev.simple_blog.encoder.bcrypt.BCrypt;
import xyz.ansidev.simple_blog.entity.User;
import xyz.ansidev.simple_blog.repository.UserRepository;
import xyz.ansidev.simple_blog.util.UserUtils;

@SpringBootApplication
public class SimpleBlogApplication {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleBlogApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SimpleBlogApplication.class, args);
	}

	private static final int fakerCount = 10;
	private static final boolean isFaker = false;

	@Bean
	public CommandLineRunner loadData(UserRepository repository) {
		LOG.info("loadData()");
		return (args) -> {
			if (isFaker) {
				Faker faker = new Faker(Locale.US);

				for (int i = 0; i < fakerCount; i++) {
					User user = new User();
					// Init user information
					user.setFirstName(faker.name().firstName());
					user.setLastName(faker.name().lastName());
					user.setUsername(UserUtils.getUserName(user.getFirstName(), user.getLastName()));
					String username = user.getUsername();

					Integer numberOfDuplicateUsername = repository.findByUsernameStartsWithIgnoreCase(username).size();

					if (numberOfDuplicateUsername > 0) {
						user.setUsername(username + numberOfDuplicateUsername);
					}

					user.setEmail(username + "@" + faker.internet().domainName());
					String hashedPassword = BCrypt.hashpw(username, BCrypt.gensalt(12));
					user.setPassword(hashedPassword);

					// Save user
					repository.save(user);
				}
			}
		};
	}
}
