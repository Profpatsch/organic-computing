package controller;

import physics.PhysicsCalculator;
import main.Car;
import routes.Route;
import routes.Section;
import main.Simulation;
import sim.engine.SimState;

class Observer {

	private Car car;
	private Section section;


	public Observer() {}

    Section getSection() {
        return section;
    }

    public float getWheelRadius() {
		return car.getWheelRadius();
	}
	
	public float getWeight() {
		return car.getWeight();
	}
	
	public float getIdealSpeed() {
		return this.section.getSpeedLimit();
	}

	public float getSpeed() {
		return this.car.getCurrentSpeed();
	}
	
	public float getAscend(){
		return this.section.getAscend();
	}

	public void step(Connector c) {
		this.car = c.getCar();
        this.section = c.getCurrentSection();
	}
	
	
}
