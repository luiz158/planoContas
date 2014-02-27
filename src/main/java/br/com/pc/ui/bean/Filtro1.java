package br.com.pc.ui.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.pc.domain.Clinica;
import br.com.pc.domain.configuracao.EnumMeses;

public class Filtro1 {

	private List<Clinica> clinicas;
	private Integer ano;
	private EnumMeses mes;
	private Date dtInicio;
	private Date dtFim;
	
	public List<Clinica> getClinicas() {
		return clinicas;
	}
	public void setClinicas(List<Clinica> clinicas) {
		this.clinicas = clinicas;
	}
	public void setClinicas(Set<Clinica> clinicas) {
		this.clinicas = new ArrayList<Clinica>(clinicas);
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public EnumMeses getMes() {
		return mes;
	}
	public void setMes(EnumMeses mes) {
		this.mes = mes;
	}
	public Date getDtInicio() {
		return dtInicio;
	}
	public void setDtInicio(Date dtInicio) {
		this.dtInicio = dtInicio;
	}
	public Date getDtFim() {
		return dtFim;
	}
	public void setDtFim(Date dtFim) {
		this.dtFim = dtFim;
	}
	
}
