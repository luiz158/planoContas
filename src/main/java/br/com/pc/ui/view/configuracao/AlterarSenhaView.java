package br.com.pc.ui.view.configuracao;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.ui.annotation.ProcessAlterarSenha;
import br.com.pc.util.CriptografiaUtil;
import br.gov.frameworkdemoiselle.template.BaseVaadinView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Window;

public class AlterarSenhaView extends BaseVaadinView {

	private static final long serialVersionUID = 1L;
	
	private Form formulario = new Form();
	// Endereço Atendimento
	private PasswordField senhaAtual;
	private PasswordField novaSenha;
	private PasswordField confirmaNovaSenha;
	
	private Button salvar;

	@Inject
	private BeanManager beanManager;
	
	@Inject 
	Credenciais credenciais;

	public AlterarSenhaView() {
		super();
	}

	//@SuppressWarnings("deprecation")
	@Override
	public void initializeComponents() {
		setCaption("Alterar Senha");
		
		senhaAtual = new PasswordField("Senha Atual");
		senhaAtual.setRequired(true);
		formulario.addField("senhaAtual", senhaAtual);
		
		novaSenha = new PasswordField("Nova Senha");
		novaSenha.setRequired(true);
		formulario.addField("novaSenha", novaSenha);
		
		confirmaNovaSenha = new PasswordField("Confirmação da Nova Senha");
		confirmaNovaSenha.setRequired(true);
		formulario.addField("confirmaNovaSenha", confirmaNovaSenha);

		salvar = new Button("Salvar");
		formulario.addField("salvar", salvar);

		addComponent(formulario);

		salvar.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("serial")
			@Override
			public void buttonClick(ClickEvent event) {
				
				//Colocando o comit para que os erros de validação sejam disparados.
				
				formulario.commit();
				
				if (CriptografiaUtil.criptografaString(senhaAtual.getValue().toString()).equals(credenciais.getSenha())) {
					if (novaSenha.getValue().equals(confirmaNovaSenha.getValue())) {
					
						beanManager.fireEvent((String)novaSenha.getValue(),
							new AnnotationLiteral<ProcessAlterarSenha>() {
							});
					}
					else {
						getApplication().getMainWindow().showNotification("Nova Senha e Confrmação não são iguais!", Window.Notification.TYPE_WARNING_MESSAGE);
					}
				}
				else {
					getApplication().getMainWindow().showNotification("Senha inválida!", Window.Notification.TYPE_WARNING_MESSAGE);
				}
			}
		});
		
		
	}

	public void clear() {
		senhaAtual.setValue("");
		novaSenha.setValue("");
		confirmaNovaSenha.setValue("");
	}
}
