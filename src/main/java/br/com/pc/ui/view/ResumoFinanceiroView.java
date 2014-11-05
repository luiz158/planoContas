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
import br.com.pc.domain.configuracao.EnumMenu;
import br.com.pc.ui.annotation.ProcessAdd;
import br.com.pc.ui.annotation.ProcessFilter;
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


public class ResumoFinanceiroView extends BaseVaadinView implements Button.ClickListener {

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
	
	DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");;
	
	@Override
	public void initializeComponents() {
		// TODO Auto-generated method stub
		setCaption(EnumMenu.PC_RESUMO_FINANCEIRO.getNome());
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
//		addComponent(montaPainel());
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
//	private Component montaPainel(){
//		Panel dados = new Panel();
//		dados.setCaption("DADOS");
//		HorizontalLayout hl = new HorizontalLayout();
//		HorizontalLayout hl2 = new HorizontalLayout();
////		hl.setCaption("DADOS");
////		hl.setMargin(true);
//		hl.setSpacing(true);
//		
////		dados.setContent(hl);
//
//		hl.addComponent(clinica);
////		hl.addComponent(dtInicio);
////		hl.addComponent(dtFim);
//		hl.addComponent(valor);
//		hl.addComponent(registro);
//
//		hl.addComponent(btAdd);
//		hl.addComponent(btSalvar);
//		hl.addComponent(btExcel);
//		hl.addComponent(btDre);
//		
////		hl.addComponent(btRem);
//		dados.addComponent(hl);
////		dados.addComponent(tabela);
//		hl2.addComponent(dados);
//
//		hl.setComponentAlignment(btAdd, Alignment.BOTTOM_LEFT);
//		hl.setComponentAlignment(btSalvar, Alignment.BOTTOM_LEFT);
//		hl.setComponentAlignment(btExcel, Alignment.BOTTOM_LEFT);
//		hl.setComponentAlignment(btDre, Alignment.BOTTOM_LEFT);
//		
////		hl.setComponentAlignment(btRem, Alignment.BOTTOM_LEFT);
//		btAdd.setIcon(new ThemeResource("icons/16/add_16.png"));
//		btSalvar.setIcon(new ThemeResource("icons/16/save_16.png"));
//		btSalvar.setEnabled(false);
//		btExcel.setIcon(new ThemeResource("icons/16/excel_16.png"));
//		btDre.setIcon(new ThemeResource("icons/16/pdf_16.png"));
//		dtInicio.setData(new Date());
//		dtFim.setData(new Date());
//		
//		return dados;
//		
//	}
	
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

		tabela.addContainerProperty("conta.conta", String.class,  null);
		tabela.addContainerProperty("conta.nome", String.class,  null);
		
		tabela.addContainerProperty("total", BigDecimal.class,  null);
		tabela.addContainerProperty("conta.totalizadora", Boolean.class,  null);
		

		tabela.setVisibleColumns(new Object[]{"conta.conta","conta.nome","total"});
		
		tabela.setColumnHeaders(new String[]{"conta","descricao","total"});
		
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
					if (row!=null && row.getItemProperty("conta.totalizadora")!=null && 
							row.getItemProperty("conta.totalizadora").getValue()!=null &&
							(Boolean)row.getItemProperty("conta.totalizadora").getValue()) {
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
	
//	public void setListFluxo(List<Fluxo> lista){
//		for (Fluxo f : lista) {
//			Item itemBean;
//			itemBean = tabela.getItem(f.getConta());
//			if (itemBean!=null){
//				String dia=String.format("%td", f.getData());
//				
//				BigDecimal bg = (BigDecimal)itemBean.getItemProperty("d"+dia).getValue();
//				BigDecimal total = (BigDecimal)itemBean.getItemProperty("total").getValue();
//				if (total == null){total = new BigDecimal("0.0");}
//				if (bg!=null){
//	//				bg = bg.add(f.getValor());
//					itemBean.getItemProperty("d"+dia).setValue(f.getValor().add(bg));
//					total = total.add(f.getValor());
//				}else{
////					if (total == null){total = new BigDecimal("0.0");}
//					itemBean.getItemProperty("d"+dia).setValue(f.getValor());
//					total = total.add(f.getValor());
//				}
//				itemBean.getItemProperty("total").setValue(total);
//				Conta conta = new ContaBC().load(f.getConta().getId());
//				if(conta.getContaPai()!=null){
//					totalizadora(conta.getContaPai(),dia,f.getValor());
//				}
//			}
//		}
//	}
//
//	private void totalizadora(Conta contaPai, String dia, BigDecimal v){
//		Item itemBean;
//		itemBean = tabela.getItem(contaPai);
//		
//		BigDecimal bg = (BigDecimal)itemBean.getItemProperty("d"+dia).getValue();
//		BigDecimal total = (BigDecimal)itemBean.getItemProperty("total").getValue();
//		BigDecimal valor2 = new BigDecimal("0");
//		if (total == null){total = new BigDecimal("0.0");}
//		else{
////			total = total.add(total);
//		}
//		if (bg!=null){
//			valor2=valor2.add(bg);
////			total=total.add(bg);
//		}
//		valor2=valor2.add(v);
//		total=total.add(v);
//		itemBean.getItemProperty("d"+dia).setValue(valor2);
//		itemBean.getItemProperty("total").setValue(total);
//		Conta conta = new ContaBC().load(contaPai.getId());
//		if(conta.getContaPai()!=null){
//			totalizadora(conta.getContaPai(),dia,v);
//		}
//	}
//	public void setListFluxo2(List<Fluxo> lista){
//		for (Fluxo f : lista) {
//			Item itemBean;
//			itemBean = tabela.getItem(f.getConta());
//			
//			String dia=String.format("%td", f.getData());
//			
//			BigDecimal bg = (BigDecimal)itemBean.getItemProperty("d"+dia).getValue();
//			BigDecimal valor = new BigDecimal("0");
//			if (bg!=null){
//				valor=valor.add(bg);
//			}
//			if (f.getValor()!=null){
//				valor=valor.add(f.getValor());
//			}
//			itemBean.getItemProperty("d"+dia).setValue(valor);
//			Conta conta = new ContaBC().load(f.getConta().getId());
//			if(conta.getContaPai()!=null){
//				totalizadora(conta.getContaPai(),dia,f.getValor());
//			}
//			
//		}
//	}
//	private void totalizadora2(Conta contaPai, String dia, BigDecimal valor){
//		Item itemBean;
//		itemBean = tabela.getItem(contaPai);
//		
//		BigDecimal bg = (BigDecimal)itemBean.getItemProperty("d"+dia).getValue();
//		BigDecimal valor2 = new BigDecimal("0");
//		
//		if (bg!=null){
//			valor2=valor2.add(bg);
//		}
//		if (valor!=null){
//			itemBean.getItemProperty("d"+dia).setValue(valor2.add(valor));
//		}else{
//			itemBean.getItemProperty("d"+dia).setValue(valor2);
//		}
//		
//		Conta conta = new ContaBC().load(contaPai.getId());
//		if(conta.getContaPai()!=null){
//			totalizadora(conta.getContaPai(),dia,valor2);
//		}
//	}
//	
	public void setListConta(List<Conta> lista){
		tabela.removeAllItems();
		for (Conta c : lista) {
			Item itemBean = tabela.addItem(c);
			if (itemBean!=null){
				itemBean.getItemProperty("conta.conta").setValue(c.getConta());
				itemBean.getItemProperty("conta.nome").setValue(c.getDescricao());
				itemBean.getItemProperty("conta.totalizadora").setValue(c.getTotalizadora());
				itemBean.getItemProperty("total").setValue(fluxoBC.somaTotal2(getFiltro1(), true, c));
				if (c.getContaPai()!=null){
					tabela.setParent(c, c.getContaPai());
				}
				if (!c.getTotalizadora()){
					tabela.setChildrenAllowed(c, false);
				}else{
					if (c.getResumoFinanceiro()){
						tabela.setCollapsed(c.getContaPai(), false);
					}
				}
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
//		if (event.getButton()==btRem){
//			beanManager.fireEvent(this, new AnnotationLiteral<ProcessDelete>() {});
//		}
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
//			try {
//				getWindow().open(new GeraXls("fluxo.xls",tabela,getApplication()).getStream());
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
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
		// TODO Auto-generated method stub
		return f;
	}
	
	

}
