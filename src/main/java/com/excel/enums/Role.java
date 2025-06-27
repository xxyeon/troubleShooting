package com.excel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
	ROLE_ADMIN("ADMIN"),
	ROLE_USER("USER"),
	ROLE_AUDIT("AUDIT");
	private String value;

	public static Role fromValue(String value) {
		for (Role role : Role.values()) {
			if (role.getValue().equals(value)) {
				return role;
			}
		}
		throw new IllegalArgumentException("Unknown role: " + value);
	}
}
