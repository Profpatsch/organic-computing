package controller;

import main.Car;
import sim.engine.SimState;

public class Controller implements Connector {
	
	Observer o;
	
	public Controller() {
		this.o = new Observer();
	}

	@Override
	public void step(SimState arg0) {
		 modifySpeed(this.o.getCar());
		
	}
	
	public void modifySpeed(Car car) {
	 	if (this.o.getIdealSpeed() > this.o.getSpeed())
				car.accelerate(<ACCELERATION>, <SECTION>);
	 	else
				car.decelerate(<DECELLERATION>, <SECTION>);
	}
}
