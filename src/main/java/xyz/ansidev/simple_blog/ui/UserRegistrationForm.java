package xyz.ansidev.simple_blog.ui;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import xyz.ansidev.simple_blog.constant.AppConstant;
import xyz.ansidev.simple_blog.constant.HtmlTag;
import xyz.ansidev.simple_blog.constant.UserFormConstant;
import xyz.ansidev.simple_blog.encoder.bcrypt.BCrypt;
import xyz.ansidev.simple_blog.entity.User;
import xyz.ansidev.simple_blog.handler.ChangeHandler;
import xyz.ansidev.simple_blog.message.UserRegistrationMessage;
import xyz.ansidev.simple_blog.repository.UserRepository;
import xyz.ansidev.simple_blog.util.HtmlUtils;

@SpringComponent
@UIScope
public class UserRegistrationForm extends FormLayout {

	private static final Logger LOG = LoggerFactory.getLogger(UserRegistrationForm.class);

	private static final long serialVersionUID = 1L;

	private final UserRepository userRepository;

	private User user;

	final TextField username = new TextField(UserFormConstant.USERNAME);
	final TextField email = new TextField(UserFormConstant.EMAIL);
	final PasswordField password = new PasswordField(UserFormConstant.PASSWORD);
	final TextField firstName = new TextField(UserFormConstant.FIRST_NAME);
	final TextField lastName = new TextField(UserFormConstant.LAST_NAME);

	/* Action Buttons */
	Button btnSave = new Button(UserFormConstant.BUTTON_SAVE, FontAwesome.SAVE);
	Button btnDelete = new Button(UserFormConstant.BUTTON_DELETE, FontAwesome.TRASH);
	Button btnReset = new Button(UserFormConstant.BUTTON_RESET, FontAwesome.TIMES);
	Button btnCancel = new Button(UserFormConstant.BUTTON_CANCEL, FontAwesome.BAN);
	CssLayout actionButtons = new CssLayout(btnSave, btnDelete, btnReset, btnCancel);

	Notification saveNotification = new Notification("");

	@Autowired
	public UserRegistrationForm(UserRepository userRepository) {
		this.userRepository = userRepository;

		setSpacing(true);
		setSizeFull();
		setVisible(false);

		addComponents(username, email, password, firstName, lastName);
		addComponent(actionButtons);

		username.setIcon(FontAwesome.USER);
		username.setWidth(100, Unit.PERCENTAGE);
		username.setRequired(true);
		username.setRequiredError(AppConstant.EMPTY);

		email.setIcon(FontAwesome.ENVELOPE);
		email.setWidth(100, Unit.PERCENTAGE);
		email.setRequired(true);

		password.setIcon(FontAwesome.KEY);
		password.setWidth(100, Unit.PERCENTAGE);
		password.setRequired(true);

		firstName.setIcon(FontAwesome.USER);
		firstName.setWidth(100, Unit.PERCENTAGE);
		firstName.setRequired(false);

		lastName.setIcon(FontAwesome.USER);
		lastName.setWidth(100, Unit.PERCENTAGE);
		lastName.setRequired(false);

		// Set validator for form fields
		setValidator();

		actionButtons.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		btnSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btnSave.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		btnDelete.setStyleName(ValoTheme.BUTTON_DANGER);

		// Wire action buttons to save, delete, reset and cancel
		btnSave.addClickListener(e -> {

			// checkForEmptyValue(username, email, password);

			boolean isUserPasswordChanged = checkIfUserPasswordChanged(user);

			// Validate form fields, must validate before hash password
			ArrayList<String> errors = validate();

			// Hash user 's password if not empty or not null
			String originalPassword = user.getPassword();
			if (!originalPassword.isEmpty()) {
				String hashedPassword = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
				user.setPassword(hashedPassword);
			}

			// Notification information
			String saveCaption;

			if (errors.size() > 0) {
				saveCaption = getAllErrorsAsString(errors);
				setVisible(true);
			} else {
				// Save user
				userRepository.save(user);

				saveCaption = String.format(UserRegistrationMessage.SAVE_SUCCESS,
						HtmlUtils.renderHtmlCode(HtmlTag.STRONG, username.getValue()));
				if (isUserPasswordChanged) {
					saveCaption += HtmlUtils.getSingleHtmlTag(HtmlTag.BR) + UserRegistrationMessage.PASSWORD_CHANGED;
				}
				setVisible(false);
			}
			saveNotification.setCaption(saveCaption);
			saveNotification.setHtmlContentAllowed(true);
			// saveNotification.setIcon(FontAwesome.COMMENT_O);
			saveNotification.setPosition(Position.TOP_RIGHT);
			saveNotification.setDelayMsec(3000);
			saveNotification.show(Page.getCurrent());
		});

		btnDelete.addClickListener(e -> userRepository.delete(user));
		btnCancel.addClickListener(e -> this.setVisible(false));
		btnReset.addClickListener(e -> this.saveUser(user));
	}

	private String getAllErrorsAsString(ArrayList<String> errors) {
		String errorsToString = AppConstant.EMPTY;
		for (String error : errors) {
			errorsToString += error + HtmlUtils.getSingleHtmlTag(HtmlTag.BR);
		}
		return errorsToString;
	}

	private boolean checkIfUserPasswordChanged(User user) {
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

	public final void saveUser(User user) {
		final boolean persisted = user.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			User dbUser = userRepository.findOne(user.getId());
			dbUser.setPassword(AppConstant.EMPTY);
			this.user = dbUser;
		} else {
			this.user = user;
		}
		btnDelete.setVisible(persisted);
		btnCancel.setVisible(true);
		// Bind user properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(this.user, this);

		// A hack to ensure the whole form is visible
		setVisible(true);

		btnSave.focus();

		// Select all text in username field automatically
		username.selectAll();
	}

	/**
	 * Set validators for form fields
	 */
	private void setValidator() {
		username.addValidator(new RegexpValidator(UserFormConstant.USERNAME_PATTERN, true,
				String.format(UserRegistrationMessage.ERROR_NOT_MATCH_REQUIREMENTS, UserFormConstant.USERNAME)));
		username.setDescription(UserFormConstant.USERNAME_DESCRIPTION);

		email.addValidator(new EmailValidator(UserRegistrationMessage.ERROR_INVALID_EMAIL));

		password.addValidator(new RegexpValidator(UserFormConstant.PASSWORD_PATTERN, true,
				String.format(UserRegistrationMessage.ERROR_NOT_MATCH_REQUIREMENTS, UserFormConstant.PASSWORD)));
		password.setDescription(UserFormConstant.PASSWORD_DESCRIPTION);

		username.addTextChangeListener(e -> {
			username.setRequiredError(
					String.format(UserRegistrationMessage.ERROR_EMPTY_NOT_ALLOWED, UserFormConstant.USERNAME));
		});
		email.addTextChangeListener(e -> {
			email.setRequiredError(
					String.format(UserRegistrationMessage.ERROR_EMPTY_NOT_ALLOWED, UserFormConstant.EMAIL));
		});
		password.addTextChangeListener(e -> {
			password.setRequiredError(
					String.format(UserRegistrationMessage.ERROR_EMPTY_NOT_ALLOWED, UserFormConstant.PASSWORD));
		});
	}

	public ArrayList<String> validate() {
		ArrayList<String> errors = new ArrayList<String>();
		String errorMessage;
		try {
			username.validate();
		} catch (EmptyValueException e) {
			username.setValidationVisible(true);

			errorMessage = String.format(UserRegistrationMessage.ERROR_EMPTY_NOT_ALLOWED, UserFormConstant.USERNAME);
			username.setRequiredError(errorMessage);

			errors.add(errorMessage);
		} catch (InvalidValueException e) {
			LOG.info(e.getMessage());

			username.setValidationVisible(true);

			errors.add(e.getMessage());
		}

		try {
			email.validate();
		} catch (EmptyValueException e) {
			email.setValidationVisible(true);

			errorMessage = String.format(UserRegistrationMessage.ERROR_EMPTY_NOT_ALLOWED, UserFormConstant.EMAIL);
			email.setRequiredError(errorMessage);

			errors.add(errorMessage);
		} catch (InvalidValueException e) {
			LOG.info(e.getMessage());

			email.setValidationVisible(true);

			errors.add(e.getMessage());
		}

		try {
			password.validate();
		} catch (EmptyValueException e) {
			password.setValidationVisible(true);

			errorMessage = String.format(UserRegistrationMessage.ERROR_EMPTY_NOT_ALLOWED, UserFormConstant.PASSWORD);
			password.setRequiredError(errorMessage);

			errors.add(errorMessage);
		} catch (InvalidValueException e) {
			LOG.info(e.getMessage());

			password.setValidationVisible(true);

			errors.add(e.getMessage());
		}

		try {
			if (firstName.getValue().trim().length() > 0) {
				firstName.addValidator(new NullValidator(UserRegistrationMessage.ERROR_NULL_NOT_ALLOWED, false));
			}
			firstName.validate();
		} catch (InvalidValueException e) {
			firstName.setValidationVisible(true);

			errors.add(e.getMessage());
		}

		try {
			if (lastName.getValue().trim().length() > 0) {
				lastName.addValidator(new NullValidator(UserRegistrationMessage.ERROR_NULL_NOT_ALLOWED, false));
			}
			lastName.validate();
		} catch (InvalidValueException e) {
			LOG.info(e.getMessage());

			lastName.setValidationVisible(true);

			errors.add(e.getMessage());
		}
		return errors;
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		btnSave.addClickListener(e -> h.onChange());
		btnDelete.addClickListener(e -> h.onChange());
	}

}
