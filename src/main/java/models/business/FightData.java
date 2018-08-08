package models.business;

public class FightData {
	
	public int playerAUserId = -1;
	public int playerBUserId = -1;
	public int fightId = -1;
	public int round = -1;
	public int playerAHealth = -1;
	public int playerBHealth = -1;
	
//	public Integer getPlayerAUserId() {
//		return Integer.parseInt(_playerAUserIdString);
//	}
//	public Integer getFightId() {
//		return Integer.parseInt(fightId);
//	}
//	public Integer getRound() {
//		return Integer.parseInt(round);
//	}
//	public Integer getPlayerAHealth() {
//		return Integer.parseInt(playerAHealth);
//	}
	//Helper methods
	public void setPlayerAUserIdString(String playerAUserIdString) {
		playerAUserId = Integer.parseInt(playerAUserIdString);
	}
	public void setFightIdString(String fightIdString) {
		fightId = Integer.parseInt(fightIdString);
	}
	public void setRoundString(String roundString) {
		round = Integer.parseInt(roundString);
	}
	public void setPlayerAHealthString(String playerAHealthString) {
		playerAHealth = Integer.parseInt(playerAHealthString);
	}
	
	public boolean isDataSet() {
		if(playerAUserId == -1 || fightId == -1 
				|| round == -1 || playerAHealth == -1) {
//			System.out.println("Aid " + playerAHealth + " fightId " + fightId + " round " + round + " playerAHealth " + playerAHealth);
			return false;
		}
		return true;
	}
	
}
