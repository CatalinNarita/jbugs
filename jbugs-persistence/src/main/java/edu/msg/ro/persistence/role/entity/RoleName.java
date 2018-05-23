package edu.msg.ro.persistence.role.entity;

public enum RoleName {

	/**
	 * Administrator
	 */

	ADM("administrator"),

	/**
	 * Project Manager
	 */

	PM("project manager"),

	/**
	 * Test Manager
	 */

	TM("test manager"),

	/**
	 * Developer
	 */

	DEV("developer"),

	/**
	 * Tester
	 */

	TEST("tester");

	String role;
	

	private RoleName(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public static RoleName[] getRoleNames() {
		return RoleName.values();
	}

	public static RoleName fromDisplayString(String displayString) {
		for (RoleName rn : RoleName.values())
			if (rn.getRole().equals(displayString))
				return rn;
		return null;
	}
	
	
}
