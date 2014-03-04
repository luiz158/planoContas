package br.com.pc.ui;

import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.business.configuracao.UsuarioBC;
import br.com.pc.domain.configuracao.Usuario;
import br.com.pc.ui.presenter.MainPresenter;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.template.VaadinApplication;
import br.gov.frameworkdemoiselle.util.ResourceBundle;

import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

@SessionScoped
public class SistemaApplication extends VaadinApplication implements HttpServletRequestListener {

	private static final long serialVersionUID = 1L;

	private static ThreadLocal<SistemaApplication> threadLocal = new ThreadLocal<SistemaApplication>();

	@Inject
	private Credenciais credenciais;
	@Inject
	private UsuarioBC usuarioBC;

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;

	@Inject
	private ResourceBundle bundle;

	@Inject
	private MainPresenter mainPresenter;

	@Inject
	private SecurityContext context;
	
	
	public void init() {
		setInstance(this); // So that we immediately have access to the current application
		setTheme("pc");
		final Window window = new Window(bundle.getString("app.title"));
		window.addComponent(mainPresenter.getView());
		setMainWindow(window);
	}


	/**
	 * Set the current application instance
	 */
	
	public void authenticate(String login, String password) {
		credenciais.setSenha(password);
		credenciais.setUsuario(login);
		context.login();

	}
	
	public void logout() {
		try {
			Usuario u = usuarioBC.load(Long.parseLong(credenciais.getId()));
			u.setDtUltimoAcesso(new Date());
			usuarioBC.update(u);
		} catch (Exception e) {
			// TODO: handle exception
		}
		credenciais.setSenha(null);
		credenciais.setUsuario(null);
		context.logout();
	}

	// @return the current application instance
	public static SistemaApplication getInstance() {
		return threadLocal.get();
	}

	// Set the current application instance
	public static void setInstance(SistemaApplication application) {
		if (getInstance() == null) {
			threadLocal.set(application);
		}
	}

	@Override
	public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		SistemaApplication.setInstance(this);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	@Override
	public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		threadLocal.remove();
	}
	

	@Override
	public void close(){
		System.out.println("DESLOGADO POR TIME OUT");
//		getWindow(bundle.getString("app.title")).showNotification("DESCONECTADO POR INATIVIDADE", Notification.TYPE_ERROR_MESSAGE);
		super.close();
	}
	
}
