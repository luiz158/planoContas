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
import br.com.pc.ui.view.FluxoView;
import br.com.pc.ui.view.ReceitasView;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

@ViewController
@SessionScoped
public class ReceitasPresenter extends AbstractPresenter<ReceitasView> {

	private static final long serialVersionUID = 1L;
	
	@Inject ClinicaBC clinicaBC;
	@Inject ContaBC contaBC;
	@Inject FluxoBC fluxoBC;
	@Inject Credenciais credenciais;
	
	public void processSave(@Observes @ProcessSave ReceitasView view) {
		try {
			if(view.getBean()!=null && view.getBean().getClinica()!=null &&
					view.getBean().getConta()!=null && view.getBean().getData()!=null &&
					view.getBean().getValor()!=null){
			
				if (view.getBean().getId()==null){
					getView().getWindow().showNotification("É NECESSÁRIO SELECIONAR UM REGISTRO!!!",Notification.TYPE_WARNING_MESSAGE);
//					fluxoBC.insert(view.getBean());
				}else{
					fluxoBC.update(view.getBean());
					getView().getWindow().showNotification("REGISTRO GRAVADO COM SUCESSO!!!");
				}
				getView().setList(fluxoBC.findByFiltro1Conta(getView().getFiltro1(),false,"1"));
			}else{
				getView().getWindow().showNotification("PREÊNCHA OS CAMPOS CORRETAMENTE!!!",Notification.TYPE_WARNING_MESSAGE);
			}
		} catch (Exception e) {
			getView().getWindow().showNotification("DESCULPE! OCORREU ALGUMA FALHA AO SALVAR!",Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	public void processAdd(@Observes @ProcessAdd ReceitasView view) {
		try {
			if(view.getBean()!=null && view.getBean().getClinica()!=null &&
					view.getBean().getConta()!=null && view.getBean().getData()!=null &&
					view.getBean().getValor()!=null){
			
				if (view.getBean().getId()==null){
					fluxoBC.insert(view.getBean());
					getView().getWindow().showNotification("REGISTRO GRAVADO COM SUCESSO!!!");
				}else{
//					fluxoBC.update(view.getBean());
				}
				getView().setList(fluxoBC.findByFiltro1Conta(getView().getFiltro1(),false,"1"));
			}else{
				getView().getWindow().showNotification("PREÊNCHA OS CAMPOS CORRETAMENTE!!!",Notification.TYPE_WARNING_MESSAGE);
			}
		} catch (Exception e) {
			getView().getWindow().showNotification("DESCULPE! OCORREU ALGUMA FALHA AO SALVAR!",Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}

	public void processItemSelection(@Observes @ProcessItemSelection Fluxo bean) {
		
	}

	public void processFiltro(@Observes @ProcessFilter ReceitasView view) {
		view.setList(fluxoBC.findByFiltro1Conta(view.getFiltro1(),false,"1"));
	}
	
	public void processDelete(@Observes @ProcessDelete ReceitasView view) {
		try {
			fluxoBC.delete(view.getBean(), view.motivoExclusao.getValue()+"");
			getView().setList(fluxoBC.findByFiltro1Conta(getView().getFiltro1(),false,"1"));
		} catch (Exception e) {
			getView().getWindow().showNotification("DESCULPE! NÃO FOI POSSÍVEL EXCLUIR O REGISTRO!",Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void beforeNavigate(@Observes @BeforeNavigateToView ReceitasView view) {
		view.setListaClinica(clinicaBC.findAll(credenciais));
		view.setListaConta(contaBC.findByTotalizadora(false,"1"));
//		view.setList(fluxoBC.findAll());
	}

	public void processFormClear(@Observes @ProcessClear ReceitasView view) {

	}

	public void processRem(@Observes @ProcessRem ReceitasView view) {
		view.getWindow().addWindow(view.modalWindow);
		view.getWindow().removeComponent(view.modalWindow);
	}
}
