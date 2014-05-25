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
import br.com.pc.ui.view.ResumoFinanceiroView;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

@ViewController
@SessionScoped
public class ResumoFinanceiroPresenter extends AbstractPresenter<ResumoFinanceiroView> {

	private static final long serialVersionUID = 1L;
	
	@Inject ClinicaBC clinicaBC;
	@Inject ContaBC contaBC;
	@Inject FluxoBC fluxoBC;
	@Inject Credenciais credenciais;
	
	public void processSave(@Observes @ProcessSave ResumoFinanceiroView view) {
		
	}
	public void processAdd(@Observes @ProcessAdd ResumoFinanceiroView view) {
		
	}

	public void processItemSelection(@Observes @ProcessItemSelection ResumoFinanceiroView view) {
		
	}

	public void processFiltro(@Observes @ProcessFilter ResumoFinanceiroView view) {
		view.setListConta(contaBC.findByFiltro1(view.getFiltro1(),true,true));
	}

	public void processDelete(@Observes @ProcessDelete ResumoFinanceiroView view) {

	}

	public void beforeNavigate(@Observes @BeforeNavigateToView ResumoFinanceiroView view) {
		view.setListaClinica(clinicaBC.findAll(credenciais));
		view.setListConta(contaBC.findByFiltro1(view.getFiltro1(),true,true));
		
//		view.limpar();
	}

	public void processFormClear(@Observes @ProcessClear ResumoFinanceiroView bean) {

	}

	public void processRem(@Observes @ProcessRem ResumoFinanceiroView bean) {

	}
}
