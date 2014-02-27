package br.com.pc.ui.view.template;

import java.util.List;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.gov.frameworkdemoiselle.template.BaseVaadinView;
import br.gov.frameworkdemoiselle.template.CrudView;
import br.gov.frameworkdemoiselle.ui.AutoTable;
import br.gov.frameworkdemoiselle.ui.CrudForm;
import br.gov.frameworkdemoiselle.ui.PagedContainer;
import br.gov.frameworkdemoiselle.ui.ResultListHandler;
import br.gov.frameworkdemoiselle.util.Reflections;

public class AbstractListView<E> extends BaseVaadinView implements CrudView<E> {

	private static final long serialVersionUID = 1L;

	private Class<E> beanClass;

	private CrudForm<E> crudForm;

	private AutoTable listTable;

	public AbstractListView(CrudForm<E> crudForm) {
		beanClass = Reflections.getGenericTypeArgument(getClass(), 0);
		this.crudForm = crudForm;
		listTable = new AutoTable(beanClass);
	}

	public void setDeleteButtonEnabled(boolean enabled) {
		getCrudForm().getButtonDelete().setEnabled(enabled);
	}


	private E instantiate() {
		try {
			return beanClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initializeComponents() {
		setBean(instantiate());
		listTable.setWidth("100%");
	}

	@Override
	public E getBean() {
		return getCrudForm().getBean();
	}

	@Override
	public void setBean(E bean) {
		getCrudForm().setBean(bean);
	}

	@Override
	public void clear() {
		getCrudForm().setBean(instantiate());
	}

	public void setCrudForm(CrudForm<E> crudForm) {
		this.crudForm = crudForm;
	}

	public CrudForm<E> getCrudForm() {
		return crudForm;
	}

	public void setListTable(AutoTable listTable) {
		this.listTable = listTable;
	}

	public void setList(ResultListHandler<E> resultListHandler) {
		getListTable().setContainerDataSource(PagedContainer.create(beanClass, resultListHandler));
	}

	@Override
	public void setList(List<E> list) {
		getListTable().setContainerDataSource(CollectionContainer.fromBeans(list));
	}

	public AutoTable getListTable() {
		return listTable;
	}

}
