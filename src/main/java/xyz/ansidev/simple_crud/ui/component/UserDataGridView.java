package xyz.ansidev.simple_crud.ui.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import xyz.ansidev.simple_crud.constant.AppConstant;
import xyz.ansidev.simple_crud.constant.HtmlTag;
import xyz.ansidev.simple_crud.constant.UserFormConstant;
import xyz.ansidev.simple_crud.entity.User;
import xyz.ansidev.simple_crud.message.UserRegistrationMessage;
import xyz.ansidev.simple_crud.repository.UserRepository;
import xyz.ansidev.simple_crud.ui.model.UserModel;
import xyz.ansidev.simple_crud.util.CustomStringUtils;
import xyz.ansidev.simple_crud.util.HtmlUtils;

public class UserDataGridView extends VerticalLayout {

	private static final long serialVersionUID = 1517211473733148786L;

	final Grid grid = new Grid();

	final TextField filter = new TextField();

	private final Button addUserButton = new Button(String.format(UserFormConstant.BUTTON_ADD, "user"),
			FontAwesome.PLUS);

	private final UserRegistrationForm userRegistrationForm;

	private final UserRepository userRepository;

	BeanItemContainer<UserModel> userBeanItemContainer;

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
		grid.setColumns(AppConstant.COLUMN_ID, AppConstant.COLUMN_USERNAME, AppConstant.COLUMN_EMAIL,
				AppConstant.COLUMN_FIRST_NAME, AppConstant.COLUMN_LAST_NAME, AppConstant.COLUMN_CREATED_DATE,
				AppConstant.COLUMN_UPDATED_DATE);
		// Set column header caption and sortable for generated columns
		Column createdDate = grid.getColumn(AppConstant.COLUMN_CREATED_DATE);
		createdDate.setHeaderCaption(CustomStringUtils.splitCamelCase(AppConstant.COLUMN_CREATED_AT));
		createdDate.setSortable(true);

		Column updatedDate = grid.getColumn(AppConstant.COLUMN_UPDATED_DATE);
		updatedDate.setHeaderCaption(CustomStringUtils.splitCamelCase(AppConstant.COLUMN_UPDATED_AT));
		updatedDate.setSortable(true);

		// Join two columns into a parent column
		HeaderRow row = grid.prependHeaderRow();
		row.join(AppConstant.COLUMN_FIRST_NAME, AppConstant.COLUMN_LAST_NAME)
				.setHtml(HtmlUtils.renderHtmlCode(HtmlTag.STRONG, UserFormConstant.FULL_NAME));

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

		// Create a header row to hold column filters
		HeaderRow filterRow = grid.appendHeaderRow();

		// Set up a filter for all columns
		for (Object pid : grid.getContainerDataSource().getContainerPropertyIds()) {
			HeaderCell cell = filterRow.getCell(pid);

			// Have an input field to use for filter
			TextField filterField = new TextField();
			filterField.addStyleName(ValoTheme.TEXTFIELD_TINY);
			filterField.setInputPrompt(AppConstant.CAPTION_FILTER_BOX);

			// Set filter field width
			filterField.setColumns(13);

			// Update filter When the filter input is changed
			filterField.addTextChangeListener(change -> {
				// Can't modify filters so need to replace
				userBeanItemContainer.removeContainerFilters(pid);

				// (Re)create the filter if necessary
				if (!change.getText().isEmpty())
					userBeanItemContainer
							.addContainerFilter(new SimpleStringFilter(pid, change.getText(), true, false));
			});

			cell.setComponent(filterField);
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
		List<User> userList;
		List<UserModel> userModelList = new ArrayList<UserModel>();
		UserModel userModel;

		if (StringUtils.isEmpty(keyword)) {
			userList = userRepository.findAll();
		} else {
			userList = userRepository.findByUsernameStartsWithIgnoreCase(keyword);
		}

		for (User user : userList) {
			userModel = new UserModel(user);
			userModelList.add(userModel);
		}
		userBeanItemContainer = new BeanItemContainer<UserModel>(UserModel.class, userModelList);
		grid.setContainerDataSource(userBeanItemContainer);
	}

}
