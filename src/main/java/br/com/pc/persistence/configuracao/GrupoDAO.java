package br.com.pc.persistence.configuracao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import br.com.pc.domain.Clinica;
import br.com.pc.domain.configuracao.Grupo;
import br.com.pc.domain.configuracao.Usuario;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
@SuppressWarnings("unchecked")
public class GrupoDAO extends JPACrud<Grupo, Long> {

	private static final long serialVersionUID = 1L;

	public Grupo findByDescricao(String descricao){
		Query query = createQuery("select b from Grupo b where b.descricao like :descricao");
		query.setParameter("descricao", descricao);
		
		try {
			List<Grupo> lista = new ArrayList<Grupo>(query.getResultList()) ;
			if (lista.size()>0){
				return (Grupo) lista.get(0);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Grupo> findGrupos() {
		Query query = createQuery("select b from Grupo b where b.id <> 0");
		return query.getResultList();
	}

	public List<Grupo> findByUsuario(Long id) {
		Query query = createQuery("select u.grupos from Usuario u where u.id = :id ");
		query.setParameter("id", id);
		return query.getResultList();
	}

	public List<Clinica> findClinicas(Long id) {
		Query query = createQuery("select c from Grupo g inner join g.clinicas c where g.id = :id ");
		query.setParameter("id", id);
		return query.getResultList();
	}

	public List<Usuario> findUsuarios(Long id) {
		Query query = createQuery("select u from Grupo g inner join g.usuarios u where g.id = :id ");
		query.setParameter("id", id);
		return query.getResultList();
	}

}
