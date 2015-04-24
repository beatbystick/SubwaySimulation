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
import com.oneway.subway.simulation.model.TimeBetweenStop;
import com.oneway.subway.simulation.model.Trip;
import com.oneway.subway.simulation.planner.FastestTripPlanner;

public class FastestTripPlannerTest  
{
	SubwaySystem subwaySystem = new SubwaySystem();
	
	@Before
	public void setup() {
		subwaySystem = new SubwaySystem();
		subwaySystem.setPlanner(new FastestTripPlanner());
		List<String> train1 = new ArrayList<String>();
		train1.add("1");
		train1.add("2");
		train1.add("3");
		train1.add("4");
		List<TimeBetweenStop> TimeBetweenStops = new ArrayList<TimeBetweenStop>();
		TimeBetweenStop duration = new TimeBetweenStop("1", "2", 2);
		TimeBetweenStops.add(duration);
		duration = new TimeBetweenStop("2", "3", 3);
		TimeBetweenStops.add(duration);
		duration = new TimeBetweenStop("3", "4", 4);
		TimeBetweenStops.add(duration);
		
		subwaySystem.addTrainLine(train1, "A", TimeBetweenStops);
		Map<String, Stop> subwayMap = subwaySystem.getMap();
		Assert.assertEquals(4, subwayMap.size());

		//Test neighbors
		Stop s1 = subwayMap.get("1");
		Assert.assertEquals("1", s1.getName());
		List<Route> s1Routes = s1.getOutGoingRoutes();
		Assert.assertEquals(1, s1Routes.size());
		Stop s2 = subwayMap.get("2");
		Assert.assertEquals("2", s2.getName());
		List<Route> s2Routes = s2.getOutGoingRoutes();
		Assert.assertEquals(2, s2Routes.size());
		Stop s3 = subwayMap.get("3");
		Assert.assertEquals("3", s3.getName());
		List<Route> s3Routes = s3.getOutGoingRoutes();
		Assert.assertEquals(2, s3Routes.size());
		Stop s4 = subwayMap.get("4");
		Assert.assertEquals("4", s4.getName());
		List<Route> s4Routes = s4.getOutGoingRoutes();
		Assert.assertEquals(1, s4Routes.size());

		//Test connections
		Assert.assertTrue(s1Routes.contains(new Route("A", s2)));
		Assert.assertTrue(s2Routes.contains(new Route("A", s1)));
		Assert.assertTrue(s2Routes.contains(new Route("A", s3)));
		Assert.assertTrue(s3Routes.contains(new Route("A", s2)));
		Assert.assertTrue(s3Routes.contains(new Route("A", s4)));
		Assert.assertTrue(s4Routes.contains(new Route("A", s3)));
		
		Assert.assertTrue(s1Routes.get(0).getDuration() == 2);
		for (Route r : s2Routes) {
			if (r.getToStop() == s1) {
				Assert.assertTrue(r.getDuration() == 2);
			} else if (r.getToStop() == s3){
				Assert.assertTrue(r.getDuration() == 3);
			} else {
				Assert.fail();
			}
		}
		for (Route r : s3Routes) {
			if (r.getToStop() == s2) {
				Assert.assertTrue(r.getDuration() == 3);
			} else if (r.getToStop() == s4){
				Assert.assertTrue(r.getDuration() == 4);
			} else {
				Assert.fail();
			}
		}
		Assert.assertTrue(s4Routes.get(0).getDuration() == 4);
		
	}
	
	@Test
	public void simpleTest() {
		//Sanity
		List<String> answer = new ArrayList<String>();
		answer.add("1");
		answer.add("2");
		answer.add("3");
		answer.add("4");
		Trip trip = subwaySystem.takeTrain("1", "4");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertEquals(9, trip.getDuration());
		Assert.assertTrue(TestHelper.checkAnswer(answer, trip.getStops()));

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
		Assert.assertEquals(2, trip.getDuration());
		Assert.assertTrue(TestHelper.checkAnswer(answer, trip.getStops()));

		//Sanity
		answer = new ArrayList<String>();
		answer.add("2");
		answer.add("3");
		answer.add("4");
		trip = subwaySystem.takeTrain("2", "4");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertEquals(7, trip.getDuration());
		Assert.assertTrue(TestHelper.checkAnswer(answer, trip.getStops()));

		//New Train, from and to on different train stops, no trip can be found
		List<String> newTrain = new ArrayList<String>();
		newTrain.add("5");
		newTrain.add("6");
		subwaySystem.addTrainLine(newTrain, "B");

		trip = subwaySystem.takeTrain("5", "3");
		Assert.assertFalse(trip.isTripFound());
	}

	@Test
	public void circleLeastStationTest() {
		//Add a new train, less stops but longer time
		List<String> newTrain = new ArrayList<String>();
		newTrain.add("1");
		newTrain.add("4");
		List<TimeBetweenStop> timeBetweenStops = new ArrayList<TimeBetweenStop>();
		TimeBetweenStop timeBetweenStop = new TimeBetweenStop("1", "4", 20);
		timeBetweenStops.add(timeBetweenStop);
		subwaySystem.addTrainLine(newTrain, "B", timeBetweenStops);
		
		List<String> answer = new ArrayList<String>();
		answer.add("1");
		answer.add("2");
		answer.add("3");
		answer.add("4");
		Trip trip = subwaySystem.takeTrain("1", "4");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertEquals(9, trip.getDuration());
		Assert.assertTrue(TestHelper.checkAnswer(answer, trip.getStops()));

		//Add a new train, least time
		newTrain = new ArrayList<String>();
		newTrain.add("1");
		newTrain.add("4");
		timeBetweenStops = new ArrayList<TimeBetweenStop>();
		timeBetweenStop = new TimeBetweenStop("1", "4", 8);
		timeBetweenStops.add(timeBetweenStop);
		subwaySystem.addTrainLine(newTrain, "C", timeBetweenStops);

		answer = new ArrayList<String>();
		answer.add("1");
		answer.add("4");
		trip = subwaySystem.takeTrain("1", "4");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertEquals(8, trip.getDuration());
		Assert.assertTrue(TestHelper.checkAnswer(answer, trip.getStops()));

		//Add a new train, more stop but even less time
		newTrain = new ArrayList<String>();
		newTrain.add("2");
		newTrain.add("5");
		newTrain.add("6");
		newTrain.add("4");
		timeBetweenStops = new ArrayList<TimeBetweenStop>();
		timeBetweenStop = new TimeBetweenStop("2", "5", 2);
		timeBetweenStops.add(timeBetweenStop);
		timeBetweenStop = new TimeBetweenStop("5", "6", 1);
		timeBetweenStops.add(timeBetweenStop);
		timeBetweenStop = new TimeBetweenStop("6", "4", 2);
		timeBetweenStops.add(timeBetweenStop);
		subwaySystem.addTrainLine(newTrain, "D", timeBetweenStops);

		answer = new ArrayList<String>();
		answer.add("1");
		answer.add("2");
		answer.add("5");
		answer.add("6");
		answer.add("4");
		trip = subwaySystem.takeTrain("1", "4");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertEquals(7, trip.getDuration());
		Assert.assertTrue(TestHelper.checkAnswer(answer, trip.getStops()));

		//Add a new train, less time than above
		newTrain = new ArrayList<String>();
		newTrain.add("7");
		newTrain.add("2");
		newTrain.add("3");
		newTrain.add("4");
		timeBetweenStops = new ArrayList<TimeBetweenStop>();
		timeBetweenStop = new TimeBetweenStop("7", "2", 1);
		timeBetweenStops.add(timeBetweenStop);
		timeBetweenStop = new TimeBetweenStop("2", "3", 3);
		timeBetweenStops.add(timeBetweenStop);
		timeBetweenStop = new TimeBetweenStop("3", "4", 1);
		timeBetweenStops.add(timeBetweenStop);
		subwaySystem.addTrainLine(newTrain, "E", timeBetweenStops);

		answer = new ArrayList<String>();
		answer.add("1");
		answer.add("2");
		answer.add("3");
		answer.add("4");
		trip = subwaySystem.takeTrain("1", "4");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertEquals(6, trip.getDuration());
		Assert.assertTrue(TestHelper.checkAnswer(answer, trip.getStops()));
	}

	@Test
	public void fallbackBasicTest() {
		//Add a new train, less stops but longer time
		List<String> newTrain = new ArrayList<String>();
		newTrain.add("1");
		newTrain.add("4");
		List<TimeBetweenStop> timeBetweenStops = new ArrayList<TimeBetweenStop>();
		TimeBetweenStop timeBetweenStop = new TimeBetweenStop("1", "4", 20);
		timeBetweenStops.add(timeBetweenStop);
		subwaySystem.addTrainLine(newTrain, "B", timeBetweenStops);
		
		List<String> answer = new ArrayList<String>();
		answer.add("1");
		answer.add("2");
		answer.add("3");
		answer.add("4");
		Trip trip = subwaySystem.takeTrain("1", "4");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertEquals(9, trip.getDuration());
		Assert.assertTrue(TestHelper.checkAnswer(answer, trip.getStops()));

		//Add a new train, no time info, should use basic
		newTrain = new ArrayList<String>();
		newTrain.add("1");
		newTrain.add("4");
		subwaySystem.addTrainLine(newTrain, "C");
		
		answer = new ArrayList<String>();
		answer.add("1");
		answer.add("4");
		trip = subwaySystem.takeTrain("1", "4");
		Assert.assertTrue(trip.isTripFound());
		Assert.assertEquals(2, trip.getStops().size());
		Assert.assertTrue(TestHelper.checkRoute(trip.getStops(), subwaySystem.getMap().get("1"), 1));
	}

}
