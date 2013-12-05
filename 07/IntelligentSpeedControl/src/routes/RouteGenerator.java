package routes;

import java.util.Random;

public class RouteGenerator {
	private Random random = new Random();

	private static RouteGenerator generator;
	
	private RouteGenerator() {
	}

	public static RouteGenerator getInstance() {
		if (generator == null)
			generator = new RouteGenerator();
		return generator;
	}

	public Route generateRoute(float length) {
		Route route = new Route();
		float rest = length;

		while (rest > 0) {
			float lengthOfSection = Float.MAX_VALUE;

			while (rest < lengthOfSection)
				lengthOfSection = random.nextFloat() * 100;

			rest -= lengthOfSection;

			Section section = new Section(lengthOfSection, random.nextFloat(),
					getRandomAngle(), getRandomSpeedLimit());

			route.addSection(section);
		}

		return route;
	}

	public Route generateRoute(int numberOfSections) {
		Route route = new Route();

		for (int i = 0; i < numberOfSections; i++) {
			float length = getRandomLength(4); // zum testen max. 3 km
			Section section = new Section(length, random.nextFloat(),
					getRandomAngle(), getRandomSpeedLimit());

			route.addSection(section);
		}

		return route;
	}

	public Route generateSectionWith120SpeedLimit() {
		Route route = new Route();
		
		Section startSection = new Section(0.1f, random.nextFloat(), 100, SpeedLimit.FIVE.getLimit());
		route.addSection(startSection);
		
		Section secondSection = new Section(0.5f, random.nextFloat(), 100, SpeedLimit.THIRTY.getLimit());
		route.addSection(secondSection);
		
		Section thirdSection = new Section(0.5f, random.nextFloat(), 100, SpeedLimit.FIFTY.getLimit());
		route.addSection(thirdSection);
		
		Section fourthSection = new Section(0.5f, random.nextFloat(), 100, SpeedLimit.EIGHTY.getLimit());
		route.addSection(fourthSection);

		Section section = new Section(5, random.nextFloat(),
				/*random.nextInt(220)*/ 100, SpeedLimit.HUNDERTTWENTY.getLimit());
		route.addSection(section);

		return route;
	}
	
	private float getRandomAngle() {
		float length = random.nextInt(250);

		if (length < 10)
			length = 10;

		return length;
	}

	private float getRandomLength(int maxLength) {
		float length = random.nextInt(maxLength);

		if (length == 0)
			length = 1;

		return length;
	}

	private int getRandomSpeedLimit() {
		int rnd = random.nextInt(6);
		switch (rnd) {
		/*
		 * case 0: return SpeedLimit.ZERO.getLimit();
		 */
		case 1:
			return SpeedLimit.THIRTY.getLimit();
		case 2:
			return SpeedLimit.FIFTY.getLimit();
		case 3:
			return SpeedLimit.EIGHTY.getLimit();
		case 4:
			return SpeedLimit.HUNDERTTWENTY.getLimit();
		case 5:
			return SpeedLimit.NONE.getLimit();
		}
		return SpeedLimit.FIFTY.getLimit();
	}
}
