package br.com.pc.accesscontrol;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import br.com.pc.business.configuracao.UsuarioBC;
import br.com.pc.domain.configuracao.Usuario;
import br.gov.frameworkdemoiselle.security.Authenticator;
import br.gov.frameworkdemoiselle.security.User;

@Alternative
public class AuthenticatorImpl implements Authenticator {

	@Inject
	private Credenciais credenciais;

	
	@Inject
	private UsuarioBC usuarioBC;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean authenticate() {
		boolean authenticated = false;
		//System.out.println(credenciais.getUsuario());
		Usuario usuario = new Usuario();
		usuario.setLogin(credenciais.getUsuario());
		usuario.setSenha(credenciais.getSenha());		
		Usuario retorno = usuarioBC.findByExemple(usuario);
		if( retorno!=null){
			credenciais.setId(retorno.getId().toString());
//			credenciais.setAttribute("perfil", retorno.getPerfil().getDescricao());
			credenciais.setSenha(retorno.getSenha());

			authenticated = true;
		}
		return authenticated;
	}
 
	@Override
	public void unAuthenticate() {
		// TODO Auto-generated method stub
	}

	@Override
	public User getUser() {
		return credenciais;
	}

}
