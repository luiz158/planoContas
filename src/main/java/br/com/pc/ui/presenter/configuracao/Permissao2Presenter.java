package br.com.pc.ui.presenter.configuracao;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.pc.business.configuracao.GrupoBC;
import br.com.pc.business.configuracao.PermissaoBC;
import br.com.pc.domain.configuracao.Permissao;
import br.com.pc.ui.annotation.ProcessAdd;
import br.com.pc.ui.view.configuracao.Permissao2View;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

import com.vaadin.ui.Window.Notification;

@ViewController
@SessionScoped
public class Permissao2Presenter extends AbstractPresenter<Permissao2View> {

	private static final long serialVersionUID = 1L;

	@Inject	private GrupoBC grupoBC;
	@Inject	private PermissaoBC permissaoBC;
	
	public void processSave(@Observes @ProcessSave Permissao permissao) {
		if (permissao.getId() != null) {
			permissaoBC.update(permissao);
			getView().getWindow().showNotification("REGISTRO SALVO COM SUCESSO!!!");
		} else {
			getView().getWindow().showNotification("É NECESSÁRIO SELECIONAR UM REGISTRO!!!",Notification.TYPE_WARNING_MESSAGE);
//			permissaoBC.insert(permissao);
		}
		getView().setList(permissaoBC.findPermissoes());
		getView().limpar();
	}
	public void processAdd(@Observes @ProcessAdd Permissao permissao) {
		permissaoBC.insert(permissao);
		getView().getWindow().showNotification("REGISTRO CRIADO COM SUCESSO!!!");
		getView().setList(permissaoBC.findPermissoes());
		getView().limpar();
	}
	
	public void processDelete(@Observes @ProcessDelete Permissao permissao) {
		if (permissao.getId() != null) {
			permissaoBC.delete(permissao.getId());
		} 
		getView().setList(permissaoBC.findPermissoes());
		getView().limpar();
	}

	public void processItemSelection(@Observes @ProcessItemSelection Permissao permissao) {

	}

	public void beforeNavigateToView(@Observes @BeforeNavigateToView Permissao2View view) {
		view.setGrupo(grupoBC.findGrupos());
		view.setList(permissaoBC.findPermissoes());
//		view.setList(permissaoBC.findPermissoes());
	}
	
	public void processFormClear(@Observes @ProcessClear Permissao2View view) {
		view.getTabela().select(null);
		view.limpar();
		view.setList(permissaoBC.findPermissoes());
	}

}
