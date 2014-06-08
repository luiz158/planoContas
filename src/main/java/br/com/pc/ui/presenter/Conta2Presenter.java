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
import br.com.pc.ui.annotation.ProcessAcao1;
import br.com.pc.ui.annotation.ProcessAdd;
import br.com.pc.ui.annotation.ProcessFilter;
import br.com.pc.ui.view.Conta2View;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.event.ProcessClear;
import br.gov.frameworkdemoiselle.event.ProcessDelete;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.event.ProcessSave;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

import com.vaadin.ui.Window.Notification;

import de.steinwedel.vaadin.MessageBox;
import de.steinwedel.vaadin.MessageBox.ButtonConfig;
import de.steinwedel.vaadin.MessageBox.ButtonType;
import de.steinwedel.vaadin.MessageBox.EventListener;
import de.steinwedel.vaadin.MessageBox.Icon;

@ViewController
@SessionScoped
public class Conta2Presenter extends AbstractPresenter<Conta2View> {

	private static final long serialVersionUID = 1L;
	
	@Inject ClinicaBC clinicaBC;
	@Inject ContaBC contaBC;
	@Inject FluxoBC fluxoBC;
	@Inject Credenciais credenciais;
	
	public void processSave(@Observes @ProcessSave Conta2View view) {
		if (view.getBean().getId()==null){
			getView().getWindow().showNotification("NECESSÁRIO SELECIONAR UM REGISTRO PARA ATUALIZAR!!!",Notification.TYPE_WARNING_MESSAGE);
		}else{
			contaBC.update(view.getBean());
			view.setList(contaBC.findByFiltro2(getFiltro()),true,null);
			view.getWindow().showNotification("REGISTRO ATUALIZADO COM SUCESSO!!!");
		}
		if (view.getBean().getTotalizadora()){
			view.setListaContaPai(contaBC.findByTotalizadora(true));
			view.getContaPai().setValue(view.getBean().getContaPai());
		}
		updateFields(view);
	}

	public void processAdd(@Observes @ProcessAdd Conta2View view) {
		contaBC.insert(view.getBean());
		getView().getWindow().showNotification("REGISTRO CRIADO COM SUCESSO!!!");
		view.setList(contaBC.findByFiltro2(getFiltro()),true,null);
		updateFields(view);
	}

	public void processItemSelection(@Observes @ProcessItemSelection Conta2View view) {
//		view.setBean(view.getBean());
	}

	public void processDelete(@Observes @ProcessDelete Conta2View view) {
//		System.out.println(contaBC.geraNumeroConta(view.getBean()));
		try {
			contaBC.delete(view.getBean());
		} catch (Exception e) {
			getView().getWindow().showNotification("ERRO AO EXCLUIR!!", "<br>Desculpe! Por alguma razão não consegui excluir essa conta ", Notification.TYPE_ERROR_MESSAGE);
		}
		updateFields(view);
		getView().setList(contaBC.findAll(credenciais),true,null);
	}

	public void beforeNavigate(@Observes @BeforeNavigateToView Conta2View view) {
		updateFields(view);
		
		view.setList(contaBC.findByFiltro2(getFiltro()),true,null);
	}

	public void updateFields(Conta2View view) {
		view.setListaContaPai(contaBC.findByTotalizadora(true));
		view.setListaDre(EnumDre.asList());
		view.setListaClinicas(clinicaBC.findAll(credenciais));
		
		view.fClinica.setContainerDataSource(CollectionContainer.fromBeans(clinicaBC.findAll(credenciais)));
		view.fContaPai.setContainerDataSource(CollectionContainer.fromBeans(contaBC.findByTotalizadora(true)));
		view.fDre.setContainerDataSource(CollectionContainer.fromBeans(EnumDre.asList()));
	}

	public void processFilter(@Observes @ProcessFilter Conta2View view) {
		view.setList(contaBC.findByFiltro2(getFiltro()),true,null);
	}

	public void processFormClear(@Observes @ProcessClear Conta2View view) {

	}
	
	@SuppressWarnings("serial")
	public void processGeraNumeroConta(@Observes @ProcessAcao1 Conta2View view) {
		//
		String contaPai=null;
		Integer size = 0;
		
		if (view.contaPai.getValue()!=null){
			contaPai=((Conta)view.contaPai.getValue()).getConta();
			if (((Conta) view.contaPai.getValue()).getContasFilha()!=null){
//				size = contaBC.load(((Conta)view.contaPai.getValue()).getId()).getContasFilha().size();
				size = contaBC.getQtContasFilhas((Conta)view.contaPai.getValue());
			}
		}else{
			size = contaBC.getQtContaSemContaPai();
		}
		final String conta = contaBC.geraConta(contaPai, size, (Boolean)view.totalizadora.getValue());
		
		MessageBox mbBox = new MessageBox(view.getWindow(),"Númer de Conta",Icon.QUESTION,
				"Deseja substituir o numero da conta atual de "+(String)view.conta.getValue()+" por "+conta+" ?",
				new ButtonConfig(ButtonType.YES,"SIM"),new ButtonConfig(ButtonType.NO,"NÃO"));
		mbBox.show(true,new EventListener(){
			@Override
			public void buttonClicked(ButtonType buttonType) {
				if (buttonType==ButtonType.YES){
					getView().conta.setValue(conta);
				}else if(buttonType==ButtonType.NO){
					
				}
			}
		});
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
