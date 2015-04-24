package com.oneway.subway.simulation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a stop
 * @author Beatbystick
 *
 */
public class Stop {
    private String name;
    private List<Route> outGoingRoutes;
    
    public Stop(String name) {
    	this.name = name;
    }
    
    public String getName() {
    	return name;
    }
    
    public List<Route> getOutGoingRoutes() {
    	return outGoingRoutes;
    }
    
    public void addOutGoingRoute(Route route) {
    	if (outGoingRoutes == null) {
    		outGoingRoutes = new ArrayList<Route>();
    	}
    	outGoingRoutes.add(route);
    }
    
}
