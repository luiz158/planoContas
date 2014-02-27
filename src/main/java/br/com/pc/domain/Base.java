package br.com.pc.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class Base implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public Base() {
		if (uuid == null){
			this.uuid = UUID.randomUUID().toString();
		}
//		if (dtEdicao == null){
//			this.dtEdicao = new Date();
//		}
		if (dtCriacao == null){
			this.dtCriacao = new Date();
		}
	}
	
	@Id
	@Column(name="UUID")
	private String uuid;
	
	@Column(name="DT_EDICAO",columnDefinition="TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@NotNull(message="{message.error.notnull}")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dtEdicao;
	
	@Column(name="DT_CRIACAO",updatable=false)
	@NotNull(message="{message.error.notnull}")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dtCriacao;

	@Version
	@Column(name="VERSAO")
	private Long versao;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getDtEdicao() {
		return dtEdicao;
	}

	public void setDtEdicao(Date dtEdicao) {
		this.dtEdicao = dtEdicao;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}
	
	
}
