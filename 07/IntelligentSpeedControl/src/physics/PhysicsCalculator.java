package physics;

import java.util.Random;

public class PhysicsCalculator {
	// 0.28 - 0.4, abh‰ngig von v
	private final float airDragCoefficient = 0.34f;

	// kg/m^3
	private final float airPressureCoefficient = 1.29f;

	// 1.5 - 2.0 m^2
	private final float crossSectionArea = 1.8f;

	// m/s^2
	public final float g = 9.81f;

	// 0.011 - 0.014, abh. von v und straﬂenbelag
	private final float rollingFrictionCoefficient = 0.013f;

	private Random random = new Random();
	private static PhysicsCalculator instance = new PhysicsCalculator();
	
	private PhysicsCalculator() {
	}
	
	public static PhysicsCalculator getInstance() {
		if (instance == null)
			instance = new PhysicsCalculator();
		return instance;
	}

	public float calculateCO2Emission(float initialAmountOfFuel,
			float restOfFuel, float distanceDriven, boolean petrol) {
		float factorDiesel = 26.2f;
		float factorPetrol = 23.2f;
		float result = 0;
		
		float usedFuel = calculateLiterPer100KM(initialAmountOfFuel, restOfFuel, distanceDriven);
		
		// l/100km * kg/10l = g/km Co2
		if (petrol)
			result = usedFuel * factorPetrol;
		else
			result = usedFuel * factorDiesel;

		return result;
	}

	public float calculateFuelConsumptionPerSecond(float speed, float acceleration, float maxAcceleration) {
		float meterPerSecond = speed / 3.6f;
		
		float maxNorm = (maxAcceleration - 1) / 4;
		float accNorm = (acceleration - 1) / 4;
		float accelerationFactor = 1.5f + (accNorm - maxNorm);		

		if (speed == 0)
			return 0.8f / 3600;

		// liter / 100 km
		float result = (float) (5 - 0.048 * speed + 0.0006 * speed * speed) * accelerationFactor;

		// liter / s based on current v
		result = result / 100000 * meterPerSecond;

		return result;
	}

	
	// e.g. 118 km/h max. for a turn with 100m radius
	public float checkTurnForces(float speed, float radius) {
		float meterPerSecond = speed / 3.6f;
		return meterPerSecond * meterPerSecond / radius;
	}

	public float calculateMaxTurnSpeed(float radius) {
		return (float) (Math.sqrt(g * 0.9 * radius) * 3.6);
	}

	public float calculateSpeedLoss(float speed, float ascend, float weight,
			float wheelRadius) {
		float torque = calculateTorque(speed / 3.6f, ascend, weight,
				wheelRadius);

		// torque of 170 Nm means, that 170kg are moved 1m in one second
		float factor = weight / torque;
		float speedLoss = (1 / factor) * 3.6f;

		return speedLoss;
	}

	private float calculateTorque(float speed, float ascend, float weight,
			float wheelRadius) {
		return wheelRadius
				* (calculateAerodynamics(speed)
						+ calculateRollingFriction(speed, ascend, weight)
						+ calculateAscending(ascend, weight) + calculateExteriorForces());
	}

	private float calculateAerodynamics(float speed) {
		return 0.5f * airPressureCoefficient * crossSectionArea
				* airDragCoefficient * speed * speed;
	}

	private float calculateAscending(float angle, float weight) {
		return (float) Math.sin(Math.toRadians(angle)) * weight * g;
	}

	// wind, route etc.
	private float calculateExteriorForces() {
		return random.nextFloat() * 10;
	}

	private float calculateLiterPer100KM(float initialAmountOfFuel,
			float amountOfFuel, float distanceDriven) {
		return (initialAmountOfFuel - amountOfFuel) * (100 / distanceDriven);
	}

	private float calculateRollingFriction(float speed, float angle,
			float weight) {
		return (float) Math.cos(Math.toRadians(angle)) * weight * g
				* rollingFrictionCoefficient;
	}
}
