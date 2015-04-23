package com.oneway.subway.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.oneway.subway.simulation.model.Route;
import com.oneway.subway.simulation.model.Stop;
import com.oneway.subway.simulation.model.SubwaySystem;
import com.oneway.subway.simulation.model.Trip;

public class LeastStationPlannerTest  
{
	SubwaySystem subwaySystem = new SubwaySystem();
	
	@Before
	public void setup() {
		subwaySystem = new SubwaySystem();
		List<String> train1 = new ArrayList<String>();
		train1.add("1");
		train1.add("2");
		train1.add("3");
		
		subwaySystem.addTrainLine(train1, "A");
		Map<String, Stop> subwayMap = subwaySystem.getMap();
		Assert.assertEquals(3, subwayMap.size());

		//Test neighbors
		Stop s1 = subwayMap.get("1");
		Assert.assertEquals("1", s1.getName());
		Assert.assertEquals(1, s1.getOutGoingRoutes().size());
		Stop s2 = subwayMap.get("2");
		Assert.assertEquals("2", s2.getName());
		Assert.assertEquals(2, s2.getOutGoingRoutes().size());
		Stop s3 = subwayMap.get("3");
		Assert.assertEquals("3", s3.getName());
		Assert.assertEquals(1, s3.getOutGoingRoutes().size());

		//Test connections
		Assert.assertTrue(s1.getOutGoingRoutes().contains(new Route("A", s2)));
		Assert.assertTrue(s2.getOutGoingRoutes().contains(new Route("A", s1)));
		Assert.assertTrue(s2.getOutGoingRoutes().contains(new Route("A", s3)));
		Assert.assertTrue(s3.getOutGoingRoutes().contains(new Route("A", s2)));
	}
	
	@Test
	public void duplicateAddTrainTest() {
		//Duplicate routes different train
		List<String> train2 = new ArrayList<String>();
		train2.add("0");
		train2.add("2");
		train2.add("3");
		train2.add("4");
		
		subwaySystem.addTrainLine(train2, "B");
		Map<String, Stop> subwayMap = subwaySystem.getMap();
		Assert.assertEquals(5, subwayMap.size());

		Stop s0 = subwayMap.get("0");
		Stop s1 = subwayMap.get("1");
		Stop s2 = subwayMap.get("2");
		Stop s3 = subwayMap.get("3");
		Stop s4 = subwayMap.get("4");

		//Test neighbors count
		Assert.assertEquals("0", s0.getName());
		Assert.assertEquals(1, s0.getOutGoingRoutes().size());
		Assert.assertEquals("1", s1.getName());
		Assert.assertEquals(1, s1.getOutGoingRoutes().size());
		Assert.assertEquals("2", s2.getName());
		Assert.assertEquals(4, s2.getOutGoingRoutes().size());
		Assert.assertEquals("3", s3.getName());
		Assert.assertEquals(3, s3.getOutGoingRoutes().size());
		Assert.assertEquals("4", s4.getName());
		Assert.assertEquals(1, s4.getOutGoingRoutes().size());

		//Test connections
		Assert.assertTrue(s0.getOutGoingRoutes().contains(new Route("B", s2)));

		Assert.assertTrue(s1.getOutGoingRoutes().contains(new Route("A", s2)));

		Assert.assertTrue(s2.getOutGoingRoutes().contains(new Route("A", s1)));
		Assert.assertTrue(s2.getOutGoingRoutes().contains(new Route("A", s3)));
		Assert.assertTrue(s2.getOutGoingRoutes().contains(new Route("B", s0)));
		Assert.assertTrue(s2.getOutGoingRoutes().contains(new Route("B", s3)));

		Assert.assertTrue(s3.getOutGoingRoutes().contains(new Route("A", s2)));
		Assert.assertTrue(s3.getOutGoingRoutes().contains(new Route("B", s2)));
		Assert.assertTrue(s3.getOutGoingRoutes().contains(new Route("B", s4)));

		Assert.assertTrue(s4.getOutGoingRoutes().contains(new Route("B", s3)));
	}
	
	@Test
	public void simpleLeastStationTest() {
		List<String> answer = new ArrayList<String>();
		answer.add("1");
		answer.add("2");
		answer.add("3");
		Trip trip = subwaySystem.takeTrain("1", "3");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertTrue(checkAnswer(answer, trip.getStops()));

		//No movement
		trip = subwaySystem.takeTrain("1", "1");
		Assert.assertFalse(trip.isTripFound());

		//Unknown station
		trip = subwaySystem.takeTrain("2", "999");
		Assert.assertFalse(trip.isTripFound());

		answer = new ArrayList<String>();
		answer.add("1");
		answer.add("2");
		trip = subwaySystem.takeTrain("1", "2");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertTrue(checkAnswer(answer, trip.getStops()));

		answer = new ArrayList<String>();
		answer.add("2");
		answer.add("3");
		trip = subwaySystem.takeTrain("2", "3");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertTrue(checkAnswer(answer, trip.getStops()));

		List<String> newTrain = new ArrayList<String>();
		newTrain.add("5");
		newTrain.add("6");
		subwaySystem.addTrainLine(newTrain, "B");

		trip = subwaySystem.takeTrain("5", "3");
		Assert.assertFalse(trip.isTripFound());
	}

	@Test
	public void circleLeastStationTest() {
		List<String> newTrain = new ArrayList<String>();
		newTrain.add("1");
		newTrain.add("4");
		newTrain.add("5");
		newTrain.add("3");
		subwaySystem.addTrainLine(newTrain, "B");
		
		List<String> answer = new ArrayList<String>();
		answer.add("1");
		answer.add("2");
		answer.add("3");
		Trip trip = subwaySystem.takeTrain("1", "3");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertTrue(checkAnswer(answer, trip.getStops()));
	}

	@Test
	public void dynamicCircleLeastStationTest() {
		List<String> newTrain = new ArrayList<String>();
		newTrain.add("3");
		newTrain.add("4");
		newTrain.add("1");
		subwaySystem.addTrainLine(newTrain, "B");
		
		List<String> answer = new ArrayList<String>();
		answer.add("1");
		answer.add("4");
		Trip trip = subwaySystem.takeTrain("1", "4");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertTrue(checkAnswer(answer, trip.getStops()));

		trip = subwaySystem.takeTrain("2", "4");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertEquals(3, trip.getStops().size());
		Assert.assertTrue(checkRoute(trip.getStops(), subwaySystem.getMap().get("2"), 1));

		//New shortcut
		newTrain = new ArrayList<String>();
		newTrain.add("2");
		newTrain.add("4");
		subwaySystem.addTrainLine(newTrain, "C");

		trip = subwaySystem.takeTrain("2", "4");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertEquals(2, trip.getStops().size());
		Assert.assertTrue(checkRoute(trip.getStops(), subwaySystem.getMap().get("2"), 1));

	}
	
	private boolean checkAnswer(List<String> answer, List<String> output) {
		Assert.assertEquals(answer.size(), output.size());
		for (int i=0; i<answer.size(); i++) {
			if (!answer.get(i).equals(output.get(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	//Naively checking legal routes 
	private boolean checkRoute(List<String> output, Stop currentStop, int outputCheckIndex) {
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
