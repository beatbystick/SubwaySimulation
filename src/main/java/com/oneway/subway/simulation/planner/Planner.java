package com.oneway.subway.simulation.planner;

import com.oneway.subway.simulation.model.Stop;
import com.oneway.subway.simulation.model.Trip;

public interface Planner {

	public Trip takeTrain(Stop from, Stop to);
}
