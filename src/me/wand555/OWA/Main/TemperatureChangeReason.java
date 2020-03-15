package me.wand555.OWA.Main;

public enum TemperatureChangeReason {
	SUN(1), FIRE(1), LAVA(1), MOON(1), ICE(1), COLD_WATER(1);

	private final int amount;
	
	private TemperatureChangeReason(int amount) {
		this.amount = amount;
	}
	
	public int getAmount() {
		return this.amount;
	}
}
