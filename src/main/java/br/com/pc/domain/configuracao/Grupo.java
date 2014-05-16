package br.com.pc.domain.configuracao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.pc.domain.Clinica;
import br.gov.frameworkdemoiselle.annotation.Field;
import br.gov.frameworkdemoiselle.annotation.TextField;

@Entity
@Table(name="DEF_GRUPO")
public class Grupo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name="ATIVO", nullable = false,  columnDefinition = "bit default 1")
	private Boolean ativo = true;
//	
//	@Column(name="DT_EDICAO")
//	@NotNull(message="{message.error.notnull}")
//	@Temporal(value = TemporalType.TIMESTAMP)
//	private Date dtEdicao;
//	
//	@Column(name="DT_CRIACAO")
//	@NotNull(message="{message.error.notnull}")
//	@Temporal(value = TemporalType.TIMESTAMP)
//	private Date dtCriacao;
//	
	@Column(name="DESCRICAO",unique=true,nullable=false)
	@TextField
	@Field(prompt = "{grupo.label.grupo}", label = "{grupo.label.grupo}")
	private String descricao;
	
	@ManyToMany(cascade = {CascadeType.MERGE,CascadeType.DETACH},targetEntity=Usuario.class,fetch=FetchType.EAGER)
 	@JoinTable (
 		  name="DEF_USUARIO_GRUPO",
 	      joinColumns=@JoinColumn(name="GRUPO_ID"),
 	      inverseJoinColumns=@JoinColumn (name="USUARIO_ID"))
 	private Set<Usuario> usuarios = new HashSet<Usuario>();
//	@ManyToMany(mappedBy="grupos")
// 	private List<Usuario> usuarios = new ArrayList<Usuario>();
	
	@ManyToMany(cascade = {CascadeType.MERGE},targetEntity=Clinica.class,fetch=FetchType.LAZY)
 	@JoinTable (
 		  name="DEF_CLINICA_GRUPO",
 	      joinColumns=@JoinColumn(name="GRUPO_ID"),
 	      inverseJoinColumns=@JoinColumn (name="CLINICA_ID"))
 	private List<Clinica> clinicas = new ArrayList<Clinica>();
	
	@OneToMany(mappedBy="grupo",cascade = {CascadeType.ALL})
	private List<Permissao> permissoes;

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
	public void addUsuario(Usuario usuario){
		if (this.usuarios == null){
			this.usuarios = new HashSet<Usuario>();
		}
		if (!this.usuarios.contains(usuario)){
			this.usuarios.add(usuario);
		}
	}
	
	@Transient
	public void addPermissao(Permissao permissao){
//		System.out.println("===> addPermissao ADICIONANDO PERMISSAO ==");
		if (this.permissoes == null){
//			System.out.println("===> criando array de PERMISSAO");
			this.permissoes = new ArrayList<Permissao>();
		}
		if (!this.permissoes.contains(permissao)){
//			System.out.println("===> ADICIONANDO PERMISSAO ==");
			permissao.setGrupo(this);
			this.permissoes.add(permissao);
		}
	}
	
//	public Date getDtEdicao() {
//		return dtEdicao;
//	}
//
//	public Date getDtCriacao() {
//		return dtCriacao;
//	}
//
//	public void setDtEdicao(Date dtEdicao) {
//		this.dtEdicao = dtEdicao;
//	}
//
//	public void setDtCriacao(Date dtCriacao) {
//		this.dtCriacao = dtCriacao;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return descricao ;
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
		Grupo other = (Grupo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public List<Clinica> getClinicas() {
		return clinicas;
	}

	public void setClinicas(List<Clinica> clinicas) {
		this.clinicas = clinicas;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

}
