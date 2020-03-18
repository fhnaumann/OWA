package me.wand555.OWA.Timer;

import org.bukkit.scheduler.BukkitRunnable;

public class CountSecondsPassedTimer extends BukkitRunnable {

	private long timePassed;
	
	public CountSecondsPassedTimer(long timePassed) {
		this.timePassed = timePassed;
	}
	
	@Override
	public void run() {
		this.timePassed += 1;
		System.out.println(this.timePassed);
	}

	/**
	 * @return the remainingTimeToNextDrop
	 */
	public long getTimePassed() {
		return timePassed;
	}

	/**
	 * @param remainingTimeToNextDrop the remainingTimeToNextDrop to set
	 */
	public void setTimePassed(long timePassed) {
		this.timePassed = timePassed;
	}

}
