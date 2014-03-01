package br.com.pc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Table;

public class GeraXls {
	
	private static final long serialVersionUID = 1L;

	private String filePath;
	private Workbook wb;
	private Sheet sheet;
	private Application app;
	
	public GeraXls(String filePath, Table tabela, Application app) throws IOException {
		this.filePath = filePath;
		this.app = app;
		NovaPlanilha();
		GravaTabela(tabela);
		SalvaArquivo();
		
	}
	
	public void NovaPlanilha(){
		 wb = new HSSFWorkbook();
	    //Workbook wb = new XSSFWorkbook();
	    sheet = wb.createSheet();
	}
	public void SalvaArquivo() throws IOException{
		FileOutputStream fileOut = new FileOutputStream(reportPath());
	    wb.write(fileOut);
	    fileOut.close();
	}
	
	public void GravaTabela(Table tabela){
		String[] titulos = tabela.getColumnHeaders();
		Row row = sheet.createRow(0);
		for (int i = 0; i < titulos.length; i++) {
			 row.createCell(i).setCellValue(titulos[i]);
		}
		Collection<?> c = (Collection<?>) tabela.getItemIds();
		int j = 0;
		for (Object object : c) {
			row = sheet.createRow(++j);
			Item item = tabela.getItem(object);
			int k = 0;
			Collection<?> p = (Collection<?>) item.getItemPropertyIds();
			for (Object p2 : p) {
//				System.out.println(item.getItemProperty(p2).getValue().getClass().toString());
				try {
					System.out.println(item.getItemProperty(p2).getValue().getClass().toString());
					if (item.getItemProperty(p2).getValue().getClass().equals(Date.class)
							||item.getItemProperty(p2).getValue().getClass().equals(Timestamp.class)
							||item.getItemProperty(p2).getValue().getClass().equals(java.sql.Date.class)){
						CellStyle styleData = wb.createCellStyle();  
						DataFormat format = wb.createDataFormat();  
						styleData.setDataFormat(format.getFormat("mm/dd/yy HH:mm")); 
						Cell cell = row.createCell(k++);//.setCellValue((Date)item.getItemProperty(p2).getValue());
						cell.setCellStyle(styleData); 
						cell.setCellValue((Date)item.getItemProperty(p2).getValue());
						
					}else if (item.getItemProperty(p2).getValue().getClass().equals(Double.class)){
						row.createCell(k++).setCellValue((Double)item.getItemProperty(p2).getValue());

					}else if (item.getItemProperty(p2).getValue().getClass().equals(BigDecimal.class)){
						row.createCell(k++).setCellValue(((BigDecimal)item.getItemProperty(p2).getValue()).doubleValue());
						
					}else if (item.getItemProperty(p2).getValue().getClass().equals(Boolean.class)){
						row.createCell(k++).setCellValue((Boolean)item.getItemProperty(p2).getValue());
						
					}else if (item.getItemProperty(p2).getValue().getClass().equals(String.class)){
						row.createCell(k++).setCellValue((String)item.getItemProperty(p2).getValue());
						
					}else if (item.getItemProperty(p2).getValue().getClass().equals(Calendar.class)){
						row.createCell(k++).setCellValue((Calendar)item.getItemProperty(p2).getValue());
						
					}else {
						row.createCell(k++).setCellValue(item.getItemProperty(p2).getValue().toString());
					}
					
//					System.out.println(item.getItemProperty(p2).getClass().toString());
//					item.getItemProperty(p2).getClass().cast(item.getItemProperty(p2).getValue());
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
		}
//		Row row = sheet.createRow(0);
//	    row.createCell(0).setCellValue(1.1);
//	    row.createCell(1).setCellValue(new Date());
//	    row.createCell(2).setCellValue(Calendar.getInstance());
//	    row.createCell(3).setCellValue("a string");
//	    row.createCell(4).setCellValue(true);
//	    row.createCell(5).setCellType(HSSFCell.CELL_TYPE_ERROR);
	}

	public FileResource getStream(){
		final FileResource stream = new FileResource(new File(reportPath()),app);
		stream.setCacheTime(1);
		return stream;
	}
	
	private String reportPath() {
		return app.getContext().getBaseDirectory() + File.separator
				+ "jasperreports" + File.separator + filePath;
	}
//	public StreamResource open() throws Exception {
////		StreamResource resource = new StreamResource(source, "arquivo.pdf", application);
////		resource.setMIMEType("application/pdf");
////		resource.setCacheTime(1);
////		return resource;
//		new FileDownloadResource(new File("e:\\historico.xls"), )
//	}
	

}
