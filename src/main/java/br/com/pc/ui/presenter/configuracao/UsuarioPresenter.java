package br.com.pc.ui.presenter.configuracao;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.pc.business.configuracao.GrupoBC;
import br.com.pc.business.configuracao.UsuarioBC;
import br.com.pc.domain.configuracao.Usuario;
import br.com.pc.ui.view.configuracao.UsuarioView;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

@ViewController
@SessionScoped
public class UsuarioPresenter extends AbstractPresenter<UsuarioView> {

	private static final long serialVersionUID = 1L;

	@Inject	private UsuarioBC usuarioBC;
	@Inject	private GrupoBC grupoBC;
	
	public void processSave(@Observes @ProcessSave Usuario usuario) {
		if (usuario.getId() != null) {
			usuarioBC.update(usuario);
		} else {
			usuarioBC.insert(usuario);
		}
		getView().clear();
		getView().setDeleteButtonEnabled(false);
		getView().setList(usuarioBC.findAll());
	}

	public void processItemSelection(@Observes @ProcessItemSelection Usuario usuario) {
		getView().setBean(usuario);
		getView().setGrupo(usuario.getGrupos());
		getView().setDeleteButtonEnabled(true);
	}

	public void beforeNavigateToView(@Observes @BeforeNavigateToView UsuarioView view) {
//		view.setPerfis(perfilBC.findAll());
		view.setGrupos(grupoBC.findAll());
		view.setList(usuarioBC.findAll());
	}

//	public void beforeNavigate(@Observes @BeforeNavigateToView UsuarioView view) {
//		getView().setPerfis(perfilBC.findAll());
//		getView().setBean(new Usuario());
//	}
	
	public void processFormClear(@Observes @ProcessClear Usuario usuario) {
//		getView().getCrudForm().setBean(new Usuario());
		getView().getTabela().select(null);
		getView().clear();
		getView().setDeleteButtonEnabled(false);
	}

}
