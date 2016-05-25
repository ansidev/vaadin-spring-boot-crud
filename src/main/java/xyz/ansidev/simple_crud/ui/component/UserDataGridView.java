package xyz.ansidev.simple_crud.ui.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

import xyz.ansidev.simple_crud.constant.AppConstant;
import xyz.ansidev.simple_crud.constant.HtmlTag;
import xyz.ansidev.simple_crud.constant.UserFormConstant;
import xyz.ansidev.simple_crud.entity.User;
import xyz.ansidev.simple_crud.message.UserRegistrationMessage;
import xyz.ansidev.simple_crud.repository.UserRepository;
import xyz.ansidev.simple_crud.util.HtmlUtils;

public class UserDataGridView extends VerticalLayout {

	private static final long serialVersionUID = 1517211473733148786L;

	final Grid grid = new Grid();

	final TextField filter = new TextField();

	private final Button addUserButton = new Button(String.format(UserFormConstant.BUTTON_ADD, "user"),
			FontAwesome.PLUS);

	private final UserRegistrationForm userRegistrationForm;

	private final UserRepository userRepository;

	BeanItemContainer<User> userBeanItemContainer;

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
		grid.setColumns("id", "username", "email", "firstName", "lastName", "createdAt", "updatedAt",
				AppConstant.COLUMN_ACTIONS);

		HeaderRow row = grid.prependHeaderRow();
		row.join("firstName", "lastName").setHtml(HtmlUtils.renderHtmlCode(HtmlTag.STRONG, UserFormConstant.FULL_NAME));

		grid.sort("id");
		// Connect selected User to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				userRegistrationForm.setVisible(false);
			} else {
				userRegistrationForm
						.setCaption(HtmlUtils.renderHtmlCode(HtmlTag.H2, UserFormConstant.FORM_CAPTION_EDIT));
				userRegistrationForm.saveUser((User) grid.getSelectedRow(), false);
			}
		});

		grid.getColumn(AppConstant.COLUMN_ACTIONS).setRenderer(new ButtonRenderer(e -> {
			User userToDelete = (User) e.getItemId();
			userRepository.delete(userToDelete);
			grid.getContainerDataSource().removeItem(e.getItemId());
		}));

		// Create a header row to hold column filters
		HeaderRow filterRow = grid.appendHeaderRow();

		// Set up a filter for all columns
		for (Object pid : grid.getContainerDataSource().getContainerPropertyIds()) {
			HeaderCell cell = filterRow.getCell(pid);

			// Have an input field to use for filter
			TextField filterField = new TextField();
			filterField.addStyleName(ValoTheme.TEXTFIELD_TINY);
			filterField.setInputPrompt("Filter");

			// Set filter field width based on data type
			if (pid.equals(AppConstant.COLUMN_ID)) {
				filterField.setColumns(5);
				cell.setStyleName("align-right");
			} else {
				filterField.setColumns(7);
			}

			// Update filter When the filter input is changed
			filterField.addTextChangeListener(change -> {
				// Can't modify filters so need to replace
				userBeanItemContainer.removeContainerFilters(pid);

				// (Re)create the filter if necessary
				if (!change.getText().isEmpty())
					userBeanItemContainer
							.addContainerFilter(new SimpleStringFilter(pid, change.getText(), true, false));
			});

			if (!AppConstant.COLUMN_ACTIONS.equals(pid)) {
				cell.setComponent(filterField);
			}
		}

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
	@SuppressWarnings("serial")
	public void listUsers(String keyword) {
		// BeanItemContainer<User> userBeanItemContainer;
		if (StringUtils.isEmpty(keyword)) {
			userBeanItemContainer = new BeanItemContainer<User>(User.class, userRepository.findAll());
		} else {
			userBeanItemContainer = new BeanItemContainer<User>(User.class,
					userRepository.findByUsernameStartsWithIgnoreCase(keyword));
		}
		GeneratedPropertyContainer extendUserBeanItemContainer = new GeneratedPropertyContainer(userBeanItemContainer);
		extendUserBeanItemContainer.addGeneratedProperty(AppConstant.COLUMN_ACTIONS,
				new PropertyValueGenerator<String>() {

					@Override
					public String getValue(Item item, Object itemId, Object propertyId) {
						return UserFormConstant.BUTTON_DELETE; // The caption
					}

					@Override
					public Class<String> getType() {
						return String.class;
					}
				});
		grid.setContainerDataSource(extendUserBeanItemContainer);

		// Render a button that deletes the data row (item)
	}

}
