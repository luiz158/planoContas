package br.com.pc.business.configuracao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.domain.configuracao.EnumMenu;
import br.com.pc.domain.configuracao.EnumTipoPermissao;
import br.com.pc.domain.configuracao.Grupo;
import br.com.pc.domain.configuracao.Permissao;
import br.com.pc.domain.configuracao.Usuario;
import br.com.pc.persistence.configuracao.PermissaoDAO;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

@BusinessController
public class PermissaoBC extends DelegateCrud<Permissao, Integer, PermissaoDAO> {

	
//	@Inject
//	private UsuarioBC usuarioBC;
	@Inject private GrupoBC grupoBC;
//	@Inject
//	private MenuBC menuBC;
	
	@Inject Credenciais credenciais;
	@Inject UsuarioBC usuarioBC;

	private static final long serialVersionUID = 1L;
	
	public void inicia(){
		List<Permissao> bean = getDelegate().findAll();
		if (bean.size() == 0) {
//			for (Permissao p : geraPermissoes(grupoBC.load(1l))) {//gera permissões para administradores
//				insert(p);
//			}
			for (Permissao p : geraPermissoes(grupoBC.load(2l))) {//gera permissões para usuarios
				insert(p);
			}
		}else{
			
		}
	}
	
	private List<Permissao> geraPermissoes(Grupo grupo){
		List<Permissao> ps = new ArrayList<Permissao>();
		if (grupo.getId()!=1l){
			Permissao p;
			for (EnumMenu menu : EnumMenu.asList()) {
				p = new Permissao(menu);
				p.setAlterar(EnumTipoPermissao.PERMITIDO);
				p.setCriar(EnumTipoPermissao.PERMITIDO);
				p.setExcluir(EnumTipoPermissao.PERMITIDO);
				p.setImprimir(EnumTipoPermissao.PERMITIDO);
				p.setVisualizar(EnumTipoPermissao.PERMITIDO);
				p.setGrupo(grupo);
				ps.add(p);
			}
		}
		return ps;
	}
	
	public Set<EnumMenu> findMenuByUsuario(Usuario usuario){
		Set<EnumMenu> n = getDelegate().findMenuByUsuario(usuario,EnumTipoPermissao.NEGADO);
		Set<EnumMenu> p = getDelegate().findMenuByUsuario(usuario,EnumTipoPermissao.PERMITIDO);

		p.removeAll(n);
		return p;
	}
	
	public Permissao getPermissao(Usuario usuario, EnumMenu menu){
		Permissao permissao = new Permissao();
		permissao.setId(0);
		GrupoBC grupoBC2 = new GrupoBC();
		if (grupoBC2.findByUsuario(usuario).contains(grupoBC2.load(1l))){
			permissao.setAlterar(EnumTipoPermissao.PERMITIDO);
			permissao.setCriar(EnumTipoPermissao.PERMITIDO);
			permissao.setExcluir(EnumTipoPermissao.PERMITIDO);
			permissao.setImprimir(EnumTipoPermissao.PERMITIDO);
			permissao.setVisualizar(EnumTipoPermissao.PERMITIDO);
		}else{
			permissao.setAlterar(getDelegate().getPermissaoAlterar(usuario, menu));
			permissao.setCriar(getDelegate().getPermissaoCriar(usuario, menu));
			permissao.setExcluir(getDelegate().getPermissaoExcluir(usuario, menu));
			permissao.setImprimir(getDelegate().getPermissaoImprimir(usuario, menu));
			permissao.setVisualizar(getDelegate().getPermissaoVisualizar(usuario, menu));
		}
		return permissao;
	}
	
	public Permissao getPermissao(Credenciais credenciais, EnumMenu menu){
		if (credenciais!=null){
			Usuario usuario = usuarioBC.load(Long.parseLong(credenciais.getId()));
			return getPermissao(usuario,menu);
		}
		return null;
	}
	
	public List<Permissao> findPermissoes() {
		return getDelegate().findPermissoes();
	}
	public List<Permissao> findAllAtivos() {
		return getDelegate().findAllAtivos();
	}
}
