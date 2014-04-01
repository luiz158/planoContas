package br.com.pc.persistence;

import java.util.List;

import javax.persistence.Parameter;
import javax.persistence.Query;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.domain.Clinica;
import br.com.pc.domain.Conta;
import br.com.pc.domain.configuracao.Grupo;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
public class ClinicaDAO extends JPACrud<Clinica, Long> {

	private static final long serialVersionUID = 1L;

	public List<Clinica> findByCredenciais(Credenciais credenciais) {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(" select c from Grupo g ");
		stringBuilder.append(" inner join g.clinicas c ");
		stringBuilder.append(" inner join g.usuarios u ");
		stringBuilder.append(" where u.id = :id and c.ativo = true  ");
		stringBuilder.append(" order by c.descricao ");
		
		Query query = createQuery(stringBuilder.toString());
		
		for (Parameter<?> p : query.getParameters()) {
			if ("id".equals(p.getName()))	{query.setParameter(p.getName(), Long.parseLong(credenciais.getId()));}
		}
		
		return query.getResultList();
	}

	public List<Clinica> findByConta(Conta conta) {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(" select clinica from Conta conta ");
		stringBuilder.append(" inner join conta.clinicas clinica ");
		stringBuilder.append(" where conta.id = :id and clinica.ativo = true ");
		stringBuilder.append(" order by clinica.descricao ");
		
		Query query = createQuery(stringBuilder.toString());
		
		for (Parameter<?> p : query.getParameters()) {
			if ("id".equals(p.getName()))	{query.setParameter(p.getName(), conta.getId());}
		}
		
		return query.getResultList();
	}
	public List<Clinica> findAllAtivos() {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(" select c from Clinica c ");
		stringBuilder.append(" where c.ativo = true  ");
		stringBuilder.append(" order by c.descricao ");
		
		Query query = createQuery(stringBuilder.toString());
		
		return query.getResultList();
	}


}
