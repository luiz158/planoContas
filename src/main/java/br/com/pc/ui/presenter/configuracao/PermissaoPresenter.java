package br.com.pc.ui.presenter.configuracao;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.pc.business.configuracao.GrupoBC;
import br.com.pc.business.configuracao.PermissaoBC;
import br.com.pc.domain.configuracao.Permissao;
import br.com.pc.ui.view.configuracao.PermissaoView;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

@ViewController
@SessionScoped
public class PermissaoPresenter extends AbstractPresenter<PermissaoView> {

	private static final long serialVersionUID = 1L;

	@Inject
	private GrupoBC grupoBC;
	@Inject
	private PermissaoBC permissaoBC;
	
	public void processSave(@Observes @ProcessSave Permissao permissao) {
		if (permissao.getId() != null) {
			permissaoBC.update(permissao);
		} else {
			permissaoBC.insert(permissao);
		}
		getView().setList(permissaoBC.findAll());
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

	public void beforeNavigateToView(@Observes @BeforeNavigateToView PermissaoView view) {
		view.setGrupo(grupoBC.findGrupos());
		view.setList(permissaoBC.findPermissoes());
		view.setList(permissaoBC.findPermissoes());
	}
	
	public void processFormClear(@Observes @ProcessClear PermissaoView view) {
		view.getTabela().select(null);
		view.limpar();
		view.setList(permissaoBC.findPermissoes());
	}

}
