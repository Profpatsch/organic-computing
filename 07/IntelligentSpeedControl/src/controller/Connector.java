package controller;

import main.Car;
import main.Simulation;
import routes.Route;
import routes.Section;
import sim.engine.SimState;
import sim.engine.Steppable;

public class Connector implements Steppable {

    private Observer observer = new Observer();

    private Controller controller = new Controller();

    private Car car;

    private Section currentSection;

    private Route currentRoute;
    @Override
    public void step(SimState simState) {
        Simulation sim = (Simulation) simState;
        car = sim.getCurrentCar();
        currentRoute = sim.getCurrentRoute();
        currentSection = sim.updateOrGetCurrentSection(car, currentRoute);

        observer.step(this);
        controller.step(this);
    }

    public Controller getController() {
        return controller;
    }

    public Observer getObserver() {
        return observer;
    }

    public Car getCar() {
        return car;
    }

    public Section getCurrentSection() {
        return currentSection;
    }

    public Route getCurrentRoute() {
        return currentRoute;
    }
}
