package xyz.ansidev.simple_crud.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import xyz.ansidev.simple_crud.constant.AppConstant;

@Entity
@Table(name = "user_meta")
public class UserMeta implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_VALUE = "value";
	
	// Default User Meta
	public static final String BIRTHDAY = "birthday";
	public static final String GENDER = "gender";
	public static final String NATIONALITY = "nationality";
	public static final String ADDRESS = "address";
	public static final String PHONE = "phone";

	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "value")
	private String value;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User owner;

	@Column(name = "created_at")
	protected LocalDateTime createdAt;

	@Column(name = "updated_at")
	protected LocalDateTime updatedAt;

	public UserMeta() {
		super();
		this.name = AppConstant.EMPTY;
		this.value = AppConstant.EMPTY;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public UserMeta(String name, String value) {
		super();
		this.name = name;
		this.value = value;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public UserMeta(String name, String value, User owner) {
		super();
		this.name = name;
		this.value = value;
		this.owner = owner;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "UserMeta [id=" + id + ", name=" + name + ", value=" + value + ", owner=" + owner.toString()
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

}
