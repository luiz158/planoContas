package br.com.pc.ui.view;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.com.pc.business.FluxoBC;
import br.com.pc.domain.Clinica;
import br.com.pc.domain.Conta;
import br.com.pc.domain.configuracao.EnumDre;
import br.com.pc.domain.configuracao.EnumMenu;
import br.com.pc.ui.annotation.ProcessAdd;
import br.com.pc.ui.annotation.ProcessFilter;
import br.com.pc.ui.bean.Dre;
import br.com.pc.ui.bean.Filtro1;
import br.com.pc.ui.report.ResumoFinanceiroBean;
import br.com.pc.ui.report.ResumoFinanceiroReport;
import br.com.pc.util.GeraXls;
import br.com.pc.util.components.FieldFactoryUtil;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.template.BaseVaadinView;

import com.vaadin.addon.treetable.TreeTable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


public class DreView extends BaseVaadinView implements Button.ClickListener {

	private static final long serialVersionUID = 1L;
	
	@Inject	private BeanManager beanManager;
	@Inject FluxoBC fluxoBC;

	private ListSelect fClinica;
	private DateField dtInicio;
	private DateField dtFim;
	private Button btFiltro;
	
	private ComboBox clinica;
	private TextField valor;
	private TextField registro;
	
	private Conta contaSelected;
	
	private TreeTable tabela;
	
	private Button btSalvar;
	private Button btAdd;
	private Button btExcel;
	private Button btResumoFinanceiro;
	
	DecimalFormat df = new DecimalFormat("#,##0.00");
	
	@Override
	public void initializeComponents() {
		// TODO Auto-generated method stub
		setCaption(EnumMenu.PC_DRE.getNome());
		setSpacing(true);
		setMargin(true);

		fClinica = new ListSelect("CLINICAS");
		fClinica.setMultiSelect(true);
		fClinica.setRows(4);

		dtInicio  = FieldFactoryUtil.createDateField("DATA INICIAL","dd/MM/yy");
		dtFim  = FieldFactoryUtil.createDateField("DATA FINAL","dd/MM/yy");
		btFiltro = new Button("FILTRAR");
		
		clinica = FieldFactoryUtil.createComboBox("CLINICA", "descricao");
		valor = FieldFactoryUtil.createTextField("VALOR");
		clinica.setRequired(true);
		dtInicio.setRequired(true);
		dtFim.setRequired(true);
		valor.setRequired(true);
		clinica.setRequiredError("Ítem obrigatório");
		dtInicio.setRequiredError("Ítem obrigatório");
		dtFim.setRequiredError("Ítem obrigatório");
		valor.setRequiredError("Ítem obrigatório");
		registro = FieldFactoryUtil.createTextField("REGISTRO");
		registro.setRequired(true);
		registro.setRequiredError("Ítem obrigatório");
		
		
		btSalvar = new Button();
		btSalvar.setDescription("Salvar registro");
		btAdd = new Button();
		btAdd.setDescription("Adicionar novo registro");
		btExcel = new Button();
		btExcel.setDescription("Exportar para excel.");
		btResumoFinanceiro = new Button();
		btResumoFinanceiro.setDescription("Gera Resumo Financeiro.");
		btExcel.setIcon(new ThemeResource("icons/16/excel_16.png"));
		btResumoFinanceiro.setIcon(new ThemeResource("icons/16/print2_16.png"));
		
		valor.setLocale(new Locale("pt","BR"));
		valor.addStyleName("align-right");
		df.setParseBigDecimal(true);
		
		montaTabela();
		addListener();

		addComponent(montaFiltro());
		addComponent(tabela);
		
	}
	
	private Component montaFiltro(){
		Panel dados = new Panel();
		dados.setCaption("FILTRO");
		HorizontalLayout hl = new HorizontalLayout();
		HorizontalLayout hl2 = new HorizontalLayout();
		VerticalLayout vl1 = new VerticalLayout();
//		hl.setMargin(true);
		hl.setSpacing(true);
		
		hl.addComponent(fClinica);
		vl1.addComponent(dtInicio);
		vl1.addComponent(dtFim);
		vl1.setSpacing(true);
		hl.addComponent(vl1);
		hl.addComponent(btFiltro);
		hl.addComponent(btExcel);
		hl.addComponent(btResumoFinanceiro);

		hl.setComponentAlignment(btFiltro, Alignment.BOTTOM_LEFT);
		hl.setComponentAlignment(btExcel, Alignment.BOTTOM_LEFT);
		hl.setComponentAlignment(btResumoFinanceiro, Alignment.BOTTOM_LEFT);

		dados.addComponent(hl);
		hl2.addComponent(dados);
		
		return dados;
	}

	
	@SuppressWarnings("serial")
	private void montaTabela(){
		tabela = new TreeTable(){
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
		
		tabela.setLocale(new Locale("pt", "BR"));
		tabela.setSelectable(true);
		tabela.setImmediate(true);
		tabela.setPageLength(20);
		tabela.setCacheRate(300);
//		tabela.setColumnCollapsingAllowed(true);
		tabela.setWidth("100%");

		tabela.addContainerProperty("conta", String.class,  null);
		
		tabela.addContainerProperty("valor", BigDecimal.class,  null);
		tabela.addContainerProperty("tipo", EnumDre.class,  null);
		

		tabela.setVisibleColumns(new Object[]{"conta","valor","tipo"});
		
		tabela.setColumnHeaders(new String[]{"conta","valor","tipo"});
		
		tabela.setCellStyleGenerator(new Table.CellStyleGenerator() {
			@Override
		      public String getStyle(Object itemId, Object propertyId) {
				if (propertyId != null && propertyId.toString().length()<=3) {
			          Item item = tabela.getItem(itemId);
			          if (item!=null && item.getItemProperty(propertyId.toString())!=null && 
			        		  item.getItemProperty(propertyId.toString()).getValue()!=null){
			        	  if (((BigDecimal)item.getItemProperty(propertyId.toString()).getValue()).signum()>=0){
			        		  return "positivo";
			        	  }else{
			        		  return "negativo";
			        	  }
			          }
				}
				if (propertyId == null) { //para linha inteira
					Item row = tabela.getItem(itemId);
					if (row!=null && row.getItemProperty("conta.dre")!=null) {
						return "bold";
					}
				}
		        return null;
		      }
			
		    });
	}
	private void addListener(){
		btSalvar.addListener(this);
		btAdd.addListener(this);
		btFiltro.addListener(this);
		btExcel.addListener(this);
		btResumoFinanceiro.addListener(this);
	}

	public void limpar(){
		valor.setValue(df.format(new BigDecimal("0.0")));
	}
	
	public void setList(List<Dre> lista){
		tabela.removeAllItems();
		for (Dre dre : lista) {
			Item itemBean = tabela.addItem(dre);
			if (itemBean!=null){
				itemBean.getItemProperty("conta").setValue(dre.getConta());
				itemBean.getItemProperty("valor").setValue(dre.getValor());
				itemBean.getItemProperty("tipo").setValue(dre.getTipo());
			}
			
		}
	}
	Double tt = 0.0;
	public List<ResumoFinanceiroBean> somaTotal(){
		tt = 0.0;
		Integer conta = 0;
		List<ResumoFinanceiroBean> listaResumoFinanceiro = new ArrayList<ResumoFinanceiroBean>();
		Collection<?> c = (Collection<?>) tabela.getItemIds();
		for (Object object : c) {
			ResumoFinanceiroBean bean;
			Item item = tabela.getItem(object);
			bean = new ResumoFinanceiroBean((String)item.getItemProperty("conta.conta").getValue(),(String)item.getItemProperty("conta.nome").getValue());
			bean.setValor((BigDecimal)item.getItemProperty("total").getValue());
			bean.setTotalizadora((Boolean)item.getItemProperty("conta.totalizadora").getValue());
			
			if (bean.getContaNumero()!=null){
				
				try {
					if (conta < Integer.valueOf(bean.getContaNumero().split("\\.")[0].toString())){
						conta = Integer.valueOf(bean.getContaNumero().split("\\.")[0].toString());
						tt += bean.getValor().doubleValue();
					}
				} catch (Exception e) {e.printStackTrace();}
			}

			listaResumoFinanceiro.add(bean);
		}
		return listaResumoFinanceiro;
	}
	
	@SuppressWarnings("serial")
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton()==btSalvar){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessSave>() {});
			contaSelected = new Conta();
		}
		if (event.getButton()==btAdd){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessAdd>() {});
			contaSelected = new Conta();
		}
		if (event.getButton()==btFiltro){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessFilter>() {});
		}
		if (event.getButton()==btExcel){
			try {
				getWindow().open(new GeraXls("ResumoFinanceiro.xls",tabela,getApplication()).getStream());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (event.getButton()==btResumoFinanceiro){
			new ResumoFinanceiroReport().resumoFinanceiroPDF(somaTotal(),getFiltro1(),tt);
		}
	}

	public Conta getContaBean() {
		if (contaSelected==null || contaSelected.getId()==null){
			contaSelected = new Conta();
		}
		return contaSelected;
	}
	public void setContaBean(Conta bean) {
		this.contaSelected = bean;
	}

	public void setListaClinica(List<Clinica> list) {
		clinica.setContainerDataSource(CollectionContainer.fromBeans(list));
		fClinica.setContainerDataSource(CollectionContainer.fromBeans(list));
	}

	@SuppressWarnings("unchecked")
	public Filtro1 getFiltro1() {
		Filtro1 f = new Filtro1();
		try {} catch (Exception e) {}
		try {f.setDtInicio((Date)dtInicio.getValue());} catch (Exception e) {}
		try {f.setDtFim((Date)dtFim.getValue());} catch (Exception e) {}
		try {f.setClinicas((Set<Clinica>)fClinica.getValue());} catch (Exception e) {e.printStackTrace();}
		if (f.getClinicas()==null || f.getClinicas().size()==0){
			f.setClinicas((List<Clinica>)fClinica.getItemIds());
		}
		return f;
	}
	
	

}
