package br.com.pc.ui.bean;

import java.math.BigDecimal;

import br.com.pc.domain.configuracao.EnumDre;

public class Dre {

	private String conta;
	private BigDecimal valor;
	private EnumDre tipo;
	
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public EnumDre getTipo() {
		return tipo;
	}
	public void setTipo(EnumDre tipo) {
		this.tipo = tipo;
	}
}
