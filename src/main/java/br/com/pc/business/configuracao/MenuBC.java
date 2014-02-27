package br.com.pc.business.configuracao;

import javax.inject.Inject;

import br.com.pc.domain.configuracao.EnumMenu;
import br.com.pc.domain.configuracao.EnumTipoPermissao;
import br.com.pc.domain.configuracao.Menu;
import br.com.pc.domain.configuracao.Permissao;
import br.com.pc.persistence.configuracao.MenuDAO;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;

@BusinessController
public class MenuBC extends DelegateCrud<Menu, Integer, MenuDAO> {

	private static final long serialVersionUID = 1L;
	
	@Inject private PermissaoBC permissaoBC;
	@Inject private GrupoBC grupoBC;
	
//	@Startup
//	public void inicia() {
//		for (EnumMenu enumMenu : EnumMenu.asList()) {
//			Menu menu = getDelegate().load(enumMenu.ordinal());
//			if (menu==null){
//				Menu entity = new Menu();
//				entity.setId(enumMenu.ordinal());
//				entity.setNome(enumMenu.getNome());
//				getDelegate().insert(entity);
//			}
//		}
//		
//		EnumMenu.atualizaNomes();
//	}
	
	public void inicia() {
		for (EnumMenu enumMenu : EnumMenu.asList()) {
			Menu menu = super.load(enumMenu.ordinal());
			if (menu==null){
				Menu entity = new Menu();
				entity.setId((Integer)enumMenu.ordinal());
				entity.setNome(enumMenu.getNome());
				super.insert(entity);
				if(permissaoBC.findAll().size()>0){
					Permissao p = new Permissao(enumMenu);
					p.setAlterar(EnumTipoPermissao.PERMITIDO);
					p.setCriar(EnumTipoPermissao.PERMITIDO);
					p.setExcluir(EnumTipoPermissao.PERMITIDO);
					p.setImprimir(EnumTipoPermissao.PERMITIDO);
					p.setVisualizar(EnumTipoPermissao.PERMITIDO);
					p.setGrupo(grupoBC.load(1l));
					permissaoBC.insert(p);
				}
			}
		}
		
		EnumMenu.atualizaNomes();
	}

	public Menu findByNome(String nome){
		return getDelegate().findByNome(nome);
	}
}
