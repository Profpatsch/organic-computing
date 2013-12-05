package routes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "section")
public class Section {
	private float angle; // angle of turn; serpentine 10m; value of 0 equals
							// straight line (dt. Kurvenradius)
	private float ascend; // degree
	private float length; // km
	private int speedLimit; // km/h

	public Section() {
	}

	public Section(float length, float ascend, float angle, int speedLimit) {
		this.angle = angle;
		this.ascend = ascend;
		this.length = length;
		this.speedLimit = speedLimit;
	}

	@XmlElement
	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public void setAscend(float ascend) {
		this.ascend = ascend;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
	}

	@XmlElement
	public float getAscend() {
		return ascend;
	}

	@XmlElement
	public float getLength() {
		return length;
	}

	@XmlElement
	public int getSpeedLimit() {
		return speedLimit;
	}
}
