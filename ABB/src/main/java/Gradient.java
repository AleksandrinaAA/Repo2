import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.SneakyThrows;
import model.ModeParameters;
import mqtt.MyMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class Gradient extends OneShotBehaviour {

    DataStore ds = new DataStore();

    String JSON_TO_AGENTS;

    String [] CLASSTER;
    AID top;
    MyMqttClient mqtt;

    public Gradient(String [] CLASSTER, AID top, MyMqttClient mqtt, String JSON_TO_AGENTS) throws IOException {
        this.CLASSTER=CLASSTER;
        this.top=top;
        this.mqtt=mqtt;
        this.JSON_TO_AGENTS=JSON_TO_AGENTS;
    }

    boolean exit = false;

    @SneakyThrows
    @Override
    public void action() {


//        mqtt.subscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava", new IMqttMessageListener() {
//
//            @Override
//            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//                System.out.println(getAgent().getLocalName() + " получил ответ с С# после Grad");
//                System.out.println(mqttMessage.toString());
//
//                byte[] payload = mqttMessage.getPayload();
//                String JSON_TO_AGENTS = new String(payload, "UTF-8");
//


                //Отправка данных второму агенту
                ACLMessage msg1 = new ACLMessage(ACLMessage.REQUEST);

                ds.setJSON_TO_JAVA_1(JSON_TO_AGENTS);

                msg1.setContent(JSON_TO_AGENTS);
                msg1.setProtocol("Slave");
                msg1.addReceiver(top);
                myAgent.send(msg1);

                int count = ds.count;
                count = count + 1;

                ds.setCount(count);

                if(count==6){
                    System.out.println("СТОП");
                    int g = 2;
                }

//                exit = true;
//
//
//
//                //System.out.println(getAgent().getLocalName()+" Отправил агенту запрос на расчет потерь");
//
//                //myAgent.addBehaviour(new WaitForAnswerFromSlave(CLASSTER, top,mqtt));
//
//            }
//
//        });


        System.out.println(getAgent().getLocalName()+" Отправил агенту запрос на расчет потерь");
        myAgent.addBehaviour(new WaitForAnswerFromSlave(CLASSTER, top,mqtt));

//        if(exit){
//
//            System.out.println(getAgent().getLocalName()+" Отправил агенту запрос на расчет потерь");
//            myAgent.addBehaviour(new WaitForAnswerFromSlave(CLASSTER, top,mqtt));
//        }



        /*
        mqtt.subscribe(Claster+"ToJava", new IMqttMessageListener() {

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(mqttMessage.toString());
                JSON_TO_JAVA = mqttMessage.toString();
            }
        });
        */




    }

}
