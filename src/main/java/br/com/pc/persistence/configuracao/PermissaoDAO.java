package br.com.pc.persistence.configuracao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import br.com.pc.domain.configuracao.EnumMenu;
import br.com.pc.domain.configuracao.EnumTipoPermissao;
import br.com.pc.domain.configuracao.Permissao;
import br.com.pc.domain.configuracao.Usuario;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
public class PermissaoDAO extends JPACrud<Permissao, Integer> {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public List<Permissao> findPermissoes() {
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select p from Permissao p " );
		queryString.append(" where p.grupo.id <> 1 " );
		queryString.append(" order by p.grupo.descricao ");
		
		Query query = createQuery(queryString.toString());

		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Set<EnumMenu> findMenuByUsuario(Usuario usuario, EnumTipoPermissao enumTipoPermissao){
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select p.menu from Permissao p " );
		queryString.append(" join p.grupo g " );
		queryString.append(" join g.usuario u " );
		queryString.append(" where u = :usuario and p.visualizar = :tipoPermissao ");
		
		Query query = createQuery(queryString.toString());
		
		query.setParameter("usuario", usuario);
		query.setParameter("tipoPermissao", enumTipoPermissao);
		
		return new HashSet<EnumMenu>(query.getResultList());
		
	}
	
	public EnumTipoPermissao getPermissaoAlterar(Usuario usuario, EnumMenu menu){
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select distinct(p.alterar) from Permissao p " );
		queryString.append(" join p.grupo g " );
		queryString.append(" join g.usuarios u " );
		queryString.append(" where u.id = :usuario and p.alterar = :permitido and p.menu = :menu " );
		queryString.append(" and p.menu not in ( " );
		queryString.append(" 	select distinct(p2.menu) from Permissao p2" );
		queryString.append("     join p2.grupo g2 " );
		queryString.append("     join g2.usuarios u2 " );
		queryString.append("		where u2.id = :usuario and p2.alterar = :negado and p2.menu = :menu " );
		queryString.append(" ) ");
		
		Query query = createQuery(queryString.toString());
//		Query query = createQuery(" select distinct(p.alterar) from Permissao p " +
//								  " where p.grupo.usuarios = :usuario "
//								  );
				
				
		query.setParameter("usuario", usuario.getId());
		query.setParameter("menu", menu);
		query.setParameter("permitido", EnumTipoPermissao.PERMITIDO);
		query.setParameter("negado", EnumTipoPermissao.NEGADO);
		
		if (query.getResultList().size() > 0){
			if ((EnumTipoPermissao) query.getSingleResult() == EnumTipoPermissao.PERMITIDO){
				return EnumTipoPermissao.PERMITIDO;
			}
			return EnumTipoPermissao.NEGADO;
		}else{
			return EnumTipoPermissao.NEGADO;
		}
		
	}
	
	public EnumTipoPermissao getPermissaoCriar(Usuario usuario, EnumMenu menu){
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select distinct(p.criar) from Permissao p " );
		queryString.append(" join p.grupo g " );
		queryString.append(" join g.usuarios u " );
		queryString.append(" where u = :usuario and p.criar = :permitido and p.menu = :menu " );
		queryString.append(" and p.menu not in ( " );
		queryString.append(" 	select distinct(p2.menu) from Permissao p2" );
		queryString.append("     join p2.grupo g2 " );
		queryString.append("     join g2.usuarios u2 " );
		queryString.append("		where u2 = :usuario and p2.criar = :negado and p2.menu = :menu " );
		queryString.append(" ) ");
		
		Query query = createQuery(queryString.toString());
		
		query.setParameter("usuario", usuario);
		query.setParameter("menu", menu);
		query.setParameter("permitido", EnumTipoPermissao.PERMITIDO);
		query.setParameter("negado", EnumTipoPermissao.NEGADO);
		
		if (query.getResultList().size() > 0){
			if ((EnumTipoPermissao) query.getSingleResult() == EnumTipoPermissao.PERMITIDO){
				return EnumTipoPermissao.PERMITIDO;
			}
			return EnumTipoPermissao.NEGADO;
		}else{
			return EnumTipoPermissao.NEGADO;
		}
		
	}
	
	public EnumTipoPermissao getPermissaoExcluir(Usuario usuario, EnumMenu menu){
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select distinct(p.excluir) from Permissao p " );
		queryString.append(" join p.grupo g " );
		queryString.append(" join g.usuarios u " );
		queryString.append(" where u = :usuario and p.excluir = :permitido and p.menu = :menu " );
		queryString.append(" and p.menu not in ( " );
		queryString.append(" 	select distinct(p2.menu) from Permissao p2" );
		queryString.append("     join p2.grupo g2 " );
		queryString.append("     join g2.usuarios u2 " );
		queryString.append("		where u2 = :usuario and p2.excluir = :negado and p2.menu = :menu " );
		queryString.append(" ) ");
		
		Query query = createQuery(queryString.toString());
		
		query.setParameter("usuario", usuario);
		query.setParameter("menu", menu);
		query.setParameter("permitido", EnumTipoPermissao.PERMITIDO);
		query.setParameter("negado", EnumTipoPermissao.NEGADO);
		
		if (query.getResultList().size() > 0){
			if ((EnumTipoPermissao) query.getSingleResult() == EnumTipoPermissao.PERMITIDO){
				return EnumTipoPermissao.PERMITIDO;
			}
			return EnumTipoPermissao.NEGADO;
		}else{
			return EnumTipoPermissao.NEGADO;
		}
		
	}
	
	public EnumTipoPermissao getPermissaoImprimir(Usuario usuario, EnumMenu menu){
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select distinct(p.imprimir) from Permissao p " );
		queryString.append(" join p.grupo g " );
		queryString.append(" join g.usuarios u " );
		queryString.append(" where u = :usuario and p.imprimir = :permitido and p.menu = :menu " );
		queryString.append(" and p.menu not in ( " );
		queryString.append(" 	select distinct(p2.menu) from Permissao p2" );
		queryString.append("     join p2.grupo g2 " );
		queryString.append("     join g2.usuarios u2 " );
		queryString.append("		where u2 = :usuario and p2.imprimir = :negado and p2.menu = :menu " );
		queryString.append(" ) ");
		
		Query query = createQuery(queryString.toString());
		
		query.setParameter("usuario", usuario);
		query.setParameter("menu", menu);
		query.setParameter("permitido", EnumTipoPermissao.PERMITIDO);
		query.setParameter("negado", EnumTipoPermissao.NEGADO);
		
		if (query.getResultList().size() > 0){
			if ((EnumTipoPermissao) query.getSingleResult() == EnumTipoPermissao.PERMITIDO){
				return EnumTipoPermissao.PERMITIDO;
			}
			return EnumTipoPermissao.NEGADO;
		}else{
			return EnumTipoPermissao.NEGADO;
		}
		
	}
	
	public EnumTipoPermissao getPermissaoVisualizar(Usuario usuario, EnumMenu menu){
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select distinct(p.visualizar) from Permissao p " );
		queryString.append(" join p.grupo g " );
		queryString.append(" join g.usuarios u " );
		queryString.append(" where u = :usuario and p.visualizar = :permitido and p.menu = :menu " );
		queryString.append(" and p.menu not in ( " );
		queryString.append(" 	select distinct(p2.menu) from Permissao p2" );
		queryString.append("     join p2.grupo g2 " );
		queryString.append("     join g2.usuarios u2 " );
		queryString.append("		where u2 = :usuario and p2.visualizar = :negado and p2.menu = :menu " );
		queryString.append(" ) ");
		
		Query query = createQuery(queryString.toString());
		
		query.setParameter("usuario", usuario);
		query.setParameter("menu", menu);
		query.setParameter("permitido", EnumTipoPermissao.PERMITIDO);
		query.setParameter("negado", EnumTipoPermissao.NEGADO);
		
		if (query.getResultList().size() > 0){
			if ((EnumTipoPermissao) query.getSingleResult() == EnumTipoPermissao.PERMITIDO){
				return EnumTipoPermissao.PERMITIDO;
			}
			return EnumTipoPermissao.NEGADO;
		}else{
			return EnumTipoPermissao.NEGADO;
		}
		
	}
	
	
}
