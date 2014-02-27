package br.com.pc.ui.presenter.configuracao;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.pc.business.ClinicaBC;
import br.com.pc.business.configuracao.GrupoBC;
import br.com.pc.business.configuracao.UsuarioBC;
import br.com.pc.domain.configuracao.Grupo;
import br.com.pc.ui.view.configuracao.Grupo2View;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

@ViewController
@SessionScoped
public class Grupo2Presenter extends AbstractPresenter<Grupo2View> {

	private static final long serialVersionUID = 1L;

	@Inject	private UsuarioBC usuarioBC;
	@Inject	private GrupoBC grupoBC;
	@Inject	private ClinicaBC clinicaBC;
	
	public void processSave(@Observes @ProcessSave Grupo2View view) {
		Grupo grupo = view.getBean();
		if (grupo.getId() != null) {
			grupoBC.update(grupo);
		} else {
			grupoBC.insert(grupo);
		}
		view.setList(grupoBC.findAll());
	}
	public void processDelete(@Observes @ProcessDelete Grupo2View view) {
		Grupo grupo = view.getBean();
		if (grupo.getId() != null) {
			grupo.setAtivo(false);
			grupoBC.update(grupo);
		} 
		view.setList(grupoBC.findAll());
	}

	public void processItemSelection(@Observes @ProcessItemSelection Grupo2View view) {
		Grupo grupo = view.getSelected();
//		view.setGrupos(grupoBC.findAll());
		view.setBean(grupo);
	}

	public void beforeNavigateToView(@Observes @BeforeNavigateToView Grupo2View view) {
//		view.setPerfis(perfilBC.findAll());
		view.setClinicas(clinicaBC.findAll());
		view.setUsuarios(usuarioBC.findAll());
		view.setList(grupoBC.findAll());
	}

//	public void beforeNavigate(@Observes @BeforeNavigateToView UsuarioView view) {
//		getView().setPerfis(perfilBC.findAll());
//		getView().setBean(new Usuario());
//	}
	
	public void processFormClear(@Observes @ProcessClear Grupo2View usuario) {

	}

}
