package sfp.simulator;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;

public class App
{
  public static double timecount  = -1;
	public static double timestep = 1;

    public static void main( String[] args ) throws InterruptedException, UnknownHostException
    {
      ArrayList<MqttTopic> listOfTopics = new ArrayList<MqttTopic>();
    	boolean publishState = true;
    	FactoryJSONParser parser = new FactoryJSONParser();

      /* This code connects to the MongoDB Replica Set, retrieve the 'simulations'
         table and adds them to the current simulations running. This way, everytime
         the app is started it will check the DB for simulations that were already
         running. */
    	MongoClient mongo = new MongoClient(
    			new MongoClientURI(
    					"mongodb://localhost:27017/?replicaSet=rs0"));
    	MongoDatabase db = mongo.getDatabase("websim");
    	MongoCollection<Document> table = db.getCollection("simulations");

    	Map<String, Exception> clientExceptions = new HashMap <String, Exception>();

    	MongoCursor<ChangeStreamDocument<Document>> updateCursor = table.watch().iterator();

    	MongoCursor<Document> oldCursor = table.find().iterator();

    	while (oldCursor.hasNext()) {
        		Document next = oldCursor.next();
  			JSONObject addedSimulation = new JSONObject(next.toJson());
        MqttTopic loadedTopic = parser.JSONToTopic(addedSimulation);
            loadedTopic.setSimulationID(next.getObjectId("_id").toString());
  			listOfTopics.add(loadedTopic);
  		}

      MqttClient client = null;
      try {
       client = new MqttClient("tcp://192.168.209.202:1883", MqttClient.generateClientId());
       client.connect();
      } catch (MqttException e) {
       clientExceptions.put("Connection Err", e);
      }

      while(true) {
        /* This will check the DB for updates. This way the app can publish topics
           in real-time with the db. */
        ChangeStreamDocument<Document> next = updateCursor.tryNext();
    		if (next != null) {
          // Check for database update type
          System.out.println(next.getOperationType().getValue());

          if (next.getOperationType().getValue().equals("update")) {
            System.out.println(next);
            // Search for the updated simulation using simulation id
            String updatedID = next.getDocumentKey().getObjectId("_id").getValue().toString();
            // Get the updated value for key 'isValid'
            boolean valid = next.getUpdateDescription().getUpdatedFields().get("isValid").asBoolean().getValue();

            ListIterator<MqttTopic> updateItr = listOfTopics.listIterator();
            boolean found = false;
            while (updateItr.hasNext() && !found) {
              MqttTopic u = updateItr.next();
              if (u.getSimulationID().equals(updatedID)) {
                found = true;
                u.setIsValid(valid);
                updateItr.set(u);
              }
            }
          }

          else if (next.getOperationType().getValue().equals("insert")) {
  	    		System.out.println(next);
  	    		JSONObject newSimulation = new JSONObject(next.getFullDocument());
  	    		MqttTopic newTopic = parser.JSONToTopic(newSimulation);
            newTopic.setSimulationID(next.getDocumentKey().getObjectId("_id").getValue().toString());
  				  listOfTopics.add(newTopic);
          }

          else { // DELETE
            System.out.println(next);
            // Search for the updated simulation using simulation id
            String updatedID = next.getDocumentKey().getObjectId("_id").getValue().toString();
            ListIterator<MqttTopic> deleteItr = listOfTopics.listIterator();
            boolean found = false;
            while (deleteItr.hasNext() && !found) {
              MqttTopic d = deleteItr.next();
              if (d.getSimulationID().equals(updatedID)) {
                found = true;
                deleteItr.remove();
              }
            }
          }
        }

        // Here is where the publishing happens
    		if (publishState == true) {
          App.timecount++;
    			ListIterator<MqttTopic> itrT = listOfTopics.listIterator();
  				while (itrT.hasNext()) {
  					MqttTopic t = itrT.next();
            try {
              if(t.getIsValid() == true) {
                t.publishData(client);
              }
  					} catch (MqttException e) {
  						clientExceptions.put(t.getTopicID()+":"+t.getQueueID(), e);
  					}
  				}
    		}
        Thread.sleep((long)(1000*timestep));
    	}
    }
}
