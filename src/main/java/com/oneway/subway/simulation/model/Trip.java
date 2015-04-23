package com.oneway.subway.simulation.model;

import java.util.List;

public class Trip {
	private boolean tripFound = false;
	private List<String> stops;

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
}
