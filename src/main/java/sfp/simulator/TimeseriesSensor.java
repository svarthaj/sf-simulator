package sfp.simulator;

public class TimeseriesSensor extends Sensor{

		private double baseline;
		private double baselineIncrement;
		private double trendLength;
		private double seasonGrowth;
		private double seasonFall;
		private double seasonLength;

		private double cycles = 1;
		private boolean growing = true;
		private double seasonCycles = 1;
		private double originalBaseline = baseline;

		public TimeseriesSensor() {
			super(SensorType.TIMESERIES);
		}

		public double getData() {
			double x = App.timecount%(trendLength+1);
			
			if (x==trendLength) {
				if (growing) {
					if (cycles == seasonGrowth) {
						baseline = baseline - baselineIncrement;
						growing = false;
						cycles = 1;
					} else {
						cycles++;
						baseline = baseline + baselineIncrement;
					}
				} else {
					if (cycles == seasonFall) {
						baseline = baseline + baselineIncrement;
						growing = true;
						cycles = 1;
						if (seasonCycles == seasonLength) {
							baseline = originalBaseline;
							seasonCycles=1;
						} else {
							seasonCycles++;
						}
					} else {
						cycles++;
						baseline = baseline - baselineIncrement;
					}
				}
			}
			return baseline;
		}


		public double getBaseline() { return baseline; }
		public double getBaselineIncrement() { return baselineIncrement; }
		public double getTrendLength() { return trendLength; }
		public double getSeasonGrowth() { return seasonGrowth; }
		public double getSeasonFall() { return seasonFall; }
		public double getSeasonLength() { return seasonLength; }

		public void setBaseline(double b) { baseline = b; }
		public void setBaselineIncrement(double bi) { baselineIncrement = bi; }
		public void setTrendLength(double tl) { trendLength= tl; }
		public void setSeasonGrowth(double sg) { seasonGrowth = sg; }
		public void setSeasonFall(double sf) { seasonFall = sf; }
		public void setSeasonLength(double sl) { seasonLength= sl; }
}
