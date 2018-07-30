package models.constant;

public enum Errors {
	
	OpponentIsMissing("Cannot find opponent");
	
	public String message;

	Errors(String msg) {
		message = msg;
	}
}
