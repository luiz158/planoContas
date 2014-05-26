package br.com.pc.ui.presenter;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.data.collectioncontainer.CollectionContainer;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.business.ClinicaBC;
import br.com.pc.business.ContaBC;
import br.com.pc.business.FluxoBC;
import br.com.pc.domain.Clinica;
import br.com.pc.domain.Conta;
import br.com.pc.domain.configuracao.EnumDre;
import br.com.pc.ui.annotation.ProcessFilter;
import br.com.pc.ui.view.ContaView;
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
public class ContaPresenter extends AbstractPresenter<ContaView> {

	private static final long serialVersionUID = 1L;
	
	@Inject ClinicaBC clinicaBC;
	@Inject ContaBC contaBC;
	@Inject FluxoBC fluxoBC;
	@Inject Credenciais credenciais;
	
	public void processSave(@Observes @ProcessSave ContaView view) {
		if (view.getBean().getId()==null){
			contaBC.insert(view.getBean());
			view.setList(contaBC.findAll(credenciais),true,null);
			getView().getWindow().showNotification("REGISTRO GRAVADO COM SUCESSO!!!");
		}else{
			contaBC.update(view.getBean());
			view.setList(contaBC.findByFiltro2(getFiltro()),true,null);
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
//		System.out.println(contaBC.geraNumeroConta(view.getBean()));
		try {
			contaBC.delete(view.getBean());
		} catch (Exception e) {
			getView().getWindow().showNotification("ERRO AO EXCLUIR!!", "<br>Desculpe! Por alguma razão não consegui excluir essa conta ", Notification.TYPE_ERROR_MESSAGE);
		}
		getView().setList(contaBC.findAll(credenciais),true,null);
	}

	public void beforeNavigate(@Observes @BeforeNavigateToView ContaView view) {
		view.setListaContaPai(contaBC.findByTotalizadora(true));
		view.setListaDre(EnumDre.asList());
		view.setListaClinicas(clinicaBC.findAll(credenciais));
		
		view.setList(contaBC.findByFiltro2(getFiltro()),true,null);
		
		view.fClinica.setContainerDataSource(CollectionContainer.fromBeans(clinicaBC.findAll(credenciais)));
		view.fContaPai.setContainerDataSource(CollectionContainer.fromBeans(contaBC.findByTotalizadora(true)));
		view.fDre.setContainerDataSource(CollectionContainer.fromBeans(EnumDre.asList()));
	}

	public void processFilter(@Observes @ProcessFilter ContaView view) {
		
		view.setList(contaBC.findByFiltro2(getFiltro()),true,null);
	}

	public void processFormClear(@Observes @ProcessClear ContaView view) {

	}
	
	private Conta getFiltro(){
		Conta filtro = new Conta();
		Clinica c = (Clinica) getView().fClinica.getValue();
		if (c!=null){
			filtro.addClinica(c);
		}else{
			filtro.setClinicas(null);
		}
		
		filtro.setConta((String) getView().fConta.getValue());
		filtro.setDescricao((String) getView().fDescricao.getValue());
		filtro.setTotalizadora((Boolean) getView().fTotalizadora.getValue());
		filtro.setResumoFinanceiro((Boolean) getView().fResumoFinanceiro.getValue());
		filtro.setContaPai((Conta) getView().fContaPai.getValue());
		filtro.setDre((EnumDre) getView().fDre.getValue());
		
		return filtro;
	}
}
