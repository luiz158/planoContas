package br.com.pc.ui.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.pc.business.ContaBC;
import br.com.pc.ui.SistemaApplication;
import br.com.pc.util.ReportUtil;


public class DreReport {

	public DreReport() {

	}

	private static DreBean gera(){
		DreBean bean = new DreBean((new ContaBC()).load(1l),new BigDecimal(12.0));
		return bean;
	}
	
	public static Collection<DreBean> dres(){
		List lista = new ArrayList();
		lista.add(gera());
		lista.add(gera());
		return lista;
	}
	
	public void drePDF() {
		try {
			List lista = new ArrayList();
			lista.add(gera());
			lista.add(gera());
			SistemaApplication
					.getInstance()
					.getMainWindow()
					.open(new ReportUtil("dre1.jasper",
							SistemaApplication.getInstance(), null,lista
				).open(), "_blank");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void drePDF(List<DreBean> somaTotal) {
		try {
			SistemaApplication
					.getInstance()
					.getMainWindow()
					.open(new ReportUtil("dre1.jasper",
							SistemaApplication.getInstance(), null,somaTotal
				).open(), "_blank");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
