package com.oneway.subway.simulation.model;

public class TripStop {
	private Stop stop;
	private TripStop prevTripStop;
	
	public TripStop(Stop stop) {
		this.stop = stop;
	}

	public Stop getStop() {
		return stop;
	}

	public TripStop getPrevTripStop() {
		return prevTripStop;
	}

	public void setPrevTripStop(TripStop prevTripStop) {
		this.prevTripStop = prevTripStop;
	}
}
