package br.com.pc.util;

import java.awt.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;

import com.vaadin.Application;
import com.vaadin.terminal.StreamResource;

/**
 * 
 * @author mariojp
 * 
 *         Classe genérica para geração de relatório
 */

public class ReportUtil {

	private String report;
	@SuppressWarnings("rawtypes")
	private Map parametros;
	private JRDataSource datasource;
	private Application application;
	private StreamResource.StreamSource source = new StreamResource.StreamSource() {
		private static final long serialVersionUID = 1L;
		private final ByteArrayOutputStream os = new ByteArrayOutputStream();

		public InputStream getStream() {

			byte[] b = null;
			try {
				String reportPath = ReportUtil.this.reportPath(report);
				b = JasperRunManager.runReportToPdf(reportPath, parametros,
						datasource);
				os.write(b);
			} catch (JRException ex) {
				ex.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new ByteArrayInputStream(os.toByteArray());
		}
	};

	@SuppressWarnings("rawtypes")
	public ReportUtil(String report, Application application,
			Map parametros, List list) {
		if (list != null) {
			this.datasource = new JRBeanCollectionDataSource(list);
		}else{
			this.datasource =new JREmptyDataSource();
		}
		this.application = application;
		this.parametros = parametros;
		this.report = report;
	}

	private String reportPath(String reportFile) {
		return application.getContext().getBaseDirectory() + File.separator
				+ "jasperreports" + File.separator + reportFile;
	}


	@SuppressWarnings("rawtypes")
	public void openReport(String titulo, Map parametros, List list)
			throws JRException {
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		JasperPrint print = JasperFillManager.fillReport(
				this.reportPath(report), parametros, jrDataSource);
		viewReportFrame(titulo, print);
	}

	private void viewReportFrame(String titulo, JasperPrint print) {
		JRViewer viewer = new JRViewer(print);
		JFrame frameRelatorio = new JFrame(titulo);
		frameRelatorio.add(viewer, BorderLayout.CENTER);
		frameRelatorio.setSize(500, 500);
		frameRelatorio.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frameRelatorio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameRelatorio.setVisible(true);
	}


	public StreamResource open() throws Exception {
		StreamResource resource = new StreamResource(source, "arquivo.pdf", application);
		resource.setMIMEType("application/pdf");
		resource.setCacheTime(1);
		return resource;
	}

}
