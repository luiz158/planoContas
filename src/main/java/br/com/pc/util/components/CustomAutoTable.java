package br.com.pc.util.components;

import javax.enterprise.util.AnnotationLiteral;

import org.apache.commons.lang.time.FastDateFormat;

import br.gov.frameworkdemoiselle.annotation.Field;
import br.gov.frameworkdemoiselle.event.ProcessItemSelection;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ResourceBundle;
import br.gov.frameworkdemoiselle.util.Strings;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;

public class CustomAutoTable extends Table {

	private static final long serialVersionUID = 1L;

	private Class<?> beanClass;
	
	private String[] columnOrder = null;
	
	public CustomAutoTable(Class<?> beanClass) {
//		super(beanClass);
//		this.beanClass = beanClass;
		// TODO Auto-generated constructor stub
		super();

		this.beanClass = beanClass;
		
		setImmediate(true);
		setSelectable(true);

		addListener(new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings("serial")
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				Object item = event.getProperty().getValue();
				if (item != null) {
					Beans.getBeanManager().fireEvent(item, new AnnotationLiteral<ProcessItemSelection>() {
					});
				}
			}

		});
	}
	
	/**
	 * If this is informed, always an setContainerDataSource is called the setVisibleColumns is
	 * called with the colunOrder as parameter.
	 * 
	 * @param properties
	 */
	public void setColumnOrder(String[] properties) {
		this.columnOrder = properties;
	}
	
	/**
     * Formats table cell property values. By default the property.toString()
     * and return a empty string for null properties.
     * 
     * @param rowId
     *            the Id of the row (same as item Id).
     * @param colId
     *            the Id of the column.
     * @param property
     *            the Property to be formatted.
     * @return the String representation of property and its value.
     * @since 3.1
     */
    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property property) {
        if (property == null) {
            return "";
        }
        
        if(property.getType().equals(java.util.Date.class)){
        	if (property.getValue()!=null){
        		return getDateFormatter().format((java.util.Date)property.getValue());
        	}
        }
        
        return property.toString();
    }    
    
    private FastDateFormat dateFormatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm",getLocale());  
    
    private FastDateFormat getDateFormatter(){
        if(dateFormatter != null){
            return dateFormatter;
        }
        return null;
//        DateFormat dateFormat = 
//        	  DateFormat.getDateInstance(DateFormat.FULL, ptBR);
//        Locale locale = getParent().getApplication().getLocale();
//        dateFormatter = FastDateFormat.getInstance("EEE d MMM yy", locale);
//        
//        return dateFormatter;
    }

	/**
	 * Adding the order behaviour
	 */
	@Override
	public void setContainerDataSource(Container newDataSource) {		
		super.setContainerDataSource(newDataSource);
		if(!newDataSource.getItemIds().isEmpty()) { 
			if(columnOrder!=null && columnOrder.length>0) {
				if(getVisibleColumns().length > 0)
					setVisibleColumns(columnOrder);
			}
		}
	}
	
//	@Override
//	public void setContainerDataSource(Container newDataSource) {
//		super.setContainerDataSource(newDataSource);
//		if(!newDataSource.getItemIds().isEmpty()) { 
//			if(columnOrder!=null && columnOrder.length>0) {
//				
////				for (String column : columnOrder) {
////					System.out.println("-for------"+column);
////					String[] property = ((String) column).split("\\.");
////					System.out.println("-------"+property[0]);
////					if (property.length>1){
////						try {
////							System.out.println("-if1------"+column);
////							Class<?> beanClassFilho;
////							if(property.length==2){
////								beanClassFilho = (Class<?>)beanClass.getDeclaredField(property[0]).getGenericType(); 
////							}else if(property.length==3){
////								Class<?> beanClassFilho1 = (Class<?>) beanClass.getDeclaredField(property[0]).getGenericType(); 
////								beanClassFilho = (Class<?>) beanClassFilho1.getDeclaredField(property[1]).getGenericType(); 
////							}else{
////								beanClassFilho = (Class<?>)beanClass.getDeclaredField(column).getGenericType(); 
////							}
////							System.out.println("-try------"+column);
////							addContainerProperty(column, beanClassFilho, null);
////							
////						} catch (Exception e) {
////							e.printStackTrace();
////						} 		
////					}
//					
////				}
//				
//				if(getVisibleColumns().length > 0){
//					super.addContainerProperty("pessoa.nomeFantasia", String.class, null);
//					setVisibleColumns(columnOrder);
//				}
//				
//			}
//		}
//	}
    
    @Override
	public String getColumnHeader(Object propertyId) {		
		try {
			String[] property = ((String) propertyId).split("\\.");
			Field field;
			if(property.length==2){
				Class<?> beanClassFilho = (Class<?>) beanClass.getDeclaredField(property[0]).getGenericType(); 
				field = beanClassFilho.getDeclaredField(property[1]).getAnnotation(Field.class);
			}else if(property.length==3){
				Class<?> beanClassFilho1 = (Class<?>) beanClass.getDeclaredField(property[0]).getGenericType(); 
				Class<?> beanClassFilho2 = (Class<?>) beanClassFilho1.getDeclaredField(property[1]).getGenericType(); 
				field = beanClassFilho2.getDeclaredField(property[2]).getAnnotation(Field.class);
			}else{
				field = beanClass.getDeclaredField((String) propertyId).getAnnotation(Field.class);
			}
			if(field!=null && !"".equals(field.label())){
				return resolveProperties(field.label());
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} 		
		
		return super.getColumnHeader(propertyId);
	}
	
	private static String resolveProperties(final String message) {
		ResourceBundle bundle = Beans.getReference(ResourceBundle.class);
		String result = message;
		if (Strings.isResourceBundleKeyFormat(message)) {
			result = bundle.getString(Strings.removeBraces(message));
		}
		return result;
	}
	
	public void montarTabela(String[] properties){
		this.columnOrder = properties;
		for (String column : columnOrder) {
			String[] property = ((String) column).split("\\.");
			if (property.length>0){
				try {
					Class<?> beanClassFilho;
					if(property.length==2){
						Class<?> beanClassFilho1 = beanClass.getDeclaredField(property[0]).getType(); 
//						beanClassFilho = (Class<?>) beanClassFilho1.getDeclaredField(property[1]).getGenericType();
						beanClassFilho = beanClassFilho1.getDeclaredField(property[1]).getType();
					}else if(property.length==3){
						Class<?> beanClassFilho1 = (Class<?>) beanClass.getDeclaredField(property[0]).getGenericType(); 
//						beanClassFilho = (Class<?>) beanClassFilho1.getDeclaredField(property[1]).getGenericType();
						beanClassFilho = beanClassFilho1.getDeclaredField(property[1]).getType();
					}else{
//						beanClassFilho = (Class<?>)beanClass.getDeclaredField(column).getGenericType();
						beanClassFilho = beanClass.getDeclaredField(column).getType();
					}
					System.out.println("Coluna: " + column + " Type: " + beanClassFilho.getName());
					addContainerProperty(column, beanClassFilho, null);
					
				} catch (Exception e) {
					e.printStackTrace();
				} 		
			}
		}
	}

}
