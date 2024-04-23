package com.biit.webforms.enumerations;

public enum FormWorkStatus {

	DESIGN(1),

	FINAL_DESIGN(2),

	DEVELOPMENT(3),

	TEST(4),

	PRODUCTION(5);

	private int level;

	FormWorkStatus(int level) {
		this.level = level;
	}

	public boolean isMovingForward(FormWorkStatus newStatus) {
		return level < newStatus.level;
	}

	public static FormWorkStatus from(String name) {
		for (FormWorkStatus value : values()) {
			if (value.toString().equals(name)) {
				return value;
			}
		}
		return null;
	}
}
