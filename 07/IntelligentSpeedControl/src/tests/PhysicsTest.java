package tests;

import org.junit.Test;

import physics.PhysicsCalculator;

public class PhysicsTest {

	@Test
	public void test() {
		PhysicsCalculator physics = PhysicsCalculator.getInstance();

		float speed = 30f; // m/s
		float ascend = 0.12f; // 12%
		float weight = 1600; // kg
		float wheelRadius = 0.30f; // m

		float result = physics.calculateSpeedLoss(speed, ascend, weight,
				wheelRadius);
		System.out.println("Speed loss: " + result);

		System.out.println("Fuel 0: " + physics.calculateFuelConsumptionPerSecond(0, 2.9f, 2.9f));
		System.out.println("Fuel 50: " + physics.calculateFuelConsumptionPerSecond(50, 2.9f, 2.9f));
		System.out.println("Fuel 80: " + physics.calculateFuelConsumptionPerSecond(80, 2.9f, 2.9f));
		System.out
				.println("Fuel 120: " + physics.calculateFuelConsumptionPerSecond(120, 2.9f, 2.9f));
		System.out
				.println("Fuel 160: " + physics.calculateFuelConsumptionPerSecond(160, 2.9f, 2.9f));

	}
}
