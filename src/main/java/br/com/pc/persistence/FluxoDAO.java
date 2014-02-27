package br.com.pc.persistence;

import java.util.List;

import javax.persistence.Parameter;
import javax.persistence.Query;

import br.com.pc.domain.Fluxo;
import br.com.pc.ui.bean.Filtro1;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
@SuppressWarnings("unchecked")
public class FluxoDAO extends JPACrud<Fluxo, Long> {

	private static final long serialVersionUID = 1L;

	public List<Fluxo> findByExample2(Fluxo example) {
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select b from Fluxo b " );
		queryString.append(" where b.ativo = true and  " );
		queryString.append(" b.conta = :conta and " );
		queryString.append(" b.data = :data and " );
		queryString.append(" b.clinica = :clinica " );
		queryString.append(" order by b.data, b.conta.conta " );
		
		Query query = createQuery(queryString.toString());
		
		for (Parameter<?> p : query.getParameters()) {
			if (	 "clinica".equals(p.getName()))	{query.setParameter(p.getName(), example.getClinica());}
			else if ("conta".equals(p.getName()))	{query.setParameter(p.getName(), example.getConta());}
			else if ("data".equals(p.getName()))	{query.setParameter(p.getName(), example.getData());}
		}
		
		return query.getResultList();
	}

	public List<Integer> findAno() {
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select year(b.data) from Fluxo b group by year(b.data) " );
		
		Query query = createQuery(queryString.toString());
		
		return query.getResultList();
	}

	public List<Fluxo> findByFiltro1(Filtro1 f, Boolean soAtivos) {
		StringBuilder queryString = new StringBuilder();
		
		queryString.append(" select b from Fluxo b " );
		queryString.append(" where b.id > 0  " );
		if (soAtivos){
			queryString.append(" and b.ativo = true  " );
		}
		if (f.getClinicas()!=null && f.getClinicas().size()>0){
			queryString.append(" and b.clinica in (:clinicas) " );
		}
		if (f.getAno()!=null){
			queryString.append(" and year(b.data) = :ano " );
		}
		if (f.getMes()!=null){
			queryString.append(" and month(b.data) = :mes " );
		}
		if (f.getDtInicio()!=null){
			queryString.append(" and b.data >= :dtInicio " );
		}
		if (f.getDtFim()!=null){
			queryString.append(" and b.data <= :dtFim " );
		}
		queryString.append(" order by b.data, b.conta.conta " );
		
		Query query = createQuery(queryString.toString());
		
		for (Parameter<?> p : query.getParameters()) {
			if (	 "clinicas".equals(p.getName()))	{query.setParameter(p.getName(), f.getClinicas());}
			else if ("ano".equals(p.getName()))	{query.setParameter(p.getName(), f.getAno());}
			else if ("mes".equals(p.getName()))	{query.setParameter(p.getName(), f.getMes().getNumMes());}
			else if ("dtInicio".equals(p.getName())){query.setParameter(p.getName(), f.getDtInicio());}
			else if ("dtFim".equals(p.getName()))	{query.setParameter(p.getName(), f.getDtFim());}
		}
		
		return query.getResultList();
	}

}
