package processing;

import physics.PhysicsCalculator;

public class DataProcessor {
	private static DataProcessor dp = null;
	private int penalty = 0;

	private boolean firstSection = true;
	private boolean nextSection = false;
	private float optimalAverageSpeed = 0;
	private int oldSpeedLimit = 0;
	private int tempSpeedLimit;

	private DataProcessor() {
	}

	public static DataProcessor getInstance() {
		if (dp == null)
			dp = new DataProcessor();
		return dp;
	}

	public void setNextSection(boolean nextSection) {
		this.nextSection = nextSection;
	}

	// penalty for too fast driving
	public void checkSpeedLimit(float speed, int speedLimit) {
		if (speed > speedLimit * 1.05)
			penalty += 1;
	}

	// check accurency of real speed and nearly optimal speed
	public void checkSpeedAccuracy(float speed, float maxAcceleration,
			int speedLimit, float lengthOfSection, float angle) {
		if (firstSection) {
			oldSpeedLimit = 0;
			if (nextSection) {
				firstSection = false;
			}
		}

		if (nextSection) {
			optimalAverageSpeed = tempSpeedLimit;
			oldSpeedLimit = tempSpeedLimit;
			nextSection = false;
		}

		tempSpeedLimit = speedLimit;

		int delta = speedLimit - oldSpeedLimit;

		if (delta < 0) {
			if (optimalAverageSpeed > speedLimit)
				optimalAverageSpeed -= (maxAcceleration / 2);
			else
				optimalAverageSpeed = speedLimit;
		} else {
			if (optimalAverageSpeed < speedLimit)
				optimalAverageSpeed += (maxAcceleration / 2);
			else
				optimalAverageSpeed = speedLimit;
		}

		float accurency = Math.abs(Math.min(optimalAverageSpeed,
				PhysicsCalculator.getInstance().calculateMaxTurnSpeed(angle))
				- speed);

		penalty += accurency;
	}

	// check forces in turn
	public void isAccelerateAllowed(float speed, float angle) {
		// if gravitational acceleration is bigger than centrifugal force, it's
		// not allowed to accelerate
		float maxTurnSpeed = PhysicsCalculator.getInstance()
				.calculateMaxTurnSpeed(angle);

		if (maxTurnSpeed < speed)
			penalty += 2;
	}

	public void printPenalty() {
		System.out.println("Penalty: " + penalty);
	}
}
