package br.com.pc.accesscontrol;

import javax.enterprise.inject.Alternative;

@Alternative
public class AuthorizatorImpl {//implements Authorizator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	@Override
	public boolean hasPermission(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return true;
	}

//	@Override
	public boolean hasRole(String arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
