package br.com.pc.ui.view;

import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import br.com.pc.business.ClinicaBC;
import br.com.pc.domain.Clinica;
import br.com.pc.ui.annotation.ProcessAdd;
import br.com.pc.util.components.FieldFactoryUtil;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.template.BaseVaadinView;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

public class ClinicaView extends BaseVaadinView implements Button.ClickListener {

	private static final long serialVersionUID = 1L;

	@Inject	private BeanManager beanManager;
	@Inject ClinicaBC clinicaBC = new ClinicaBC();
	
	public TextField descricao;
	public TextField razaoSocial;
	public TextField responsavel;
	public TextField cnpj;
	public TextField inscricaoEstadual;
	public TextField inscricaoMunicipal;
	public TextField endereco;
	public TextField cep;
	public TextField tel;
	public TextField fax;
	
	private Panel dados;
	
	private Clinica bean;
	
	private Table tabela;
	
	private Button btSave;
	private Button btAdd;
	private Button btRem;
	
	@Override
	public void initializeComponents() {
		setCaption("CLINICA");
		setSpacing(true);
		setMargin(true);
		tabela = new Table();
		bean = new Clinica();

		descricao 	= FieldFactoryUtil.createTextField("DESCRICAO");
		razaoSocial = FieldFactoryUtil.createTextField("RAZÃO SOCIAL");
		responsavel = FieldFactoryUtil.createTextField("RESPONSÁVEL");
		cnpj 		= FieldFactoryUtil.createTextField("CNPJ");
		inscricaoEstadual 	= FieldFactoryUtil.createTextField("IN. ESTADUAL");
		inscricaoMunicipal 	= FieldFactoryUtil.createTextField("IN. MUNICIPAL");
		endereco 	= FieldFactoryUtil.createTextField("ENDEREÇO");
		cep 		= FieldFactoryUtil.createTextField("CEP");
		tel 		= FieldFactoryUtil.createTextField("TEL");
		fax 		= FieldFactoryUtil.createTextField("FAX");

		btSave = new Button();
		btAdd = new Button();
		btRem = new Button();

		descricao.setRequired(true);
		
		montaTabela();
		montaPainel();
		addListener();
		
		addComponent(dados);
		addComponent(tabela);
	}

	private void montaPainel(){
		dados = new Panel();

		GridLayout gMenu = new GridLayout(3,1);
		GridLayout gl = new GridLayout(5,2);
		gl.setSpacing(true);
		gMenu.setSpacing(true);
		
		gl.addComponent(descricao);
		gl.addComponent(razaoSocial);
		gl.addComponent(responsavel);
		gl.addComponent(cnpj);
		gl.addComponent(inscricaoEstadual);
		gl.addComponent(inscricaoMunicipal);
		gl.addComponent(endereco);
		gl.addComponent(cep);
		gl.addComponent(tel);
		gl.addComponent(fax);

		gMenu.addComponent(btAdd);
		gMenu.addComponent(btSave);
		gMenu.addComponent(btRem);
		
		btSave.setIcon(new ThemeResource("icons/16/save_16.png"));
		btRem.setIcon(new ThemeResource("icons/16/recycle_16.png"));
		btSave.setDescription("Atualiza um registro selecionado.");
		btRem.setDescription("Exclui registro.");
		btAdd.setIcon(new ThemeResource("icons/16/add_16.png"));
		btAdd.setDescription("Adiciona um novo registro.");

		gl.setComponentAlignment(btAdd, Alignment.BOTTOM_LEFT);
		gl.setComponentAlignment(btSave, Alignment.BOTTOM_LEFT);
		gl.setComponentAlignment(btRem, Alignment.BOTTOM_LEFT);

		dados.addComponent(gl);
		dados.addComponent(gMenu);
	}
	private void montaTabela(){
		tabela = new Table();
		tabela.setSelectable(true);
		tabela.setImmediate(true);
		tabela.setPageLength(20);
		tabela.setCacheRate(1000);
		tabela.setWidth("100%");

		
		tabela.addContainerProperty("clinica.descricao", String.class,  null);

		tabela.setVisibleColumns(new Object[]{"clinica.descricao"});
		
		tabela.setColumnHeaders(new String[]{"descrição"});
		
	}
	private void addListener(){
		btAdd.addListener(this);
		btSave.addListener(this);
		btRem.addListener(this);
		
		tabela.addListener(new Table.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
//			@SuppressWarnings("serial")
			@Override
			public void valueChange(ValueChangeEvent event) {
				bean = (Clinica)event.getProperty().getValue();	
				if (bean != null) {
//					beanManager.fireEvent(this, new AnnotationLiteral<ProcessItemSelection>() {});
					setBean(bean);
				}else{
					bean = new Clinica();
				}
			}
		});
	}

	public void setList(List<Clinica> lista){
		tabela.removeAllItems();
		for (Clinica c : lista) {
			Item itemBean;
			if (tabela.getItem(c)==null){
				itemBean = tabela.addItem(c);
			}else{
				itemBean = tabela.getItem(c);
			}
			try {itemBean.getItemProperty("clinica.descricao").setValue(c.getDescricao());} catch (Exception e) {}
			
		}
	}
	
	@SuppressWarnings("serial")
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton()==btSave){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessSave>() {});
		}
		if (event.getButton()==btAdd){
			bean = new Clinica();
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessAdd>() {});
			bean = new Clinica();
		}
		if (event.getButton()==btRem){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessDelete>() {});
			bean = new Clinica();
		}
	}

	public Clinica getBean() {
		if (bean==null){
			bean = new Clinica();
		}
		try {bean.setDescricao((String)descricao.getValue());} catch (Exception e) {}
		try {bean.setRazaoSocial((String)razaoSocial.getValue());} catch (Exception e) {}
		try {bean.setResponsavel((String)responsavel.getValue());} catch (Exception e) {}
		try {bean.setCnpj((String)cnpj.getValue());} catch (Exception e) {}
		try {bean.setInscricaoEstadual((String)inscricaoEstadual.getValue());} catch (Exception e) {}
		try {bean.setInscricaoMunicipal((String)inscricaoMunicipal.getValue());} catch (Exception e) {}
		try {bean.setEndereco((String)endereco.getValue());} catch (Exception e) {}
		try {bean.setCep((String)cep.getValue());} catch (Exception e) {}
		try {bean.setTel((String)tel.getValue());} catch (Exception e) {}
		try {bean.setFax((String)fax.getValue());} catch (Exception e) {}

		return bean;
	}

	public void setBean(Clinica bean) {
		this.bean = bean;
		if (bean != null) {
			descricao.setValue(bean.getDescricao());
			razaoSocial.setValue(bean.getRazaoSocial());
			responsavel.setValue(bean.getResponsavel());
			cnpj.setValue(bean.getCnpj());
			inscricaoEstadual.setValue(bean.getInscricaoEstadual());
			inscricaoMunicipal.setValue(bean.getInscricaoMunicipal());
			endereco.setValue(bean.getEndereco());
			cep.setValue(bean.getCep());
			tel.setValue(bean.getTel());
			fax.setValue(bean.getFax());
		}
	}
	
}
