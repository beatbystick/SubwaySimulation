package com.oneway.subway.simulation.model;

/**
 * Represents route between stops
 * @author Beatbystick
 *
 */
public class Route {
	private String trainName;
	private Stop toStop;
	private int duration;
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	
	public Route(String trainName, Stop toStop) {
		this.toStop = toStop;
		this.trainName = trainName;
	}
	
	public Stop getToStop() {
		return toStop;
	}
	
	public String getTrainName() {
		return trainName;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) {
			return true;
		}
		if((obj == null) || !(obj instanceof Route)) {
			return false;
		}
		Route r = (Route) obj;
		return toStop == r.getToStop() && trainName.equals(r.getTrainName());
	}
	
	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 31 * hash + trainName.hashCode();
		hash = 31 * hash + toStop.getName().hashCode();
		return hash;
	}

}
