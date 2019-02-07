package sfp.simulator;

public class OutlierSensor extends Sensor {
	private double baseline;
	private double range;
	private double outlierProbability;
	
	public OutlierSensor() {
		super(SensorType.OUTLIER);
	}
	
	public double getData() {
		if (Math.random() <= outlierProbability)  {
			if (Math.random() < 0.5) { 
				return baseline + range; 
			} else {
				return baseline - range;
			}
		}
		else return 0.0;
	}
	
	public double getBaseline() { return baseline; }
	public double getRange() { return range; }
	public double getOutlierProbability() { return outlierProbability; }

	public void setBaseline(double b) { baseline = b; }
	public void setRange(double r) { range = r; }
	public void setOutlierProbability(double p) { outlierProbability = p; }
}
