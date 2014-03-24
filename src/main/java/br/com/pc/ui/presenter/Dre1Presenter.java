package br.com.pc.ui.presenter;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.vaadin.ui.Window.Notification;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.business.ClinicaBC;
import br.com.pc.business.ContaBC;
import br.com.pc.business.FluxoBC;
import br.com.pc.domain.Fluxo;
import br.com.pc.domain.configuracao.EnumMeses;
import br.com.pc.ui.annotation.ProcessAdd;
import br.com.pc.ui.annotation.ProcessFilter;
import br.com.pc.ui.annotation.ProcessRem;
import br.com.pc.ui.view.Dre1View;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

@ViewController
@SessionScoped
public class Dre1Presenter extends AbstractPresenter<Dre1View> {

	private static final long serialVersionUID = 1L;
	
	@Inject ClinicaBC clinicaBC;
	@Inject ContaBC contaBC;
	@Inject FluxoBC fluxoBC;
	@Inject Credenciais credenciais;
	
	public void processSave(@Observes @ProcessSave Dre1View view) {
		
	}
	public void processAdd(@Observes @ProcessAdd Dre1View view) {
		
	}

	public void processItemSelection(@Observes @ProcessItemSelection Dre1View view) {
		
	}

	public void processFiltro(@Observes @ProcessFilter Dre1View view) {
		view.setListConta(contaBC.findByFiltro1(view.getFiltro1(),true));
	}

	public void processDelete(@Observes @ProcessDelete Dre1View view) {

	}

	public void beforeNavigate(@Observes @BeforeNavigateToView Dre1View view) {
		view.setListaClinica(clinicaBC.findAll(credenciais));
		view.setListConta(contaBC.findByFiltro1(view.getFiltro1(),true));
		
//		view.limpar();
	}

	public void processFormClear(@Observes @ProcessClear Dre1View bean) {

	}

	public void processRem(@Observes @ProcessRem Dre1View bean) {

	}
}
