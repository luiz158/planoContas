package br.com.pc.ui.view.template;

import com.vaadin.ui.Panel;

public interface MyBasicFunctions<E> {
	/**
	 * Metodo para retornar um panel com layout montado com os fields
	 * @return
	 */
	public Panel getPanel();
	
	/**
	 * Metodo para efetuar a limpeza dos fields
	 */
	public void limpar();
	/**
	 * Metodo para preencher os fields baseado em um bean
	 * @param subBean
	 */
	public void atualizaFields(E subBean);
	/**
	 * Metodo para atualizar um bean baseado nos fields
	 * @param bean
	 */
	public void atualizaBean(E bean);
	
}
