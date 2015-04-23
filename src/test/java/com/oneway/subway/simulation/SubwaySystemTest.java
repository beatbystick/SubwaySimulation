package com.oneway.subway.simulation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.oneway.subway.simulation.model.SubwaySystem;
import com.oneway.subway.simulation.model.Trip;

public class SubwaySystemTest 
{
	SubwaySystem subwaySystem = new SubwaySystem();

	@Test(expected=IllegalArgumentException.class)
	public void addTrainLineInvalidStationsTest() {
		
		List<String> stations = new ArrayList<String>();
		stations.add("1");
		
		subwaySystem.addTrainLine(stations, "1");
	}

	@Test(expected=IllegalArgumentException.class)
	public void addTrainLineInvalidTrainNameTest() {
		
		List<String> stations = new ArrayList<String>();
		stations.add("1");
		stations.add("2");
		
		subwaySystem.addTrainLine(stations, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void takeTrainInvalidStopsTest() {
		subwaySystem.takeTrain(null, null);
	}

	@Test
	public void takeTrainUnkownStopsTest() {
		Trip trip = subwaySystem.takeTrain("123", "456");
		Assert.assertFalse(trip.isTripFound());
	}

	@Test
	public void takeTrainSameStopTest() {
		List<String> stations = new ArrayList<String>();
		stations.add("1");
		stations.add("2");
		
		subwaySystem.addTrainLine(stations, "A");
		Trip trip = subwaySystem.takeTrain("1", "1");
		Assert.assertFalse(trip.isTripFound());
	}
}
