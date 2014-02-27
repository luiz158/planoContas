package br.com.pc.accesscontrol;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.SessionScoped;

import java.util.HashMap;

import br.gov.frameworkdemoiselle.security.User;

@SessionScoped
public class Credenciais implements Serializable,User {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String usuario;
	private String senha;
	private String id;
	private Map<String,String> propriedades = new HashMap<String,String>();
	
	public void clear() {
		setUsuario(null);
		setSenha(null);
		setId(null);
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param senha the senha to set
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}

	/**
	 * @return the senha
	 */
	public String getSenha() {
		return senha;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Object getAttribute(Object key) {
		// TODO Auto-generated method stub
		return propriedades.get(key);
	}

	@Override
	public void setAttribute(Object key, Object value) {
		propriedades.put(key+"", value+"");
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	

}
