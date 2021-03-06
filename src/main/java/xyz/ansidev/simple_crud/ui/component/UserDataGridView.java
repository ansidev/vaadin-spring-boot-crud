package xyz.ansidev.simple_crud.ui.component;

import java.util.ArrayList;
import java.util.List;

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
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

import xyz.ansidev.simple_crud.constant.AppConstant;
import xyz.ansidev.simple_crud.constant.CssConstant;
import xyz.ansidev.simple_crud.constant.HtmlTag;
import xyz.ansidev.simple_crud.constant.UserFormConstant;
import xyz.ansidev.simple_crud.entity.User;
import xyz.ansidev.simple_crud.message.UserRegistrationMessage;
import xyz.ansidev.simple_crud.repository.UserRepository;
import xyz.ansidev.simple_crud.ui.model.UserModel;
import xyz.ansidev.simple_crud.util.CustomStringUtils;
import xyz.ansidev.simple_crud.util.HtmlUtils;
import xyz.ansidev.simple_crud.util.transformer.UserTransformer;

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

		// Allow HTML Code in form caption
		userRegistrationForm.setCaptionAsHtml(true);

		// Set button width to 120px
		addUserButton.setWidth(120, Unit.PIXELS);
		addUserButton.addClickListener(e -> {
			userRegistrationForm.setCaption(HtmlUtils.renderHtmlCode(HtmlTag.H2, UserFormConstant.FORM_CAPTION_ADD));
			this.userRegistrationForm.saveUser(new User(), true);
		});

		// Set filter placeholder
		filter.setInputPrompt(UserRegistrationMessage.FILTER_PLACEHOLDER);

		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listUsers(e.getText()));

		final HorizontalLayout actionComponents = new HorizontalLayout(filter, addUserButton);
		actionComponents.setSpacing(true);
		actionComponents.setHeight(100, Unit.PERCENTAGE);

		// Set full width and full height
		grid.setSizeFull();

		// Set which column will be displayed
		grid.setColumns(AppConstant.COLUMN_ID, AppConstant.COLUMN_USERNAME, AppConstant.COLUMN_EMAIL,
				AppConstant.COLUMN_FIRST_NAME, AppConstant.COLUMN_LAST_NAME, AppConstant.COLUMN_CREATED_AT,
				AppConstant.COLUMN_UPDATED_AT, AppConstant.COLUMN_ACTIONS);

		// Set column header caption and sortable for generated columns
		Column createdAt = grid.getColumn(AppConstant.COLUMN_CREATED_AT);
		Column updatedAt = grid.getColumn(AppConstant.COLUMN_UPDATED_AT);
		Column actions = grid.getColumn(AppConstant.COLUMN_ACTIONS);

		createdAt.setHeaderCaption(CustomStringUtils.splitCamelCase(AppConstant.COLUMN_CREATED_AT));
		updatedAt.setHeaderCaption(CustomStringUtils.splitCamelCase(AppConstant.COLUMN_UPDATED_AT));

		actions.setRenderer(new ButtonRenderer(e -> {
			UserModel userModel = (UserModel) e.getItemId();
			UserTransformer userTransformer = new UserTransformer();
			User userToDelete = userTransformer.transformToModel(userModel);
			// Delete user from database
			userRepository.delete(userToDelete);
			// Remove user from data grid view
			grid.getContainerDataSource().removeItem(e.getItemId());
		}));

		// Join two columns into a parent column
		HeaderRow row = grid.prependHeaderRow();
		row.join(AppConstant.COLUMN_FIRST_NAME, AppConstant.COLUMN_LAST_NAME)
				.setHtml(HtmlUtils.renderHtmlCode(HtmlTag.STRONG, UserFormConstant.FULL_NAME));

		// Sort by Column ID
		grid.sort(AppConstant.COLUMN_ID);

		// Connect selected User to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				userRegistrationForm.setVisible(false);
			} else {
				userRegistrationForm
						.setCaption(HtmlUtils.renderHtmlCode(HtmlTag.H2, UserFormConstant.FORM_CAPTION_EDIT));
				UserModel userModel = (UserModel) grid.getSelectedRow();
				UserTransformer transformer = new UserTransformer();
				User user = transformer.transformToModel(userModel);
				userRegistrationForm.saveUser(user, false);
			}
		});

		// Set right align for "Created At" and "Updated At" columns
		grid.setCellStyleGenerator(cellReference -> {
			Object pid = cellReference.getPropertyId();
			if (AppConstant.COLUMN_CREATED_AT.equals(pid) || AppConstant.COLUMN_UPDATED_AT.equals(pid)) {
				// When the current cell is number such as age, align text to right
				return CssConstant.ALIGN_RIGHT;
			} else {
				return null;
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

			// Set filter field width based on column
			if (pid.equals(AppConstant.COLUMN_ID)) {
				filterField.setColumns(5);
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
		GeneratedPropertyContainer extendUserBeanItemContainer = new GeneratedPropertyContainer(userBeanItemContainer);
		extendUserBeanItemContainer.addGeneratedProperty(AppConstant.COLUMN_ACTIONS,
				new PropertyValueGenerator<String>() {

					@Override
					public String getValue(Item item, Object itemId, Object propertyId) {
						return UserFormConstant.BUTTON_DELETE;
					}

					@Override
					public Class<String> getType() {
						return String.class;
					}
				});
		grid.setContainerDataSource(extendUserBeanItemContainer);
	}

}
