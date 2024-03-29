package br.com.pc.domain.configuracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DEF_MENU")
public class Menu implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID")
	private Integer id;

	@Column(name="NOME")
	private String nome;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
