package br.com.pc.ui.presenter;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.pc.business.ClinicaBC;
import br.com.pc.business.ContaBC;
import br.com.pc.business.FluxoBC;
import br.com.pc.domain.Clinica;
import br.com.pc.domain.Conta;
import br.com.pc.ui.view.ContaView;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

@ViewController
@SessionScoped
public class ContaPresenter extends AbstractPresenter<ContaView> {

	private static final long serialVersionUID = 1L;
	
	@Inject ClinicaBC clinicaBC;
	@Inject ContaBC contaBC;
	@Inject FluxoBC fluxoBC;
	
	public void processSave(@Observes @ProcessSave Conta bean) {
		if (bean.getId()==null){
			contaBC.insert(bean);
		}else{
			contaBC.update(bean);
		}
		getView().setList(contaBC.findAll());
		if (bean.getTotalizadora()){
			getView().setListaContaPai(contaBC.findByTotalizadora(true));
			getView().getContaPai().setValue(bean.getContaPai());
//			getView().setListaClinicas(clinicaBC.findAll());
		}
	}

	public void processItemSelection(@Observes @ProcessItemSelection Conta bean) {
		getView().setBean(bean);
	}

	public void processDelete(@Observes @ProcessDelete Conta bean) {
		contaBC.delete(bean.getId());
		getView().setList(contaBC.findAll());
	}

	public void beforeNavigate(@Observes @BeforeNavigateToView ContaView view) {
		view.setListaContaPai(contaBC.findByTotalizadora(true));
		view.setList(contaBC.findAll());
		getView().setListaClinicas(clinicaBC.findAll());
	}

	public void processFormClear(@Observes @ProcessClear Conta bean) {

	}
}
