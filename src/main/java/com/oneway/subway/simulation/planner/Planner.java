package com.oneway.subway.simulation.planner;

import com.oneway.subway.simulation.model.Stop;
import com.oneway.subway.simulation.model.Trip;

/**
 * Interface for different trip search
 * @author Beatbystick
 *
 */
public interface Planner {

	public Trip takeTrain(Stop from, Stop to);
}
