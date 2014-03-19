package br.com.pc.domain.configuracao;

import java.util.ArrayList;
import java.util.List;

import br.com.pc.business.configuracao.MenuBC;

import com.vaadin.terminal.ThemeResource;

public enum EnumMenu {
	LOGIN			("LOGIN","icons/16/user.png"),
		LOGOUT			("SAIR",true,LOGIN),
		ALTERA_SENHA	("ALTERAR SENHA",true,LOGIN),
	GERENCIA		("GERENCIA","icons/16/document.png"),
		PERMISSOES	("PERMISSÕES",true,GERENCIA),
		CRUD_GRUPO		("GRUPO",true,GERENCIA),
		CRUD_USUARIO	("USUARIO",true,GERENCIA),
	PC_LANCAMENTOS		("LANÇAMENTOS"),
		PC_FLUXO		("HISTÓRICO",true,PC_LANCAMENTOS),
		PC_RECEITAS		("RECEITAS",true,PC_LANCAMENTOS),
		PC_DESPESAS		("DESPESAS",true,PC_LANCAMENTOS),
	PC_CADASTROS		("CADASTROS"),
		PC_CONTA		("CONTA",true,PC_CADASTROS),
		PC_CLINICA		("CLINICA",true,PC_CADASTROS),
	PC_FLUXO2		("FLUXO MENSAL",true),
	PC_DRE			("D.R.E.",true);
//------------

//-----------

	
	
	public static List<EnumMenu> asList() {
		List<EnumMenu> lista = new ArrayList<EnumMenu>();

//---PLANO CONTAS
		lista.add(PC_LANCAMENTOS);
		lista.add(PC_FLUXO);
		lista.add(PC_RECEITAS);
		lista.add(PC_DESPESAS);
		lista.add(PC_CADASTROS);
		lista.add(PC_CONTA);
		lista.add(PC_CLINICA);
		lista.add(PC_FLUXO2);
		lista.add(PC_DRE);

//---GERENCIA
		lista.add(GERENCIA);
			lista.add(PERMISSOES);
			lista.add(CRUD_GRUPO);
			lista.add(CRUD_USUARIO);

//---CRUDS
		
			
//		lista.add(LOGIN);
			lista.add(ALTERA_SENHA);
			lista.add(LOGOUT);
		return lista;
	}


	private String nome;
	private Boolean executa;
	private EnumMenu pai;
	private String icone;

	EnumMenu(String nome, Boolean executa, EnumMenu pai, String icone) {
		this.nome = nome;
		this.executa = executa;
		this.pai = pai;
		if (icone != null && icone.length() >0){
			this.icone = icone;
		}else{
			this.icone=null;
		}
	}
	EnumMenu(String nome, Boolean executa, EnumMenu pai) {
		this.nome = nome;
		this.executa = executa;
		this.pai = pai;
		this.icone = null;
	}
	EnumMenu(String nome, Boolean executa) {
		this.nome = nome;
		this.executa = executa;
		this.pai = null;
		this.icone = null;
	}
	EnumMenu(String nome, Boolean executa, String icone) {
		this.nome = nome;
		this.executa = executa;
		this.pai = null;
		if (icone != null && icone.length() >0){
			this.icone = icone;
		}else{
			this.icone=null;
		}
	}
	EnumMenu(String nome) {
		this.nome = nome;
		this.executa = false;
		this.pai = null;
		this.icone = null;
	}
	EnumMenu(String nome, String icone) {
		this.nome = nome;
		this.executa = false;
		this.pai = null;
		if (icone != null && icone.length() >0){
			this.icone = icone;
		}else{
			this.icone=null;
		}
	}
	
	public static EnumMenu findByNome(String nome) {
		for (EnumMenu bean : EnumMenu.asList()) {
			if (bean.nome.toUpperCase().equals(nome.toUpperCase())){
				return bean;
			}
		}
		return null;
	}
	
	public static EnumMenu findByPai(EnumMenu pai) {
		for (EnumMenu bean : EnumMenu.asList()) {
			if (bean.pai.equals(pai)){
				return bean;
			}
		}
		return null;
	}
	
//	@Inject
//	static MenuBC menuBC;
	public static void atualizaNomes() {
			MenuBC menuBC = new MenuBC();
		for (EnumMenu bean : EnumMenu.asList()) {
			try {
				bean.setNome(menuBC.load(bean.ordinal()).getNome());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public EnumMenu getPai() {
		return pai;
	}

	public void setPai(EnumMenu pai) {
		this.pai = pai;
	}

	public Boolean getExecuta() {
		return executa;
	}

	public void setExecuta(Boolean executa) {
		this.executa = executa;
	}

	public ThemeResource getIcone() {
		if (icone != null && icone.length() > 0){
			return new ThemeResource(icone);
		}else{
			return null;
		}
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}
}
