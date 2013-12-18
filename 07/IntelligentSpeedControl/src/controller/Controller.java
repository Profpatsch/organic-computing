package controller;

import physics.PhysicsCalculator;
import main.Car;
import routes.Section;
import sim.engine.SimState;

public class Controller {

    private Observer observer;

	public void modifySpeed(Car car) {
						
		float acceleration = observer.getIdealSpeed() - observer.getSpeed();
		
		if(acceleration > 0){
			if (acceleration > car.getMaxAcceleration()) {
				acceleration = car.getMaxAcceleration();
			}
			else {			
				//compensate speedloss
				float speedLossDelta = 0;
				float lastSpeedLoss = 0;
				float speedLoss = 0;
				float initialAcceleration = acceleration;
				for (int i = 1; i < 10; i++){
					float speed = observer.getSpeed() + acceleration;
					speedLossDelta = PhysicsCalculator.getInstance()
							.calculateSpeedLoss(speed, observer.getAscend(), observer.getWeight(), observer.getWheelRadius()) - lastSpeedLoss;
					speedLoss += speedLossDelta;
					acceleration = initialAcceleration + speedLoss;
					lastSpeedLoss = speedLoss;
				}
			}
		}	
		if(acceleration < 0) {
			
		}
		
		car.accelerate(acceleration, observer.getSection());
	}

    public void step(Connector c) {
        observer = c.getObserver();
        modifySpeed(c.getCar());

    }
}
