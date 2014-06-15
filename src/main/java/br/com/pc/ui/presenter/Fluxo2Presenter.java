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
import br.com.pc.ui.view.Fluxo2View;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

@ViewController
@SessionScoped
public class Fluxo2Presenter extends AbstractPresenter<Fluxo2View> {

	private static final long serialVersionUID = 1L;
	
	@Inject ClinicaBC clinicaBC;
	@Inject ContaBC contaBC;
	@Inject FluxoBC fluxoBC;
	@Inject Credenciais credenciais;
	
	public void processSave(@Observes @ProcessSave Fluxo2View view) {
		Fluxo bean = fluxoBC.getByExample(getView().getFluxoBean());
		if (bean==null){
			bean=view.getFluxoBean();
		}
		if(bean!=null && bean.getClinica()!=null &&
				bean.getConta()!=null && bean.getData()!=null &&
				bean.getValor()!=null){
			if (!bean.getConta().getTotalizadora()){
				if (bean.getId()==null){
					getView().getWindow().showNotification("É NECESSÁRIO SELECIONAR UM REGISTRO!!!",Notification.TYPE_WARNING_MESSAGE);
//					fluxoBC.insert(bean);
//					view.getWindow().showNotification("REGISTRO GRAVADO COM SUCESSO!!!");
				}else{
					bean.setValor(getView().getFluxoBean().getValor());
					fluxoBC.update(bean);
					view.getWindow().showNotification("REGISTRO ATUALIZADO COM SUCESSO!!!");
				}
				view.setListConta(contaBC.findByFiltro1(view.getFiltro1(),true));
				view.setListFluxo(fluxoBC.findByFiltro1(view.getFiltro1(),true));
			}else{
				view.getWindow().showNotification("SELECIONE UMA CONTA QUE NÃO SEJA TOTALIZADORA!!!",null,Notification.TYPE_ERROR_MESSAGE);
			}
		}else{
			getView().getWindow().showNotification("PREÊNCHA OS CAMPOS CORRETAMENTE!!!",Notification.TYPE_WARNING_MESSAGE);
		}
	}
	public void processAdd(@Observes @ProcessAdd Fluxo2View view) {
		Fluxo bean=view.getFluxoBean();
		if(bean!=null && bean.getClinica()!=null &&
				bean.getConta()!=null && bean.getData()!=null &&
				bean.getValor()!=null){
			if (!bean.getConta().getTotalizadora()){
				bean.setId(null);
				fluxoBC.insert(bean);
				view.getWindow().showNotification("REGISTRO GRAVADO COM SUCESSO!!!");
				view.setListConta(contaBC.findByFiltro1(view.getFiltro1(),true));
				view.setListFluxo(fluxoBC.findByFiltro1(view.getFiltro1(),true));
			}else{
				view.getWindow().showNotification("SELECIONE UMA CONTA QUE NÃO SEJA TOTALIZADORA!!!",null,Notification.TYPE_ERROR_MESSAGE);
			}
		}else{
			view.getWindow().showNotification("PREÊNCHA OS CAMPOS CORRETAMENTE!!!",Notification.TYPE_WARNING_MESSAGE);
		}
		
	}

	public void processItemSelection(@Observes @ProcessItemSelection Fluxo2View view) {
		
	}

	public void processFiltro(@Observes @ProcessFilter Fluxo2View view) {
		view.setListConta(contaBC.findByFiltro1(view.getFiltro1(),true));
		view.setListFluxo(fluxoBC.findByFiltro1(view.getFiltro1(),true));
	}

	public void processDelete(@Observes @ProcessDelete Fluxo2View view) {
//		fluxoBC.delete(view.g);
		view.setListConta(contaBC.findByFiltro1(view.getFiltro1(),true));
		view.setListFluxo(fluxoBC.findByFiltro1(view.getFiltro1(),true));
	}

	public void beforeNavigate(@Observes @BeforeNavigateToView Fluxo2View view) {
		view.setListaAno(fluxoBC.findAnos());
		view.setListaMeses(EnumMeses.asList());
		
		view.setListaClinica(clinicaBC.findAll(credenciais));
		view.setListConta(contaBC.findByFiltro1(view.getFiltro1(),true));
		view.setListFluxo(fluxoBC.findByFiltro1(view.getFiltro1(),true));
		
		view.limpar();
	}

	public void processFormClear(@Observes @ProcessClear Fluxo2View bean) {

	}

	public void processRem(@Observes @ProcessRem Fluxo2View bean) {

	}
}
