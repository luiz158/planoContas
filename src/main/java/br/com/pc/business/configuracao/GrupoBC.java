package br.com.pc.business.configuracao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.pc.accesscontrol.Credenciais;
import br.com.pc.domain.Clinica;
import br.com.pc.domain.configuracao.EnumMenu;
import br.com.pc.domain.configuracao.EnumTipoPermissao;
import br.com.pc.domain.configuracao.Grupo;
import br.com.pc.domain.configuracao.Permissao;
import br.com.pc.domain.configuracao.Usuario;
import br.com.pc.persistence.configuracao.GrupoDAO;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

@BusinessController
public class GrupoBC extends DelegateCrud<Grupo, Long, GrupoDAO> {

	private static final long serialVersionUID = 1L;

	@Inject	private UsuarioBC usuarioBC;
	
	public void inicia(){
		List<Grupo> bean = getDelegate().findAll();
		if (bean.size() == 0) {
			List<String> lista  = new ArrayList<String>() ;
			lista.add("ADMINISTRADORES");
			lista.add("USUARIOS");

			Long j = 0l;
			for (String i : lista) {
				j+=1l;
				Grupo entity = new Grupo();
				entity.setDescricao(i.toString());
				entity.addUsuario(usuarioBC.load(j));
				getDelegate().insert(entity);
			}	
		}	
	}
	
	/**
	 * @param grupo
	 * @return Lista com as permissões PERMITIDO para todos os menus
	 */
	public List<Permissao> geraPermissoes(Grupo grupo){
		List<Permissao> ps = new ArrayList<Permissao>();
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
		return ps;
	}
	
	public Grupo findByDescricao(String descricao){
		return getDelegate().findByDescricao(descricao);
	}
	
	public List<Grupo> findGrupos(){
		return getDelegate().findGrupos();
	}
	public List<Grupo> findByCredenciais(Credenciais credenciais){
		return findByUsuario(usuarioBC.load(Long.parseLong(credenciais.getId())));
	}

	public List<Grupo> findByUsuario(Usuario usuario){
		return getDelegate().findByUsuario(usuario.getId());
	}

	public List<Clinica> findClinicas(Long id) {
		return getDelegate().findClinicas(id);
	}

	public List<Usuario> findUsuarios(Long id) {
		return getDelegate().findUsuarios(id);
	}

}
