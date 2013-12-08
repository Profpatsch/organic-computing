package controller;

import physics.PhysicsCalculator;
import main.Car;
import sim.engine.SimState;

public class Controller implements Connector {
	
	Observer observer;
	
	public Controller() {
		this.observer = new Observer();
	}

	@Override
	public void step(SimState arg0) {
		observer.step(arg0);
		modifySpeed(observer.getCar());
	}
	
	public void modifySpeed(Car car) {
						
		float acceleration = observer.getIdealSpeed() - observer.getSpeed();
		
		if(acceleration > 0){
			if (acceleration > observer.getCar().getMaxAcceleration()) {
				acceleration = observer.getCar().getMaxAcceleration();
			}
			else {			
				//compensate speedloss
				float speedLossDelta = 0;
				float lastSpeedLoss = 0;
				float speedLoss = 0;
				float initialAcceleration = acceleration;
				for (int i = 1; i < 100; i++){
					float speed = observer.getSpeed() + acceleration;
					speedLossDelta = PhysicsCalculator.getInstance()
							.calculateSpeedLoss(speed, observer.getAscend(), observer.getCar().getWeight(), observer.getCar().getWheelRadius()) - lastSpeedLoss;
					speedLoss += speedLossDelta;
					acceleration = initialAcceleration + speedLoss;
					lastSpeedLoss = speedLoss;
				}
			}
		}	
		if(acceleration < 0){
			
		}
		
		car.accelerate(acceleration, observer.getSection());
	}	
}
