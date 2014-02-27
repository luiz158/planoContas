package br.com.pc.ui;

import java.util.ResourceBundle;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;

import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.ui.AutoForm;
import br.gov.frameworkdemoiselle.util.Beans;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

public class ListForm<E> extends AutoForm<E> {

	private static final long serialVersionUID = 1246614603336033228L;

	// private Button buttonSave;

	// private Button buttonClear;

	private Button buttonDelete;

	private BeanItem<E> beanItem;

	protected ResourceBundle bundle;

	private BeanManager beanManager;

	public ListForm(Class<E> cls) {
		super(cls);
		this.bundle = Beans.getReference(ResourceBundle.class);
		this.beanManager = Beans.getBeanManager();
		setInvalidCommitted(false);
	}

	public E getBean() {
		return beanItem.getBean();
	}

	public void setBean(E bean) {
		if (buttonDelete == null) {
			createButtons();
		}
		beanItem = new BeanItem<E>(bean);
		setItemDataSource(beanItem);
	}

	protected void createButtons() {
		buttonDelete = new Button("Imprimir");

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		// buttons.addComponent(getButtonSave());
		buttons.addComponent(getButtonDelete());
		// buttons.addComponent(buttonClear);
		getFooter().addComponent(buttons);

		getButtonDelete().addListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings("serial")
			@Override
			public void buttonClick(ClickEvent event) {
				beanManager.fireEvent(getBean(),
						new AnnotationLiteral<ProcessDelete>() {
						});
			}

		});
	}

	public void setButtonDelete(Button buttonDelete) {
		this.buttonDelete = buttonDelete;
	}

	public Button getButtonDelete() {
		return buttonDelete;
	}

	protected BeanManager getBeanManager() {
		return beanManager;
	}
}
