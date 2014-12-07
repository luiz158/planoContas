package br.com.pc.ui.presenter;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.vaadin.ui.Window.Notification;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.business.ClinicaBC;
import br.com.pc.business.ContaBC;
import br.com.pc.business.FluxoBC;
import br.com.pc.domain.Clinica;
import br.com.pc.ui.annotation.ProcessAdd;
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
	
	public void processSave(@Observes @ProcessSave ClinicaView view) {
		Clinica bean = view.getBean();
		if (bean.getId()!=null){
			clinicaBC.update(bean);
			getView().getWindow().showNotification("REGISTRO ATUALIZADO COM SUCESSO!!!");
		}else{
			getView().getWindow().showNotification("SELECIONA UM REGISTRO",Notification.TYPE_WARNING_MESSAGE);
		}
		getView().setList(clinicaBC.findAllAtivos());
		
		limpar(view);
	}
	public void processAdd(@Observes @ProcessAdd ClinicaView view) {
		Clinica bean = view.getBean();
		
		clinicaBC.insert(bean);
		getView().getWindow().showNotification("REGISTRO GRAVADO COM SUCESSO!!!");
		
		getView().setList(clinicaBC.findAllAtivos());
		
		limpar(view);
	}


	public void processItemSelection(@Observes @ProcessItemSelection ClinicaView view) {
//		Clinica bean = view.getBean();
//		getView().setBean(bean);
	}

	public void processDelete(@Observes @ProcessDelete ClinicaView view) {
		Clinica bean = view.getBean();
		clinicaBC.delete(bean);
		getView().setList(clinicaBC.findAllAtivos());
	}

	public void beforeNavigate(@Observes @BeforeNavigateToView ClinicaView view) {
//		view.setListaContaPai(contaBC.findByTotalizadora(true));
		view.setList(clinicaBC.findAllAtivos());
//		getView().setListaClinicas(clinicaBC.findAll());
	}

	public void processFormClear(@Observes @ProcessClear ClinicaView view) {

	}
	
	public void limpar(ClinicaView view) {
		view.descricao.setValue(null);
		view.razaoSocial.setValue(null);
		view.responsavel.setValue(null);
		view.cnpj.setValue(null);
		view.inscricaoEstadual.setValue(null);
		view.inscricaoMunicipal.setValue(null);
		view.endereco.setValue(null);
		view.cep.setValue(null);
		view.tel.setValue(null);
		view.fax.setValue(null);
	}
}
