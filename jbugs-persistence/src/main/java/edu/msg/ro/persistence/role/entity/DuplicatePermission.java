package edu.msg.ro.persistence.role.entity;

public class DuplicatePermission extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicatePermission() {
		super();
	}

	public DuplicatePermission(String mes) {
		super(mes);
	}

}
