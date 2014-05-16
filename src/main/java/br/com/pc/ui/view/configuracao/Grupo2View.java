package br.com.pc.ui.view.configuracao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.com.pc.business.ClinicaBC;
import br.com.pc.business.configuracao.GrupoBC;
import br.com.pc.business.configuracao.UsuarioBC;
import br.com.pc.domain.Clinica;
import br.com.pc.domain.configuracao.EnumMenu;
import br.com.pc.domain.configuracao.Grupo;
import br.com.pc.domain.configuracao.Usuario;
import br.com.pc.util.components.FieldFactoryUtil;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.template.BaseVaadinView;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

public class Grupo2View extends BaseVaadinView implements Button.ClickListener {

	private static final long serialVersionUID = 1L;

	@Inject	private BeanManager beanManager;
	@Inject private UsuarioBC usuarioBC;
	@Inject private ClinicaBC clinicaBC;
	@Inject private GrupoBC grupoBC;
	
	private Grupo bean;

	private TwinColSelect usuarios = new TwinColSelect();
	private TwinColSelect clinicas = new TwinColSelect();
	private TextField grupo;
//	private TextField senha;
	private Panel dados;

	private Button btAdd;
	private Button btRem;
	
	private Table tabela;
	
	private Set<Usuario> usuarioSet;
	
	public void initializeComponents() {
		setCaption(EnumMenu.CRUD_GRUPO.getNome());
		setSpacing(true);
		setMargin(true);

		usuarios =  FieldFactoryUtil.createTwinColSelect("USUÁRIOS","descricao");
		clinicas =  FieldFactoryUtil.createTwinColSelect("CLINICAS","descricao");
		grupo = FieldFactoryUtil.createTextField("DESCRIÇÃO");
		grupo.setImmediate(true);

//		login = FieldFactoryUtil.createTextField("LOGIN");
//		senha = FieldFactoryUtil.createTextField("SENHA");
		btAdd = new Button();
		btRem = new Button();
		btAdd.setIcon(new ThemeResource("icons/16/save_16.png"));
		btRem.setIcon(new ThemeResource("icons/16/recycle_16.png"));
		btAdd.setDescription("Salva registro.");
		btRem.setDescription("Exclui registro.");
		
//		login.setImmediate(true);
//		senha.setImmediate(true);
		usuarios.setImmediate(true);
		
		montaPainel();
		montaTabela();
		addListener();
		
		addComponent(dados);
		addComponent(tabela);
	}
	
	private void montaPainel(){
		dados = new Panel();
		
		usuarios.setRows(6);
		usuarios.setNullSelectionAllowed(true);
		usuarios.setMultiSelect(true);
		usuarios.setImmediate(true);
		usuarios.setLeftColumnCaption("USUÁRIOS");
		usuarios.setRightColumnCaption("SELECIONADOS");
		usuarios.setWidth("360px");
		usuarios.setItemCaptionPropertyId("login");

		clinicas.setRows(6);
		clinicas.setNullSelectionAllowed(true);
		clinicas.setMultiSelect(true);
		clinicas.setImmediate(true);
		clinicas.setLeftColumnCaption("CLINICAS");
		clinicas.setRightColumnCaption("SELECIONADAS");
		clinicas.setWidth("360px");
		clinicas.setItemCaptionPropertyId("descricao");
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		HorizontalLayout hl2 = new HorizontalLayout();
		hl2.setSpacing(true);
		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);

//		vl.addComponent(login);
//		vl.addComponent(senha);
		hl.addComponent(usuarios);
		hl.addComponent(clinicas);
		hl.addComponent(vl);
		vl.addComponent(grupo);
		hl2.addComponent(btAdd);
		hl2.addComponent(btRem);
		
		hl2.setComponentAlignment(btAdd, Alignment.BOTTOM_LEFT);
		hl2.setComponentAlignment(btRem, Alignment.BOTTOM_LEFT);

		vl.addComponent(hl2);
		vl.setComponentAlignment(hl2, Alignment.BOTTOM_RIGHT);
		
		dados.addComponent(hl);
	}

	private void montaTabela(){
		tabela = new Table();
//		tabela.setLocale(new Locale("pt", "BR"));
		tabela.setSelectable(true);
		tabela.setImmediate(true);

		tabela.setWidth("100%");

		tabela.addContainerProperty("grupo.id", Long.class,  null);
		tabela.addContainerProperty("grupo.descricao", String.class,  null);
//		tabela.addContainerProperty("usuario.ultimoAcesso", Date.class,  null);
		tabela.addContainerProperty("grupo.usuarios", Set.class,  new HashSet<Usuario>());
		tabela.addContainerProperty("grupo.clinicas", List.class,  new ArrayList<Clinica>());

		tabela.setVisibleColumns(new Object[]{"grupo.id","grupo.descricao","grupo.clinicas","grupo.usuarios",});
		
		tabela.setColumnHeaders(new String[]{"id","descrição","clinicas","usuarios",});
		
	}
	private void addListener(){
		btAdd.addListener(this);
		btRem.addListener(this);
		
		tabela.addListener(new Table.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
//				setBean((Usuario)event.getProperty().getValue());	
				bean = (Grupo)event.getProperty().getValue();
				beanManager.fireEvent(Grupo2View.this, new AnnotationLiteral<ProcessItemSelection>() {});
			}
		});
		
		usuarios.addListener(new TwinColSelect.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				usuarioSet = (Set<Usuario>)event.getProperty().getValue();
			}
		});
	}

	public void setList(List<Grupo> lista){
		tabela.removeAllItems();
		for (Grupo c : lista) {
			Item itemBean;
			if (tabela.getItem(c)==null){
				itemBean = tabela.addItem(c);
			}else{
				itemBean = tabela.getItem(c);
			}
			try {itemBean.getItemProperty("grupo.id").setValue(c.getId());} catch (Exception e) {}
			try {itemBean.getItemProperty("grupo.descricao").setValue(c.getDescricao());} catch (Exception e) {}
//			try {itemBean.getItemProperty("grupo.clinicas").setValue(c.getClinicas());} catch (Exception e) {}
			try {itemBean.getItemProperty("grupo.usuarios").setValue(c.getUsuarios());} catch (Exception e) {}
			
//			List<Clinica> g = grupoBC.findClinicas(c.getId());
//			List<Usuario> u = grupoBC.findUsuarios(c.getId());
			try {itemBean.getItemProperty("grupo.clinicas").setValue(grupoBC.findClinicas(c.getId()));} catch (Exception e) {}
//			try {itemBean.getItemProperty("grupo.usuarios").setValue(grupoBC.findUsuarios(c.getId()));} catch (Exception e) {}
			
		}
	}
	
	public void setUsuarios(List<Usuario> list) {
		if (this.usuarios != null) {
			this.usuarios.setContainerDataSource(CollectionContainer.fromBeans(list));
		}
	}
	public void setClinicas(List<Clinica> list) {
		if (this.clinicas != null) {
			this.clinicas.setContainerDataSource(CollectionContainer.fromBeans(list));
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton()==btAdd){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessSave>() {});
			bean = new Grupo();
		}
		if (event.getButton()==btRem){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessDelete>() {});
		}
	}

	
	public Grupo getSelected(){
		return bean;
	}
	@SuppressWarnings("unchecked")
	public Grupo getBean() {
		if (bean==null || bean.getId()==null){
			bean = new Grupo();
		}
		try {} catch (Exception e) {}
		try {bean.setDescricao((String)grupo.getValue());} catch (Exception e) {}
//		bean.setUsuarios(usuarioSet);
		bean.setUsuarios((Set<Usuario>) usuarios.getValue());
		bean.setClinicas(new ArrayList<Clinica>((Collection<? extends Clinica>) clinicas.getValue()));
		return bean;
	}

	public void setBean(Grupo bean) {
		this.bean = bean;
//		limpar();
		if (bean != null) {
			grupo.setValue(bean.getDescricao());
//			GrupoBC grupoBC = new GrupoBC();
//			ClinicaBC clinicaBC = new ClinicaBC();
			usuarios.setValue(bean.getUsuarios());
//			clinicas.setValue(bean.getClinicas());
			if (bean.getId()!=null){
				Grupo g = grupoBC.load(bean.getId());
	//			usuarios.setValue(g.getUsuarios());
	//			usuarioSet = g.getUsuarios();
				clinicas.setValue(g.getClinicas());
			}
			
		}
	}


	public void limpar(){
		grupo.setValue(null);
		usuarios.setValue(null);
		clinicas.setValue(null);
	}
}
