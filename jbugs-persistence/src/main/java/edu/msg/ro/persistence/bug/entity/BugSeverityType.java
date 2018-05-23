package edu.msg.ro.persistence.bug.entity;

public enum BugSeverityType {

	CRITICAL("critical"), HIGH("high"), MEDIUM("medium"), LOW("low");

	String type;

	private BugSeverityType(String type) {
		this.type = type;
	}

	public static BugSeverityType fromDisplayString(String displayString) {
		for (BugSeverityType type : BugSeverityType.values())
			if (type.getType().equals(displayString))
				return type;
		return null;
	}

	public String getType() {
		return type;
	}

}
