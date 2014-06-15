package br.com.pc.ui.view;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.com.pc.domain.Clinica;
import br.com.pc.domain.Conta;
import br.com.pc.domain.Fluxo;
import br.com.pc.domain.configuracao.EnumMenu;
import br.com.pc.domain.configuracao.Usuario;
import br.com.pc.ui.annotation.ProcessAdd;
import br.com.pc.ui.annotation.ProcessFilter;
import br.com.pc.ui.annotation.ProcessRem;
import br.com.pc.ui.bean.Filtro1;
import br.com.pc.util.BigDecimalColumnGenerator;
import br.com.pc.util.DataColumnGenerator;
import br.com.pc.util.DataHoraColumnGenerator;
import br.com.pc.util.GeraXls;
import br.com.pc.util.SimNaoColumnGenerator;
import br.com.pc.util.components.FieldFactoryUtil;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.template.BaseVaadinView;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.PropertyFormatter;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


public class FluxoView extends BaseVaadinView implements Button.ClickListener {

	private static final long serialVersionUID = 1L;
	
	@Inject	private BeanManager beanManager;

	private ListSelect fClinica;
	private DateField dtInicio;
	private DateField dtFim;
	private Button btFiltro;
	
	private ComboBox clinica;
	private ComboBox conta;
	private DateField data;
	private TextField valor;
	private TextField registro;
	
	private Fluxo bean;
	
	private Table tabela;
	
	private Button btSave;
	private Button btAdd;
	private Button btRem;
	private Button btExcel;

	public TextField motivoExclusao;
	public Window modalWindow;
	private Button btDelete;
	
	DecimalFormat df = new DecimalFormat("#,##0.00");
	
	@Override
	public void initializeComponents() {
		// TODO Auto-generated method stub
		setCaption(EnumMenu.PC_FLUXO.getNome());
		setSpacing(true);
		setMargin(true);
		bean = new Fluxo();

		fClinica = new ListSelect("CLINICAS");
		fClinica.setMultiSelect(true);
		fClinica.setRows(4);
		
		dtInicio  = FieldFactoryUtil.createDateField("DATA INICIAL","dd/MM/yy");
		dtFim  = FieldFactoryUtil.createDateField("DATA FINAL","dd/MM/yy");
		btFiltro = new Button("FILTRAR");
		
		clinica = FieldFactoryUtil.createComboBox("CLINICA", "descricao");
		conta = FieldFactoryUtil.createComboBox("CONTA", "contaPaiDescricaoConta");
		data  = FieldFactoryUtil.createDateField("DATA","dd/MM/yy");
		valor = FieldFactoryUtil.createTextField("VALOR");
		clinica.setRequired(true);
		data.setRequired(true);
		conta.setRequired(true);
		valor.setRequired(true);
		clinica.setRequiredError("Ítem obrigatório");
		data.setRequiredError("Ítem obrigatório");
		conta.setRequiredError("Ítem obrigatório");
		valor.setRequiredError("Ítem obrigatório");
		registro = FieldFactoryUtil.createTextField("REGISTRO");
//		registro.setRequired(true);
//		registro.setRequiredError("Ítem obrigatório");
		
		btSave = new Button();
		btAdd = new Button();
		btRem = new Button();
		btExcel = new Button();

		motivoExclusao = FieldFactoryUtil.createTextField("MOTIVO DA EXLUSÃO");
		btDelete = new Button("EXCLUIR");
		montaModalWindows();
		
		valor.setLocale(new Locale("pt","BR"));
		valor.addStyleName("align-right");
		df.setParseBigDecimal(true);
		df.setDecimalSeparatorAlwaysShown(true);
		df.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("pt","BR")));
		
		montaTabela();
		addListener();

		addComponent(montaFiltro());
		addComponent(montaPainel());
		addComponent(tabela);
		
	}
	
	private void montaModalWindows(){
		modalWindow = new Window();
		modalWindow.setModal(true);
		modalWindow.setWidth("200px");
		modalWindow.setHeight("200px");
		modalWindow.center();
		
		VerticalLayout vl = new VerticalLayout();
		vl.addComponent(motivoExclusao);
		vl.addComponent(btDelete);
		vl.setSpacing(true);
//		vl.setMargin(true);
		
		modalWindow.addComponent(vl);
	}
	
	private void formataValor(){
		Property property = new Property() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private BigDecimal value;

            public Object getValue() {
                return value;
            }

            public void setValue(Object newValue) throws ReadOnlyException,
                    ConversionException {
                if (newValue == null) {
                    value = null;
                } else if (newValue instanceof BigDecimal) {
                    value = (BigDecimal) newValue;
                } else {
                    throw new ConversionException();
                }
            }

            public Class<?> getType() {
                return BigDecimal.class;
            }

            public boolean isReadOnly() {
                return false;
            }

            public void setReadOnly(boolean newStatus) {
                // ignore
            }
        };
        
        PropertyFormatter formatter = new PropertyFormatter(property) {

            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private final DecimalFormat df = new DecimalFormat("#,##0.00");
//			private final DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
            {
                df.setParseBigDecimal(true);
                // df.setRoundingMode(RoundingMode.HALF_UP);
            }

            @Override
            public String format(Object value) {

                final String retVal;
                if (value == null) {
                    retVal = "";
                } else {
                    retVal = df.format(value);
                }
                return retVal;
            }

            @Override
            public Object parse(String formattedValue) throws Exception {
                if (formattedValue != null
                        && formattedValue.trim().length() != 0) {
                    BigDecimal value = (BigDecimal) df.parse(formattedValue);
                    value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
                    return value;
                }
                return null;
            }
        };
        
        valor.setPropertyDataSource(formatter);
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

		hl.setComponentAlignment(btFiltro, Alignment.BOTTOM_LEFT);

		dados.addComponent(hl);
		hl2.addComponent(dados);
		return dados;
	}
	private Component montaPainel(){
		Panel dados = new Panel();
		dados.setCaption("DADOS");
		HorizontalLayout hl = new HorizontalLayout();
		HorizontalLayout hl2 = new HorizontalLayout();

		GridLayout gl = new GridLayout(8,2);
//		hl.setCaption("DADOS");
//		hl.setMargin(true);
		gl.setSpacing(true);
		
//		dados.setContent(hl);

		conta.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
		gl.addComponent(clinica);
		gl.addComponent(data);
		gl.addComponent(valor);
		gl.addComponent(registro);
		gl.addComponent(btAdd);
		gl.addComponent(btSave);
		gl.addComponent(btRem);
		gl.addComponent(btExcel);
		gl.addComponent(conta,0,1,3,1);
		conta.setWidth("100%");
		dados.addComponent(gl);
		btAdd.setIcon(new ThemeResource("icons/16/add_16.png"));
		btSave.setIcon(new ThemeResource("icons/16/save_16.png"));
		btRem.setIcon(new ThemeResource("icons/16/recycle_16.png"));
		btExcel.setIcon(new ThemeResource("icons/16/excel_16.png"));
		btSave.setDescription("Salva registro.");
		btAdd.setDescription("Adiciona novo registro.");
		btRem.setDescription("Exclui registro.");
		btExcel.setDescription("Exportar para excel.");
//		btAdd.setEnabled(false);
		btRem.setEnabled(false);
//		dados.addComponent(tabela);
		
		hl2.addComponent(dados);
		
		gl.setComponentAlignment(btSave, Alignment.BOTTOM_LEFT);
		gl.setComponentAlignment(btRem, Alignment.BOTTOM_LEFT);
		gl.setComponentAlignment(btAdd, Alignment.BOTTOM_LEFT);
		gl.setComponentAlignment(btExcel, Alignment.BOTTOM_LEFT);
		
		return dados;
		
	}
	
	private void montaTabela(){
		tabela = new Table();
		tabela.setLocale(new Locale("pt", "BR"));
		tabela.setSelectable(true);
		tabela.setImmediate(true);
		tabela.setPageLength(20);
//		tabela.setColumnCollapsingAllowed(true);
		tabela.setWidth("100%");

		tabela.addContainerProperty("fluxo.clinica", Clinica.class,  null);
		tabela.addContainerProperty("fluxo.conta", Conta.class,  null);
		tabela.addContainerProperty("fluxo.contaPai", Conta.class,  null);
		tabela.addContainerProperty("fluxo.data", Date.class,  null);
		tabela.addContainerProperty("fluxo.valor", BigDecimal.class,  null);
		tabela.addContainerProperty("fluxo.registro", String.class,  null);
		tabela.addContainerProperty("fluxo.criacao", Date.class,  null);
		tabela.addContainerProperty("fluxo.alteracao", Date.class,  null);
		tabela.addContainerProperty("fluxo.usuario", Usuario.class,  null);
		tabela.addContainerProperty("fluxo.ativo", Boolean.class,  null);
		tabela.addContainerProperty("fluxo.motivo", String.class,  null);

		tabela.setVisibleColumns(new Object[]{"fluxo.clinica","fluxo.contaPai","fluxo.conta", "fluxo.data", "fluxo.valor", 
				"fluxo.registro","fluxo.criacao","fluxo.alteracao","fluxo.usuario","fluxo.ativo","fluxo.motivo"});
		
		tabela.setColumnHeaders(new String[]{"clinica","conta pai","conta", "data", "valor",
				"registro","data criação","data alteração", "usuario","ativo","motivo"});
		
		tabela.addGeneratedColumn("fluxo.data", new DataColumnGenerator());
		tabela.addGeneratedColumn("fluxo.criacao", new DataHoraColumnGenerator());
		tabela.addGeneratedColumn("fluxo.alteracao", new DataHoraColumnGenerator());
		tabela.addGeneratedColumn("fluxo.valor", new BigDecimalColumnGenerator());
		tabela.addGeneratedColumn("fluxo.ativo", new SimNaoColumnGenerator());
		
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
					if (row!=null && row.getItemProperty("fluxo.ativo")!=null && 
							row.getItemProperty("fluxo.ativo").getValue()!=null &&
							!(Boolean)row.getItemProperty("fluxo.ativo").getValue()) {
						return "inativo";
					}
				}
		        return null;
		      }
			
		    });
	}
	private void addListener(){
		btSave.addListener(this);
		btRem.addListener(this);
		btAdd.addListener(this);
		btExcel.addListener(this);
		btFiltro.addListener(this);
		
		tabela.addListener(new Table.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				bean = (Fluxo)event.getProperty().getValue();	
				if (bean != null) {
					clinica.setValue(bean.getClinica());
					conta.setValue(bean.getConta());
					data.setValue(bean.getData());
					registro.setValue(bean.getRegistro());
					try {valor.setValue(df.format(bean.getValor()));} catch (Exception e) {}
					btRem.setEnabled(true);
				}else{
					limparNovo();
					btRem.setEnabled(false);
				}
			}
		});
	}

	public void limpar(){
		clinica.select(null);
		conta.setValue(null);
		registro.setValue(null);
		data.setValue(new Date());
		valor.setValue(df.format(new BigDecimal("0.0")));
	}
	public void limparNovo(){
		registro.setValue(null);
		data.setValue(new Date());
	}
	
	public void setList(List<Fluxo> lista){
		tabela.removeAllItems();
		for (Fluxo f : lista) {
			Item itemBean;
			if (tabela.getItem(f)==null){
				itemBean = tabela.addItem(f);
			}else{
				itemBean = tabela.getItem(f);
			}
			itemBean.getItemProperty("fluxo.clinica").setValue(f.getClinica());
			itemBean.getItemProperty("fluxo.contaPai").setValue(f.getConta().getContaPai());
			itemBean.getItemProperty("fluxo.conta").setValue(f.getConta());
			itemBean.getItemProperty("fluxo.data").setValue(f.getData());
			try {itemBean.getItemProperty("fluxo.valor").setValue(f.getValor().toPlainString());} catch (Exception e) {}
			try {itemBean.getItemProperty("fluxo.usuario").setValue(f.getUsuario());} catch (Exception e) {}

			itemBean.getItemProperty("fluxo.criacao").setValue(f.getDtCriacao());
			itemBean.getItemProperty("fluxo.alteracao").setValue(f.getDtEdicao());

			itemBean.getItemProperty("fluxo.registro").setValue(f.getRegistro());
			itemBean.getItemProperty("fluxo.ativo").setValue(f.getAtivo());
			itemBean.getItemProperty("fluxo.motivo").setValue(f.getMotivoExclusao());
			
		}
	}
	
	@SuppressWarnings("serial")
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton()==btSave){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessSave>() {});
//			bean = new Fluxo();
		}
		if (event.getButton()==btAdd){
			bean = new Fluxo();
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessAdd>() {});
			bean = new Fluxo();
		}
		if (event.getButton()==btRem){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessRem>() {});
		}
		if (event.getButton()==btDelete){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessDelete>() {});
		}
		if (event.getButton()==btFiltro){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessFilter>() {});
		}
		if (event.getButton()==btExcel){
			try {
				getWindow().open(new GeraXls("historico.xls",tabela,getApplication()).getStream());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public Fluxo getBean() {
		if (bean==null || bean.getId()==null){
			bean = new Fluxo();
		}
		try {} catch (Exception e) {}
		try {bean.setClinica((Clinica)clinica.getValue());} catch (Exception e) {}
		try {bean.setConta((Conta)conta.getValue());} catch (Exception e) {}
		try {bean.setData((Date)data.getValue());} catch (Exception e) {}
		try {bean.setValor((BigDecimal)df.parse(valor.getValue().toString()));} catch (Exception e) {}
		try {bean.setRegistro((String)registro.getValue());} catch (Exception e) {}
//		try {bean.setMotivoExclusao((String).getValue());} catch (Exception e) {}
		
		return bean;
	}

	public void setBean(Fluxo bean) {
		this.bean = bean;
	}

	public void setListaClinica(List<Clinica> list) {
		clinica.setContainerDataSource(CollectionContainer.fromBeans(list));
		fClinica.setContainerDataSource(CollectionContainer.fromBeans(list));
	}

	public void setListaConta(List<Conta> list) {
		conta.setContainerDataSource(CollectionContainer.fromBeans(list));
	}

	public Filtro1 getFiltro1() {
		Filtro1 f = new Filtro1();
		try {f.setClinicas((Set<Clinica>)fClinica.getValue());} catch (Exception e) {e.printStackTrace();}
		try {f.setDtInicio((Date)dtInicio.getValue());} catch (Exception e) {}
		try {f.setDtFim((Date)dtFim.getValue());} catch (Exception e) {}
		// TODO Auto-generated method stub
		return f;
	}

}
