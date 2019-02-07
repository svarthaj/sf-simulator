package sfp.simulator;

public class SinSensor extends Sensor {
	private double period = 600;
	private double amplitude = 30;
	private double baseline = 30;
	
	public SinSensor() {
		super(SensorType.SIN);
	}
	
	public double getData() {
		return baseline + amplitude*Math.sin(2*Math.PI*App.timecount*App.timestep/period);
	}
	
	public double getPeriod() { return period; }
	public double getAmplitude() { return amplitude; }
	public double getBaseline() { return baseline; }

	public void setPeriod(double p) { period = p; }
	public void setAmplitude(double a) { amplitude = a; }
	public void setBaseline(double b) { baseline = b; }
}
