package controller;

import physics.PhysicsCalculator;
import main.Car;
import main.ParameterProvider;
import main.Simulation;
import routes.Route;
import routes.Section;
import sim.engine.SimState;
import sim.engine.Steppable;

public class SimpleController implements Connector {
	public void accelerate(Car car, Route route, Section section) {
		float limit = section.getSpeedLimit();
		float speed = car.getCurrentSpeed();

		if (limit > speed)
			if ((limit - speed) > 20)
				car.accelerate(car.getMaxAcceleration(), section);
			else
				car.accelerate(car.getMaxAcceleration() / 2, section);
	}

	public void decelerate(Car car, Route route, Section section) {
		float limit = section.getSpeedLimit();
		float speed = car.getCurrentSpeed();

		if (limit < speed)
			if ((speed - limit) > 30)
				car.decelerate(car.getMinAcceleration(), section);
			else
				car.decelerate(car.getMinAcceleration() / 2, section);
	}

	@Override
	public void step(SimState state) {
		Simulation simulation = (Simulation) state;

		Car car = simulation.getCurrentCar();
		Route currentRoute = simulation.getCurrentRoute();
		Section currentSection = simulation.updateOrGetCurrentSection(car, currentRoute);

		if (currentSection == null) {
			simulation.finish();
			return;
		}
		
		float maxTurnSpeed = PhysicsCalculator.getInstance().calculateMaxTurnSpeed(currentSection.getAngle());

		if (ParameterProvider.DEBUG) {
			System.out.println("Step: " + simulation.schedule.getSteps());
			System.out.println("------------------------");
			System.out.println("SECTION: " + currentSection.getLength() + ", "
					+ currentSection.getSpeedLimit());
		}

		accelerate(car, currentRoute, currentSection);
		decelerate(car, currentRoute, currentSection);
	}
}
