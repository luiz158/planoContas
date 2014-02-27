package br.com.pc.ui.presenter.configuracao;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.pc.business.configuracao.GrupoBC;
import br.com.pc.business.configuracao.UsuarioBC;
import br.com.pc.domain.configuracao.Grupo;
import br.com.pc.ui.view.configuracao.GrupoView;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

@ViewController
@SessionScoped
public class GrupoPresenter extends AbstractPresenter<GrupoView> {

	private static final long serialVersionUID = 1L;

	@Inject	private UsuarioBC usuarioBC;
	@Inject	private GrupoBC grupoBC;
	
	public void processSave(@Observes @ProcessSave Grupo grupo) {
		if (grupo.getId() != null) {
			grupoBC.update(grupo);
		} else {
			grupoBC.insert(grupo);
		}
		getView().clear();
		getView().setDeleteButtonEnabled(false);
	}

	public void processItemSelection(@Observes @ProcessItemSelection Grupo grupo) {
		getView().setBean(grupo);
		getView().setUsuario(grupo.getUsuarios());
		getView().setDeleteButtonEnabled(true);
	}

	public void beforeNavigateToView(@Observes @BeforeNavigateToView GrupoView view) {
//		view.setPerfis(perfilBC.findAll());
		view.setUsuarios(usuarioBC.findAll());
		view.setList(grupoBC.findAll());
	}

//	public void beforeNavigate(@Observes @BeforeNavigateToView UsuarioView view) {
//		getView().setPerfis(perfilBC.findAll());
//		getView().setBean(new Usuario());
//	}
	
	public void processFormClear(@Observes @ProcessClear Grupo grupo) {
//		getView().getCrudForm().setBean(new Usuario());
		getView().getTabela().select(null);
		getView().clear();
		getView().setDeleteButtonEnabled(false);
	}

}
