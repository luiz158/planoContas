package br.com.pc.ui.view.configuracao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.com.pc.domain.configuracao.EnumMenu;
import br.com.pc.domain.configuracao.EnumTipoPermissao;
import br.com.pc.domain.configuracao.Grupo;
import br.com.pc.domain.configuracao.Permissao;
import br.com.pc.util.components.FieldFactoryUtil;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.View;
import br.gov.frameworkdemoiselle.template.BaseVaadinView;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;

@View
public class PermissaoView extends BaseVaadinView implements Button.ClickListener {

	private static final long serialVersionUID = 1L;

	@Inject
	private BeanManager beanManager;
	
	private ComboBox grupo;
	private ComboBox menu;
	private OptionGroup visualizar;
	private OptionGroup alterar;
	private OptionGroup criar;
	private OptionGroup excluir;
	private OptionGroup imprimir;
	
	private Button btSalvar;
	private Button btDeletar;
	private Button btLimpar;
	
	private Table tabela;
	
	private Permissao bean;
	
	public void initializeComponents() {
		setCaption(EnumMenu.PERMISSOES.getNome());
		
		grupo	= FieldFactoryUtil.createComboBox("GRUPO", "descricao");
		menu	= FieldFactoryUtil.createComboBox("MENU", "nome");
		visualizar	= FieldFactoryUtil.createOptionGroup("VISUALIZAR","descricao");
		alterar	= FieldFactoryUtil.createOptionGroup("ALTERAR", "descricao");
		criar	= FieldFactoryUtil.createOptionGroup("CRIAR", "descricao");
		excluir	= FieldFactoryUtil.createOptionGroup("EXCLUIR", "descricao");
		imprimir	= FieldFactoryUtil.createOptionGroup("IMPRIMIR", "descricao");
		
		btSalvar = new Button("SALVAR");
		btDeletar = new Button("DELETAR");
		btLimpar = new Button("LIMPAR");
		tabela = new Table();
		
		btSalvar.addListener(this);
		btDeletar.addListener(this);
		btLimpar.addListener(this);
		class TitularComparator implements Comparator<EnumMenu> {
		    public int compare(EnumMenu nome, EnumMenu outroNome) {
		        return nome.getNome().compareTo(outroNome.getNome());
		    }
		}
		TitularComparator comparator = new TitularComparator();
		Collections.sort(EnumMenu.asList(), comparator);
		menu.setContainerDataSource(CollectionContainer.fromBeans(EnumMenu.asList()));
		
		visualizar.setContainerDataSource(CollectionContainer.fromBeans(EnumTipoPermissao.asList()));
		alterar.setContainerDataSource(CollectionContainer.fromBeans(EnumTipoPermissao.asList()));
		criar.setContainerDataSource(CollectionContainer.fromBeans(EnumTipoPermissao.asList()));
		excluir.setContainerDataSource(CollectionContainer.fromBeans(EnumTipoPermissao.asList()));
		imprimir.setContainerDataSource(CollectionContainer.fromBeans(EnumTipoPermissao.asList()));
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.setMargin(true);
		hl.setSpacing(true);
		hl.addComponent(grupo);
		hl.addComponent(menu);
		hl.addComponent(visualizar);
		hl.addComponent(alterar);
		hl.addComponent(criar);
		hl.addComponent(excluir);
		hl.addComponent(imprimir);
		hl.addComponent(btSalvar);
		hl.addComponent(btDeletar);
		hl.addComponent(btLimpar);

		montarTabela();
		
		addComponent(hl);
		addComponent(tabela);
		
		limpar();
		
	}
	
	private void montarTabela(){
		tabela.setSelectable(true);
		tabela.setImmediate(true);
		tabela.setWidth("100%");
		
		tabela.addContainerProperty("grupo", Grupo.class,  null);
		tabela.addContainerProperty("menu", String.class,  null);
		tabela.addContainerProperty("visualizar", EnumTipoPermissao.class,  null);
		tabela.addContainerProperty("alterar", EnumTipoPermissao.class,  null);
		tabela.addContainerProperty("criar", EnumTipoPermissao.class,  null);
		tabela.addContainerProperty("excluir", EnumTipoPermissao.class,  null);
		tabela.addContainerProperty("imprimir", EnumTipoPermissao.class,  null);
		
		tabela.setVisibleColumns(new Object[]{"grupo", "menu", "visualizar", 
				"alterar", "criar", "excluir","imprimir"});
		
		tabela.setColumnHeaders(new String[]{"grupo", "menu", "visualizar", 
				"alterar", "criar", "excluir","imprimir"});
		
		addListener();
	}
	
	public Table getTabela(){
		return tabela;
	}
	
	public void addListener(){
		tabela.addListener(new Table.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
//				Object item = event.getProperty().getValue();
				bean = (Permissao)event.getProperty().getValue();	
				if (bean != null) {
					//TODO comando para preencher fields e bean
					grupo.select(bean.getGrupo());
					menu.select(bean.getMenu());
					visualizar.setValue(bean.getVisualizar());
					alterar.select(bean.getAlterar());
					criar.setValue(bean.getCriar());
					excluir.setValue(bean.getExcluir());
					imprimir.setValue(bean.getImprimir());
				}else{
					limpar();
				}
			}
		});
	}
	
	public void limpar(){
		limpaComponentes();
		bean = new Permissao();
	}
	
	public void limpaComponentes(){
		grupo.select(null);
		menu.select(null);
		visualizar.setValue(EnumTipoPermissao.INDETERMINADO);
		alterar.select(EnumTipoPermissao.INDETERMINADO);
		criar.setValue(EnumTipoPermissao.INDETERMINADO);
		excluir.setValue(EnumTipoPermissao.INDETERMINADO);
		imprimir.setValue(EnumTipoPermissao.INDETERMINADO);
	}

	public void setGrupo(List<Grupo> list) {
		if (this.grupo != null) {
			this.grupo.setContainerDataSource(CollectionContainer.fromBeans(list));
		}
	}
	
	public void setList(List<Permissao> list){
		tabela.setContainerDataSource(CollectionContainer.fromBeans(list));
		tabela.setVisibleColumns(new Object[]{"grupo", "menu", "visualizar", 
				"alterar", "criar", "excluir","imprimir"});
		tabela.setColumnHeaders(new String[]{"grupo", "menu", "visualizar", 
				"alterar", "criar", "excluir","imprimir"});
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton()==btSalvar){
			if (bean.getGrupo()==null){
				bean.setGrupo((Grupo)grupo.getValue());
			}
			if (bean.getMenu()==null){
				bean.setMenu((EnumMenu)menu.getValue());
			}
			
			bean.setVisualizar((EnumTipoPermissao)visualizar.getValue());
			bean.setAlterar((EnumTipoPermissao)alterar.getValue());
			bean.setCriar((EnumTipoPermissao)criar.getValue());
			bean.setExcluir((EnumTipoPermissao)excluir.getValue());
			bean.setImprimir((EnumTipoPermissao)imprimir.getValue());
			
			beanManager.fireEvent(bean, new AnnotationLiteral<ProcessSave>() { });
		}
		if (event.getButton()==btDeletar){
			beanManager.fireEvent(bean, new AnnotationLiteral<ProcessDelete>() { });
		}
		if (event.getButton()==btLimpar){
			beanManager.fireEvent(this, new AnnotationLiteral<ProcessClear>() { });
		}
		
	}

}
