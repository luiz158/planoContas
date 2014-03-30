package br.com.pc.business;

import java.util.List;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.domain.Conta;
import br.com.pc.persistence.ContaDAO;
import br.com.pc.ui.bean.Filtro1;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

@BusinessController
public class ContaBC extends DelegateCrud<Conta, Long, ContaDAO> {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void insert(Conta bean) {
		super.insert(bean);
	}
	
	@Override
	public void update(Conta bean) {
//		try {
//			
//		} catch (javax.persistence.OptimisticLockException e) {
//			// TODO: handle exception
//		}
		super.update(bean);
	}
	public void delete(Conta bean) {
		bean.setAtivo(false);
		super.update(bean);
	}
	
	public List<Conta> findByTotalizadora(Boolean totalizadora){
		return getDelegate().findByTotalizadora(totalizadora, null);
	}
	public List<Conta> findByTotalizadora(Boolean totalizadora, String conta){
		return getDelegate().findByTotalizadora(totalizadora, conta);
	}

	@Override
	public List<Conta> findAll(){
		return getDelegate().findAllAtivo();
	}

	public List<Conta> findAll(Credenciais credenciais) {
		return getDelegate().findAllAtivo(credenciais);
	}

	public List<Conta> findByFiltro1(Filtro1 filtro1, Boolean soAtivos) {
		return getDelegate().findByFiltro1(filtro1,soAtivos);
	}
	
	
}
