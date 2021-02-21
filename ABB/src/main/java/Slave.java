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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Slave extends OneShotBehaviour {


    String JSON_TO_AGENTS;

    String[] CLASSTER;
    AID top;
    MyMqttClient mqtt;

    public Slave(String[] CLASSTER, AID top, MyMqttClient mqtt) {
        this.CLASSTER = CLASSTER;
        this.top = top;
        this.mqtt = mqtt;
    }


    @SneakyThrows
    @Override
    public void onStart() {
        try {
            communicationWithCSharp.RequestToCSharpLocalOptimization(CLASSTER, mqtt, myAgent);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @SneakyThrows
    public void action() {

        mqtt.publishMessage(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");
        mqtt.unsubscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");

        mqtt.subscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava_loc", new IMqttMessageListener() {

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(getAgent().getLocalName() + " получил ответ с С# после Local");
                System.out.println(mqttMessage.toString());

                byte[] payload = mqttMessage.getPayload();
                String JSON_TO_AGENTS = new String(payload, "UTF-8");


                //Отправка данных о локальной оптимизации в топик
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                //AID aid = new AID(j, false);
                msg.setContent(JSON_TO_AGENTS);
                msg.setProtocol("Local Optimization");
                msg.addReceiver(top);
                myAgent.send(msg);

                System.out.println(getAgent().getLocalName() + " Local optimization has done");
                System.out.println(getAgent().getLocalName() + " Отправил в топик результат лок оптимиз" + JSON_TO_AGENTS);


//                myAgent.addBehaviour(new WaitForRequestFromMaster(CLASSTER,top,mqtt));


                mqtt.unsubscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava_loc");
            }

        });


        myAgent.addBehaviour(new WaitForRequestFromMaster(CLASSTER,top,mqtt));


//        //Фиксирование граничных параметров
//        //border_nodes = communicationWithCSharp.DataFromTXT(CLASSTER, JSON_TO_AGENTS);
//
//        //Отправка данных о локальной оптимизации в топик
//        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
//        //AID aid = new AID(j, false);
//        msg.setContent(JSON_TO_AGENTS);
//        msg.setProtocol("LocalOptimization");
//        msg.addReceiver(top);
//        myAgent.send(msg);
//
//
//        myAgent.addBehaviour(new WaitForRequestFromMaster(CLASSTER,top,mqtt));

        /*
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */


        //Set<Integer> set = new HashSet<>(borders);
        //borders.clear();
        //borders.addAll(set);


        //double[][] numbers_ekv = new double[][] { {35001, 22005},{18.1, 245.5} };

        //String [][] ParametrName = new String[] {"Pow","Vras","Pow","Vras"};

        //double [][] NUMBERS_EKV = new double[][] { {35001, 35001,22005, 22005},{245,510,18.1,213} };

        //border_nodes = new double[][] { {25002, 35001,220101, 300011},{233,520,244,522}, {19,250,-245.43,-17.5}};


        //communicationWithCSharp.Communication(CLASSTER,border_nodes);


        //communicationWithCSharp.Communication(CLASSTER,0);

        //communicationWithCSharp.Communication();

        //ModeParameters mp = new ModeParameters();


        //Градиентный спуск для основного кластера
        //Локальная отпимизация
        //Расчёт потерь


    }
}

//    public boolean done() {
//        return false;
//    }






