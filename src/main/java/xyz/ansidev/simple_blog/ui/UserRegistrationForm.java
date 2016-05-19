package xyz.ansidev.simple_blog.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import xyz.ansidev.simple_blog.constant.HtmlTag;
import xyz.ansidev.simple_blog.constant.UserFormConstant;
import xyz.ansidev.simple_blog.entity.User;
import xyz.ansidev.simple_blog.handler.ChangeHandler;
import xyz.ansidev.simple_blog.message.UserRegistrationMessage;
import xyz.ansidev.simple_blog.repository.UserRepository;
import xyz.ansidev.simple_blog.util.HtmlUtil;

@SpringComponent
@UIScope
public class UserRegistrationForm extends FormLayout {

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

	Notification registrationNotification = new Notification("");

	@Autowired
	public UserRegistrationForm(UserRepository userRepository) {
		this.userRepository = userRepository;

		setSizeFull();

		username.setIcon(FontAwesome.USER);
		username.setRequired(true);
		username.setSizeFull();

		email.setIcon(FontAwesome.ENVELOPE);
		email.setRequired(true);

		password.setIcon(FontAwesome.KEY);
		password.setRequired(true);

		firstName.setIcon(FontAwesome.USER);
		firstName.setRequired(true);

		lastName.setIcon(FontAwesome.USER);
		lastName.setRequired(true);

//		setValidator();

		addComponents(username, email, password, firstName, lastName);
		addComponent(actionButtons);

		setSpacing(true);
		actionButtons.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		btnSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btnSave.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		btnDelete.setStyleName(ValoTheme.BUTTON_DANGER);

		// Wire action buttons to save, delete, reset and cancel
		btnSave.addClickListener(e -> {
			userRepository.save(user);
			registrationNotification.setCaption(String.format(UserRegistrationMessage.SAVE_SUCCESS,
					HtmlUtil.renderHtmlCode(HtmlTag.STRONG, username.getValue())));
			registrationNotification.setHtmlContentAllowed(true);
			registrationNotification.setIcon(FontAwesome.REGISTERED);
			registrationNotification.setPosition(Position.TOP_RIGHT);
			registrationNotification.setDelayMsec(3000);
			registrationNotification.show(Page.getCurrent());
		});
		btnDelete.addClickListener(e -> userRepository.delete(user));
		// btnReset.addClickListener(e -> editCustomer(user));
		btnCancel.addClickListener(e -> this.setVisible(false));
		setVisible(false);

	}

	public final void saveUser(User user) {
		final boolean persisted = user.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			this.user = userRepository.findOne(user.getId());
		} else {
			this.user = user;
		}
		btnCancel.setVisible(persisted);

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

	private void setValidator() {
		username.addValidator(new NullValidator("Null value is not allowed", false));
		username.addValidator(new StringLengthValidator(UserRegistrationMessage.ERROR_USERNAME_INVALID_LENGTH,
				UserFormConstant.USERNAME_MIN_LENGTH, UserFormConstant.USERNAME_MAX_LENGTH, true));
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		btnSave.addClickListener(e -> h.onChange());
		btnDelete.addClickListener(e -> h.onChange());
	}

}
