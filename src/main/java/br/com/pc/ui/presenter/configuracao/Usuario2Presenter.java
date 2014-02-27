package br.com.pc.ui.presenter.configuracao;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.pc.business.configuracao.GrupoBC;
import br.com.pc.business.configuracao.UsuarioBC;
import br.com.pc.domain.configuracao.Usuario;
import br.com.pc.ui.view.configuracao.Usuario2View;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

@ViewController
@SessionScoped
public class Usuario2Presenter extends AbstractPresenter<Usuario2View> {

	private static final long serialVersionUID = 1L;

	@Inject	private UsuarioBC usuarioBC;
	@Inject	private GrupoBC grupoBC;
	
	public void processSave(@Observes @ProcessSave Usuario2View view) {
		Usuario usuario = view.getBean();
		if (usuario.getId() != null) {
			usuarioBC.update(usuario);
		} else {
			usuarioBC.insert(usuario);
		}
		view.setList(usuarioBC.findAll());
	}
	public void processDelete(@Observes @ProcessDelete Usuario2View view) {
		Usuario usuario = view.getBean();
		if (usuario.getId() != null) {
			usuario.setAtivo(false);
			usuarioBC.update(usuario);
		} 
		view.setList(usuarioBC.findAll());
	}

	public void processItemSelection(@Observes @ProcessItemSelection Usuario2View view) {
		Usuario usuario = view.getSelected();
//		view.setGrupos(grupoBC.findAll());
		view.setBean(usuario);
	}

	public void beforeNavigateToView(@Observes @BeforeNavigateToView Usuario2View view) {
//		view.setPerfis(perfilBC.findAll());
		view.setGrupos(grupoBC.findAll());
		view.setList(usuarioBC.findAll());
	}

//	public void beforeNavigate(@Observes @BeforeNavigateToView UsuarioView view) {
//		getView().setPerfis(perfilBC.findAll());
//		getView().setBean(new Usuario());
//	}
	
	public void processFormClear(@Observes @ProcessClear Usuario2View usuario) {

	}

}
