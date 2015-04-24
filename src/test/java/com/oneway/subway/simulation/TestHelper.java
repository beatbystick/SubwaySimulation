package com.oneway.subway.simulation;

import java.util.List;

import org.junit.Assert;

import com.oneway.subway.simulation.model.Route;
import com.oneway.subway.simulation.model.Stop;

public class TestHelper {

	public static boolean checkAnswer(List<String> answer, List<String> output) {
		Assert.assertEquals(answer.size(), output.size());
		for (int i=0; i<answer.size(); i++) {
			if (!answer.get(i).equals(output.get(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	//Naively checking legal routes 
	public static boolean checkRoute(List<String> output, Stop currentStop, int outputCheckIndex) {
		if (outputCheckIndex >= output.size()) {
			return false;
		}

		String checkStop = output.get(outputCheckIndex);
		List<Route> currentRoutes = currentStop.getOutGoingRoutes();
		if (currentRoutes != null) {
			Stop mapStop = null;
			for (Route r : currentRoutes) {
				mapStop = r.getToStop();
				if (mapStop.getName().equals(checkStop)) {
					if (outputCheckIndex == output.size() -1) {
						return true;
					} else {
						return checkRoute(output, mapStop, outputCheckIndex+1);
					}
				}
			}
		}
		return false;
	}
}
