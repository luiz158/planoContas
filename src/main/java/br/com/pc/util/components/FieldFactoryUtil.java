package br.com.pc.util.components;

import java.util.Locale;
import java.util.ResourceBundle;

import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.FieldFactory;
import br.gov.frameworkdemoiselle.util.Strings;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.MaskedTextField;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;

final public class FieldFactoryUtil {
	
	private FieldFactoryUtil() {

	}
	
	public static HoraField createHoraField(String caption) {
		ResourceBundle bundle = Beans.getReference(ResourceBundle.class);
		String inputPrompt = "HH:mm";
		HoraField field = new HoraField();
		field.setNullRepresentation("");
		setBasicProperties(field, caption);

		if (Strings.isResourceBundleKeyFormat(inputPrompt)) {
			field.setInputPrompt(bundle.getString(Strings.removeBraces(inputPrompt)));
		} else {
			field.setInputPrompt(inputPrompt);
		}

		return field;
	}
	
	private static void setBasicProperties(Field field, String caption) {
		ResourceBundle bundle = Beans.getReference(ResourceBundle.class);
		if (Strings.isResourceBundleKeyFormat(caption)) {
			try {
				field.setCaption(bundle.getString(Strings.removeBraces(caption)));
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			field.setCaption(caption);
		}
	}
	
	public static String getBundleFromReference(String caption){
		ResourceBundle bundle = Beans.getReference(ResourceBundle.class);
		if (Strings.isResourceBundleKeyFormat(caption)) {
			try {
				return bundle.getString(Strings.removeBraces(caption));
			} catch (Exception e) {
				return caption;
			}
		} else {
			return caption;
		}
	}
	
	public static MoedaField createMoedaField(String caption) {
		ResourceBundle bundle = Beans.getReference(ResourceBundle.class);
		String inputPrompt = "0,00";
		MoedaField field = new MoedaField();
		field.setNullRepresentation("");
		setBasicProperties(field, caption);

		if (Strings.isResourceBundleKeyFormat(inputPrompt)) {
			field.setInputPrompt(bundle.getString(Strings.removeBraces(inputPrompt)));
		} else {
			field.setInputPrompt(inputPrompt);
		}
//		Validator validaValor = new RegexpValidator(
//				"^(\\d{1,10}){1}(\\.\\d{3})*((\\,|\\.)(\\d{1}|\\d{2}|\\d{3}))?$", "Deve ser um Valor Válida");
//		field.addValidator(validaValor);
		return field;
	}
	
//	public static BoolTextField createBoolTextField(String caption) {
////		ResourceBundle bundle = Beans.getReference(ResourceBundle.class);
//		
//		BoolTextField field = new BoolTextField(caption);
//		field.getTextField().setNullRepresentation("");
////		setBasicProperties(field.getTextField(), caption);
//
////		if (Strings.isResourceBundleKeyFormat(inputPrompt)) {
////			field.setInputPrompt(bundle.getString(Strings.removeBraces(inputPrompt)));
////		} else {
////			field.setInputPrompt(inputPrompt);
////		}
////		Validator validaValor = new RegexpValidator(
////				"^(\\d{1,10}){1}(\\.\\d{3})*((\\,|\\.)(\\d{1}|\\d{2}|\\d{3}))?$", "Deve ser um Valor Válida");
////		field.addValidator(validaValor);
//		return field;
//	}
	
	public static TextField createTextField(String inputPrompt, String caption) {
		return FieldFactory.createTextField(inputPrompt, caption);
	}
	public static TextField createTextField(String caption) {
		return createTextField(null,caption);
	}
	
	public static TextArea createTextArea(String inputPrompt, String caption, int rows, int columns) {
		return FieldFactory.createTextArea(inputPrompt, caption, rows, columns);
	}
	public static TextArea createTextArea(String caption, int rows, int columns) {
		return FieldFactory.createTextArea(caption, caption, rows, columns);
	}
		
	public static ComboBox createComboBox(String inputPrompt, String caption, String itemCaptionPropertyId) {
		return FieldFactory.createComboBox(inputPrompt, caption, itemCaptionPropertyId);
	}
	public static ComboBox createComboBox(String caption, String itemCaptionPropertyId) {
		return FieldFactory.createComboBox(null, caption, itemCaptionPropertyId);
	}
	
	public static PopupDateField createDateField(String inputPrompt, String caption, String format) {
		return FieldFactory.createDateField(inputPrompt, caption, format);
	}
	public static PopupDateField createDateField(String caption, String format) {
		return FieldFactory.createDateField("dd/mm/aa", caption, format);
	}
	public static PopupDateField createDateField(String caption) {
		return FieldFactory.createDateField("dd/mm/aa", caption, "dd/MM/yy");
	}

	public static CheckBox createCheckBox(String caption) {
		return FieldFactory.createCheckBox(null, caption);
	}

	public static Field createCPFField(String caption) {
		return FieldFactory.createCPFField("000.000.000-00", caption);
	}

	public static Field createCNPJField(String caption) {
		return FieldFactory.createFormattedField("00.000.000/0000-00", caption,"000.000.000-00",true);
	}
	
	public static Field createPhoneField(String caption) {
		return FieldFactory.createPhoneField("0000-0000", caption);
	}
	
	public static Field createFormattedField(String prompt, String caption, final String formato, final boolean direcao) {
		return FieldFactory.createFormattedField(prompt, caption, formato, direcao);
	}
		
	public static MaskedTextField createMaskCPFField(String caption) {
//		ResourceBundle bundle = Beans.getReference(ResourceBundle.class);
		MaskedTextField field = new MaskedTextField(caption,"###.###.###-##");
		field.setNullRepresentation("");
		setBasicProperties(field, caption);

//		if (Strings.isResourceBundleKeyFormat(inputPrompt)) {
//			field.setInputPrompt(bundle.getString(Strings.removeBraces(inputPrompt)));
//		} else {
//			field.setInputPrompt(inputPrompt);
//		}

		return field;
	}
		
	public static MaskedTextField createMaskRGField(String caption) {
		MaskedTextField field = new MaskedTextField(caption,"##.###.###-##");
		field.setNullRepresentation("");
		setBasicProperties(field, caption);

		return field;
	}
		
	public static MaskedTextField createMaskField(String caption, String mask) {
		if (mask.length()==0){
			mask="#";
		}
		MaskedTextField field = new MaskedTextField(caption,mask);
		field.setNullRepresentation("");
		setBasicProperties(field, caption);

		return field;
	}
		
//	public static NumberField createNumeberField(String caption, Character decimalSeparator, Integer scale) {
//		Locale brasilLocale = new Locale("pt","BR"); 
//		NumberField field = new NumberField();
//		field.setCaption(caption);
//		field.setNullRepresentation("");
//		field.setLocale(brasilLocale);
//		field.setDecimalSeparator(decimalSeparator);
//		field.setDecimalPrecision(scale);
//		setBasicProperties(field, caption);
//		return field;
//	}
//	
//	public static NumberField createMoedaNumericField(String caption){
//		NumberField field = createNumeberField(caption,',',2);
//		field.setInputPrompt("0,00");
//		field.setDecimalSeparatorAlwaysShown(true); 
//		field.setMinimumFractionDigits(2);
//		field.setValueIgnoreReadOnly(0d);
//		return field;
//	}
	
	public static TwinColSelect createTwinColSelect(String caption, String itemCaptionPropertyId) {
		TwinColSelect field = new TwinColSelect();
//		field.setNullRepresentation("");
//		field.setLeftColumnCaption(caption);
		
		ResourceBundle bundle = Beans.getReference(ResourceBundle.class);
		if (Strings.isResourceBundleKeyFormat(caption)) {
			field.setLeftColumnCaption(bundle.getString(Strings.removeBraces(caption)));
		} else {
			field.setLeftColumnCaption(caption);
		}
		
		field.setRightColumnCaption("SELECIONADOS");
		if (itemCaptionPropertyId!=null){
			field.setItemCaptionPropertyId(itemCaptionPropertyId);
//			field.setItemCaptionMode
		}
//		setBasicProperties(field, caption);
		return field;
	}
	
	public static ListSelect createListSelect(String caption) {
		ListSelect field = new ListSelect(caption);
//		field.setNullRepresentation("");
		setBasicProperties(field, caption);

		return field;
	}
	
	public static OptionGroup createOptionGroup(String caption, String itemCaptionPropertyId) {
		OptionGroup field = new OptionGroup(caption);
//		field.setNullRepresentation("");
		setBasicProperties(field, caption);
		if (itemCaptionPropertyId!=null){
			field.setItemCaptionPropertyId(itemCaptionPropertyId);
		}
		return field;
	}
	
	
}
