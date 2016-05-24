package xyz.ansidev.simple_blog.ui.component;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.Renderer;

import xyz.ansidev.simple_blog.constant.AppConstant;
import xyz.ansidev.simple_blog.constant.HtmlTag;
import xyz.ansidev.simple_blog.constant.UserFormConstant;
import xyz.ansidev.simple_blog.entity.User;
import xyz.ansidev.simple_blog.message.UserRegistrationMessage;
import xyz.ansidev.simple_blog.repository.UserRepository;
import xyz.ansidev.simple_blog.util.HtmlUtils;

public class UserDataGridView extends VerticalLayout {

	private static final long serialVersionUID = 1517211473733148786L;

	final Grid grid = new Grid();

	final TextField filter = new TextField();

	private final Button addUserButton = new Button(String.format(UserFormConstant.BUTTON_ADD, "user"),
			FontAwesome.PLUS);

	private final UserRegistrationForm userRegistrationForm;

	private final UserRepository userRepository;

	@Autowired
	public UserDataGridView(UserRepository userRepository, UserRegistrationForm userRegistrationForm) {
		this.userRegistrationForm = userRegistrationForm;
		this.userRepository = userRepository;

		userRegistrationForm.setCaptionAsHtml(true);

		addUserButton.setWidth(120, Unit.PIXELS);
		addUserButton.addClickListener(e -> {
			userRegistrationForm.setCaption(HtmlUtils.renderHtmlCode(HtmlTag.H2, UserFormConstant.FORM_CAPTION_ADD));
			this.userRegistrationForm.saveUser(new User(), true);
		});

		filter.setInputPrompt(UserRegistrationMessage.FILTER_PLACEHOLDER);
		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listUsers(e.getText()));

		final HorizontalLayout actionComponents = new HorizontalLayout(filter, addUserButton);
		actionComponents.setSpacing(true);
		actionComponents.setHeight(100, Unit.PERCENTAGE);

		grid.setSizeFull();
		grid.setColumns("id", "username", "email", "firstName", "lastName", "createdAt", "updatedAt");
		// grid.setColumns("id", "username", "email", "firstName", "lastName");
		grid.sort("id");
		// Connect selected Customer to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				userRegistrationForm.setVisible(false);
			} else {
				userRegistrationForm
						.setCaption(HtmlUtils.renderHtmlCode(HtmlTag.H2, UserFormConstant.FORM_CAPTION_EDIT));
				userRegistrationForm.saveUser((User) grid.getSelectedRow(), false);
			}
		});

		// grid.getColumn("createdAt").setRenderer(new LocalDateTimeRenderer(AppConstant.DATE_FORMAT_STRING));
//		LocalDateTimeRenderer renderer = new LocalDateTimeRenderer(AppConstant.DATE_FORMAT_STRING);
//		LocalDateTimeToStringConverter converter = new LocalDateTimeToStringConverter(AppConstant.DATE_FORMAT_STRING);
		// grid.getColumn("createdAt").setConverter(new StringToLocalDateTimeConverter(AppConstant.DATE_FORMAT_STRING));
		addComponents(actionComponents, grid);
		setSpacing(true);
		// You shouldn't use .setSizeFull() or setHeight(100, Unit.PERCENTAGE).
		setWidth(100, Unit.PERCENTAGE);

		// Listen changes made by the editor, refresh data from backend
		userRegistrationForm.setChangeHandler(() -> {
			listUsers(filter.getValue());
		});

		// Initialize listing
		listUsers(null);
	}

	/**
	 * Find all matches users by keyword.
	 * 
	 * @param keyword
	 *            User input keyword
	 */
	public void listUsers(String keyword) {
		if (StringUtils.isEmpty(keyword)) {
			grid.setContainerDataSource(new BeanItemContainer<User>(User.class, userRepository.findAll()));
		} else {
			grid.setContainerDataSource(new BeanItemContainer<User>(User.class,
					userRepository.findByUsernameStartsWithIgnoreCase(keyword)));
		}
	}

}
