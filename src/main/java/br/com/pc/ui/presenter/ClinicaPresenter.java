package br.com.pc.ui.presenter;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.business.ClinicaBC;
import br.com.pc.business.ContaBC;
import br.com.pc.business.FluxoBC;
import br.com.pc.domain.Clinica;
import br.com.pc.domain.Conta;
import br.com.pc.ui.view.ClinicaView;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

@ViewController
@SessionScoped
public class ClinicaPresenter extends AbstractPresenter<ClinicaView> {

	private static final long serialVersionUID = 1L;
	
	@Inject ClinicaBC clinicaBC;
	@Inject ContaBC contaBC;
	@Inject FluxoBC fluxoBC;
	@Inject Credenciais credenciais;
	
	public void processSave(@Observes @ProcessSave Clinica bean) {
		if (bean.getId()==null){
			clinicaBC.insert(bean);
			getView().getWindow().showNotification("REGISTRO GRAVADO COM SUCESSO!!!");
		}else{
			clinicaBC.update(bean);
			getView().getWindow().showNotification("REGISTRO ATUALIZADO COM SUCESSO!!!");
		}
		getView().setList(clinicaBC.findAll(credenciais));
//		getView().setList(contaBC.findAll());
//		if (bean.getTotalizadora()){
//			getView().setListaContaPai(contaBC.findByTotalizadora(true));
//			getView().getContaPai().setValue(bean.getContaPai());
//			getView().setListaClinicas(clinicaBC.findAll());
//		}
	}

	public void processItemSelection(@Observes @ProcessItemSelection Clinica bean) {
		getView().setBean(bean);
	}

	public void processDelete(@Observes @ProcessDelete Clinica bean) {
		clinicaBC.delete(bean);
		getView().setList(clinicaBC.findAll(credenciais));
	}

	public void beforeNavigate(@Observes @BeforeNavigateToView ClinicaView view) {
//		view.setListaContaPai(contaBC.findByTotalizadora(true));
		view.setList(clinicaBC.findAll(credenciais));
//		getView().setListaClinicas(clinicaBC.findAll());
	}

	public void processFormClear(@Observes @ProcessClear Clinica bean) {

	}
}
