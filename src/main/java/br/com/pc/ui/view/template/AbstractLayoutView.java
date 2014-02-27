package br.com.pc.ui.view.template;

import java.util.List;
import java.util.Locale;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.com.pc.ui.annotation.ProcessFilter;
import br.com.pc.util.components.CustomAutoTable;
import br.com.pc.util.components.FieldFactoryUtil;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.template.BaseVaadinView;
import br.gov.frameworkdemoiselle.ui.PagedContainer;
import br.gov.frameworkdemoiselle.ui.ResultListHandler;
import br.gov.frameworkdemoiselle.util.Reflections;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public abstract class AbstractLayoutView<E> 
				extends BaseVaadinView 
				implements Button.ClickListener {

	private static final long serialVersionUID = 1L;

	@Inject
	private BeanManager beanManager;

	private Class<E> beanClass;
	
	private E bean;
	
	private Button btSalvar = new Button("Salvar");
	private Button btNovo = new Button("Novo");
	private Button btLimpar = new Button("Limpar");
	private Button btExcluir = new Button("Excluir");
	private Button btExcluirConfirmar = new Button("Excluir");
	private Button btFiltro = new Button("Filtro");

	private Accordion accordion;
	private GridLayout accordionGrid1;
	private VerticalLayout accordionVl1;
	
	private CustomAutoTable tabela ;
	
	private TabSheet componentes;
	private HorizontalLayout controles;
	
	private GridLayout telaPrincipal = new GridLayout(1, 1);
	
	private Window subwindow = new Window("Confirma Exclusão?");
    private TextArea message = FieldFactoryUtil.createTextArea("Motivo da Exclusão", 2, 10);
	
	public AbstractLayoutView() {
		beanClass = Reflections.getGenericTypeArgument(getClass(), 0);
		tabela = new CustomAutoTable(beanClass);
		Locale l = new Locale("pt", "BR");
		tabela.setLocale(l);
	}
	
//	public AbstractLayoutView<E> me(){
//		return this;
//	}

	@Override
	public void initializeComponents() {
		tabela.removeAllActionHandlers();
		tabela.addListener(new Table.ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				Object item = event.getProperty().getValue();
				if (item != null) {
					eventoItemSelection(item);
				}
			}

		});
		
//		setBean(instantiate());
		//view
//		setSizeFull();
//		this.setSizeFull();
//		super.setSizeFull();
//		this.setSpacing(true);
//		setHeight("500px");
//		super.setSizeFull()
		//SPLIT PANEL
		HorizontalLayout principal = new HorizontalLayout();
		principal.setSizeFull();
		principal.setSpacing(true);
		//LISTENER
		btNovo.addListener(this);
		btSalvar.addListener(this);
		btLimpar.addListener(this);
		btExcluir.addListener(this);
		btExcluirConfirmar.addListener(this);
		btFiltro.addListener(this);
		
		//Accordion BARRA LATERAL ESQUERDA
		accordionVl1 = new VerticalLayout();
		accordionGrid1 = new GridLayout(1, 1);
		accordionGrid1.setSpacing(true);
		accordionVl1.setMargin(true);
		accordionVl1.addComponent(accordionGrid1);
		accordionVl1.addComponent(btFiltro);
		accordion = new Accordion();
		accordionVl1.setHeight("100%");
//		accordion.setSizeFull();
		accordion.addTab(accordionVl1,"FILTRO BÁSICO",null);
		
		//TELA PRINCIPAL
		
		telaPrincipal.setSizeFull();
		telaPrincipal.setMargin(true);
		telaPrincipal.setSpacing(true);

//		tabela = new AutoTable(Reflections.getGenericTypeArgument(getClass(), 0));
//		beanClass = Reflections.getGenericTypeArgument(getClass(), 0);
//		tabela = new CustomAutoTable(beanClass);
		tabela.setWidth("100%");
		tabela.setHeight("250px");
		
		componentes = new TabSheet();
		componentes.setWidth("100%");
		componentes.setHeight("300px");

		controles = new HorizontalLayout();
		
		controles.setSpacing(true);
		controles.setWidth("100%");

		controles.addComponent(btNovo);
		controles.addComponent(btSalvar);
		controles.addComponent(btLimpar);
		controles.addComponent(btExcluir);
		controles.setComponentAlignment(btNovo, Alignment.MIDDLE_LEFT);
		controles.setComponentAlignment(btSalvar, Alignment.MIDDLE_LEFT);
		controles.setComponentAlignment(btLimpar, Alignment.MIDDLE_LEFT);
		controles.setExpandRatio(btExcluir, 1.0f);
		controles.setComponentAlignment(btExcluir, Alignment.MIDDLE_RIGHT);
		
		
		telaPrincipal.addComponent(tabela);
		telaPrincipal.addComponent(componentes);
		telaPrincipal.addComponent(controles);

		accordion.setWidth("200px");
		accordion.setHeight("100%");
		principal.addComponent(accordion);
		principal.addComponent(telaPrincipal);

		principal.setExpandRatio(telaPrincipal, 1);
		addComponent(principal);
		
		setConfirmaExclusao();
	}
	
	public void addPainel(Component tab){
		componentes.addTab(tab,tab.getCaption(),null);
		tab.setCaption(null);
	}
	public void addPainel(Component tab, String caption){
		componentes.addTab(tab,caption,null);
		tab.setCaption(null);
	}
	public void addPainel(Panel tab, String caption, Resource icon ){
		componentes.addTab(tab,caption,icon);
		tab.setCaption(null);
	}
	
	public void addFiltro(Component c){
		accordionGrid1.addComponent(c);
	}
	
	//OBRIGATÓRIOS
	abstract public void montarFiltro();
	abstract public E getFiltroInicio();
	abstract public E getFiltroFim();
	abstract public void limpar();
	abstract public void atualizaForms();
	
	//EVENTOS DA TELA
	@SuppressWarnings({ "serial", "unchecked" })
	public void eventoItemSelection(Object item){
		setBean((E) item);
		beanManager.fireEvent(this, new AnnotationLiteral<ProcessItemSelection>() {
		});
	}
	
	@SuppressWarnings({ "serial" })
	@Override
	public void buttonClick(ClickEvent event) {
		if(event.getButton()==btSalvar){
//			beanManager.fireEvent(getBean(), new AnnotationLiteral<ProcessSave>() { });
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessSave>() { });
		}		
		if(event.getButton()==btNovo){
//			try {
//				bean = (E) Reflections.getGenericTypeArgument(getClass(), 0).newInstance();
			limpar();
			bean = instantiate();
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
		}
		if(event.getButton()==btLimpar){
			limpar();
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessClear>() { });
//			limpar();
		}
		if(event.getButton()==btExcluir){
	    	if (subwindow.getParent() != null) {
	            getWindow().showNotification(
	                    "Janela já está aberta!");
	        } else {
	        	message.setValue(null);
	            getWindow().addWindow(subwindow);
	        }
		}
		if(event.getButton()==btExcluirConfirmar){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessDelete>() { });
		}
		if(event.getButton()==btFiltro){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessFilter>() { });
		}
	}

	
	private E instantiate() {
		try {
			return beanClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}catch (NullPointerException e) {
			throw new RuntimeException(e);
		}
	}
	
    private void setConfirmaExclusao(){
    	subwindow.setModal(true);

	    VerticalLayout layout = (VerticalLayout) subwindow.getContent();
	    layout.setMargin(true);
	    layout.setSpacing(true);
	
	    layout.addComponent(message);
	
	    layout.addComponent(btExcluirConfirmar);
	    layout.setComponentAlignment(btExcluirConfirmar, Alignment.TOP_RIGHT);
	    
    }
    
	public E getBean() {
		return bean;
	}
	public void setBean(E bean) {
		this.bean = bean;
		atualizaForms();
	}
	
	public void setList(List<E> list) {
		tabela.setContainerDataSource(CollectionContainer.fromBeans(list));
	}

	public void setList(ResultListHandler<E> resultListHandler) {
		tabela.setContainerDataSource(PagedContainer.create(beanClass, resultListHandler));
	}

	public CustomAutoTable getTabela() {
		return tabela;
	}

	public void setTabela(CustomAutoTable tabela) {
		this.tabela = tabela;
	}

	public TextArea getMessage() {
		return message;
	}
}
