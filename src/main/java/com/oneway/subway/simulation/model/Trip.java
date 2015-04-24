package com.oneway.subway.simulation.model;

import java.util.List;

/**
 * Represents a trip information from origin stop to destination stop
 * @author Beatbystick
 *
 */
public class Trip {
	private boolean tripFound = false;
	private List<String> stops;
	private int duration;

	public List<String> getStops() {
		return stops;
	}

	public void setStops(List<String> stops) {
		this.stops = stops;
	}
	
	public void setTripFound(boolean found) {
		this.tripFound = found;
	}

	public boolean isTripFound() {
		return tripFound;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
