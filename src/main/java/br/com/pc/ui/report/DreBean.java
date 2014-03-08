package br.com.pc.ui.report;

import java.math.BigDecimal;

import br.com.pc.domain.Conta;

public class DreBean {

	private Conta conta;
	private String contaNumero;
	private String contaDescricao;
	private BigDecimal valor;
	
	public DreBean(Conta conta, BigDecimal valor) {
		super();
		this.conta = conta;
		this.valor = valor;
		this.contaNumero = conta.getConta();
		this.contaDescricao = conta.getDescricao();
	}
	public DreBean(String contaNumero, String contaDescricao) {
		super();
		this.valor = new BigDecimal("0");
		this.contaNumero = contaNumero;
		this.contaDescricao = contaDescricao;
	}
	
	public void addValor(BigDecimal val){
		this.valor = this.valor.add(val);
	}
	
	public Conta getConta() {
		return conta;
	}
	public void setConta(Conta conta) {
		this.conta = conta;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getContaNumero() {
		return contaNumero;
	}

	public void setContaNumero(String contaNumero) {
		this.contaNumero = contaNumero;
	}

	public String getContaDescricao() {
		return contaDescricao;
	}

	public void setContaDescricao(String contaDescricao) {
		this.contaDescricao = contaDescricao;
	}
	
	
}
