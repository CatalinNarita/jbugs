package edu.msg.ro.persistence.bug.entity;

public enum BugStatusType {

	NEW("new", "nou"), IN_PROGRESS("in_progress", "in_progres"), FIXED("fixed","fixat"), CLOSED("closed","inchis"), REJECTED("rejected","respins"), INFO_NEEDED("info_needed","detalii_necesare");

	String type_en;
	String type_ro;

	private BugStatusType(String type_en, String type_ro) {
		this.type_en = type_en;
		this.type_ro = type_ro;
	}
	
	public static BugStatusType fromDisplayString(String displayString) {
		for (BugStatusType type : BugStatusType.values())
			if (type.getType_en().equals(displayString) || type.getType_ro().equals(displayString))
				return type;
		return null;
	}

	public String getType_en() {
		return type_en;
	}

	public String getType_ro() {
		return type_ro;
	}


}
