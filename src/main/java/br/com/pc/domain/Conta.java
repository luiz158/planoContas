package br.com.pc.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import br.com.pc.domain.configuracao.EnumDre;

@Entity
@Table(name="PC_CONTA")
public class Conta implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@PrePersist
    protected void onCreate() {
		dtCriacao = new Date();
    }
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name="ATIVO", nullable = false,  columnDefinition = "bit default 1")
	private Boolean ativo=true;
	
	@Version
	@Column(name="VR")
	private Long versao;
	
	@Column(name="DT_EDICAO",columnDefinition="TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dtEdicao;
	
	@Column(name="DT_CRIACAO", nullable=false,updatable=false)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dtCriacao;

	@Column(name="DESCRICAO")
	private String descricao;

	@Column(name="CONTA")
	private String conta;

	@Column(name="TOTALIZADORA", nullable = false,  columnDefinition = "bit default 0")
	private Boolean totalizadora=false;

	@Column(name="RESUMO_FINANCEIRO", nullable = false,  columnDefinition = "bit default 0")
	private Boolean resumoFinanceiro=false;
	
	@ManyToOne(cascade={CascadeType.MERGE},optional = true, targetEntity = Conta.class)
	@JoinColumn(name = "CONTA_ID")
	private Conta contaPai;
	
	@OneToMany(cascade={CascadeType.MERGE},targetEntity=Conta.class, fetch=FetchType.LAZY)
	@JoinColumn(name = "CONTA_ID")
	private Set<Conta> contasFilha;
	
	@ManyToMany(cascade = {CascadeType.MERGE},targetEntity=Clinica.class, fetch=FetchType.LAZY)
 	@JoinTable (
 		  name="PC_CONTA_CLINICA",
 	      joinColumns=@JoinColumn(name="CONTA_ID"),
 	      inverseJoinColumns=@JoinColumn (name="CLINICA_ID"))
 	private List<Clinica> clinicas = new ArrayList<Clinica>();
	
	@Column(name="DRE")
	private EnumDre dre;
	
	@Transient
	public void addClinica(Clinica clinica){
		if (this.clinicas == null){
			this.clinicas = new ArrayList<Clinica>();
		}
		if (!this.clinicas.contains(clinica)){
			this.clinicas.add(clinica);
		}
	}
	
	@Transient
	private String contaDescricao;

	@Transient
	private String contaPaiDescricao;	
	@Transient
	private String contaPaiDescricaoConta;

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

	public String getDescricao() {
		return (descricao != null ? descricao.toUpperCase() : descricao);
	}

	public void setDescricao(String descricao) {
		this.descricao = (descricao != null ? descricao.toUpperCase() : descricao);;
	}

	public String getConta() {
		return (conta != null ? conta.toUpperCase() : conta);
	}

	public void setConta(String conta) {
		this.conta = (conta != null ? conta.toUpperCase() : conta);
	}

	public Boolean getTotalizadora() {
		return totalizadora;
	}

	public void setTotalizadora(Boolean totalizadora) {
		this.totalizadora = totalizadora;
	}

	public Conta getContaPai() {
		return contaPai;
	}

	public void setContaPai(Conta contaPai) {
		this.contaPai = contaPai;
	}

	public Set<Conta> getContasFilha() {
		return contasFilha;
	}

	public void setContasFilha(Set<Conta> contasFilha) {
		this.contasFilha = contasFilha;
	}

	@Override
	public String toString() {
		return (descricao != null ? descricao.toUpperCase() : "");
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
		Conta other = (Conta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public String getContaDescricao() {
		contaDescricao = getConta() +" - "+ getDescricao();
		return contaDescricao;
	}
	@Transient
	public void setContaDescricao(String contaDescricao) {
		this.contaDescricao = contaDescricao;
	}

	@Transient
	public String getContaPaiDescricao() {
		if (getContaPai()!=null){
			contaPaiDescricao = String.format("%s > %s",getContaPai().getContaPaiDescricao(),getDescricao());
		}else{
			contaPaiDescricao = String.format("%s",getDescricao());
		}
		return contaPaiDescricao;
	}
	@Transient
	public void setContaPaiDescricao(String contaPaiDescricao) {
		this.contaPaiDescricao = contaPaiDescricao;
	}
	@Transient
	public String getContaPaiDescricaoConta() {
		this.contaPaiDescricaoConta = String.format("%s > %s",getContaPaiDescricao(),conta);
		return this.contaPaiDescricaoConta;
	}
	@Transient
	public void setContaPaiDescricaoConta(String contaPaiDescricaoConta) {
		this.contaPaiDescricaoConta = contaPaiDescricaoConta;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
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

	public List<Clinica> getClinicas() {
		return clinicas;
	}

	public void setClinicas(List<Clinica> clinicas) {
		this.clinicas = clinicas;
	}

	public Boolean getResumoFinanceiro() {
		return resumoFinanceiro;
	}

	public void setResumoFinanceiro(Boolean resumoFinanceiro) {
		this.resumoFinanceiro = resumoFinanceiro;
	}

	public EnumDre getDre() {
		return dre;
	}

	public void setDre(EnumDre dre) {
		this.dre = dre;
	}
	
}
