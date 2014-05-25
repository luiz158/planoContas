package br.com.pc.ui.view;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

import br.com.pc.business.ContaBC;
import br.com.pc.business.FluxoBC;
import br.com.pc.domain.Clinica;
import br.com.pc.domain.Conta;
import br.com.pc.domain.Fluxo;
import br.com.pc.domain.configuracao.EnumMenu;
import br.com.pc.domain.configuracao.EnumMeses;
import br.com.pc.ui.annotation.ProcessAdd;
import br.com.pc.ui.annotation.ProcessFilter;
import br.com.pc.ui.bean.Filtro1;
import br.com.pc.ui.bean.FluxoDias;
import br.com.pc.ui.report.ResumoFinanceiroBean;
import br.com.pc.ui.report.ResumoFinanceiroReport;
import br.com.pc.util.GeraXls;
import br.com.pc.util.components.FieldFactoryUtil;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.template.BaseVaadinView;

import com.vaadin.addon.treetable.TreeTable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
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


public class Fluxo2View extends BaseVaadinView implements Button.ClickListener {

	private static final long serialVersionUID = 1L;
	
	@Inject	private BeanManager beanManager;
	@Inject FluxoBC fluxoBC;

	private ListSelect fClinica;
	private ComboBox ano;
	private ComboBox mes;
	private Button btFiltro;
	
	private ComboBox clinica;
	private DateField data;
	private TextField valor;
	private TextField registro;
	
	private Conta contaSelected;
	private Fluxo fluxoBean;
	
	private TreeTable tabela;
	
	private Button btSalvar;
	private Button btAdd;
	private Button btExcel;
	private Button btDre;
	
	DecimalFormat df = new DecimalFormat("#,##0.00");
	
	@Override
	public void initializeComponents() {
		// TODO Auto-generated method stub
		setCaption(EnumMenu.PC_FLUXO2.getNome());
		setSpacing(true);
		setMargin(true);

		fClinica = new ListSelect("CLINICAS");
		fClinica.setMultiSelect(true);
		fClinica.setRows(4);
		
		ano  = FieldFactoryUtil.createComboBox("ANO",null);
		mes  = FieldFactoryUtil.createComboBox("MES",null);
		btFiltro = new Button("FILTRAR");
		
		clinica = FieldFactoryUtil.createComboBox("CLINICA", "descricao");
		data  = FieldFactoryUtil.createDateField("DATA","dd/MM/yy");
		valor = FieldFactoryUtil.createTextField("VALOR");
		clinica.setRequired(true);
		data.setRequired(true);
		valor.setRequired(true);
		clinica.setRequiredError("Ítem obrigatório");
		data.setRequiredError("Ítem obrigatório");
		valor.setRequiredError("Ítem obrigatório");
		registro = FieldFactoryUtil.createTextField("REGISTRO");
//		registro.setRequired(true);
//		registro.setRequiredError("Ítem obrigatório");
		
		
		btSalvar = new Button();
		btSalvar.setDescription("Salvar registro");
		btAdd = new Button();
		btAdd.setDescription("Adicionar novo registro");
		btExcel = new Button();
		btExcel.setDescription("Exportar para excel.");
		btDre = new Button();
		btDre.setDescription("Gera DRE.");
		
		valor.setLocale(new Locale("pt","BR"));
		valor.addStyleName("align-right");
		df.setParseBigDecimal(true);
		df.setDecimalSeparatorAlwaysShown(true);
		df.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("pt","BR")));
		
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
		vl1.addComponent(ano);
		vl1.addComponent(mes);
		vl1.setSpacing(true);
		hl.addComponent(vl1);
		hl.addComponent(btFiltro);
		hl.addComponent(btExcel);

		hl.setComponentAlignment(btFiltro, Alignment.BOTTOM_LEFT);
		hl.setComponentAlignment(btExcel, Alignment.BOTTOM_LEFT);
		btExcel.setIcon(new ThemeResource("icons/16/excel_16.png"));

		dados.addComponent(hl);
		hl2.addComponent(dados);
		
		return dados;
	}
	private Component montaPainel(){
		Panel dados = new Panel();
		dados.setCaption("DADOS");
		HorizontalLayout hl = new HorizontalLayout();
		HorizontalLayout hl2 = new HorizontalLayout();
//		hl.setCaption("DADOS");
//		hl.setMargin(true);
		hl.setSpacing(true);
		
//		dados.setContent(hl);

		hl.addComponent(clinica);
		hl.addComponent(data);
		hl.addComponent(valor);
		hl.addComponent(registro);

		hl.addComponent(btAdd);
		hl.addComponent(btSalvar);
//		hl.addComponent(btExcel);
		hl.addComponent(btDre);
		
//		hl.addComponent(btRem);
		dados.addComponent(hl);
//		dados.addComponent(tabela);
		hl2.addComponent(dados);

		hl.setComponentAlignment(btAdd, Alignment.BOTTOM_LEFT);
		hl.setComponentAlignment(btSalvar, Alignment.BOTTOM_LEFT);
		hl.setComponentAlignment(btExcel, Alignment.BOTTOM_LEFT);
		hl.setComponentAlignment(btDre, Alignment.BOTTOM_LEFT);
		
//		hl.setComponentAlignment(btRem, Alignment.BOTTOM_LEFT);
		btAdd.setIcon(new ThemeResource("icons/16/add_16.png"));
		btSalvar.setIcon(new ThemeResource("icons/16/save_16.png"));
		btSalvar.setEnabled(false);
		btExcel.setIcon(new ThemeResource("icons/16/excel_16.png"));
		btDre.setIcon(new ThemeResource("icons/16/pdf_16.png"));
		data.setData(new Date());
		
		return dados;
		
	}
	
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
		
		tabela.addContainerProperty("d01", BigDecimal.class,  null);
		tabela.addContainerProperty("d02", BigDecimal.class,  null);
		tabela.addContainerProperty("d03", BigDecimal.class,  null);
		tabela.addContainerProperty("d04", BigDecimal.class,  null);
		tabela.addContainerProperty("d05", BigDecimal.class,  null);
		tabela.addContainerProperty("d06", BigDecimal.class,  null);
		tabela.addContainerProperty("d07", BigDecimal.class,  null);
		tabela.addContainerProperty("d08", BigDecimal.class,  null);
		tabela.addContainerProperty("d09", BigDecimal.class,  null);
		tabela.addContainerProperty("d10", BigDecimal.class,  null);
		tabela.addContainerProperty("d11", BigDecimal.class,  null);
		tabela.addContainerProperty("d12", BigDecimal.class,  null);
		tabela.addContainerProperty("d13", BigDecimal.class,  null);
		tabela.addContainerProperty("d14", BigDecimal.class,  null);
		tabela.addContainerProperty("d15", BigDecimal.class,  null);
		tabela.addContainerProperty("d16", BigDecimal.class,  null);
		tabela.addContainerProperty("d17", BigDecimal.class,  null);
		tabela.addContainerProperty("d18", BigDecimal.class,  null);
		tabela.addContainerProperty("d19", BigDecimal.class,  null);
		tabela.addContainerProperty("d20", BigDecimal.class,  null);
		tabela.addContainerProperty("d21", BigDecimal.class,  null);
		tabela.addContainerProperty("d22", BigDecimal.class,  null);
		tabela.addContainerProperty("d23", BigDecimal.class,  null);
		tabela.addContainerProperty("d24", BigDecimal.class,  null);
		tabela.addContainerProperty("d25", BigDecimal.class,  null);
		tabela.addContainerProperty("d26", BigDecimal.class,  null);
		tabela.addContainerProperty("d27", BigDecimal.class,  null);
		tabela.addContainerProperty("d28", BigDecimal.class,  null);
		tabela.addContainerProperty("d29", BigDecimal.class,  null);
		tabela.addContainerProperty("d30", BigDecimal.class,  null);
		tabela.addContainerProperty("d31", BigDecimal.class,  null);
		tabela.addContainerProperty("total", BigDecimal.class,  null);
		tabela.addContainerProperty("conta.totalizadora", Boolean.class,  null);
		

		tabela.setVisibleColumns(new Object[]{"conta.conta","conta.nome", 
				"d01","d02","d03","d04","d05","d06","d07","d08","d09","d10",
				"d11","d12","d13","d14","d15","d16","d17","d18","d19","d20",
				"d21","d22","d23","d24","d25","d26","d27","d28","d29","d30",
				"d31","total"});
		
		tabela.setColumnHeaders(new String[]{"conta","descricao", 
				"01","02","03","04","05","06","07","08","09","10",
				"11","12","13","14","15","16","17","18","19","20",
				"21","22","23","24","25","26","27","28","29","30",
				"31","total"});
		
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
//				          return "veiculo " + item.getItemProperty("veiculoCor").getValue();
			          }
				}
//			    }else if ("servico".equals(propertyId)) {
//					Item row = tabela.getItem(itemId);
//					if (row!=null && (Boolean)row.getItemProperty("privativo").getValue()){
//						return "privativo";
//					}
//				}else if ("paxNome".equals(propertyId)) {
//					Item row = tabela.getItem(itemId);
//					if (row!=null && (Boolean)row.getItemProperty("vip").getValue()){
//						return "vip";
//					}
//				}
//				else 
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
		btDre.addListener(this);
		
		tabela.addListener(new Table.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				contaSelected = (Conta)event.getProperty().getValue();	
				if (contaSelected != null && !contaSelected.getTotalizadora()) {
					btSalvar.setEnabled(true);
//					btRem.setEnabled(true);
					try {
						Item itemBean = tabela.getItem(contaSelected);
						String dia = String.format("d%td", (Date) data.getValue());
						String v = itemBean.getItemProperty(dia).getValue().toString();
						BigDecimal bg = new BigDecimal(v);
						valor.setValue(df.format(bg));
					} catch (Exception e) {
						valor.setValue(df.format(new BigDecimal("0.0")));
					}
					beanManager.fireEvent(Fluxo2View.this, new AnnotationLiteral<ProcessItemSelection>() {});
				}else{
					btSalvar.setEnabled(false);
//					valor.setValue(df.format(new BigDecimal("0.0")));
//					btRem.setEnabled(false);
//					limpar();
				}
			}
		});
	}

	public void limpar(){
//		clinica.select(null);
		data.setValue(new Date());
		valor.setValue(df.format(new BigDecimal("0.0")));
	}
	
	public void setListFluxo(List<Fluxo> lista){
		for (Fluxo f : lista) {
			Item itemBean;
			itemBean = tabela.getItem(f.getConta());
			if (itemBean!=null){
				String dia=String.format("%td", f.getData());
				
				BigDecimal bg = (BigDecimal)itemBean.getItemProperty("d"+dia).getValue();
				BigDecimal total = (BigDecimal)itemBean.getItemProperty("total").getValue();
				if (total == null){total = new BigDecimal("0.0");}
				if (bg!=null){
	//				bg = bg.add(f.getValor());
					itemBean.getItemProperty("d"+dia).setValue(f.getValor().add(bg));
					total = total.add(f.getValor());
				}else{
//					if (total == null){total = new BigDecimal("0.0");}
					itemBean.getItemProperty("d"+dia).setValue(f.getValor());
					total = total.add(f.getValor());
				}
				itemBean.getItemProperty("total").setValue(total);
				Conta conta = new ContaBC().load(f.getConta().getId());
				if(conta.getContaPai()!=null){
					totalizadora(conta.getContaPai(),dia,f.getValor());
				}
			}
		}
	}

	private void totalizadora(Conta contaPai, String dia, BigDecimal v){
		Item itemBean;
		itemBean = tabela.getItem(contaPai);
		
		BigDecimal bg = (BigDecimal)itemBean.getItemProperty("d"+dia).getValue();
		BigDecimal total = (BigDecimal)itemBean.getItemProperty("total").getValue();
		BigDecimal valor2 = new BigDecimal("0");
		if (total == null){total = new BigDecimal("0.0");}
		else{
//			total = total.add(total);
		}
		if (bg!=null){
			valor2=valor2.add(bg);
//			total=total.add(bg);
		}
		valor2=valor2.add(v);
		total=total.add(v);
		itemBean.getItemProperty("d"+dia).setValue(valor2);
		itemBean.getItemProperty("total").setValue(total);
		Conta conta = new ContaBC().load(contaPai.getId());
		if(conta.getContaPai()!=null){
			totalizadora(conta.getContaPai(),dia,v);
		}
	}
	public void setListFluxo2(List<Fluxo> lista){
		for (Fluxo f : lista) {
			Item itemBean;
			itemBean = tabela.getItem(f.getConta());
			
			String dia=String.format("%td", f.getData());
			
			BigDecimal bg = (BigDecimal)itemBean.getItemProperty("d"+dia).getValue();
			BigDecimal valor = new BigDecimal("0");
			if (bg!=null){
				valor=valor.add(bg);
			}
			if (f.getValor()!=null){
				valor=valor.add(f.getValor());
			}
			itemBean.getItemProperty("d"+dia).setValue(valor);
			Conta conta = new ContaBC().load(f.getConta().getId());
			if(conta.getContaPai()!=null){
				totalizadora(conta.getContaPai(),dia,f.getValor());
			}
			
		}
	}
	private void totalizadora2(Conta contaPai, String dia, BigDecimal valor){
		Item itemBean;
		itemBean = tabela.getItem(contaPai);
		
		BigDecimal bg = (BigDecimal)itemBean.getItemProperty("d"+dia).getValue();
		BigDecimal valor2 = new BigDecimal("0");
		
		if (bg!=null){
			valor2=valor2.add(bg);
		}
		if (valor!=null){
			itemBean.getItemProperty("d"+dia).setValue(valor2.add(valor));
		}else{
			itemBean.getItemProperty("d"+dia).setValue(valor2);
		}
		
		Conta conta = new ContaBC().load(contaPai.getId());
		if(conta.getContaPai()!=null){
			totalizadora(conta.getContaPai(),dia,valor2);
		}
	}
	
	public void setListConta(List<Conta> lista){
		tabela.removeAllItems();
		for (Conta c : lista) {
			Item itemBean = tabela.addItem(c);
			if (itemBean!=null){
				itemBean.getItemProperty("conta.conta").setValue(c.getConta());
				itemBean.getItemProperty("conta.nome").setValue(c.getDescricao());
				itemBean.getItemProperty("conta.totalizadora").setValue(c.getTotalizadora());
//				itemBean.getItemProperty("total").setValue(fluxoBC.somaTotal(getFiltro1(), true, c));
				if (c.getContaPai()!=null){
					tabela.setParent(c, c.getContaPai());
				}
				if (!c.getTotalizadora()){
					tabela.setChildrenAllowed(c, false);
				}
				tabela.setCollapsed(c, false);
			}
			
		}
	}
	
	public List<ResumoFinanceiroBean> somaTotal(){
		List<ResumoFinanceiroBean> listaDre = new ArrayList<ResumoFinanceiroBean>();
		Collection<?> c = (Collection<?>) tabela.getItemIds();
		for (Object object : c) {
//			if (((Conta)c).getTotalizadora()){
//				
//			}
			ResumoFinanceiroBean bean;
			Item item = tabela.getItem(object);
			if ((Boolean)item.getItemProperty("conta.totalizadora").getValue()){
				Collection<?> p = (Collection<?>) item.getItemPropertyIds();
				bean = new ResumoFinanceiroBean((String)item.getItemProperty("conta.conta").getValue(),(String)item.getItemProperty("conta.nome").getValue());
				for (Object p2 : p) {
					if (p2.toString().length()<=3){
						try {
							bean.addValor((BigDecimal)item.getItemProperty(p2).getValue());
						} catch (Exception e) {
//							System.out.println("DEU ERRO");
						}
					}
				}
				listaDre.add(bean);
			}
		}
		return listaDre;
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
				getWindow().open(new GeraXls("fluxo.xls",tabela,getApplication()).getStream());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (event.getButton()==btDre){
			new ResumoFinanceiroReport().resumoFinanceiroPDF(somaTotal(),getFiltro1(),0.0);
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
	public Fluxo getFluxoBean() {
		fluxoBean = new Fluxo();
		try {} catch (Exception e) {}
		try {fluxoBean.setClinica((Clinica)clinica.getValue());} catch (Exception e) {}
		try {fluxoBean.setConta(contaSelected);} catch (Exception e) {}
		try {fluxoBean.setData((Date)data.getValue());} catch (Exception e) {}
		try {fluxoBean.setValor((BigDecimal)df.parse(valor.getValue().toString()));} catch (Exception e) {}
		try {fluxoBean.setRegistro((String)registro.getValue());} catch (Exception e) {}
		
		return fluxoBean;
	}
	public void setFluxoBean(Fluxo bean) {
		this.fluxoBean = bean;
	}
	
	

	public void setListaClinica(List<Clinica> list) {
		clinica.setContainerDataSource(CollectionContainer.fromBeans(list));
		fClinica.setContainerDataSource(CollectionContainer.fromBeans(list));
	}
	public void setListaAno(List<Integer> list) {
		try {
			ano.setContainerDataSource(CollectionContainer.fromBeans(list));
			ano.setValue(ano.getItemIds().iterator().next());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setListaMeses(List<EnumMeses> list) {
		try {
			mes.setContainerDataSource(CollectionContainer.fromBeans(list));
			mes.setValue(EnumMeses.getMes(Integer.valueOf(String.format("%tm", new Date()))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Filtro1 getFiltro1() {
		Filtro1 f = new Filtro1();
		try {} catch (Exception e) {}
		try {f.setAno(Integer.valueOf((String)ano.getValue()));} catch (Exception e) {}
		try {f.setMes((EnumMeses)mes.getValue());} catch (Exception e) {}
		try {f.setClinicas((Set<Clinica>)fClinica.getValue());} catch (Exception e) {e.printStackTrace();}
		if (f.getClinicas()==null || f.getClinicas().size()==0){
			f.setClinicas((List<Clinica>)fClinica.getItemIds());
		}
		// TODO Auto-generated method stub
		return f;
	}
	
	

}
