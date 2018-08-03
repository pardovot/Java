package com.solar.system.model;

import java.util.LinkedList;
import java.util.List;

public class AllPlanets {
	
	private List<Planet> planets = new LinkedList<>();
	
	public void FF() {
		for(Planet planet : planets) {
			planet.setAxisRotationVelocity(planet.getAxisRotationVelocity()*1.1f);
			planet.setOrbitVelocity(planet.getOrbitVelocity()*1.1f);
		}
	}
	
	public void SM() {
		for(Planet planet : planets) {
			planet.setAxisRotationVelocity(planet.getAxisRotationVelocity()/1.1f);
			planet.setOrbitVelocity(planet.getOrbitVelocity()/1.1f);
		}
	}
	
	public void add(Planet planet) {
		planets.add(planet);
	}
	
	public void render() {
		for(Planet planet : planets) {
			planet.render();
		}
	}
}
