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
import br.com.pc.ui.view.LancamentoGeralView;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

import com.vaadin.ui.Window.Notification;

@ViewController
@SessionScoped
public class LancamentoGeralPresenter extends AbstractPresenter<LancamentoGeralView> {

	private static final long serialVersionUID = 1L;
	
	@Inject ClinicaBC clinicaBC;
	@Inject ContaBC contaBC;
	@Inject FluxoBC fluxoBC;
	@Inject Credenciais credenciais;
	
	public void processSave(@Observes @ProcessSave LancamentoGeralView view) {
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
				getView().setList(fluxoBC.findByFiltro1(getView().getFiltro1(),false));
			}else{
				getView().getWindow().showNotification("PREÊNCHA OS CAMPOS CORRETAMENTE!!!",Notification.TYPE_WARNING_MESSAGE);
			}
		} catch (Exception e) {
			getView().getWindow().showNotification("DESCULPE! OCORREU ALGUMA FALHA AO SALVAR!",Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	public void processAdd(@Observes @ProcessAdd LancamentoGeralView view) {
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
				getView().setList(fluxoBC.findByFiltro1(getView().getFiltro1(),false));
			}else{
				getView().getWindow().showNotification("PREÊNCHA OS CAMPOS CORRETAMENTE!!!",Notification.TYPE_WARNING_MESSAGE);
			}
		} catch (Exception e) {
			getView().getWindow().showNotification("DESCULPE! OCORREU ALGUMA FALHA AO SALVAR!",Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}

	public void processItemSelection(@Observes @ProcessItemSelection LancamentoGeralView view) {
		
	}

	public void processFiltro(@Observes @ProcessFilter LancamentoGeralView view) {
		view.setList(fluxoBC.findByFiltro1(view.getFiltro1(),false));
	}
	
	public void processDelete(@Observes @ProcessDelete LancamentoGeralView view) {
		try {
			fluxoBC.delete(view.getBean());
			getView().setList(fluxoBC.findByFiltro1(getView().getFiltro1(),false));
		} catch (Exception e) {
			getView().getWindow().showNotification("DESCULPE! NÃO FOI POSSÍVEL EXCLUIR O REGISTRO!",Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void beforeNavigate(@Observes @BeforeNavigateToView LancamentoGeralView view) {
		view.setListaClinica(clinicaBC.findAll(credenciais));
		view.setListaConta(contaBC.findByTotalizadora(false));
//		view.setList(fluxoBC.findAll());
	}

	public void processFormClear(@Observes @ProcessClear LancamentoGeralView view) {

	}

	public void processRem(@Observes @ProcessRem LancamentoGeralView view) {
		view.getWindow().addWindow(view.modalWindow);
		view.getWindow().removeComponent(view.modalWindow);
	}
}
