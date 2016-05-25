package xyz.ansidev.simple_crud.ui.component;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.ConfirmButton;

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
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import xyz.ansidev.simple_crud.constant.AppConstant;
import xyz.ansidev.simple_crud.constant.HtmlTag;
import xyz.ansidev.simple_crud.constant.UserFormConstant;
import xyz.ansidev.simple_crud.encoder.bcrypt.BCrypt;
import xyz.ansidev.simple_crud.entity.User;
import xyz.ansidev.simple_crud.handler.ChangeHandler;
import xyz.ansidev.simple_crud.message.UserRegistrationMessage;
import xyz.ansidev.simple_crud.repository.UserRepository;
import xyz.ansidev.simple_crud.util.HtmlUtils;
import xyz.ansidev.simple_crud.util.UserUtils;
import xyz.ansidev.simple_crud.util.validator.IFormValidator;

@SpringComponent
@UIScope
public class UserRegistrationForm extends FormLayout implements IFormValidator {

//	private static final Logger LOG = LoggerFactory.getLogger(UserRegistrationForm.class);

	private static final long serialVersionUID = 1L;

	private final UserRepository userRepository;

	private User user;

	private boolean isNewUser;

	final TextField username = new TextField(UserFormConstant.USERNAME);
	final TextField email = new TextField(UserFormConstant.EMAIL);
	final PasswordField password = new PasswordField(UserFormConstant.PASSWORD);
	final TextField firstName = new TextField(UserFormConstant.FIRST_NAME);
	final TextField lastName = new TextField(UserFormConstant.LAST_NAME);
	final OptionGroup gender = new OptionGroup(UserFormConstant.GENDER);

	ArrayList<String> errorMessages = new ArrayList<String>();

	/* Action Buttons */
	Button btnSave = new Button(UserFormConstant.BUTTON_SAVE, FontAwesome.SAVE);
	ConfirmButton btnDelete = new ConfirmButton(FontAwesome.TRASH, UserFormConstant.BUTTON_DELETE, UserRegistrationMessage.MESSAGE_CONFIRM_DELETE,
			this::remove);
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

		UserUtils.settingField(username, FontAwesome.USER, AppConstant.FULL_WIDTH, Unit.PERCENTAGE, true);
		UserUtils.settingField(email, FontAwesome.ENVELOPE, AppConstant.FULL_WIDTH, Unit.PERCENTAGE, true);
		UserUtils.settingField(password, FontAwesome.KEY, AppConstant.FULL_WIDTH, Unit.PERCENTAGE, true);
		UserUtils.settingField(firstName, FontAwesome.USER, AppConstant.FULL_WIDTH, Unit.PERCENTAGE, false);
		UserUtils.settingField(lastName, FontAwesome.USER, AppConstant.FULL_WIDTH, Unit.PERCENTAGE, false);

		gender.addItems(UserFormConstant.GENDER_MALE, UserFormConstant.GENDER_FEMALE);

		// Set validator for form fields
		setValidator();

		actionButtons.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		btnSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btnSave.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		btnDelete.setStyleName(ValoTheme.BUTTON_DANGER);

		// Wire action buttons to save, delete, reset and cancel
		btnSave.addClickListener(e -> {

			boolean isUserPasswordChanged = UserUtils.checkIfUserPasswordChanged(this.user, this.userRepository);

			// Validate form fields, must validate before hash password
			validate();

			// Hash user 's password if not empty or not null
			String originalPassword = this.user.getPassword();
			if (!originalPassword.isEmpty()) {
				String hashedPassword = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
				this.user.setPassword(hashedPassword);
			}

			// Notification information
			String saveCaption;

			if (this.errorMessages.size() > 0) {
				saveCaption = getAllErrorsAsString(this.errorMessages);
				setVisible(true);
			} else {
				// Save user
				userRepository.save(this.user);

				saveCaption = String.format(UserRegistrationMessage.SAVE_SUCCESS,
						HtmlUtils.renderHtmlCode(HtmlTag.STRONG, username.getValue()));
				if (isUserPasswordChanged) {
					saveCaption += HtmlUtils.getSingleHtmlTag(HtmlTag.BR) + UserRegistrationMessage.PASSWORD_CHANGED;
				}
				setVisible(false);
			}
			saveNotification.setCaption(saveCaption);
			saveNotification.setHtmlContentAllowed(true);
			saveNotification.setPosition(Position.TOP_RIGHT);
			saveNotification.setDelayMsec(3000);
			saveNotification.show(Page.getCurrent());
		});

		btnCancel.addClickListener(e -> this.setVisible(false));
		btnReset.addClickListener(e -> this.saveUser(this.user, this.isNewUser));
	}

	public boolean isNewUser() {
		return isNewUser;
	}

	public void setNewUser(boolean isNewUser) {
		this.isNewUser = isNewUser;
	}

	private String getAllErrorsAsString(ArrayList<String> errors) {
		String errorsToString = AppConstant.EMPTY;
		for (String error : errors) {
			errorsToString += error + HtmlUtils.getSingleHtmlTag(HtmlTag.BR);
		}
		return errorsToString;
	}

	public final void saveUser(User user, boolean isNewUser) {
		final boolean persisted = user.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			User dbUser = userRepository.findOne(user.getId());
			dbUser.setPassword(AppConstant.EMPTY);
			this.user = dbUser;
		} else {
			this.user = user;
		}
		// Bind user properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(this.user, this);

		// A hack to ensure the whole form is visible
		setVisible(true);

		btnSave.focus();
		btnDelete.setVisible(persisted);
		btnCancel.setVisible(true);

		// Select all text in username field automatically
		username.selectAll();
		if (isNewUser != true) {
			this.isNewUser = false;
		} else {
			this.isNewUser = isNewUser;
		}
	}

	/**
	 * Set validators for form fields
	 */
	public void setValidator() {
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

	public void validate() {
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
			email.setValidationVisible(true);

			errors.add(e.getMessage());
		}

		try {
			password.validate();
		} catch (EmptyValueException e) {
			// Allow empty password field if not new user.
			if (this.isNewUser) {
				password.setValidationVisible(true);
				errorMessage = String.format(UserRegistrationMessage.ERROR_EMPTY_NOT_ALLOWED,
						UserFormConstant.PASSWORD);
				password.setRequiredError(errorMessage);

				errors.add(errorMessage);
			}
		} catch (InvalidValueException e) {
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
			lastName.setValidationVisible(true);
			errors.add(e.getMessage());
		}
		this.errorMessages = errors;
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete is clicked
		btnSave.addClickListener(e -> h.onChange());
		btnDelete.addClickListener(e -> h.onChange());
	}

	public void remove(ClickEvent e) {
		userRepository.delete(this.user);
	}
}