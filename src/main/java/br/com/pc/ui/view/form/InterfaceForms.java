package br.com.pc.ui.view.form;

public interface InterfaceForms<E> {
	
	public E novo();
	
	public void setBean(E bean);
	
	public E getBean();
	
	public void limpar();
	
	public void excluir();
	
	public void salvar();
	
}
