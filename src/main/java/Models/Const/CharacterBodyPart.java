package Models.Const;

public enum CharacterBodyPart {

	HEAD("head", 6), HANDS("hands", 4), BODY("body", 5), LEGS("legs", 4);

	private String bodyPartName;
	private int damagePoints;

	private CharacterBodyPart(String characterBodyPart, int defaultDamagePoints) {
		bodyPartName = characterBodyPart;
		damagePoints = defaultDamagePoints;
	}

	public static CharacterBodyPart getByBodyPartName(String characterBodyPartName) {
		for (CharacterBodyPart bodyPart : CharacterBodyPart.values()) {
			if (characterBodyPartName.equals(bodyPart.getBodyPartName())) {
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
