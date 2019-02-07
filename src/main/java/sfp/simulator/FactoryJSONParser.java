package sfp.simulator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class FactoryJSONParser {

	public FactoryJSONParser() {

	}

	// JSON TO DATA
	public MqttTopic JSONToTopic(JSONObject jsonObject) {
		boolean valid = jsonObject.getBoolean("isValid");
		String simID = jsonObject.getJSONObject("_id").getString("$oid");
		String topicID = jsonObject.getString("topicId");
		String queueID = jsonObject.getString("queueId");
		MqttTopic topic = new MqttTopic(topicID);
		topic.setQueueID(queueID);
		topic.setSimulationID(simID);
		topic.setIsValid(valid);
		JSONArray sen = jsonObject.getJSONArray("sensors");
		for(int i=0; i<sen.length(); ++i) {
			Sensor parsedSensor = JSONToSensor((JSONObject) sen.get(i));
			parsedSensor.setParent(topic);
			topic.addSensor(parsedSensor);
		}

		return topic;
	}

	public Sensor JSONToSensor(JSONObject object) {
		String sType = (String) object.getString("SensorType");
		String sID = (String) object.getString("SensorID");
		JSONArray param = object.getJSONArray("SensorParams");

		switch(sType) {
		case "TEMPERATURE":
			TemperatureSensor tSensor = new TemperatureSensor();
			tSensor.setId(sID);
			tSensor.setBaseline(Double.parseDouble(param.get(0).toString()));
			return tSensor;

		case "NOISE":
			NoiseSensor nSensor = new NoiseSensor();
			nSensor.setId(sID);
			nSensor.setBaseline(Double.parseDouble(param.get(0).toString()));
			nSensor.setNoiseRange(Double.parseDouble(param.get(1).toString()));
			return nSensor;

		case "OUTLIER":
			OutlierSensor otlSensor = new OutlierSensor();
			otlSensor.setId(sID);
			otlSensor.setBaseline(Double.parseDouble(param.get(0).toString()));
			otlSensor.setRange(Double.parseDouble(param.get(1).toString()));
			otlSensor.setOutlierProbability(1/(Double.parseDouble(param.get(2).toString())*60));
			return otlSensor;

		case "SIN":
			SinSensor sSensor = new SinSensor();
			sSensor.setId(sID);
			sSensor.setBaseline(Double.parseDouble(param.get(0).toString()));
			sSensor.setPeriod(Double.parseDouble(param.get(1).toString()));
			sSensor.setAmplitude(Double.parseDouble(param.get(2).toString()));
			return sSensor;

		case "LINEAR":
			LinearSensor lSensor = new LinearSensor();
			lSensor.setId(sID);
			lSensor.setSlope(Double.parseDouble(param.get(0).toString()));
			lSensor.setBaseline(Double.parseDouble(param.get(1).toString()));
			return lSensor;

		case "TIMESERIES":
			TimeseriesSensor tsSensor = new TimeseriesSensor();
			tsSensor.setId(sID);
			tsSensor.setBaseline(Double.parseDouble(param.get(0).toString()));
			tsSensor.setBaselineIncrement(Double.parseDouble(param.get(1).toString()));
			tsSensor.setSeasonGrowth(Double.parseDouble(param.get(2).toString()));
			tsSensor.setSeasonFall(Double.parseDouble(param.get(3).toString()));
			tsSensor.setSeasonLength(Double.parseDouble(param.get(4).toString()));
			tsSensor.setTrendLength(Double.parseDouble(param.get(5).toString()));
			return tsSensor;

		default:
			return null;
		}
	}
}
