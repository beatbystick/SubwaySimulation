package com.oneway.subway.simulation.planner;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.oneway.subway.simulation.model.Route;
import com.oneway.subway.simulation.model.Stop;
import com.oneway.subway.simulation.model.Trip;
import com.oneway.subway.simulation.model.TripStop;

/**
 * This uses a simple BFS search
 * @author Beatbystick
 *
 */
public class LeastStationPlanner implements Planner {

	@Override
	public Trip takeTrain(Stop from, Stop to) {

		LinkedList<String> stops = new LinkedList<String>();
		TripStop stop = plan(from, to);
		
		while (stop != null) {
			stops.addFirst(stop.getStop().getName());
			stop = stop.getPrevTripStop();
		}
		
		Trip trip = new Trip();
		if (stops.size() > 0) {
			trip.setTripFound(true);
			trip.setStops(stops);
		}
		
		return trip;
	}

	//BFS to return least stations
	private TripStop plan(Stop fromStop, Stop toStop) {
		Queue<TripStop> queue = new LinkedList<TripStop>();
		Set<String> visited = new HashSet<String>();
		
		queue.offer(new TripStop(fromStop));
		visited.add(fromStop.getName());

		while (!queue.isEmpty()) {
			TripStop current = queue.poll();
			
			for (Route r : current.getStop().getOutGoingRoutes()) {
				Stop routeStop = r.getToStop();
				if (!visited.contains(routeStop.getName())) {
					if (r.getToStop() == toStop) {
						TripStop last = new TripStop(routeStop);
						last.setPrevTripStop(current);
						return last;
					}
				
					TripStop newStop = new TripStop(routeStop);
					newStop.setPrevTripStop(current);

					visited.add(routeStop.getName());
					queue.offer(newStop);
				}
			}
		}

		return null;
	}

}
