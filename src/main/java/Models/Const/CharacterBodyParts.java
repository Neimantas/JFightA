package Models.Const;

public enum CharacterBodyParts {

	HEAD("head", 6), HANDS("hands", 4), BODY("body", 5), LEGS("legs", 4);

	private String bodyParts;
	private int damagePoints;

	private CharacterBodyParts(String defaultBodyParts, int defaultDamagePoints) {
		bodyParts = defaultBodyParts;
		damagePoints = defaultDamagePoints;
	}

	public static CharacterBodyParts getByBodyPart(String characterBodyPart) {
		for (CharacterBodyParts bodyPart : CharacterBodyParts.values()) {
			if (characterBodyPart.equals(bodyPart.getBodyParts())) {
				return bodyPart;
			}
		}
		return null;
	}

	public static CharacterBodyParts getByDefaultDamagePoints(int defaultDamagePoints) {
		for (CharacterBodyParts bodyPart : CharacterBodyParts.values()) {
			if (defaultDamagePoints == bodyPart.getDamagePoints()) {
				return bodyPart;
			}
		}
		return null;
	}

	public String getBodyParts() {
		return bodyParts;
	}

	public int getDamagePoints() {
		return damagePoints;
	}

}
