package br.com.pc.ui.view;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.inject.Inject;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.business.configuracao.PermissaoBC;
import br.com.pc.business.configuracao.UsuarioBC;
import br.com.pc.domain.configuracao.EnumMenu;
import br.com.pc.domain.configuracao.EnumTipoPermissao;
import br.com.pc.domain.configuracao.Permissao;
import br.com.pc.domain.configuracao.Usuario;
import br.com.pc.ui.SistemaApplication;
import br.com.pc.util.CriptografiaUtil;
import br.gov.frameworkdemoiselle.annotation.Navigable;
import br.gov.frameworkdemoiselle.event.ProcessMenuSelection;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.ui.StructuredView;
import br.gov.frameworkdemoiselle.util.ResourceBundle;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;

public class MainView extends StructuredView {

	private static final long serialVersionUID = 1L;

	private String nomeSrv;

	private MenuBar.MenuItem menuLogin;

	private LoginForm loginForm = new LoginForm();
	private GridLayout gridLayoutcorpo = new GridLayout(3, 2);

	@Inject	private SecurityContext context;
	@Inject	private Credenciais credenciais;
	@Inject	private UsuarioBC usuarioBC;
	@Inject	private ResourceBundle bundle;

	@Navigable	private TabSheet tabSheet = new TabSheet();

	@Inject
	@ProcessMenuSelection
	private javax.enterprise.event.Event<EnumMenu> menuEvent;

	private Command menuCommand = new Command() {

		private static final long serialVersionUID = 1L;
		
		public void menuSelected(MenuItem selectedItem) {
			menuEvent.fire(EnumMenu.findByNome(selectedItem.getText()));
		}

	};

	@Override
	public void initializeComponents() {
		
		getHeader().setHeight("45px");
		Embedded em = new Embedded(null, new ThemeResource("images/facimagem2.png"));
		em.setHeight("40px");
		getHeader().addComponent(em);
		getHeader().setComponentAlignment(em, Alignment.TOP_LEFT);

		getFooter().setHeight("15px");
		getMenuBar().setWidth("100%");
		
		loginForm.setUsernameCaption("Usuario:");
		loginForm.setPasswordCaption("Senha:");
		loginForm.setLoginButtonCaption("Entrar");
		
		nomeSrv = "";
		try {
			if (InetAddress.getLocalHost().getHostName().equals("mercurio")) {
				nomeSrv="Versão Teste";
			}
			
	    } catch (UnknownHostException e) {
	    	nomeSrv = "";
	    }
		gridLayoutcorpo.addComponent(loginForm, 2, 0);
		getContent().addComponent(gridLayoutcorpo);
		getContent().setSizeFull();
		getFooter().addComponent(new Label(bundle.getString("app.footer") + nomeSrv+" v 0.0.6"));
		loginForm.addListener(new LoginForm.LoginListener() {

			private static final long serialVersionUID = 1L;

			public void onLogin(LoginEvent event) {

				SistemaApplication.getInstance().authenticate(
						event.getLoginParameter("username"),
						CriptografiaUtil.criptografaString(event.getLoginParameter("password")));
				if (context.getUser() != null) {
					
					getWindow().showNotification(
							"Bem Vindo " + usuarioBC.load(Long.parseLong(context.getUser().getId())).getLogin());

					
					
					getContent().removeComponent(gridLayoutcorpo);
					createMenu();
					getContent().addComponent(getTabSheet());
				
					getTabSheet().setSizeFull();

				} else {
					getWindow().showNotification("Usuario ou senha invalido");
				}
			}
		});

	}
	
	public void onLogout() {
		getTabSheet().removeAllComponents();
		getContent().removeAllComponents();
		getMenuBar().removeItems();
		getMenuBar().setWidth("100%");
		getContent().addComponent(gridLayoutcorpo);
	}

	public TabSheet getTabSheet() {
		tabSheet.setSizeFull();
		return tabSheet;
	}

	private void createMenu() {
		if (nomeSrv.length()>0){
			getMenuBar().addItem("VERSÃO TESTE", null).setStyleName("letravermelha");
			getFooter().getComponent(0).setStyleName("rodape");
		}
		
//		for (EnumMenu menu : EnumMenu.asList()) {
//			if(menu.getPai()==null){
//				if(menu.getExecuta()){
//					getMenuBar().addItem(menu.getNome(),menu.getIcone(),menuCommand);
//				}else{
//					MenuBar.MenuItem menu1 = getMenuBar().addItem(menu.getNome(),menu.getIcone(),null);
//					for (EnumMenu sub1 : EnumMenu.asList()) {
//						if (sub1.getPai()!=null){
//							if (sub1.getPai().getNome().equals(menu1.getText())){
//								if (sub1.getExecuta()){
//									menu1.addItem(sub1.getNome(), sub1.getIcone(),menuCommand);
//								}else{
//									
//									MenuBar.MenuItem menu2 = menu1.addItem(sub1.getNome(),sub1.getIcone(),null);
//									for (EnumMenu sub2 : EnumMenu.asList()) {
//										if (sub2.getPai()!=null){
//											if (sub2.getPai().getNome().equals(menu2.getText())){
//												if (sub2.getExecuta()){
//													menu2.addItem(sub2.getNome(), sub2.getIcone(),menuCommand);
//												}else{
//													
//												}
//											}
//										}
//									}
//									
//								}
//							}
//						}
//					}
//				}
//			}
//		}
		Usuario usuario = usuarioBC.load(Long.valueOf(credenciais.getId()));
		PermissaoBC permissaoBC = new PermissaoBC();
		for (EnumMenu menu : EnumMenu.asList()) {
			Permissao p1 = permissaoBC.getPermissao(usuario, menu);
			if (p1.getVisualizar()==EnumTipoPermissao.PERMITIDO){
				if(menu.getPai()==null){
					if(menu.getExecuta()){
						getMenuBar().addItem(menu.getNome(),menu.getIcone(),menuCommand);
					}else{
						MenuBar.MenuItem menu1 = getMenuBar().addItem(menu.getNome(),menu.getIcone(),null);
						for (EnumMenu sub1 : EnumMenu.asList()) {
							Permissao p2 = permissaoBC.getPermissao(usuario, sub1);
							if (p2.getVisualizar()==EnumTipoPermissao.PERMITIDO){
								if (sub1.getPai()!=null){
									if (sub1.getPai().getNome().equals(menu1.getText())){
										if (sub1.getExecuta()){
											menu1.addItem(sub1.getNome(), sub1.getIcone(),menuCommand);
										}else{
											MenuBar.MenuItem menu2 = menu1.addItem(sub1.getNome(),sub1.getIcone(),null);
											for (EnumMenu sub2 : EnumMenu.asList()) {
												Permissao p3 = permissaoBC.getPermissao(usuario, sub2);
												if (p3.getVisualizar()==EnumTipoPermissao.PERMITIDO){
													if (sub2.getPai()!=null){
														if (sub2.getPai().getNome().equals(menu2.getText())){
															if (sub2.getExecuta()){
																menu2.addItem(sub2.getNome(), sub2.getIcone(),menuCommand);
															}else{
																
															}
														}
													}
												}
											}	
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		menuLogin = getMenuBar().addItem(credenciais.getUsuario().toUpperCase(), null);
		menuLogin.setIcon(new ThemeResource("icons/16/user.png"));
		menuLogin.addItem(EnumMenu.ALTERA_SENHA.getNome().toUpperCase(), menuCommand);
		menuLogin.addItem(EnumMenu.LOGOUT.getNome().toUpperCase(), menuCommand);


	}

}
