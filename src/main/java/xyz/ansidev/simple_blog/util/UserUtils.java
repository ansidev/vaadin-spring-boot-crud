package xyz.ansidev.simple_blog.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractField;

import xyz.ansidev.simple_blog.SimpleBlogApplication;
import xyz.ansidev.simple_blog.constant.AppConstant;
import xyz.ansidev.simple_blog.encoder.bcrypt.BCrypt;
import xyz.ansidev.simple_blog.entity.User;
import xyz.ansidev.simple_blog.entity.UserMeta;
import xyz.ansidev.simple_blog.repository.UserRepository;

public class UserUtils {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleBlogApplication.class);

	public static String getUserName(String firstName, String lastName) {
		LOG.info("getUserName: " + firstName + "-" + lastName);
		String username = AppConstant.EMPTY;
		String[] lastNameArr = lastName.trim().split(AppConstant.WHITE_SPACE);
		int lastNameLength = lastNameArr.length;
		username += lastNameArr[lastNameLength - 1] + getFirstCharacter(firstName);
		for (int i = 0; i < lastNameLength - 1; i++) {
			username += getFirstCharacter(lastNameArr[i]);
		}
		LOG.info(username);
		return VNCharacterUtils.removeAccent(username.trim().toLowerCase());
	}

	private static String getFirstCharacter(String s) {
		return s.trim().substring(0, 1);
	}

	public static User initUser(String fullname) {
		String[] fullnameArr = fullname.trim().split(AppConstant.WHITE_SPACE);
		String firstName = fullnameArr[0];
		String lastName = fullname.substring(firstName.length());
		String username = getUserName(firstName, lastName);

		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUsername(username);

		return user;
	}

	public static void initUserMeta(User user) {
		List<UserMeta> userMetaList = new ArrayList<UserMeta>();
		userMetaList.add(new UserMeta(UserMeta.BIRTHDAY, null));
		userMetaList.add(new UserMeta(UserMeta.GENDER, null));
		userMetaList.add(new UserMeta(UserMeta.NATIONALITY, null));
		userMetaList.add(new UserMeta(UserMeta.ADDRESS, null));
		userMetaList.add(new UserMeta(UserMeta.PHONE, null));

		user.setUserMeta(userMetaList);
	}

	/**
	 * Basic settings for field.
	 * 
	 * @param field
	 *            TextField/PasswordField
	 * @param icon
	 *            Field Icon
	 * @param width
	 *            Field width
	 * @param unit
	 *            Unit type
	 * @param required
	 *            Required or not
	 */
	public static void settingField(AbstractField<?> field, Resource icon, float width, Unit unit, boolean required) {
		field.setIcon(icon);
		field.setWidth(width, unit);
		field.setRequired(required);
	}

	public static boolean checkIfUserPasswordChanged(User user, UserRepository userRepository) {
		String password = user.getPassword();
		final boolean persisted = user.getId() != null;
		if (persisted) {
			Integer userId = user.getId();
			boolean isEmptyPassword = AppConstant.EMPTY.equals(password);
			boolean isExistUser = userRepository.exists(userId);
			if (isExistUser) {
				User dbUser = userRepository.findOne(userId);
				if (isEmptyPassword) {
					user.setPassword(dbUser.getPassword());
					return false;
				} else if (BCrypt.checkpw(password, dbUser.getPassword())) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
