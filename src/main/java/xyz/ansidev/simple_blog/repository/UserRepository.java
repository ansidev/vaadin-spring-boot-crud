package xyz.ansidev.simple_blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.ansidev.simple_blog.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findByUsernameStartsWithIgnoreCase(String username);

	List<User> findByFirstNameStartsWithIgnoreCase(String firstName);

	List<User> findByLastNameStartsWithIgnoreCase(String lastName);

}
