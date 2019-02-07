package sfp.simulator;

public class NoiseSensor extends Sensor{
	private double baseline;
	private double noiseRange;
	
	public NoiseSensor() {
		super(SensorType.NOISE);
	}

	public double getData() {
		double maxNoise = baseline + noiseRange;
		double minNoise = baseline - noiseRange;

		return minNoise + (double) (Math.random() * (maxNoise - minNoise));
	}
	
	public double getBaseline() { return baseline; }
	public void setBaseline(double b) { baseline = b; }
	public double getNoiseRange() { return noiseRange; }
	public void setNoiseRange(double r) { noiseRange = r; }
	
}
