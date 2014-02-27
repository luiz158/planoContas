package br.com.pc.persistence.configuracao;

import java.util.List;

import javax.persistence.Query;

import br.com.pc.domain.configuracao.Grupo;
import br.com.pc.domain.configuracao.Usuario;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
public class UsuarioDAO extends JPACrud<Usuario, Long> {

	private static final long serialVersionUID = 1L;
	
	
	@Override
	public List<Usuario> findByExample(Usuario example) {
		return super.findByExample(example);
	}

	public Usuario findByLogin(String login) {
		Query query = createQuery("select t from usuario t where t.login = :login");
		query.setParameter("login", login);
	
		@SuppressWarnings("unchecked")
		List<Usuario> p = query.getResultList();	
		
		if (p.size()>0){
			return  (Usuario)query.getResultList().get(0);	
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> findAllAtivo() {
		Query query = createQuery("select t from Usuario t where t.ativo = true");
		
		return  query.getResultList();
	}

	public List<Grupo> findGroupsByUsuario(Long id) {
		Query query = createQuery("select u.grupos from Usuario u where u.id = :id ");
		query.setParameter("id", id);
		return query.getResultList();
	}

}
