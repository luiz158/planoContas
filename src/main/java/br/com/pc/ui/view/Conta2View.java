package br.com.pc.ui.view;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.com.pc.business.ClinicaBC;
import br.com.pc.business.ContaBC;
import br.com.pc.domain.Clinica;
import br.com.pc.domain.Conta;
import br.com.pc.domain.configuracao.EnumDre;
import br.com.pc.ui.annotation.ProcessAcao1;
import br.com.pc.ui.annotation.ProcessAdd;
import br.com.pc.ui.annotation.ProcessFilter;
import br.com.pc.util.SimNaoColumnGenerator;
import br.com.pc.util.components.FieldFactoryUtil;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.View;
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
import com.vaadin.ui.VerticalLayout;

@View
public class Conta2View extends BaseVaadinView implements Button.ClickListener {

	private static final long serialVersionUID = 1L;

	@Inject	private BeanManager beanManager;
	@Inject ClinicaBC clinicaBC = new ClinicaBC();
	
	public TextField conta;
	private TextField descricao;
	public CheckBox totalizadora;
	private CheckBox resumoFinanceiro;
	public ComboBox contaPai;
	private ComboBox dre;
	
	private Panel dados;
	
	private Conta bean;
	
	public Table tabela;

	private Button btAdd;
	private Button btSave;
	private Button btRem;
	private Button btConta;

//	protected Property clinica;
	
	private TwinColSelect clinicas;
	
	//FILTROS
	private Panel filtro;
	public TextField fConta;
	public TextField fDescricao;
	public CheckBox fTotalizadora;
	public CheckBox fResumoFinanceiro;
	public ComboBox fContaPai;
	public ComboBox fDre;
	public ComboBox fClinica;
	private Button btFiltro;
	
	
	DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");
	
	@Override
	public void initializeComponents() {
		setCaption("CONTA");
		setSpacing(true);
		setMargin(true);
		tabela = new Table();
		bean = new Conta();

		conta = FieldFactoryUtil.createTextField("CONTA");
		descricao = FieldFactoryUtil.createTextField("DESCRIÇÃO");
		contaPai = FieldFactoryUtil.createComboBox("CONTA PAI", "contaPaiDescricaoConta");
		dre = FieldFactoryUtil.createComboBox("TIPO DE DRE", "descricao");
		totalizadora = FieldFactoryUtil.createCheckBox("TOTALIZADORA");
		resumoFinanceiro = FieldFactoryUtil.createCheckBox("RESUMO FINANCEIRO");
		clinicas =  FieldFactoryUtil.createTwinColSelect("CLINICAS","descricao");

		btAdd = new Button();
		btSave = new Button();
		btRem = new Button();
		btConta = new Button("...");
		
		fConta = FieldFactoryUtil.createTextField("CONTA");
		fDescricao = FieldFactoryUtil.createTextField("DESCRIÇÃO");
		fContaPai = FieldFactoryUtil.createComboBox("CONTA PAI", "contaPaiDescricaoConta");
		fDre = FieldFactoryUtil.createComboBox("TIPO DE DRE", "descricao");
		fTotalizadora = FieldFactoryUtil.createCheckBox("TOTALIZADORA");
		fResumoFinanceiro = FieldFactoryUtil.createCheckBox("RESUMO FINANCEIRO");
		fClinica =  FieldFactoryUtil.createComboBox("CLINICAS","descricao");
		btFiltro = new Button("FILTRAR");

		conta.setRequired(true);
		descricao.setRequired(true);
		conta.setRequiredError("Ítem obrigatório");
		descricao.setRequiredError("Ítem obrigatório");
		
		montaFiltro();
		montaTabela();
		montaPainel();
		addListener();

		addComponent(filtro);
		addComponent(dados);
		addComponent(tabela);
	}
	
	private void montaFiltro(){
		filtro = new Panel("FILTRO");
		
		GridLayout gl = new GridLayout(5,2);
		VerticalLayout vl = new VerticalLayout();
		gl.setSpacing(true);

		vl.addComponent(fTotalizadora);
		vl.addComponent(fResumoFinanceiro);
		
		gl.addComponent(fConta,0,0);
		gl.addComponent(fDescricao,0,1);
		gl.addComponent(vl,1,0);
		gl.addComponent(fContaPai,1,1);
		gl.addComponent(fClinica,2,0);
		gl.addComponent(fDre,2,1);
		
		gl.addComponent(btFiltro,3,1);
		btFiltro.setDescription("Executa o filtro.");
		
		gl.setComponentAlignment(btFiltro, Alignment.BOTTOM_LEFT);
		
		fContaPai.setWidth("100%");
		fDre.setWidth("100%");
		
		filtro.addComponent(gl);

	}
	private void montaPainel(){
		dados = new Panel("DADOS");

		clinicas.setRows(4);
		clinicas.setNullSelectionAllowed(true);
		clinicas.setMultiSelect(true);
		clinicas.setImmediate(true);
		clinicas.setLeftColumnCaption("CLINICAS");
		clinicas.setRightColumnCaption("SELECIONADAS");
		clinicas.setWidth("300px");
		clinicas.setItemCaptionPropertyId("descricao");
		
		GridLayout gl = new GridLayout(6,3);
		HorizontalLayout hlConta = new HorizontalLayout();
		VerticalLayout vl = new VerticalLayout();
//		hl.setMargin(true);
		gl.setSpacing(true);
		
//		dados.setContent(hl);
		contaPai.setWidth("150px");
		dre.setWidth("150px");
		
		vl.addComponent(totalizadora);
		vl.addComponent(resumoFinanceiro);
		
		hlConta.addComponent(conta);
		hlConta.addComponent(btConta);
		
		gl.addComponent(descricao,0,0);
		gl.addComponent(vl,0,1);
		gl.addComponent(contaPai,1,0);
		gl.addComponent(dre,1,1);
		gl.addComponent(clinicas,2,0,2,2);


		gl.addComponent(hlConta,3,0,5,0);
		gl.addComponent(btAdd,3,1);
		gl.addComponent(btSave,4,1);
		gl.addComponent(btRem,5,1);
		btSave.setIcon(new ThemeResource("icons/16/save_16.png"));
		btRem.setIcon(new ThemeResource("icons/16/recycle_16.png"));
		btSave.setDescription("Atualiza um registro selecionado.");
		btRem.setDescription("Exclui registro selecionado.");
		btConta.setDescription("Gera número da conta.");
		btAdd.setIcon(new ThemeResource("icons/16/add_16.png"));
		btAdd.setDescription("Adiciona um novo registro.");
		
		gl.setComponentAlignment(totalizadora, Alignment.MIDDLE_LEFT);
		gl.setComponentAlignment(resumoFinanceiro, Alignment.MIDDLE_LEFT);
		gl.setComponentAlignment(btSave, Alignment.BOTTOM_LEFT);
		gl.setComponentAlignment(btAdd, Alignment.BOTTOM_LEFT);
		gl.setComponentAlignment(btRem, Alignment.BOTTOM_LEFT);
		hlConta.setComponentAlignment(btConta, Alignment.BOTTOM_LEFT);
		
		dados.addComponent(gl);
//		dados.addComponent(dre);
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

					if (row!=null && row.getItemProperty("conta.ativo")!=null && 
							row.getItemProperty("conta.ativo").getValue()!=null &&
							!(Boolean)row.getItemProperty("conta.ativo").getValue()) {
						return "inativo";
					}else if (row!=null && row.getItemProperty("conta.totalizadora")!=null && 
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


		tabela.addContainerProperty("conta.ativo", Boolean.class,  null);
		tabela.addContainerProperty("conta.conta", String.class,  null);
		tabela.addContainerProperty("conta.descricao", String.class,  null);
		tabela.addContainerProperty("conta.totalizadora", Boolean.class,  null);
		tabela.addContainerProperty("conta.resumoFinanceiro", Boolean.class,  null);
		tabela.addContainerProperty("conta.contaPai", Conta.class,  null);
		tabela.addContainerProperty("conta.clinicas", List.class,  null);
		tabela.addContainerProperty("conta.dre", String.class,  null);

		tabela.setVisibleColumns(new Object[]{"conta.conta","conta.descricao","conta.totalizadora","conta.resumoFinanceiro","conta.contaPai","conta.clinicas","conta.dre"});
		
		tabela.setColumnHeaders(new String[]{"conta","descricao","totalizadora","R.F.","conta Pai","clinicas","dre"});
		
		tabela.addGeneratedColumn("conta.totalizadora", new SimNaoColumnGenerator());
		
		
	}
	private void addListener(){
		btSave.addListener(this);
		btAdd.addListener(this);
		btRem.addListener(this);
		btFiltro.addListener(this);
		btConta.addListener(this);
		
		tabela.addListener(new Table.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("serial")
			@Override
			public void valueChange(ValueChangeEvent event) {
				bean = (Conta)event.getProperty().getValue();	
				if (bean != null) {
					setBean(bean);
					beanManager.fireEvent(this, new AnnotationLiteral<ProcessItemSelection>() {});
				}else{
					limpar();
					bean = new Conta();
				}
			}
		});
	}

	protected void limpar() {
		conta.setValue(null);
		descricao.setValue(null);
		contaPai.setValue(null);
		clinicas.setValue(null);
		resumoFinanceiro.setValue(false);
		dre.setValue(false);
		totalizadora.setValue(false);
	}

	public void setList(List<Conta> lista, Boolean removeAllItems, Conta b){
		if (removeAllItems)	tabela.removeAllItems();
		ContaBC contaBC = new ContaBC();
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
			try {itemBean.getItemProperty("conta.resumoFinanceiro").setValue(c.getResumoFinanceiro());} catch (Exception e) {}
			try {itemBean.getItemProperty("conta.ativo").setValue(c.getAtivo());} catch (Exception e) {}
			try {itemBean.getItemProperty("conta.dre").setValue(c.getDre().getDescricao());} catch (Exception e) {}
			
		 	List<Clinica> c2 = clinicaBC.findByConta(c);
			try {itemBean.getItemProperty("conta.clinicas").setValue(c2);} catch (Exception e) {}
//			try {itemBean.getItemProperty("conta.clinicas").setValue(c.getClinicas());} catch (Exception e) {}
			try {itemBean.getItemProperty("conta.contaPai").setValue(
					(c.getContaPai()!=null?contaBC.load(c.getContaPai().getId()):null));
					} catch (Exception e) {e.printStackTrace();}
//			System.out.println("conta pai: "+(c.getContaPai()!=null?c.getContaPai().toString():""));
		}
		if (b!=null){
			tabela.select(b);
		}
	}
	
	@SuppressWarnings("serial")
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton()==btSave){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessSave>() {});
		}
		if (event.getButton()==btAdd){
			bean = new Conta();
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessAdd>() {});
			bean = new Conta();
		}
		if (event.getButton()==btRem){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessDelete>() {});
			bean = new Conta();
		}
		if (event.getButton()==btFiltro){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessFilter>() {});
		}
		if (event.getButton()==btConta){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessAcao1>() {});
		}
	}

	@SuppressWarnings("unchecked")
	public Conta getBean() {
		if (bean==null || bean.getId()==null){
			bean = new Conta();
		}
		try {} catch (Exception e) {}
		try {bean.setConta((String)conta.getValue());} catch (Exception e) {}
		try {bean.setContaPai((Conta)contaPai.getValue());} catch (Exception e) {}
		try {bean.setDre((EnumDre)dre.getValue());} catch (Exception e) {}
		try {bean.setDescricao((String)descricao.getValue());} catch (Exception e) {}
		try {bean.setTotalizadora((Boolean)totalizadora.getValue());} catch (Exception e) {}
		try {bean.setResumoFinanceiro((Boolean)resumoFinanceiro.getValue());} catch (Exception e) {}
		try {bean.setClinicas(new ArrayList<Clinica>((Collection<? extends Clinica>) clinicas.getValue()));} catch (Exception e) {bean.setClinicas(null);}
		
		return bean;
	}

	public void setBean(Conta bean) {
		this.bean = bean;
		if (bean != null) {
			conta.setValue(bean.getConta());
			descricao.setValue(bean.getDescricao());
			totalizadora.setValue(bean.getTotalizadora());
			resumoFinanceiro.setValue(bean.getResumoFinanceiro());
			contaPai.setValue(bean.getContaPai());
			dre.setValue(bean.getDre());

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

	public void setListaDre(List<EnumDre> list) {
		dre.setContainerDataSource(CollectionContainer.fromBeans(list));
		dre.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
	}
	public ComboBox getDre() {
		return dre;
	}
	
	public void setListaClinicas(List<Clinica> list) {
		clinicas.setContainerDataSource(CollectionContainer.fromBeans(list));
//		contaPai.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
	}

	public TwinColSelect getClinicas() {
		return clinicas;
	}
	
}
