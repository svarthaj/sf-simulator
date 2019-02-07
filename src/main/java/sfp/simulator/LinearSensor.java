package sfp.simulator;

public class LinearSensor extends Sensor {
	private double slope;
	private double baseline;
	
	public LinearSensor() {
		super(SensorType.LINEAR);
	}
	
	public double getData() {
		return baseline + slope*App.timecount;
	}
	
	public double getSlope() { return slope; }
	public double getBaseline() { return baseline; }

	public void setSlope(double s) { slope = s; }
	public void setBaseline(double b) { baseline = b; }
}
