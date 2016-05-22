package xyz.ansidev.simple_blog.ui;

import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import xyz.ansidev.simple_blog.constant.HtmlTag;
import xyz.ansidev.simple_blog.constant.UserFormConstant;
import xyz.ansidev.simple_blog.entity.User;
import xyz.ansidev.simple_blog.message.UserRegistrationMessage;
import xyz.ansidev.simple_blog.repository.UserRepository;
import xyz.ansidev.simple_blog.util.HtmlUtils;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Title("Simple Blog")
@Theme("valo")
// @Theme("liferay")
// @Theme("chameleon")
// @Theme("reindeer")
@SpringUI
public class BlogUI extends UI {

	private static final Logger LOG = LoggerFactory.getLogger(BlogUI.class);

	private static final long serialVersionUID = 1L;

	private final UserRepository userRepository;

	private final UserRegistrationForm userRegistrationForm;

	private final Grid grid;

	private final TextField filter;

	private final Button addUserButton;

	@Autowired
	public BlogUI(UserRepository userRepository, UserRegistrationForm userRegistrationForm) {
		this.userRepository = userRepository;
		this.userRegistrationForm = userRegistrationForm;
		this.grid = new Grid();
		this.filter = new TextField();
		this.addUserButton = new Button(String.format(UserFormConstant.BUTTON_ADD, "user"), FontAwesome.PLUS);
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		/* Build layout */
		// final CssLayout actionsGroup = new CssLayout(filter, addUserButton);
		// final HorizontalLayout actionComponents = new
		// HorizontalLayout(actionsGroup);
		final HorizontalLayout actionComponents = new HorizontalLayout(filter, addUserButton);
		final VerticalLayout verticalLayout = new VerticalLayout(actionComponents, grid);
		final HorizontalLayout rootLayout = new HorizontalLayout(verticalLayout, userRegistrationForm);

		setContent(rootLayout);

		/* Configure layouts and components */

		// actionsGroup.setWidth(100, Unit.PERCENTAGE);
		actionComponents.setSpacing(true);
//		actionComponents.setWidth(100, Unit.PERCENTAGE);
		actionComponents.setHeight(100, Unit.PERCENTAGE);
		addUserButton.setWidth(120, Unit.PIXELS);

		verticalLayout.setSpacing(true);
		// You shouldn't use .setSizeFull() or setHeight(100, Unit.PERCENTAGE).
		verticalLayout.setWidth(100, Unit.PERCENTAGE);

		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();
		rootLayout.setExpandRatio(verticalLayout, 6);
		rootLayout.setExpandRatio(userRegistrationForm, 4);

		grid.setSizeFull();
		grid.setColumns("id", "username", "email", "firstName", "lastName", "createdAt", "updatedAt");
		grid.sort("id");

		userRegistrationForm.setCaption(HtmlUtils.renderHtmlCode(HtmlTag.H2, UserFormConstant.FORM_CAPTION));
		userRegistrationForm.setCaptionAsHtml(true);

		filter.setInputPrompt(UserRegistrationMessage.FILTER_PLACEHOLDER);
		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listUsers(e.getText()));

		// Connect selected Customer to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				userRegistrationForm.setVisible(false);
			} else {
				userRegistrationForm.saveUser((User) grid.getSelectedRow());
			}
		});

		addUserButton.addClickListener(e -> userRegistrationForm.saveUser(new User()));

		// Listen changes made by the editor, refresh data from backend
		userRegistrationForm.setChangeHandler(() -> {
			LOG.info("setChangeHandler");
			 listUsers(filter.getValue());
		});

		// Initialize listing
		listUsers(null);

	}

	@WebServlet(urlPatterns = "/*", name = "BlogUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = BlogUI.class, productionMode = false)
	public static class BlogUIServlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	}

	@WebServlet(urlPatterns = "/login", name = "LoginUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = BlogUI.class, productionMode = false)
	public static class LoginUIServlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	}

	/**
	 * Find all matches users by keyword.
	 * 
	 * @param keyword
	 *            User input keyword
	 */
	private void listUsers(String keyword) {
		if (StringUtils.isEmpty(keyword)) {
			grid.setContainerDataSource(new BeanItemContainer<User>(User.class, userRepository.findAll()));
		} else {
			grid.setContainerDataSource(new BeanItemContainer<User>(User.class,
					userRepository.findByUsernameStartsWithIgnoreCase(keyword)));
		}
	}

}
