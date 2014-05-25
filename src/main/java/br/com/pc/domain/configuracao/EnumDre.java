package br.com.pc.domain.configuracao;

import java.util.ArrayList;
import java.util.List;

public enum EnumDre {
	LUCRO_BRUTO ("LUCRO BRUTO"),
	RES_ANTES_EFEITOS_FINANCEIROS ("RESULTADO ANTES DOS EFEITOS FINCANCEIROS"),
	LUCRO_ANTES_IMPOSTOS ("LUCRO ANTES DOS IMPOSTOS"),
	LUCRO_PREJUIZO ("LUCRO/PREJUIZO");
	
	private String descricao;
	
	private EnumDre(String descricao) {  
		this.descricao = descricao;
	}
	
	public static List<EnumDre> asList() {
		List<EnumDre> lista = new ArrayList<EnumDre>();
		for (EnumDre e : EnumDre.values()) {
			lista.add(e);
		}
		return lista;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
