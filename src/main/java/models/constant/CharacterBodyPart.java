package models.constant;

public enum CharacterBodyPart {

	HEAD(1, "Head", 6), BODY(2, "Body", 5), HANDS(3, "Hands", 4), LEGS(4, "Legs", 4);

	private int bodyPartId;
	private String bodyPartName;
	private int damagePoints;

	private CharacterBodyPart(int characterBodyPartId, String characterBodyPartName, int defaultDamagePoints) {
		bodyPartId = characterBodyPartId;
		bodyPartName = characterBodyPartName;
		damagePoints = defaultDamagePoints;
	}

	public static CharacterBodyPart getByBodyPartId(int characterBodyPartId) {
		for (CharacterBodyPart bodyPart : CharacterBodyPart.values()) {
			if (characterBodyPartId == bodyPart.getBodyPartId()) {
				return bodyPart;
			}
		}
		return null;
	}

	public static CharacterBodyPart getByBodyPartName(String characterBodyPartName) {
		for (CharacterBodyPart bodyPart : CharacterBodyPart.values()) {
			if (characterBodyPartName.equalsIgnoreCase(bodyPart.getBodyPartName())) {
				return bodyPart;
			}
		}
		return null;
	}

	public int getBodyPartId() {
		return bodyPartId;
	}

	public String getBodyPartName() {
		return bodyPartName;
	}

	public int getDamagePoints() {
		return damagePoints;
	}

}
