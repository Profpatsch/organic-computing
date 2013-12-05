package main;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import physics.PhysicsCalculator;
import processing.DataProcessor;
import routes.Section;

@XmlRootElement(name = "car")
public class Car {
	private FloatProperty amountOfFuel = new SimpleFloatProperty(); // l
	private FloatProperty co2Emission = new SimpleFloatProperty(); // g/km
	private FloatProperty currentSpeed = new SimpleFloatProperty(); // km/h
	private FloatProperty distanceDriven = new SimpleFloatProperty();
	private float distanceDrivenInCurrentSection = 0;
	private float initialAmountOfFuel; // l
	private float maxAcceleration; // m/s^2
	private float maxSpeed; // km/h
	private float minAcceleration; // max. braking effect
	private boolean petrol; // benzin or diesel
	private PhysicsCalculator physics;
	// fuel thats really used caused by exterior influences
	private FloatProperty realFuelConsumption = new SimpleFloatProperty();
	private float weight; // in kg
	private float wheelRadius; // in cm
	private DataProcessor dp;

	public Car() {
		this.dp = DataProcessor.getInstance();
		this.physics = PhysicsCalculator.getInstance();
	}

	public Car(boolean petrol, float amountOfFuel, float maxSpeed,
			float minAcceleration, float maxAcceleration, float wheelRadius,
			float weight) {
		this.petrol = petrol;
		this.setAmountOfFuel(amountOfFuel);
		this.initialAmountOfFuel = amountOfFuel;
		this.maxSpeed = maxSpeed;
		this.maxAcceleration = maxAcceleration;
		this.minAcceleration = minAcceleration;
		this.wheelRadius = wheelRadius;
		this.weight = weight;
		this.setCurrentSpeed(0);
		this.setRealFuelConsumption(0);
		
		this.dp = DataProcessor.getInstance();
		this.physics = PhysicsCalculator.getInstance();
	}

	public void accelerate(float acceleration, Section section) {
		updateSimulationParameters(acceleration, section.getAscend(),
				section.getAngle(), section.getLength(), maxAcceleration,
				false, section.getSpeedLimit());
	}

	public void decelerate(float acceleration, Section section) {
		if(acceleration < 0)
			acceleration = acceleration * (-1);
		
		updateSimulationParameters(acceleration, section.getAscend(),
				section.getAngle(), section.getLength(), minAcceleration, true,
				section.getSpeedLimit());
	}

	private void updateSimulationParameters(float acceleration, float ascend,
			float angle, float lengthOfSection, float accelerationLimit,
			boolean slowdown, int speedLimit) {
		float currentSpeed = getCurrentSpeed();

		// max. acceleration / braking effect
		if (acceleration > accelerationLimit)
			acceleration = accelerationLimit;

		if (slowdown && !(currentSpeed - acceleration < 0))
			currentSpeed -= acceleration;
		else if (!slowdown && !(currentSpeed + acceleration > maxSpeed))
			currentSpeed += acceleration;

		setCurrentSpeed(currentSpeed);
		decreaseFuel(acceleration);

		if (ParameterProvider.DEBUG)
			System.out.println("current speed: " + this.getCurrentSpeed());

		currentSpeed -= physics.calculateSpeedLoss(currentSpeed, ascend,
				weight, wheelRadius);
		setCurrentSpeed(currentSpeed);

		if (ParameterProvider.DEBUG)
			System.out.println("real current speed: " + this.getCurrentSpeed());

		setDistanceDriven();
		calculateCo2Emission();

		dp.checkSpeedLimit(currentSpeed, speedLimit);
		dp.checkSpeedAccuracy(currentSpeed, accelerationLimit, speedLimit,
				lengthOfSection, angle);
		dp.isAccelerateAllowed(currentSpeed, angle);
		dp.printPenalty();
	}

	public FloatProperty co2EmissionProperty() {
		return co2Emission;
	}

	public FloatProperty currentFuelProperty() {
		return amountOfFuel;
	}

	public FloatProperty currentSpeedProperty() {
		return currentSpeed;
	}

	public FloatProperty distanceDrivenProperty() {
		return distanceDriven;
	}

	private void decreaseFuel(float acceleration) {
		float usedFuel = physics.calculateFuelConsumptionPerSecond(
				getCurrentSpeed(), acceleration, maxAcceleration);

		this.setAmountOfFuel(getAmountOfFuel() - usedFuel);
		this.setRealFuelConsumption(usedFuel);
	}

	private float calculateCo2Emission() {
		float emission = physics.calculateCO2Emission(this.initialAmountOfFuel,
				this.getAmountOfFuel(), this.getDistanceDriven(), this.petrol);
		co2Emission.set(emission);

		return emission;
	}

	@XmlElement
	public float getAmountOfFuel() {
		return amountOfFuel.get();
	}

	public float getCurrentSpeed() {
		return currentSpeed.get();
	}

	public float getDistanceDriven() {
		return distanceDriven.get();
	}

	@XmlTransient
	public float getDistanceDrivenInCurrentSection() {
		return distanceDrivenInCurrentSection;
	}

	@XmlElement
	public float getMaxAcceleration() {
		return maxAcceleration;
	}

	@XmlElement
	public float getMaxSpeed() {
		return maxSpeed;
	}

	@XmlElement
	public float getMinAcceleration() {
		return minAcceleration;
	}

	@XmlElement
	public float getWeight() {
		return weight;
	}

	@XmlElement
	public float getWheelRadius() {
		return wheelRadius;
	}

	public FloatProperty realFuelPerKilometerProperty() {
		return realFuelConsumption;
	}

	public void setInitialAmountOfFuel(float initialAmountOfFuel) {
		this.initialAmountOfFuel = initialAmountOfFuel;
	}

	private void setAmountOfFuel(float value) {
		amountOfFuel.set(value);
	}

	private void setCurrentSpeed(float value) {
		currentSpeed.set(value);
	}

	private void setDistanceDriven() {
		float meterPerSecond = (float) (this.getCurrentSpeed() / 3.6);
		this.distanceDriven.setValue(this.getDistanceDriven() + meterPerSecond
				/ 1000);
		distanceDrivenInCurrentSection += meterPerSecond;
	}

	public void setDistanceDrivenInCurrentSection(
			float distanceDrivenInCurrentSection) {
		this.distanceDrivenInCurrentSection = distanceDrivenInCurrentSection;
	}

	public void setMaxAcceleration(float maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public void setMinAcceleration(float minAcceleration) {
		this.minAcceleration = minAcceleration;
	}

	private void setRealFuelConsumption(float realFuelConsumption) {
		this.realFuelConsumption.set(realFuelConsumption);
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void setWheelRadius(float wheelRadius) {
		this.wheelRadius = wheelRadius;
	}
}
