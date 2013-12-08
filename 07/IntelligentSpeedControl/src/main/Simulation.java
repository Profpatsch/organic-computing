package main;

import gui.FileChooserHelper;
import processing.DataProcessor;
import routes.Route;
import routes.RouteGenerator;
import routes.Section;
import sim.engine.SimState;
import sim.engine.Steppable;
import controller.Connector;
import controller.Controller;
import controller.SimpleController;

public class Simulation extends SimState {
	private Connector connector;
	private Car currentCar;
	private Route currentRoute;
	private Section currentSection;
	private FileChooserHelper fch;
	private DataProcessor dp;

	public Simulation(long seed) {
		super(seed);
		dp = DataProcessor.getInstance();
		fch = FileChooserHelper.getInstance();
	}

	public Car getCurrentCar() {
		return currentCar;
	}

	public Route getCurrentRoute() {
		return currentRoute;
	}

	public Section updateOrGetCurrentSection(Car car, Route route) {
		float distance = car.getDistanceDrivenInCurrentSection();

		if (distance >= currentSection.getLength() * 1000) {
			int pos = route.getPosition(currentSection);

			if (pos + 1 == route.getSize())
				return null;

			Section nextSection = route.getSection(pos + 1);
			this.currentSection = nextSection;

			car.setDistanceDrivenInCurrentSection(0);

			dp.setNextSection(true);

			return nextSection;
		}
		return currentSection;
	}

	public void loadCar() {
		currentCar = fch.loadCarFromXML();
		currentCar.setInitialAmountOfFuel(currentCar.getAmountOfFuel());
	}

	public void loadConnector() {
		this.connector = new Controller();
		schedule.scheduleRepeating((Steppable) this.connector);
	}

	public void loadRoute() {
		this.currentRoute = fch.loadRouteFromXML();
		currentSection = this.currentRoute.getSection(0);
	}

	public void loadDefaultSimulation() {
		// volvo V60
		currentCar = new Car(true, 30f, 160, 2.5f, 2.9f, 0.3f, 1637);

		loadConnector();

		RouteGenerator routeGenerator = RouteGenerator.getInstance();
		this.currentRoute = routeGenerator.generateSectionWith120SpeedLimit();
		currentSection = this.currentRoute.getSection(0);
	}

	public void runSimulation(String[] args) {
		doLoop(Simulation.class, args);
		System.exit(0);
	}

	public void writeCarToXML() {
		fch.saveToXML(currentCar);
	}

	public void writeRouteToXML() {
		fch.saveToXML(currentRoute);
	}

	@Override
	public void start() {
		super.start();
	}
}
