package com.oneway.subway.simulation.model;

/**
 * Represents travel duration between stops.
 * This is used to specify duration when adding stops.
 * @author Beatbystick
 *
 */
public class TimeBetweenStop {
	private String from;
	private String to;
	private int duration;
	
	public TimeBetweenStop(String from, String to, int duration) {
		this.from = from;
		this.to = to;
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public String getTo() {
		return to;
	}

	public String getFrom() {
		return from;
	}

}
