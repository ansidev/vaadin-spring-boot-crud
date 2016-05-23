package xyz.ansidev.simple_blog.ui;

import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

import xyz.ansidev.simple_blog.constant.HtmlTag;
import xyz.ansidev.simple_blog.constant.UserFormConstant;
import xyz.ansidev.simple_blog.repository.UserRepository;
import xyz.ansidev.simple_blog.ui.component.UserDataGridView;
import xyz.ansidev.simple_blog.ui.component.UserRegistrationForm;
import xyz.ansidev.simple_blog.util.HtmlUtils;

/**
 * This UI is the application entry point. A UI may either represent a browser window (or tab) or some part of a html
 * page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be overridden to add component
 * to the user interface and initialize non-component functionality.
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
	private final UserDataGridView userDataGridView;

	@Autowired
	public BlogUI(UserRepository userRepository, UserRegistrationForm userRegistrationForm) {
		this.userRepository = userRepository;
		this.userRegistrationForm = userRegistrationForm;
		this.userDataGridView = new UserDataGridView(userRepository, userRegistrationForm);
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		/* Build layout */
		final HorizontalLayout rootLayout = new HorizontalLayout(userDataGridView, userRegistrationForm);
		setContent(rootLayout);

		/* Configure layouts and components */
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();
		rootLayout.setExpandRatio(userDataGridView, 6);
		rootLayout.setExpandRatio(userRegistrationForm, 4);
	}

	@WebServlet(urlPatterns = "/*", name = "BlogUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = BlogUI.class, productionMode = false)
	public static class BlogUIServlet extends VaadinServlet {
		private static final long serialVersionUID = -8844423569138263543L;
	}

}
