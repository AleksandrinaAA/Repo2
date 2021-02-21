package mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;

public class MyMqttClient {
    private MqttClient mqttClient;

    public MyMqttClient(String broker){
        this(broker,null,null);
    }

    public MyMqttClient(String broker, String userName, String password){
       this(broker,userName,password,null,null);
    }

    public MyMqttClient(String broker, String userName, String password, String lastWillTopic, String lastWillMsg){
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            mqttClient = new MqttClient(broker, MqttClient.generateClientId() + (new Random().nextInt(10000)), persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            if (userName != null)
                connOpts.setUserName(userName);
            if (password != null)
                connOpts.setPassword(password.toCharArray());
            if (lastWillMsg != null && lastWillTopic != null)
                connOpts.setWill(lastWillTopic,lastWillMsg.getBytes(),2,true);
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            mqttClient.connect(connOpts);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void subscribe(String topic, IMqttMessageListener listener){
        try {
            mqttClient.subscribe(topic,2, listener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe (String topic){
        try {
            mqttClient.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }



    public void sendMessage(String topic, String content){
         sendMessage(topic,content,false);
    }

    public void sendMessage(String topic, String content, boolean retained){
        try{
            MqttMessage msg =new MqttMessage(content.getBytes());
            msg.setQos(2);
            msg.setRetained(retained);
            mqttClient.publish(topic,msg);
        } catch (MqttException me ){
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public void publishMessage(String topic){
        try{
            //MqttMessage msg =new MqttMessage(content.getBytes());
            mqttClient.publish(topic,new byte[0],0,true);
            //mqttClient.disconnect();
            //mqttClient.connect();

        } catch (MqttException me ){
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }



}
