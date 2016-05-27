package xyz.ansidev.simple_crud.ui.model;

import java.io.Serializable;

import xyz.ansidev.simple_crud.entity.User;
import xyz.ansidev.simple_crud.util.formatter.CustomDateFormatter;

@SuppressWarnings("serial")
public class UserModel implements Serializable {

	protected Integer id;

	protected String username;

	protected String password;

	protected String firstName;

	protected String lastName;

	protected String email;

	protected String createdAt;

	protected String updatedAt;

	public UserModel(Integer id, String username, String password, String firstName, String lastName, String email,
			String createdAt, String updatedAt) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public UserModel(Integer id, String username, String password, String firstName, String lastName, String email) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public UserModel(User user) {
		this(user.getId(), user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(),
				user.getEmail());
		CustomDateFormatter formatter = new CustomDateFormatter();
		this.createdAt = formatter.format(user.getCreatedAt());
		this.updatedAt = formatter.format(user.getUpdatedAt());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

}
