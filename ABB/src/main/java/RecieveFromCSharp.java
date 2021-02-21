import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import model.ModeParameters;
import mqtt.MyMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.File;
import java.io.IOException;

public class RecieveFromCSharp extends Behaviour {

    String path;
    boolean exit = false;

    MyMqttClient mqtt;

    String JSON_TO_JAVA;

    String  [] CLASSTER;

    public RecieveFromCSharp(String  [] CLASSTER, MyMqttClient mqtt){
        this.mqtt=mqtt;
        this.CLASSTER=CLASSTER;
    }

    DataStore ds = new DataStore();

    @SneakyThrows
    @Override
    public void action() {

        mqtt.publishMessage(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");
        mqtt.unsubscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");

        mqtt.subscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1]+"ToJava", new IMqttMessageListener() {

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(mqttMessage.toString());
                JSON_TO_JAVA = mqttMessage.toString();
            }
        });



        if (JSON_TO_JAVA != null) {

            if(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1].equals("rej23.rg2")){
                ds.setJSON_TO_JAVA_1(JSON_TO_JAVA);
            }else{
                ds.setJSON_TO_JAVA_2(JSON_TO_JAVA);
            }


            exit = true;


        }else{
            //System.out.println("gdhsjsj");
            block();
        }
    }

    @Override
    public boolean done() {
        return exit;
    }



}
