package br.com.pc.ui.view.configuracao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.com.pc.business.configuracao.GrupoBC;
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

public class Usuario2View extends BaseVaadinView implements Button.ClickListener {

	private static final long serialVersionUID = 1L;

	@Inject	private BeanManager beanManager;
//	@Inject private UsuarioBC usuarioBC;
	@Inject private GrupoBC grupoBC;
	
	private Usuario bean;

	private TwinColSelect grupos = new TwinColSelect();
	private TextField login;
	private TextField senha;
	private Panel dados;

	private Button btAdd;
	private Button btRem;
	
	private Table tabela;
	
	public void initializeComponents() {
		setCaption(EnumMenu.CRUD_USUARIO.getNome());
		setSpacing(true);
		setMargin(true);

		grupos =  FieldFactoryUtil.createTwinColSelect("GRUPOS","descricao");

		login = FieldFactoryUtil.createTextField("LOGIN");
		senha = FieldFactoryUtil.createTextField("SENHA");
		btAdd = new Button();
		btRem = new Button();
		
		login.setImmediate(true);
		senha.setImmediate(true);
		grupos.setImmediate(true);
		
		montaPainel();
		montaTabela();
		addListener();
		
		addComponent(dados);
		addComponent(tabela);
	}
	
	private void montaPainel(){
		dados = new Panel();
		
		grupos.setRows(6);
		grupos.setNullSelectionAllowed(true);
		grupos.setMultiSelect(true);
		grupos.setImmediate(true);
		grupos.setLeftColumnCaption("MEMBRO DE");
		grupos.setRightColumnCaption("SELECIONADOS");
		grupos.setWidth("360px");
		grupos.setItemCaptionPropertyId("descricao");
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		HorizontalLayout hl2 = new HorizontalLayout();
		hl2.setSpacing(true);
		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);

		vl.addComponent(login);
		vl.addComponent(senha);
		hl.addComponent(grupos);
		hl.addComponent(vl);
		
		hl2.addComponent(btAdd);
		hl2.addComponent(btRem);
		btAdd.setIcon(new ThemeResource("icons/16/save_16.png"));
		btRem.setIcon(new ThemeResource("icons/16/recycle_16.png"));
		btAdd.setDescription("Salva registro.");
		btRem.setDescription("Exclui registro.");
		
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

		tabela.addContainerProperty("usuario.id", Long.class,  null);
		tabela.addContainerProperty("usuario.login", String.class,  null);
		tabela.addContainerProperty("usuario.ultimoAcesso", Date.class,  null);
		tabela.addContainerProperty("usuario.grupos", List.class,  null);

		tabela.setVisibleColumns(new Object[]{"usuario.id","usuario.login","usuario.ultimoAcesso","usuario.grupos",});
		
		tabela.setColumnHeaders(new String[]{"id","login","ultimo acesso","grupos",});
		
	}
	private void addListener(){
		btAdd.addListener(this);
		btRem.addListener(this);
		
		tabela.addListener(new Table.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
//				setBean((Usuario)event.getProperty().getValue());	
				bean = (Usuario)event.getProperty().getValue();
				if (bean==null){
					bean = new Usuario();
				}
				beanManager.fireEvent(Usuario2View.this, new AnnotationLiteral<ProcessItemSelection>() {});
			}
		});
	}

	public void setList(List<Usuario> lista){
		tabela.removeAllItems();
		for (Usuario c : lista) {
			Item itemBean;
			if (tabela.getItem(c)==null){
				itemBean = tabela.addItem(c);
			}else{
				itemBean = tabela.getItem(c);
			}
			try {itemBean.getItemProperty("usuario.id").setValue(c.getId());} catch (Exception e) {}
			try {itemBean.getItemProperty("usuario.login").setValue(c.getLogin());} catch (Exception e) {}
			try {itemBean.getItemProperty("usuario.ultimoAcesso").setValue(c.getDtUltimoAcesso());} catch (Exception e) {}
//			GrupoBC grupoBC = new GrupoBC();
			try {itemBean.getItemProperty("usuario.grupos").setValue(grupoBC.findByUsuario(c));} catch (Exception e) {}
			
		}
	}
	
	public void setGrupos(List<Grupo> list) {
		if (this.grupos != null) {
			this.grupos.setContainerDataSource(CollectionContainer.fromBeans(list));
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton()==btAdd){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessSave>() {});
			bean = new Usuario();
		}
		if (event.getButton()==btRem){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessDelete>() {});
		}
	}

	
	public Usuario getSelected(){
		return bean;
	}
	public Usuario getBean() {
		if (bean==null || bean.getId()==null){
			bean = new Usuario();
		}
		try {} catch (Exception e) {}
		try {bean.setLogin((String)login.getValue());} catch (Exception e) {}
		try {bean.setSenha((String)senha.getValue());} catch (Exception e) {}
		Set<Grupo> t = new HashSet<Grupo>();
		t.addAll((Collection<? extends Grupo>) grupos.getValue());
		bean.setGrupos(new ArrayList<Grupo>((Collection<? extends Grupo>) grupos.getValue()));
		return bean;
	}

	public void setBean(Usuario bean) {
		this.bean = bean;
//		limpar();
		if (bean != null) {
			login.setValue(bean.getLogin());
			senha.setValue(bean.getSenha());
//			GrupoBC grupoBC = new GrupoBC();
			grupos.setValue(grupoBC.findByUsuario(bean));
//			grupos.setValue(bean.getGrupos());
		}
	}


	public void limpar(){
		login.setValue(null);
		senha.setValue(null);
		grupos.setValue(null);
	}
}
