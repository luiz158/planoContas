package br.com.pc.ui.presenter;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.vaadin.ui.Window.Notification;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.business.ClinicaBC;
import br.com.pc.business.ContaBC;
import br.com.pc.business.FluxoBC;
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
	@Inject Credenciais credenciais;
	
	public void processSave(@Observes @ProcessSave ContaView view) {
		if (view.getBean().getId()==null){
			contaBC.insert(view.getBean());
			view.setList(contaBC.findAll(credenciais),false,null);
			getView().getWindow().showNotification("REGISTRO GRAVADO COM SUCESSO!!!");
		}else{
			contaBC.update(view.getBean());
			view.setList(contaBC.findAll(credenciais),false,null);
			view.getWindow().showNotification("REGISTRO ATUALIZADO COM SUCESSO!!!");
		}
		if (view.getBean().getTotalizadora()){
			view.setListaContaPai(contaBC.findByTotalizadora(true));
			view.getContaPai().setValue(view.getBean().getContaPai());
//			getView().setListaClinicas(clinicaBC.findAll());
		}
	}

	public void processItemSelection(@Observes @ProcessItemSelection ContaView view) {
//		view.setBean(view.getBean());
	}

	public void processDelete(@Observes @ProcessDelete ContaView view) {
		try {
			contaBC.delete(view.getBean());
		} catch (Exception e) {
			getView().getWindow().showNotification("ERRO AO EXCLUIR!!", "<br>Desculpe! Por alguma razão não consegui excluir essa conta ", Notification.TYPE_ERROR_MESSAGE);
		}
		getView().setList(contaBC.findAll(credenciais),true,null);
	}

	public void beforeNavigate(@Observes @BeforeNavigateToView ContaView view) {
		view.setListaContaPai(contaBC.findByTotalizadora(true));
		view.setListaClinicas(clinicaBC.findAll(credenciais));
		
		view.setList(contaBC.findAll(credenciais),true,null);
	}

	public void processFormClear(@Observes @ProcessClear ContaView view) {

	}
}
