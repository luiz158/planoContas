package br.com.pc.ui.view.configuracao;

import java.util.HashSet;
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

public class UsuarioView extends AbstractCrudView<Usuario> {

	private static final long serialVersionUID = 1L;

	public UsuarioView() {
		super(new CrudForm<Usuario>(Usuario.class));
	}

	private TwinColSelect grupos =  FieldFactoryUtil.createTwinColSelect("{usuario.label.grupos}","descricao");
	private Set<Grupo> grupoSet;
	private AutoTable tabela;
	
	public void initializeComponents() {
		super.initializeComponents();
		setCaption(EnumMenu.CRUD_USUARIO.getNome());
		
		grupos.setRows(7);
		grupos.setNullSelectionAllowed(true);
		grupos.setMultiSelect(true);
		grupos.setImmediate(true);
		grupos.setLeftColumnCaption("MEMBRO DE");
		grupos.setRightColumnCaption("SELECIONADOS");
		grupos.setWidth("450px");
		
		tabela = getListTable();
		tabela.setPageLength(0);
		tabela.setColumnOrder(new String[] { "id", "login", "dtCriacao", "dtUltimoAcesso", "email","pessoa","grupos"});
		tabela.setColumnHeader("dtCriacao","CRIACAO");
		tabela.setColumnHeader("dtUltimoAcesso","ULTIMO ACESSO");
		
		
		HorizontalLayout hl = new HorizontalLayout();
		
		hl.addComponent(getCrudForm());
		hl.addComponent(grupos);
		
		addComponent(hl);
		addComponent(tabela);

		grupos.addListener(new TwinColSelect.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				grupoSet = (Set<Grupo>)event.getProperty().getValue();
				getCrudForm().getBean().setGrupos(getGrupoList());
			}
		});
	}
	
	public Set<Grupo> getGrupoList(){
		return new HashSet<Grupo>(grupoSet);
	}
	
	public AutoTable getTabela(){
		return tabela;
	}

	public void setGrupos(List<Grupo> list) {
		if (this.grupos != null) {
			this.grupos.setContainerDataSource(CollectionContainer.fromBeans(list));
		}
	}

	public void setGrupo(Set<Grupo> grupos) {
		if (grupos != null) {
			this.grupos.setValue(grupos);
		}
	}

	public void setDeleteButtonEnabled(boolean enabled) {
		getCrudForm().getButtonDelete().setEnabled(enabled);
	}

}
