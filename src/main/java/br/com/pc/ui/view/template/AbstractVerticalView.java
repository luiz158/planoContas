package br.com.pc.ui.view.template;

import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.template.BaseVaadinView;
import br.gov.frameworkdemoiselle.ui.AutoTable;
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
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractVerticalView<E> extends BaseVaadinView implements Button.ClickListener {

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

	private Accordion accordion;
	private GridLayout accordionGrid1;
	
	private AutoTable tabela ;
	
	private VerticalLayout conteudo;
	private HorizontalLayout controles;
	
	private GridLayout telaPrincipal = new GridLayout(1, 3);
	
	public AbstractVerticalView() {
		beanClass = Reflections.getGenericTypeArgument(getClass(), 0);
		tabela = new AutoTable(beanClass);
	}
	
	@Override
	public void initializeComponents() {
		setBean(instantiate());
		//view
//		setSizeFull();
//		this.setSizeFull();
//		super.setSizeFull();
//		this.setSpacing(true);
//		setHeight("500px");
//		super.setSizeFull()
		//splitPanel
		HorizontalLayout principal = new HorizontalLayout();
		principal.setSizeFull();
		principal.setSpacing(true);
		
		//Accordion BARRA LATERAL ESQUERDA
		accordionGrid1 = new GridLayout(1, 1);
		accordionGrid1.setSpacing(true);
		accordion = new Accordion();
//		accordion.setSizeFull();
		accordion.addTab(accordionGrid1,"FILTRO BÁSICO",null);
		
		//TELA PRINCIPAL
		
		telaPrincipal.setSizeFull();
		telaPrincipal.setMargin(true);
		telaPrincipal.setSpacing(true);

//		tabela = new AutoTable(Reflections.getGenericTypeArgument(getClass(), 0));
		beanClass = Reflections.getGenericTypeArgument(getClass(), 0);
		tabela = new AutoTable(beanClass);
		tabela.setWidth("100%");
		tabela.setHeight("300px");
		
		conteudo = new VerticalLayout();
		conteudo.setWidth("100%");

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
		telaPrincipal.addComponent(conteudo);
		telaPrincipal.addComponent(controles);

		accordion.setWidth("150px");
		accordion.setHeight("100%");
		principal.addComponent(accordion);
		principal.addComponent(telaPrincipal);

		principal.setExpandRatio(telaPrincipal, 1);
		addComponent(principal);
	}
	
	public void addPainel(Component painel){
		conteudo.addComponent(painel);
	}
	public void addPainel(Component painel, String caption){
		conteudo.addComponent(painel);
		painel.setCaption(caption);
	}
	public void addPainel(Panel painel, String caption, Resource icon ){
		conteudo.addComponent(painel);
		painel.setCaption(caption);
		painel.setIcon(icon);
	}

	abstract public void limpar();
	
	@SuppressWarnings({ "serial", "unchecked" })
	@Override
	public void buttonClick(ClickEvent event) {
		if(event.getButton()==btSalvar){
			beanManager.fireEvent(getBean(),
					new AnnotationLiteral<ProcessSave>() { });
		}		
		if(event.getButton()==btNovo){
			try {
				bean = (E) Reflections.getGenericTypeArgument(getClass(), 0).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if(event.getButton()==btLimpar){
			limpar();
		}
		if(event.getButton()==btExcluirConfirmar){
			beanManager.fireEvent(getBean(),
					new AnnotationLiteral<ProcessDelete>() { });
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
	
	public E getBean() {
		return bean;
	}
	public void setBean(E bean) {
		this.bean = bean;
	}
	
	public void setList(List<E> list) {
		tabela.setContainerDataSource(CollectionContainer.fromBeans(list));
	}

	public void setList(ResultListHandler<E> resultListHandler) {
		tabela.setContainerDataSource(PagedContainer.create(beanClass, resultListHandler));
	}

	public AutoTable getTabela() {
		return tabela;
	}

	public void setTabela(AutoTable tabela) {
		this.tabela = tabela;
	}
}
