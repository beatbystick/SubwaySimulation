package com.oneway.subway.simulation.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oneway.subway.simulation.planner.LeastStationPlanner;
import com.oneway.subway.simulation.planner.Planner;

/**
 * Represents the subway system, it holds a map structure 
 * @author Beatbystick
 *
 */
public class SubwaySystem {
	private Planner leastStationPlanner = new LeastStationPlanner();
	private Planner planner = leastStationPlanner;
	private Map<String, Stop> stops = new HashMap<String, Stop>();
	boolean hasTimeBetweenStop = true;

	public void addTrainLine(List<String> trainStops, String trainName, List<TimeBetweenStop> durations) {
		if (trainStops == null || trainStops.size() <= 1) {
			throw new IllegalArgumentException("Train shall have at least 2 stops.");
		}
		if (trainName == null || trainName.length() == 0) {
			throw new IllegalArgumentException("Train has no name");
		}
		
		Stop prevStop = null;
		for (String s : trainStops) {
			Stop stop = stops.get(s);
			if (stop == null) {
				stop = new Stop(s);
				stops.put(s, stop);
			}
			
			//Add two directional paths
			if (prevStop != null) {
				int duration = findDuration(durations, prevStop.getName(), stop.getName());
				Route r = new Route(trainName, stop);
				if (duration != -1) {
					r.setDuration(duration);
				} else {
					//If we add a new route but now time, we use basic search
					hasTimeBetweenStop = false;
				}
				prevStop.addOutGoingRoute(r);
				r = new Route(trainName, prevStop);
				if (duration != -1) {
					r.setDuration(duration);
				}
				stop.addOutGoingRoute(r);
			}
			
			prevStop = stop;
		}
		
	}
	
	private int findDuration(List<TimeBetweenStop> durations, String from, String to) {
		if (durations == null || durations.size() == 0) {
			return -1;
		}
		for (TimeBetweenStop duration : durations) {
			if (duration.getFrom().equals(from) && duration.getTo().equals(to)) {
				return duration.getDuration();
			}
		}
		
		return -1;
	}
	
	public void addTrainLine(List<String> trainStops, String trainName) {
		addTrainLine(trainStops, trainName, null);
	}

	public void setPlanner(Planner planner) {
		this.planner = planner;
	}

	public Trip takeTrain(String from, String to) {
		if (from == null || from.length() == 0 || to == null || to.length() == 0) {
			throw new IllegalArgumentException();
		}
		
		Stop fromStop = stops.get(from);
		Stop toStop = stops.get(to);
		
		if (fromStop == null || toStop == null || fromStop == toStop) {
			return new Trip();
		} else {
			if (hasTimeBetweenStop) {
				return planner.takeTrain(fromStop, toStop);
			} else {
				return leastStationPlanner.takeTrain(fromStop, toStop);
			}
		}
	}
	
	public Map<String, Stop> getMap() {
		return stops;
	}
	
}
