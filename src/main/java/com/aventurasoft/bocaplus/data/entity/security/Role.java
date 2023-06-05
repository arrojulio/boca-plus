package com.aventurasoft.bocaplus.data.entity.security;

public class Role {
	public static final String SOCIO = "socio";
	public static final String COMERCIO = "comercio";
	public static final String CLUB = "club";
	// This role implicitly allows access to all views.
	public static final String ADMIN = "admin";

	private Role() {
		// Static methods and fields only
	}

	public static String[] getAllRoles() {
		return new String[] { SOCIO, COMERCIO, CLUB, ADMIN };
	}

}
