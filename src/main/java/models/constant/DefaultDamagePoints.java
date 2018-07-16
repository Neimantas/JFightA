package models.constant;

public enum DefaultDamagePoints {

	HEAD(20), BODY(10), HANDS(5), LEGS(5);

	private int damagePoints;

	private DefaultDamagePoints(int defaultDamagePoints) {
		damagePoints = defaultDamagePoints;
	}

	public int getDamagePoints() {
		return damagePoints;
	}

}
