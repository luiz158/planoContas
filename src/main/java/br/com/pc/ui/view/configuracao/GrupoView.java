package br.com.pc.ui.view.configuracao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.com.pc.domain.configuracao.EnumMenu;
import br.com.pc.domain.configuracao.Grupo;
import br.com.pc.domain.configuracao.Usuario;
import br.com.pc.util.components.FieldFactoryUtil;
import br.gov.frameworkdemoiselle.template.AbstractCrudView;
import br.gov.frameworkdemoiselle.ui.AutoTable;
import br.gov.frameworkdemoiselle.ui.CrudForm;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TwinColSelect;

public class GrupoView extends AbstractCrudView<Grupo> {

	private static final long serialVersionUID = 1L;

	public GrupoView() {
		super(new CrudForm<Grupo>(Grupo.class));
	}

	private TwinColSelect usuarios =  FieldFactoryUtil.createTwinColSelect("{grupo.label.usuarios}","login");
	private Set<Usuario> usuarioSet;
	private AutoTable tabela;
	
	public void initializeComponents() {
		super.initializeComponents();
		setCaption(EnumMenu.CRUD_GRUPO.getNome());
		
		usuarios.setRows(7);
		usuarios.setNullSelectionAllowed(true);
		usuarios.setMultiSelect(true);
		usuarios.setImmediate(true);
		usuarios.setLeftColumnCaption("USUÁRIOS");
		usuarios.setRightColumnCaption("SELECIONADOS");
		usuarios.setWidth("350px");
		
		tabela = getListTable();
		tabela.setPageLength(0);
		tabela.setColumnOrder(new String[] { "id", "descricao", "empresas", "usuarios"});
		
		HorizontalLayout hl = new HorizontalLayout();
		
		hl.addComponent(getCrudForm());
		hl.addComponent(usuarios);
		hl.setSpacing(true);
		hl.setMargin(true);
		
		addComponent(hl);
		addComponent(tabela);

		usuarios.addListener(new TwinColSelect.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				usuarioSet = (Set<Usuario>)event.getProperty().getValue();
				getCrudForm().getBean().setUsuarios((Set<Usuario>) getUsuarioList());
			}
		});
	}
	
	public List<Usuario> getUsuarioList(){
		return new ArrayList<Usuario>(usuarioSet);
	}
	
	public AutoTable getTabela(){
		return tabela;
	}

	public void setUsuarios(List<Usuario> list) {
		if (this.usuarios != null) {
			this.usuarios.setContainerDataSource(CollectionContainer.fromBeans(list));
		}
	}

	public void setUsuario(Set<Usuario> usuarios) {
		if (usuarios != null) {
			this.usuarios.setValue(usuarios);
		}
	}
	
	public void setDeleteButtonEnabled(boolean enabled) {
		getCrudForm().getButtonDelete().setEnabled(enabled);
	}

}
