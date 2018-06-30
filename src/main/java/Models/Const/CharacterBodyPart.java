package Models.Const;

public enum CharacterBodyPart {

	HEAD("Head", 6), HANDS("Hands", 4), BODY("Body", 5), LEGS("Legs", 4);

	private String bodyPartName;
	private int damagePoints;

	private CharacterBodyPart(String characterBodyPart, int defaultDamagePoints) {
		bodyPartName = characterBodyPart;
		damagePoints = defaultDamagePoints;
	}

	public static CharacterBodyPart getByBodyPartName(String characterBodyPartName) {
		for (CharacterBodyPart bodyPart : CharacterBodyPart.values()) {
			if (characterBodyPartName.equalsIgnoreCase(bodyPart.getBodyPartName())) {
				return bodyPart;
			}
		}
		return null;
	}

	public static CharacterBodyPart getByDefaultDamagePoints(int defaultDamagePoints) {
		for (CharacterBodyPart bodyPart : CharacterBodyPart.values()) {
			if (defaultDamagePoints == bodyPart.getDamagePoints()) {
				return bodyPart;
			}
		}
		return null;
	}

	public String getBodyPartName() {
		return bodyPartName;
	}

	public int getDamagePoints() {
		return damagePoints;
	}

}
