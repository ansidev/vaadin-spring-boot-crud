package xyz.ansidev.simple_crud.ui.model;

import xyz.ansidev.simple_crud.entity.User;
import xyz.ansidev.simple_crud.util.formatter.CustomDateFormatter;

@SuppressWarnings("serial")
public class UserModel extends User {

	private String createdDate;
	private String updatedDate;

	public UserModel() {
		super();
		initDate();
	}

	public UserModel(User user) {
		super(user.getId(), user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(),
				user.getEmail(), user.getPosts(), user.getUserMeta(), user.getCreatedAt(), user.getUpdatedAt());
		initDate();
	}

	public User toUser() {
		return new User(id, username, password, firstName, lastName, email, posts, userMeta, createdAt, updatedAt);
	}

	private void initDate() {
		createdDate = CustomDateFormatter.toDate(createdAt);
		updatedDate = CustomDateFormatter.toDate(updatedAt);
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

}
