package br.com.pc.ui.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.pc.business.ContaBC;
import br.com.pc.ui.SistemaApplication;
import br.com.pc.ui.bean.Dre;
import br.com.pc.ui.bean.Filtro1;
import br.com.pc.util.ReportUtil;


public class ResumoFinanceiroReport {

	public ResumoFinanceiroReport() {

	}

	private static ResumoFinanceiroBean gera(){
		ResumoFinanceiroBean bean = new ResumoFinanceiroBean((new ContaBC()).load(1l),new BigDecimal(12.0));
		return bean;
	}
	
	public static Collection<ResumoFinanceiroBean> resumosFinanceiros(){
		List<ResumoFinanceiroBean> lista = new ArrayList<ResumoFinanceiroBean>();
		lista.add(gera());
		lista.add(gera());
		return lista;
	}
	
	public void resumoFinanceiroPDF() {
		try {
			List<ResumoFinanceiroBean> lista = new ArrayList<ResumoFinanceiroBean>();
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void resumoFinanceiroPDF(List<ResumoFinanceiroBean> somaTotal, Filtro1 filtro1, Double total) {
		try {
			Map parametros = new HashMap();
			parametros.put("DT_INICIO", filtro1.getDtInicio());
			parametros.put("DT_FIM", filtro1.getDtFim());
			parametros.put("ANO", filtro1.getAno());
			parametros.put("MES", filtro1.getMes()!=null?filtro1.getMes().name():null);
			parametros.put("CLINICAS", filtro1.getClinicas().toString().replace("[", "").replace("]", "").length() > 0 ? 
					filtro1.getClinicas().toString().replace("[", "").replace("]", "") : "");
			parametros.put("CLINICAS1", filtro1.getClinicas());
			parametros.put("TOTAL", total);
			parametros.put("LOGO", SistemaApplication
										.getInstance()
										.getRequest()
										.getSession()
										.getServletContext()
										.getRealPath("/VAADIN/themes/pc/images/facimagem1.png"));
			SistemaApplication
					.getInstance()
					.getMainWindow()
					.open(new ReportUtil("resumoFinanceiro.jasper",
							SistemaApplication.getInstance(), parametros,somaTotal
				).open(), "_blank");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void drePDF(List<Dre> dre, Filtro1 filtro1) {
		try {
			Map parametros = new HashMap();
			parametros.put("DT_INICIO", filtro1.getDtInicio());
			parametros.put("DT_FIM", filtro1.getDtFim());
			parametros.put("ANO", filtro1.getAno());
			parametros.put("MES", filtro1.getMes()!=null?filtro1.getMes().name():null);
			parametros.put("CLINICAS", filtro1.getClinicas().toString().replace("[", "").replace("]", "").length() > 0 ? 
					filtro1.getClinicas().toString().replace("[", "").replace("]", "") : "");
			parametros.put("CLINICAS1", filtro1.getClinicas());
			parametros.put("LOGO", SistemaApplication
										.getInstance()
										.getRequest()
										.getSession()
										.getServletContext()
										.getRealPath("/VAADIN/themes/pc/images/facimagem1.png"));
			SistemaApplication
					.getInstance()
					.getMainWindow()
					.open(new ReportUtil("dre1.jasper",
							SistemaApplication.getInstance(), parametros,dre
				).open(), "_blank");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
