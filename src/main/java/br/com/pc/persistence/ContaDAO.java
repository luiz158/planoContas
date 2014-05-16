package br.com.pc.persistence;

import java.util.List;

import javax.persistence.Parameter;
import javax.persistence.Query;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.domain.Conta;
import br.com.pc.ui.bean.Filtro1;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
@SuppressWarnings("unchecked")
public class ContaDAO extends JPACrud<Conta, Long> {

	private static final long serialVersionUID = 1L;

	public List<Conta> findByTotalizadora(Boolean totalizadora, String conta) {
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select b from Conta b where b.ativo = true and b.totalizadora = :totalizadora ");
		if (conta != null && conta.length()>0){
			queryString.append(" and b.conta like :conta " );
		}
		queryString.append(" order by b.conta " );
		
		Query query = createQuery(queryString.toString());
		
		for (Parameter<?> p : query.getParameters()) {
			if ("totalizadora".equals(p.getName()))	{query.setParameter(p.getName(), totalizadora);}
			if ("conta".equals(p.getName()))	{query.setParameter(p.getName(), conta+"%");}
		}
		
		return query.getResultList();
	}

	public List<Conta> findAllAtivo() {
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select b from Conta b where b.ativo = true order by b.conta " );
		
		Query query = createQuery(queryString.toString());
		
		return query.getResultList();
	}

	public List<Conta> findAllAtivo(Credenciais credenciais) {
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select b " +
				" from Conta b " +
				" left outer join b.clinicas c " +
				" where b.ativo = true and " +
				" (c in (select c from Grupo g inner join g.clinicas c inner join g.usuarios u where u.id = :id )  " +
				"  or c is null ) " +
				" order by b.conta " );
		Query query = createQuery(queryString.toString());
		
		for (Parameter<?> p : query.getParameters()) {
			if ("id".equals(p.getName()))	{query.setParameter(p.getName(), Long.parseLong(credenciais.getId()));}
		}
		
		return query.getResultList();
	}

	public List<Conta> findAll(Credenciais credenciais) {
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select b " +
				" from Conta b " +
				" left outer join b.clinicas c " +
				" where " +
				" (c in (select c from Grupo g inner join g.clinicas c inner join g.usuarios u where u.id = :id )  " +
				"  or c is null ) " +
				" order by b.conta " );
		Query query = createQuery(queryString.toString());
		
		for (Parameter<?> p : query.getParameters()) {
			if ("id".equals(p.getName()))	{query.setParameter(p.getName(), Long.parseLong(credenciais.getId()));}
		}
		
		return query.getResultList();
	}

	public List<Conta> findByFiltro1(Filtro1 filtro1, Boolean soAtivos, Boolean soDre) {
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select b " +
				" from Conta b " +
				" left outer join b.clinicas c " +
				" where b.id > 0 and " +
				" c in (:clinicas) or c is null ");
		if (soAtivos){
			queryString.append(" and b.ativo = true  " );
		}
		if (soDre){
			queryString.append(" and b.dre = true  " );
		}
		queryString.append(" order by b.conta ");
		
		Query query = createQuery(queryString.toString());
		
		for (Parameter<?> p : query.getParameters()) {
			if ("clinicas".equals(p.getName()))	{query.setParameter(p.getName(), filtro1.getClinicas());}
		}
		
		return query.getResultList();
	}

	public Integer getQtContaSemContaPai() {
		StringBuilder queryString = new StringBuilder();
		queryString.append(" select count(b.id) " +
				" from Conta b " +
				" where " +
				" b.contaPai is null ");
		Query query = createQuery(queryString.toString());
//		for (Parameter<?> p : query.getParameters()) {
//			if ("clinicas".equals(p.getName()))	{query.setParameter(p.getName(), filtro1.getClinicas());}
//		}
		return (Integer) query.getSingleResult();
	}

}
