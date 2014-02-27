package br.com.pc.ui.view;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.com.pc.business.ClinicaBC;
import br.com.pc.domain.Clinica;
import br.com.pc.domain.Conta;
import br.com.pc.domain.Fluxo;
import br.com.pc.util.SimNaoColumnGenerator;
import br.com.pc.util.components.FieldFactoryUtil;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.template.BaseVaadinView;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;

public class ContaView extends BaseVaadinView implements Button.ClickListener {

	private static final long serialVersionUID = 1L;

	@Inject	private BeanManager beanManager;
	@Inject ClinicaBC clinicaBC = new ClinicaBC();
	
	private TextField conta;
	private TextField descricao;
	private CheckBox totalizadora;
	private ComboBox contaPai;
	
	private Panel dados;
	
	private Conta bean;
	
	private Table tabela;
	
	private Button btAdd;
	private Button btRem;

//	protected Property clinica;
	
	private TwinColSelect clinicas = new TwinColSelect();
	
	DecimalFormat df = new DecimalFormat("#,##0.00");
	
	@Override
	public void initializeComponents() {
		setCaption("CONTA");
		setSpacing(true);
		setMargin(true);
		tabela = new Table();
		bean = new Conta();

		conta = FieldFactoryUtil.createTextField("CONTA");
		descricao = FieldFactoryUtil.createTextField("DESCRICAO");
		contaPai = FieldFactoryUtil.createComboBox("CONTA PAI", "descricao");
		totalizadora = FieldFactoryUtil.createCheckBox("TOTALIZADORA");
		clinicas =  FieldFactoryUtil.createTwinColSelect("CLINICAS","descricao");

		btAdd = new Button();
		btRem = new Button();

		conta.setRequired(true);
		descricao.setRequired(true);
		conta.setRequiredError("Ítem obrigatório");
		descricao.setRequiredError("Ítem obrigatório");
		
		montaTabela();
		montaPainel();
		addListener();
		
		addComponent(dados);
		addComponent(tabela);
	}

	private void montaPainel(){
		dados = new Panel();

		clinicas.setRows(4);
		clinicas.setNullSelectionAllowed(true);
		clinicas.setMultiSelect(true);
		clinicas.setImmediate(true);
		clinicas.setLeftColumnCaption("CLINICAS");
		clinicas.setRightColumnCaption("SELECIONADAS");
		clinicas.setWidth("300px");
		clinicas.setItemCaptionPropertyId("descricao");
		
		GridLayout gl = new GridLayout(5,2);
//		hl.setMargin(true);
		gl.setSpacing(true);
		
//		dados.setContent(hl);

		gl.addComponent(conta,0,0);
		gl.addComponent(descricao,0,1);
		gl.addComponent(totalizadora,1,0);
		gl.addComponent(contaPai,1,1);
		gl.addComponent(clinicas,2,0,2,1);
		
		gl.addComponent(btAdd,3,1);
		gl.addComponent(btRem,4,1);
		btAdd.setIcon(new ThemeResource("icons/16/save_16.png"));
		btRem.setIcon(new ThemeResource("icons/16/recycle_16.png"));
		btAdd.setDescription("Salva registro.");
		btRem.setDescription("Exclui registro.");
		
		gl.setComponentAlignment(totalizadora, Alignment.MIDDLE_LEFT);
		gl.setComponentAlignment(btAdd, Alignment.BOTTOM_LEFT);
		gl.setComponentAlignment(btRem, Alignment.BOTTOM_LEFT);
		
		dados.addComponent(gl);
//		dados.addComponent(tabela);
	}
	private void montaTabela(){
		tabela = new Table(){
			private static final long serialVersionUID = 1L;

			@Override
		    protected String formatPropertyValue(Object rowId, Object colId, Property property) {
		        // Format by property type
		        if (property.getType() == Date.class && property.getValue()!=null) {
		        	if("data".equals(colId)){
		        		SimpleDateFormat df =
			                new SimpleDateFormat("dd/MM/yy");
			            return df.format((Date)property.getValue());
		        	}else if("hora".equals(colId)||"apanha".equals(colId)){
			            SimpleDateFormat df =
			                new SimpleDateFormat("HH:mm");
			            return df.format((Date)property.getValue());
		        	}else{
			            SimpleDateFormat df =
			                new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			            return df.format((Date)property.getValue());
		        	}
		        }else if(property.getType() == Boolean.class && property.getValue()!=null){
		        	if ((Boolean)property.getValue()){
		        		return "S";
		        	}else{
		        		return "N";
		        	}
		        }else if(property.getType() == BigDecimal.class && property.getValue()!=null){
		        	return df.format((BigDecimal)property.getValue());
		        }

		        return super.formatPropertyValue(rowId, colId, property);
		    }
		};
		tabela.setCellStyleGenerator(new Table.CellStyleGenerator() {
			@Override
		      public String getStyle(Object itemId, Object propertyId) {
				if (propertyId == null) { //para linha inteira
					Item row = tabela.getItem(itemId);
					if (row!=null && row.getItemProperty("conta.totalizadora")!=null && 
							row.getItemProperty("conta.totalizadora").getValue()!=null &&
							(Boolean)row.getItemProperty("conta.totalizadora").getValue()) {
						return "bold";
					}
				}
		        return null;
		      }
			
		    });
//		tabela.setLocale(new Locale("pt", "BR"));
		tabela.setSelectable(true);
		tabela.setImmediate(true);
		tabela.setPageLength(20);
		tabela.setCacheRate(1000);
		tabela.setWidth("100%");

		
		tabela.addContainerProperty("conta.conta", String.class,  null);
		tabela.addContainerProperty("conta.descricao", String.class,  null);
		tabela.addContainerProperty("conta.totalizadora", Boolean.class,  null);
		tabela.addContainerProperty("conta.contaPai", Conta.class,  null);
		tabela.addContainerProperty("conta.clinicas", List.class,  null);

		tabela.setVisibleColumns(new Object[]{"conta.conta","conta.descricao","conta.totalizadora","conta.contaPai","conta.clinicas",});
		
		tabela.setColumnHeaders(new String[]{"conta","descricao","totalizadora","contaPai","clinicas",});
		
		tabela.addGeneratedColumn("conta.totalizadora", new SimNaoColumnGenerator());
		
	}
	private void addListener(){
		btAdd.addListener(this);
		btRem.addListener(this);
		
		tabela.addListener(new Table.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				bean = (Conta)event.getProperty().getValue();	
				if (bean != null) {
//					conta.setValue(bean.getConta());
//					descricao.setValue(bean.getDescricao());
//					totalizadora.setValue(bean.getTotalizadora());
//					contaPai.setValue(bean.getContaPai());
//					try {clinicas.setValue(bean.getClinicas());} catch (Exception e) {clinicas.setValue(null);}
					beanManager.fireEvent(bean, new AnnotationLiteral<ProcessItemSelection>() {});
				}else{
//					limpar();
					bean = new Conta();
				}
			}
		});
	}

	public void setList(List<Conta> lista){
//		tabela.removeAllItems();
		for (Conta c : lista) {
			Item itemBean;
			if (tabela.getItem(c)==null){
				itemBean = tabela.addItem(c);
			}else{
				itemBean = tabela.getItem(c);
			}
			try {itemBean.getItemProperty("conta.conta").setValue(c.getConta());} catch (Exception e) {}
			try {itemBean.getItemProperty("conta.descricao").setValue(c.getDescricao());} catch (Exception e) {}
			try {itemBean.getItemProperty("conta.totalizadora").setValue(c.getTotalizadora());} catch (Exception e) {}
			try {itemBean.getItemProperty("conta.contaPai").setValue(c.getContaPai());} catch (Exception e) {}
			
		 	List<Clinica> c2 = clinicaBC.findByConta(c);
			try {itemBean.getItemProperty("conta.clinicas").setValue(c2);} catch (Exception e) {}
//			try {itemBean.getItemProperty("conta.clinicas").setValue(c.getClinicas());} catch (Exception e) {}
			
		}
	}
	
	@SuppressWarnings("serial")
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton()==btAdd){
			beanManager.fireEvent(getBean(), new AnnotationLiteral<ProcessSave>() {});
			bean = new Conta();
		}
		if (event.getButton()==btRem){
			beanManager.fireEvent(bean, new AnnotationLiteral<ProcessDelete>() {});
			bean = new Conta();
		}
	}

	public Conta getBean() {
		if (bean==null || bean.getId()==null){
			bean = new Conta();
		}
		try {} catch (Exception e) {}
		try {bean.setConta((String)conta.getValue());} catch (Exception e) {}
		try {bean.setContaPai((Conta)contaPai.getValue());} catch (Exception e) {}
		try {bean.setDescricao((String)descricao.getValue());} catch (Exception e) {}
		try {bean.setTotalizadora((Boolean)totalizadora.getValue());} catch (Exception e) {}
		try {bean.setClinicas(new ArrayList<Clinica>((Collection<? extends Clinica>) clinicas.getValue()));} catch (Exception e) {bean.setClinicas(null);}
		
		return bean;
	}

	public void setBean(Conta bean) {
		this.bean = bean;
		if (bean != null) {
			conta.setValue(bean.getConta());
			descricao.setValue(bean.getDescricao());
			totalizadora.setValue(bean.getTotalizadora());
			contaPai.setValue(bean.getContaPai());

		 	List<Clinica> c2 = clinicaBC.findByConta(bean);
//			try {itemBean.getItemProperty("conta.clinicas").setValue(c2);} catch (Exception e) {}
			try {clinicas.setValue(c2);} catch (Exception e) {}
		}
	}

	public void setListaContaPai(List<Conta> list) {
		contaPai.setContainerDataSource(CollectionContainer.fromBeans(list));
		contaPai.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
	}

	public ComboBox getContaPai() {
		return contaPai;
	}
	
	public void setListaClinicas(List<Clinica> list) {
		clinicas.setContainerDataSource(CollectionContainer.fromBeans(list));
//		contaPai.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
	}

	public TwinColSelect getClinicas() {
		return clinicas;
	}
	
}
