package br.com.pc.business.configuracao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.domain.configuracao.Grupo;
import br.com.pc.domain.configuracao.Usuario;
import br.com.pc.persistence.configuracao.UsuarioDAO;
import br.com.pc.util.CriptografiaUtil;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

@BusinessController
public class UsuarioBC extends DelegateCrud<Usuario, Long, UsuarioDAO> {
	
	private static final long serialVersionUID = 1L;

//	@Inject private GrupoBC grupoBC;
	@Inject Credenciais credenciais;
	
	@Override
	public void insert(Usuario bean) {
//		if (credenciais != null){
//			bean.set(load(Long.parseLong(credenciais.getId()));
//		}
		//Criptografa a senha antes de mandar pro banco de dados.
		bean.setSenha(CriptografiaUtil.criptografaString(bean.getSenha()));
		super.insert(bean);
	}
	
	@Override
	public void update(Usuario bean) {
		//Criptografa a senha antes de mandar pro banco de dados.
		if (bean.getSenha().length()<30){
			bean.setSenha(CriptografiaUtil.criptografaString(bean.getSenha()));
		}
		super.update(bean);
	}
	/**
	 * 
	 * @param usuario
	 * @return
	 */
	public Usuario findByExemple(Usuario usuario) {
		UsuarioDAO dao = (UsuarioDAO) getDelegate();
		Usuario user = null;
		List<Usuario> lista = dao.findByExample(usuario);
		if(lista.size()==1){
			user = lista.get(0);
		}
		return user;
	}
	
	public void inicia(){
		List<Usuario> bean = getDelegate().findAll();
		if (bean.size() == 0) {
			List<String> lista  = new ArrayList<String>() ;
			lista.add("admin");
			lista.add("usuario");
			
			long j = 0;
			for (String i : lista) {
				j +=1;
				Usuario entity = new Usuario();
				entity.setLogin(i.toString());
				entity.setSenha(i.toString());
//				entity.addGrupo(grupoBC.load(j));
				insert(entity);
			}	
		}	
	}

	@Override
	public List<Usuario> findAll(){
		return getDelegate().findAllAtivo();
	}

	public List<Grupo> findGroupsByUsuario(Usuario usuario){
		return getDelegate().findGroupsByUsuario(usuario.getId());
	}
}
