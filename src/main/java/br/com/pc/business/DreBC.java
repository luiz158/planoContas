package br.com.pc.business;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.pc.domain.Conta;
import br.com.pc.domain.configuracao.EnumDre;
import br.com.pc.ui.bean.Dre;
import br.com.pc.ui.bean.Filtro1;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;

@BusinessController
public class DreBC implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject public ContaBC contaBC;
	@Inject public FluxoBC fluxoBC;
	
	
	public List<Dre> findAll(Filtro1 filtro) {
		List<Dre> lista = new ArrayList<Dre>();
		for (EnumDre tipo : EnumDre.asList()) {
			BigDecimal valor = new BigDecimal(0.0);
			for (Conta conta : contaBC.findByDre(tipo)) {
				Dre dre = new Dre();
				dre.setConta(conta.getDescricao());
				dre.setTipo(null);
				dre.setValor(fluxoBC.somaTotal2(filtro, true, conta));
				lista.add(dre);
				valor = valor.add(dre.getValor());
			}
			Dre dre = new Dre();
			dre.setConta(tipo.getDescricao());
			dre.setTipo(tipo);
			dre.setValor(valor);
			lista.add(dre);
		}
		
		return lista;
	}
	
}
