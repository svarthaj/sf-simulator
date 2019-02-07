package sfp.simulator;

public class TemperatureSensor extends Sensor {
	private double baseline;
		
	public TemperatureSensor() {
		super(SensorType.TEMPERATURE);
	}

	public double getData() {
		double maxNoise = 2*baseline + 7;
		double minNoise = 2*baseline + 3;
		if (baseline + Math.exp(App.timecount) < minNoise)
			return baseline + Math.exp(App.timecount);
		else
			return minNoise + (double) (Math.random() * (maxNoise - minNoise));
	}

	public double getBaseline() { return baseline; }
	public void setBaseline(double b) { baseline = b; }
}
