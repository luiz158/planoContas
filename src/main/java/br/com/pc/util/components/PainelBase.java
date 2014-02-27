package br.com.pc.util.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class PainelBase extends VerticalLayout {
	
	private Panel p1 = new Panel();
	private VerticalLayout v1 = new VerticalLayout();
	
	public PainelBase(Boolean top){
		super.addComponent(this.p1);
		super.setMargin(top,true,true,true);
		this.p1.addComponent(v1);
		this.v1.setSpacing(true);
//		this.setSpacing(true);
	}
	
	public PainelBase(String nome,Boolean top){
		this(top);
		this.p1.setCaption(nome);
//		super.addComponent(this.p1);
//		this.setSpacing(true);
	}

	@Override
	public void setCaption(String nome){
		this.p1.setCaption(nome);
	}
	
	@Override
    public void addComponent(Component c) {
		this.p1.addComponent(c);
	}
	
	public void setSpacing(Boolean spacing){
		this.v1.setSpacing(spacing);
	}
	
    public void addComponent(VerticalLayout c) {
    	c.setSpacing(true);
		this.v1.addComponent(c);
	}
	
    public void addComponent(HorizontalLayout c) {
    	c.setSpacing(true);
//    	c.setMargin(false, false, true, false);
		this.v1.addComponent(c);
	}
	
    public void addComponent(GridLayout c) {
    	c.setSpacing(true);
		this.v1.addComponent(c);
	}

}
