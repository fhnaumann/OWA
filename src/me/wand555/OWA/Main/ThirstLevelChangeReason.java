package me.wand555.OWA.Main;

public enum ThirstLevelChangeReason {
	WATER_BOTTLE(1), STILL_WATER(1), TIME(1);

	private final int amount;
	
	private ThirstLevelChangeReason(int amount) {
		this.amount = amount;
	} 
	
	public int getAmount() {
		return this.amount;
	}
}
