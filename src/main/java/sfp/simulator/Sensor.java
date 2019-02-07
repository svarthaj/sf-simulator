package sfp.simulator;

public abstract class Sensor {
		private String id;
		private MqttTopic parent = null;
		private SensorType type;

		public Sensor(SensorType type) {
			this.type = type;
		}
		
		public Sensor(String id, SensorType type) {
			this.type = type;
			this.id = id;
		}
		
		public abstract double getData();
		
		public void setId(String sid) { id=sid; }
		public String getId() { return id; }
		
		public void setType(SensorType st) { type=st; }
		public SensorType getType() { return type; }

		public void setParent(MqttTopic t) { this.parent = t; }
		public MqttTopic getParent() { return this.parent; }
				
		public String toString() {
			return "SensorID: " + this.id + " SensorType: " + this.type;
		}
}
