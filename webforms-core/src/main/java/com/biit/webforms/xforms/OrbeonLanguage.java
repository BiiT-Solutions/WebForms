package com.biit.webforms.xforms;

public enum OrbeonLanguage {
	DUTCH("nl"), ENGLISH("en"), SPANISH("es");

	private String abbreviature;

	public String getAbbreviature() {
		return abbreviature;
	}

	private OrbeonLanguage(String abbreviature) {
		this.abbreviature = abbreviature;
	}

	public static OrbeonLanguage get(String abbreviature) {
		for (OrbeonLanguage language : OrbeonLanguage.values()) {
			if (language.getAbbreviature().equals(abbreviature)) {
				return language;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return getAbbreviature();
	}
}
