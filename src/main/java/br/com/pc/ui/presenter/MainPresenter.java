package br.com.pc.ui.presenter;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.pc.domain.configuracao.EnumMenu;
import br.com.pc.ui.SistemaApplication;
import br.com.pc.ui.presenter.configuracao.AlterarSenhaPresenter;
import br.com.pc.ui.presenter.configuracao.Grupo2Presenter;
import br.com.pc.ui.presenter.configuracao.Permissao2Presenter;
import br.com.pc.ui.presenter.configuracao.Usuario2Presenter;
import br.com.pc.ui.view.MainView;
import br.gov.frameworkdemoiselle.event.ProcessMenuSelection;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;
import br.gov.frameworkdemoiselle.util.ViewNavigator;

@ViewController
@SessionScoped
public class MainPresenter extends AbstractPresenter<MainView> {

	private static final long serialVersionUID = 1L;

	@Inject	private ViewNavigator navigator;
	@Inject	private Usuario2Presenter usuarioPresenter;
	@Inject	private Grupo2Presenter grupo2Presenter;
	@Inject	private Permissao2Presenter permissao2Presenter;
	@Inject	private AlterarSenhaPresenter alterarSenhaPresenter;


	@Inject	private FluxoPresenter fluxoPresenter;
	@Inject	private Fluxo2Presenter fluxo2Presenter;
	@Inject	private ContaPresenter contaPresenter;
	@Inject	private ClinicaPresenter clinicaPresenter;
	@Inject	private ReceitasPresenter receitasPresenter;
	@Inject	private DespesasPresenter despesasPresenter;
	@Inject	private ResumoFinanceiroPresenter resumoFinanceiroPresenter;
	@Inject	private DrePresenter drePresenter;
	
	public void fechaAbaAtual() {
		navigator.pop();
		getView().getTabSheet().removeComponent(getView().getTabSheet().getSelectedTab());
	}

	public void processMenuSelected(
			@Observes @ProcessMenuSelection EnumMenu selection) {

		switch(selection){
	      case ALTERA_SENHA:
	    	  navigator.navigate(alterarSenhaPresenter.getView());
	    	  break;
	      case GERENCIA:
	    	  break;
	      case CRUD_USUARIO:
	    	  navigator.navigate(usuarioPresenter.getView());
	    	  break;
	      case CRUD_GRUPO:
	    	  navigator.navigate(grupo2Presenter.getView());
	    	  break;
	      case PERMISSOES:
	    	  navigator.navigate(permissao2Presenter.getView());
	    	  break;
	      case LOGOUT:
	    	  SistemaApplication.getInstance().logout();
	    	  getView().onLogout();
	    	  break;

	      case PC_FLUXO:
	    	  navigator.navigate(fluxoPresenter.getView());
	    	  break;
	      case PC_FLUXO2:
	    	  navigator.navigate(fluxo2Presenter.getView());
	    	  break;
	      case PC_CONTA:
	    	  navigator.navigate(contaPresenter.getView());
	    	  break;
	      case PC_CLINICA:
	    	  navigator.navigate(clinicaPresenter.getView());
	    	  break;
	      case PC_DESPESAS:
	    	  navigator.navigate(despesasPresenter.getView());
	    	  break;
	      case PC_RECEITAS:
	    	  navigator.navigate(receitasPresenter.getView());
	    	  break;
	      case PC_RESUMO_FINANCEIRO:
	    	  navigator.navigate(resumoFinanceiroPresenter.getView());
	    	  break;
	      case PC_DRE:
	    	  navigator.navigate(drePresenter.getView());
	    	  break;
	    	  
	    }   
		
	}


}
