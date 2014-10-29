package br.com.pc.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import br.com.pc.domain.configuracao.Usuario;

@Entity
@Table(name="PC_FLUXO")
public class Fluxo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Version
	@Column(name="VR")
	private Long versao;
	
	@Column(name="DT_EDICAO",columnDefinition="TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dtEdicao;
	
	@Column(name="DT_CRIACAO", nullable=false,updatable=false)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dtCriacao;
	
	@Column(name="ATIVO", nullable = false,  columnDefinition = "bit default 1")
	private Boolean ativo=true;
	
	@PrePersist
    protected void onCreate() {
		dtCriacao = new Date();
    }
	
	@ManyToOne(cascade={CascadeType.MERGE},optional = false, targetEntity = Conta.class)
	@JoinColumn(name = "CONTA_ID")
	private Conta conta;
	
	@Column(name="DATA", nullable=false)
	@Temporal(value = TemporalType.DATE)
	private Date data;
	
	@Column(name="VALOR", nullable=false, precision = 10, scale = 4)
	private BigDecimal valor = new BigDecimal("0.0");

	@ManyToOne(cascade={CascadeType.MERGE},optional = false, targetEntity = Clinica.class)
	@JoinColumn(name = "CLINICA_ID")
	private Clinica clinica;

	@ManyToOne(cascade={CascadeType.MERGE},optional = false, targetEntity = Usuario.class)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;

	@Column(name="REGISTRO",  columnDefinition = "varchar(500)")
	private String registro;

	@Column(name="MOTIVO_EXCLUSAO")
	private String motivoExclusao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		valor.setScale(4);
		return valor;
	}

	public void setValor(BigDecimal valor) {
		valor.setScale(4);
		this.valor = valor;
	}

	public Long getVersao() {
		return versao;
	}

	public Date getDtEdicao() {
		return dtEdicao;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fluxo other = (Fluxo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Clinica getClinica() {
		return clinica;
	}

	public void setClinica(Clinica clinica) {
		this.clinica = clinica;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public void setDtEdicao(Date dtEdicao) {
		this.dtEdicao = dtEdicao;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public String getMotivoExclusao() {
		return motivoExclusao;
	}

	public void setMotivoExclusao(String motivoExclusao) {
		this.motivoExclusao = motivoExclusao;
	}

	
}
