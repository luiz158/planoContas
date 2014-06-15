package br.com.pc.ui.view;

import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import br.com.pc.business.ClinicaBC;
import br.com.pc.domain.Clinica;
import br.com.pc.domain.Conta;
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

		descricao = FieldFactoryUtil.createTextField("DESCRICAO");

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
		
		GridLayout gl = new GridLayout(5,1);
		gl.setSpacing(true);
		
		gl.addComponent(descricao);

		gl.addComponent(btAdd);
		gl.addComponent(btSave);
		gl.addComponent(btRem);
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
		
		return bean;
	}

	public void setBean(Clinica bean) {
		this.bean = bean;
		if (bean != null) {
			descricao.setValue(bean.getDescricao());
		}
	}
	
}
