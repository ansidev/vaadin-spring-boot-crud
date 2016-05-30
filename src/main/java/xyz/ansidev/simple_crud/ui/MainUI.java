package xyz.ansidev.simple_crud.ui;

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

import xyz.ansidev.simple_crud.constant.AppConstant;
import xyz.ansidev.simple_crud.constant.HtmlTag;
import xyz.ansidev.simple_crud.constant.UserFormConstant;
import xyz.ansidev.simple_crud.repository.UserRepository;
import xyz.ansidev.simple_crud.ui.component.UserDataGridView;
import xyz.ansidev.simple_crud.ui.component.UserRegistrationForm;
import xyz.ansidev.simple_crud.util.HtmlUtils;

/**
 * This UI is the application entry point. A UI may either represent a browser window (or tab) or some part of a html
 * page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be overridden to add component
 * to the user interface and initialize non-component functionality.
 */
@Title(AppConstant.APP_NAME)
@Theme("grape")
// @Theme("liferay")
// @Theme("chameleon")
// @Theme("reindeer")
@SpringUI
public class MainUI extends UI {

	private static final Logger LOG = LoggerFactory.getLogger(MainUI.class);

	private static final long serialVersionUID = 1L;

	private final UserRepository userRepository;

	private final UserRegistrationForm userRegistrationForm;
	private final UserDataGridView userDataGridView;

	@Autowired
	public MainUI(UserRepository userRepository, UserRegistrationForm userRegistrationForm) {
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

	@WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
	public static class BlogUIServlet extends VaadinServlet {
		private static final long serialVersionUID = -8844423569138263543L;
	}

}
