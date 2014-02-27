package br.com.pc.ui.presenter.configuracao;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.business.configuracao.UsuarioBC;
import br.com.pc.domain.configuracao.Usuario;
import br.com.pc.ui.annotation.ProcessAlterarSenha;
import br.com.pc.ui.view.configuracao.AlterarSenhaView;
import br.gov.frameworkdemoiselle.event.BeforeNavigateToView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPresenter;

import com.vaadin.ui.Window;

@ViewController
@SessionScoped
public class AlterarSenhaPresenter extends AbstractPresenter<AlterarSenhaView> {

	private static final long serialVersionUID = 1L;

	@Inject 
	Credenciais credenciais;
	
	@Inject
	UsuarioBC usuarioBC;
	

	public void alterarSenha(@Observes @ProcessAlterarSenha String novaSenha) {
		Usuario usuario = usuarioBC.load(Long.valueOf(credenciais.getId()));
		usuario.setSenha(novaSenha);
		usuarioBC.update(usuario);
		credenciais.setSenha(usuario.getSenha());
		getView().getApplication().getMainWindow().showNotification("Senha alterada com sucesso!", Window.Notification.TYPE_HUMANIZED_MESSAGE);
		getView().clear();
	}

	public void beforeNavigate(@Observes @BeforeNavigateToView AlterarSenhaView view) {
		getView().clear();
	}

}
