package controller;

import physics.PhysicsCalculator;
import main.Car;
import routes.Route;
import routes.Section;
import main.Simulation;
import sim.engine.SimState;

class Observer implements Connector {
	
	private Car car;
	private Section section;
	private Route route;
	
	public float getMaxTurnSpeed() {
		return PhysicsCalculator.getInstance().calculateMaxTurnSpeed(section.getAngle());
	}

	public Section getSection() {
		return section;
	}

	public Observer() {

	}
	
	public float getIdealSpeed() {
		return this.section.getSpeedLimit();
	}

	public float getSpeed() {
		return this.car.getCurrentSpeed();
	}
	
	public Car getCar() {
		return this.car;
	}
	
	public float getAscend(){
		return this.section.getAscend();
	}

	@Override
	public void step(SimState simState) {
		Simulation sim = (Simulation)simState;
		this.car = sim.getCurrentCar();
		this.route = sim.getCurrentRoute();
		this.section = sim.updateOrGetCurrentSection(car, route);		
	}
	
	
}
