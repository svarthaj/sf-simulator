package sfp.simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class MqttTopic {

	private boolean isValid;
	private String simulationID;
	private String topicID;
	private String queueID;
	public ArrayList<Sensor> listOfSensors = new ArrayList<Sensor>();

	public MqttTopic(String id) {
		topicID = id;
	}

	public void addSensor(Sensor sen){
		listOfSensors.add(sen);
	}

	public void listAllSensors() {
		Iterator<Sensor> itr = listOfSensors.iterator();
		while (itr.hasNext()) {
			Sensor sen = itr.next();
			System.out.println(sen.toString());
		}
	}

	public String getData() {
		Iterator<Sensor> itr = listOfSensors.iterator();
		double dataSum = 0.0;
		while (itr.hasNext()) {
			Sensor sen = itr.next();
			dataSum+=sen.getData();
		}
		return String.format(Locale.US, "%.2f", dataSum);
	}

	public void publishData(MqttClient client) throws MqttException {
		String messageString = "{\""+queueID+"\":"+getData()+"}";
		String topicString = this.topicID;

		MqttMessage message = new MqttMessage();
		message.setPayload(messageString.getBytes());
		client.publish(topicString, message);
	}

	public void setIsValid(boolean flag) { isValid = flag; }
	public boolean getIsValid( ) { return isValid; }
	public void setSimulationID(String id) { simulationID = id; }
	public String getSimulationID( ) { return simulationID; }
	public void setTopicID(String id) { topicID = id; }
	public String getTopicID( ) { return topicID; }
	public void setQueueID(String id) { queueID = id; }
	public String 	getQueueID() { return queueID; }

	public String toString() {
		return "Topic "+this.topicID+" is:\n"+this.isValid;
	}
}
