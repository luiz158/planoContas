package br.com.pc.business;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.business.configuracao.UsuarioBC;
import br.com.pc.domain.Conta;
import br.com.pc.domain.Fluxo;
import br.com.pc.persistence.FluxoDAO;
import br.com.pc.ui.bean.Filtro1;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

@BusinessController
public class FluxoBC extends DelegateCrud<Fluxo, Long, FluxoDAO> {

	private static final long serialVersionUID = 1L;
	
	@Inject Credenciais credenciais;
	@Inject UsuarioBC usuarioBC;

	@Override
	public void insert(Fluxo bean) {
		if (credenciais!=null){
			bean.setUsuario(usuarioBC.load(Long.parseLong(credenciais.getId())));
		}
		super.insert(bean);
	}
	
	@Override
	public void update(Fluxo bean) {
		if (credenciais!=null){
			bean.setUsuario(usuarioBC.load(Long.parseLong(credenciais.getId())));
		}
		super.update(bean);
	}

	public void delete(Fluxo bean) {
		if (credenciais!=null){
			bean.setUsuario(usuarioBC.load(Long.parseLong(credenciais.getId())));
		}
		bean.setAtivo(false);
		super.update(bean);
	}
	public void delete(Fluxo bean, String motivo) {
		bean.setMotivoExclusao(motivo);
		delete(bean);
	}
	
	public Fluxo getByExample(Fluxo example){
		List<Fluxo> l = getDelegate().findByExample2(example);
		if (l!=null && l.size()>0){
			return l.get(0);
		}
		return null;
	}
	
	public List<Integer> findAnos(){
		return getDelegate().findAno();
	}
	
	public List<Fluxo> findByFiltro1(Filtro1 filtro1, Boolean soAtivos){
		return getDelegate().findByFiltro1(filtro1, soAtivos, null);
	}
	
	public BigDecimal somaTotal(Filtro1 filtro1, Boolean soAtivos, Conta conta){
		return getDelegate().somaTotal(filtro1, soAtivos, conta);
	}
	public BigDecimal somaTotal2(Filtro1 filtro1, Boolean soAtivos, Conta conta){
		return getDelegate().somaTotal2(filtro1, soAtivos, conta);
	}

	public List<Fluxo> findByFiltro1Conta(Filtro1 filtro1, boolean soAtivos, String conta) {
		return getDelegate().findByFiltro1(filtro1, soAtivos, conta);
	}
	
	
}
