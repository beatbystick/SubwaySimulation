package com.oneway.subway.simulation.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oneway.subway.simulation.planner.LeastStationPlanner;
import com.oneway.subway.simulation.planner.Planner;

public class SubwaySystem {
	private Planner planner = new LeastStationPlanner();
	private Map<String, Stop> stops = new HashMap<String, Stop>();

	public void addTrainLine(List<String> trainStops, String trainName) {
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
				Route r = new Route(trainName, stop);
				prevStop.addOutGoingRoute(r);
				r = new Route(trainName, prevStop);
				stop.addOutGoingRoute(r);
			}
			
			prevStop = stop;
		}
	}
	
	public void setPlannerStrategy(Planner planner) {
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
			return planner.takeTrain(fromStop, toStop);
		}
	}
	
	public Map<String, Stop> getMap() {
		return stops;
	}
	
}
