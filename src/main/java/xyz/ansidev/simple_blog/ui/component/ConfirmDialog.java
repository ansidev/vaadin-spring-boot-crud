package xyz.ansidev.simple_blog.ui.component;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import xyz.ansidev.simple_blog.constant.AppConstant;

/**
 * 
 * @author Le Minh Tri This class allow developer to render a confirm dialog quickly. <br>
 *         Reference link: http://demo.vaadin.com/sampler/#ui/structure/popup-view <br>
 *
 */
@SuppressWarnings("serial")
public class ConfirmDialog implements PopupView.Content {

	private final VerticalLayout dialogLayout;
	private final CssLayout buttonGroup;
	private final TextField textField;
	private final Button okButton, cancelButton;
	private boolean isConfirmed = false;

	public ConfirmDialog(String message) {
		dialogLayout = new VerticalLayout();
		buttonGroup = new CssLayout();
		textField = new TextField(message);
		okButton = new Button(AppConstant.CAPTION_OK_BUTTON);
		cancelButton = new Button(AppConstant.CAPTION_CANCEL_BUTTON);
		renderLayout();
	}

	private void renderLayout() {
		buttonGroup.addComponents(okButton, cancelButton);
		dialogLayout.addComponents(textField, buttonGroup);
	}

	@Override
	public String getMinimizedValueAsHTML() {
		return String.valueOf(isConfirmed);
	}

	@Override
	public Component getPopupComponent() {
		return dialogLayout;
	}

}
