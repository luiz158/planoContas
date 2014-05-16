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
		if (bean.getConta()==null || bean.getConta().length()==0){
			bean.setConta(geraNumeroConta(bean));
		}
		super.insert(bean);
	}
	
	@Override
	public void update(Conta bean) {
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
		return getDelegate().findByFiltro1(filtro1,soAtivos,false);
	}

	public List<Conta> findByFiltro1(Filtro1 filtro1, Boolean soAtivos, Boolean soDre) {
		return getDelegate().findByFiltro1(filtro1,soAtivos,soDre);
	}
	
	private Integer getQtContaSemContaPai(){
		return getDelegate().getQtContaSemContaPai();
	}
	public String geraNumeroConta(Conta conta){
		//conta nova ou atualização de conta? (mudança de conta pai, por exemplo)
		//a conta tem conta pai?
		//a conta é uma totalizadora?
		if (conta.getTotalizadora()){
			//conta sem 9 digitos
			if (conta.getContaPai()==null){
				return getQtContaSemContaPai()+"";
			}else{
				int nv = conta.getContaPai().getConta().split("\\.").length+1;
				int qt = load(conta.getContaPai().getId()).getContasFilha().size();
				String ct = conta.getContaPai().getConta();
				switch (nv) {
				case 1:
					ct += String.format(".%01d", qt+1);
					break;
				case 2:
					ct += String.format(".%01d", qt+1);
					break;
				case 3:
					ct += String.format(".%02d", qt+1);
					break;
				case 4:
					ct += String.format(".%02d", qt+1);
					break;
				case 5:
					ct += String.format(".%03d", qt+1);
					break;

				default:
					break;
				}
				return ct;
			}
		}else{
			//conta com 9 digitos
			//conta não é totalizadora
			if (conta.getContaPai()==null){
				return getQtContaSemContaPai()+"";
			}else{
				int nv = conta.getContaPai().getConta().split("\\.").length+1;
				int qt = load(conta.getContaPai().getId()).getContasFilha().size();
				String ct = conta.getContaPai().getConta();
				
				switch (nv) {
				case 1:
					ct += String.format(".%01d.1.01.01.001", qt+1);
					break;
				case 2:
					ct += String.format(".%01d.01.01.001", qt+1);
					break;
				case 3:
					ct += String.format(".%02d.01.001", qt+1);
					break;
				case 4:
					ct += String.format(".%02d.001", qt+1);
					break;
				case 5:
					ct += String.format(".%03d", qt+1);
					break;

				default:
					break;
				}
				return ct;
			}
		}
	}
	
}
