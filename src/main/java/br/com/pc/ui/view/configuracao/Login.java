package br.com.pc.ui.view.configuracao;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Login extends Window
{
    private Button btnLogin = new Button("Login");
    private TextField login = new TextField ( "Usuario");
    private PasswordField password = new PasswordField("Senha");


    public Login ()
    {
        super("Authentication Required !");
        setName ( "login" );
        initUI();
    }

    private void initUI ()
    {

        addComponent ( new Label ("Please login in order to use the application") );
        addComponent ( new Label () );
        addComponent ( login );
        addComponent ( password );
        addComponent ( btnLogin );
    }
}

