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
import br.com.pc.ui.annotation.ProcessAdd;
import br.com.pc.ui.annotation.ProcessFilter;
import br.com.pc.ui.annotation.ProcessRem;
import br.com.pc.ui.view.DespesasView;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

@ViewController
@SessionScoped
public class DespesasPresenter extends AbstractPresenter<DespesasView> {

	private static final long serialVersionUID = 1L;
	
	@Inject ClinicaBC clinicaBC;
	@Inject ContaBC contaBC;
	@Inject FluxoBC fluxoBC;
	@Inject Credenciais credenciais;
	
	public void processSave(@Observes @ProcessSave Fluxo bean) {
		if(bean!=null && bean.getClinica()!=null &&
				bean.getConta()!=null && bean.getData()!=null &&
				bean.getValor()!=null && bean.getRegistro()!=null){
		
			if (bean.getId()==null){
				fluxoBC.insert(bean);
			}else{
				fluxoBC.update(bean);
			}
			getView().setList(fluxoBC.findByFiltro1Conta(getView().getFiltro1(),false,"2"));
			getView().getWindow().showNotification("REGISTRO GRAVADO COM SUCESSO!!!");
		}else{
			getView().getWindow().showNotification("PREÊNCHA OS CAMPOS CORRETAMENTE!!!",Notification.TYPE_WARNING_MESSAGE);
		}
	}

	public void processItemSelection(@Observes @ProcessItemSelection Fluxo bean) {
		
	}

	public void processFiltro(@Observes @ProcessFilter DespesasView view) {
		view.setList(fluxoBC.findByFiltro1Conta(view.getFiltro1(),false,"2"));
	}
	
	public void processDelete(@Observes @ProcessDelete Fluxo bean) {
		try {
			fluxoBC.delete(bean);
			getView().setList(fluxoBC.findByFiltro1Conta(getView().getFiltro1(),false,"2"));
		} catch (Exception e) {
			getView().getWindow().showNotification("DESCULPE! NÃO FOI POSSÍVEL EXCLUIR O REGISTRO!",Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void beforeNavigate(@Observes @BeforeNavigateToView DespesasView view) {
		view.setListaClinica(clinicaBC.findAll(credenciais));
		view.setListaConta(contaBC.findByTotalizadora(false,"2"));
//		view.setList(fluxoBC.findAll());
	}

	public void processFormClear(@Observes @ProcessClear Fluxo bean) {

	}

	public void processAdd(@Observes @ProcessAdd Fluxo bean) {

	}

	public void processRem(@Observes @ProcessRem Fluxo bean) {

	}
}
