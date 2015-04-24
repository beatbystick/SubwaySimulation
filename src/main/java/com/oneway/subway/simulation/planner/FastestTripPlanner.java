package com.oneway.subway.simulation.planner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

import com.oneway.subway.simulation.model.Route;
import com.oneway.subway.simulation.model.Stop;
import com.oneway.subway.simulation.model.Trip;
import com.oneway.subway.simulation.model.TripStop;

/**
 * This uses Dijkstra's algo 
 * @author Beatbystick
 *
 */
public class FastestTripPlanner implements Planner {


	@Override
	public Trip takeTrain(Stop from, Stop to) {
		LinkedList<String> stops = new LinkedList<String>();
		TripStop stop = plan(from, to);

		Trip trip = new Trip();
		if (stop != null) {
			trip.setDuration(stop.getDurationFromStart());
		}

		while (stop != null) {
			stops.addFirst(stop.getStop().getName());
			stop = stop.getPrevTripStop();
		}

		if (stops.size() > 0) {
			trip.setTripFound(true);
			trip.setStops(stops);
		}

		return trip;
	}
	
	private TripStop plan(Stop from, Stop to) {
		Map<Stop, TripStop> stopStore = new HashMap<Stop, TripStop>();

		TripStop fromTripStop = new TripStop(from);
		fromTripStop.setDurationFromStart(0);

		PriorityQueue<TripStop> stopQueue = new PriorityQueue<TripStop>();
		stopQueue.add(fromTripStop);
		stopStore.put(from, fromTripStop);

		while (!stopQueue.isEmpty()) {
			TripStop currentStop = stopQueue.poll();
			if (currentStop.getStop() == to) {  //Reach destination stop
				return currentStop;
			}

			for (Route r : currentStop.getStop().getOutGoingRoutes()) {
				Stop nextStop = r.getToStop();
				TripStop nextTripStop = stopStore.get(nextStop);
				if (nextTripStop == null) {
					nextTripStop = new TripStop(nextStop);
					stopStore.put(nextStop, nextTripStop);
				}
				int newDuration = currentStop.getDurationFromStart() + r.getDuration();
				if (newDuration < nextTripStop.getDurationFromStart()) {
					stopQueue.remove(nextTripStop);
					nextTripStop.setDurationFromStart(newDuration);
					nextTripStop.setPrevTripStop(currentStop);
					stopQueue.add(nextTripStop);
				}
			}
		}
		
		return null;

	}

}
