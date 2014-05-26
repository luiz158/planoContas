package br.com.pc.ui.presenter;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.business.ClinicaBC;
import br.com.pc.business.ContaBC;
import br.com.pc.business.FluxoBC;
import br.com.pc.ui.annotation.ProcessAdd;
import br.com.pc.ui.annotation.ProcessFilter;
import br.com.pc.ui.annotation.ProcessRem;
import br.com.pc.ui.view.DreView;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

@ViewController
@SessionScoped
public class DrePresenter extends AbstractPresenter<DreView> {

	private static final long serialVersionUID = 1L;
	
	@Inject ClinicaBC clinicaBC;
	@Inject ContaBC contaBC;
	@Inject FluxoBC fluxoBC;
	@Inject Credenciais credenciais;
	
	public void processSave(@Observes @ProcessSave DreView view) {
		
	}
	public void processAdd(@Observes @ProcessAdd DreView view) {
		
	}

	public void processItemSelection(@Observes @ProcessItemSelection DreView view) {
		
	}

	public void processFiltro(@Observes @ProcessFilter DreView view) {
		view.setListConta(contaBC.findByFiltro1(view.getFiltro1(),true,true));
	}

	public void processDelete(@Observes @ProcessDelete DreView view) {

	}

	public void beforeNavigate(@Observes @BeforeNavigateToView DreView view) {
		view.setListaClinica(clinicaBC.findAll(credenciais));
		view.setListConta(contaBC.findByFiltro1(view.getFiltro1(),true,true));
		
//		view.limpar();
	}

	public void processFormClear(@Observes @ProcessClear DreView bean) {

	}

	public void processRem(@Observes @ProcessRem DreView bean) {

	}
}
