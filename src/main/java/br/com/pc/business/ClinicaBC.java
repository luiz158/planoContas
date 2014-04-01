package br.com.pc.business;

import java.util.List;

import javax.inject.Inject;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.business.configuracao.GrupoBC;
import br.com.pc.domain.Clinica;
import br.com.pc.domain.Conta;
import br.com.pc.persistence.ClinicaDAO;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

@BusinessController
public class ClinicaBC extends DelegateCrud<Clinica, Long, ClinicaDAO> {

	private static final long serialVersionUID = 1L;
	
//	@Override
//	public void insert(Clinica bean) {
//		super.insert(bean);
//	}
//	
//	@Override
//	public void update(Clinica bean) {
//		super.update(bean);
//	}
	public void delete(Clinica bean) {
		bean.setAtivo(false);
		super.update(bean);
	}

	public List<Clinica> findAll(Credenciais credenciais) {
		return getDelegate().findByCredenciais(credenciais);
	}
	public List<Clinica> findAllAtivos() {
		return getDelegate().findAllAtivos();
	}

	public List<Clinica> findByConta(Conta conta) {
		return getDelegate().findByConta(conta);
	}
}
