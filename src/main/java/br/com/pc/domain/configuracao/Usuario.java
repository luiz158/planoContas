package br.com.pc.domain.configuracao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.gov.frameworkdemoiselle.annotation.Field;
import br.gov.frameworkdemoiselle.annotation.PasswordField;
import br.gov.frameworkdemoiselle.annotation.TextField;

@Entity
@Table(name="DEF_USUARIO")
public class Usuario implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@PrePersist
    protected void onCreate() {
		dtCriacao = new Date();
    }
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name="DT_EDICAO",columnDefinition="TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dtEdicao;
	
	@Column(name="DT_CRIACAO", updatable=false)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dtCriacao;
	
	@Column(name="DT_ULTIMO_ACESSO")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dtUltimoAcesso;
	
	@Column(name="ATIVO", nullable = false,  columnDefinition = "bit default 1")
//	@NotNull(message="{message.error.notnull}")
//	@CheckBox
//	@Field(label = "{usuario.label.ativo}")
	private Boolean ativo = true;
	
	@Column(name="LOGIN",unique=true,updatable=false,nullable=false)
	@NotNull(message="{message.error.notnull}")
	@TextField
	@Field(prompt = "{usuario.prompt.login}", label = "{usuario.label.login}")
	private String login;
	
	@Column(name="SENHA",nullable=false)
	@NotNull(message="{message.error.notnull}")
	@PasswordField
	@Field(label = "{usuario.label.senha}")
	private String senha;
	
	@Column(name="EMAIL")
	@TextField
	@Field(prompt = "{usuario.prompt.email}", label = "{usuario.label.email}")
	private String email;
	
	@ManyToMany(cascade = {CascadeType.MERGE},targetEntity=Grupo.class,fetch=FetchType.LAZY)
 	@JoinTable (
 		  name="DEF_USUARIO_GRUPO",
 	      joinColumns=@JoinColumn(name="USUARIO_ID"),
 	      inverseJoinColumns=@JoinColumn (name="GRUPO_ID"))
    private Set<Grupo> grupos = new HashSet<Grupo>();
	
	@Transient
	public void addGrupo(Grupo grupo){
		if (this.grupos == null){
			this.grupos = new HashSet<Grupo>();
		}
		if (!this.grupos.contains(grupo)){
//			grupo.addUsuario(this);
			this.grupos.add(grupo);
		}
	}
	
	public Date getDtEdicao() {
		return dtEdicao;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	public String getLogin() {
		return login;
	}

	public String getSenha() {
		return senha;
	}

	public String getEmail() {
		return email;
	}

	public void setDtEdicao(Date dtEdicao) {
		this.dtEdicao = dtEdicao;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDtUltimoAcesso() {
		return dtUltimoAcesso;
	}

	public void setDtUltimoAcesso(Date dtUltimoAcesso) {
		this.dtUltimoAcesso = dtUltimoAcesso;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return login;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void setGrupos(Set<Grupo> grupos) {
		this.grupos = grupos;
	}

	public Set<Grupo> getGrupos() {
		return grupos;
	}	
}
