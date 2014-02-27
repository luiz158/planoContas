package br.com.pc.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

public class MoedaColumnGenerator implements ColumnGenerator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	String format; /* Format string for the Double values. */

	 /**
     * Creates double value column formatter with the given
     * format string.
     */
//    public MoedaColumnGenerator(String format) {
//        this.format = format;
//    }

    /**
     * Generates the cell containing the Double value.
     * The column is irrelevant in this use case.
     */
    public Component generateCell(Table source, Object itemId, Object columnId) {
    	if (itemId!=null && columnId!=null && source.getItem(itemId)!=null){
	    	DecimalFormat nf = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR"))); 
	        Property prop = source.getItem(itemId).getItemProperty(columnId);
	        if (prop.getType().equals(Double.class) && prop.getValue() != null) {
	            return new Label(nf.format(Double.parseDouble( prop.getValue().toString())));
	        }
    	}
        return null;
    }
}

