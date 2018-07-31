package models.constant;

public enum DefaultDamagePoints {

	HEAD(20), BODY(10), ARMS(5), LEGS(5);

	private int _damagePoints;

	private DefaultDamagePoints(int defaultDamagePoints) {
		_damagePoints = defaultDamagePoints;
	}

	public int getDamagePoints() {
		return _damagePoints;
	}

}
