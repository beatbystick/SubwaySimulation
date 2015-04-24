package com.oneway.subway.simulation.model;

/**
 * A wrapper on top of Stop for calculating trips
 * @author Beatbystick
 *
 */
public class TripStop implements Comparable<TripStop> {
	private Stop stop;
	private TripStop prevTripStop;
	private int durationFromStart = Integer.MAX_VALUE;
	
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

	public Integer getDurationFromStart() {
		return durationFromStart;
	}

	public void setDurationFromStart(Integer duration) {
		this.durationFromStart = duration;
	}

	@Override
	public int compareTo(TripStop o) {
		return Integer.compare(durationFromStart, o.durationFromStart);
	}
}
