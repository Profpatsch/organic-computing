package controller;

import main.Car;
import routes.Route;
import routes.Section;
import main.Simulation;
import sim.engine.SimState;

class Observer implements Connector {
	
	private double speed;
	private double idealSpeed;
	private Simulation sim; 
	private Car car;
	
	public Observer() {
		this.speed = 0;
		this.idealSpeed = 0;
		this.sim = null;
		this.car = null;
	}
	
	public double getIdealSpeed() {
		return idealSpeed;
	}

	public double getSpeed() {
		return this.speed;
	}
	
	public Car getCar() {
		return this.car;
	}

	@Override
	public void step(SimState simState) {
		this.sim = (Simulation)simState;
		this.car = sim.getCurrentCar();
		Route route = sim.getCurrentRoute();
		this.speed = car.getCurrentSpeed();
		this.idealSpeed = sim.updateOrGetCurrentSection(car, route).getSpeedLimit();
	}
	
	
}
