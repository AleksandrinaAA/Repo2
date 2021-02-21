import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sun.org.apache.regexp.internal.RE;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
//import javafx.application.Platform;
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

public class Master extends OneShotBehaviour {

    String[] CLASSTER;

    String JSON_TO_AGENTS;

    AID top;



    private boolean exit = false;

    DataStore ds = new DataStore();

    MyMqttClient mqtt;

    public Master(String[] CLASSTER, AID top, MyMqttClient mqtt) {
        this.top = top;
        this.CLASSTER = CLASSTER;
        this.mqtt = mqtt;
    }

    boolean ex = false;

    String losses;


    @SneakyThrows
    @Override
    public void onStart() {
        try {
            communicationWithCSharp.RequestToCSharpLocalOptimization(CLASSTER, mqtt, myAgent);
            System.out.println(getAgent().getLocalName() + " Local optimization has done");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @SneakyThrows
    @Override
    public void action() {


        mqtt.subscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava_loc", new IMqttMessageListener() {

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(getAgent().getLocalName() + " получил ответ с С# после LOcal");
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

                //System.out.println("Отправил в топик к другому агенту \n"+ JSON_TO_AGENTS);


                //ex = true;

                myAgent.addBehaviour(new FirstGrad(CLASSTER, top, mqtt));

                ds.setJSON_TO_JAVA_1(JSON_TO_AGENTS);


                //myAgent.addBehaviour(new FirstGrad(CLASSTER, top, mqtt));

                //myAgent.addBehaviour(new Gradient(CLASSTER,top,mqtt,JSON_TO_AGENTS));


                //myAgent.addBehaviour(new Gradient(CLASSTER,top,mqtt,JSON_TO_AGENTS));

                mqtt.unsubscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava_loc");
            }

        });




//        if(ex){
//            myAgent.addBehaviour(new FirstGrad(CLASSTER, top, mqtt));
//        }




        /*
        try {

            //Градиент
            communicationWithCSharp.RequestToCSharpGradient(CLASSTER, mqtt, myAgent);

            myAgent.addBehaviour(new Gradient(CLASSTER,top,mqtt,JSON_TO_AGENTS));
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


    }

    /*
    @Override
    public boolean done() {


        String LL = ds.getJSON_TO_JAVA_1();
        System.out.println("КОНЕЦ");
        System.out.println(LL);

        System.out.println(exit);

        return exit;
    }
    */





    //myAgent.addBehaviour(new RecieveFromCSharp(CLASSTER,mqtt));

        //JSON_TO_AGENTS = ds.getJSON_TO_JAVA_1();

        //System.out.println("ДЖИСОН");
        //System.out.println(JSON_TO_AGENTS[0]);


        //int a = 2;
        //int SS = a +2;

        //Отправка данных о локальной оптимизации в топик
//        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
//        //AID aid = new AID(j, false);
//        msg.setContent(JSON_TO_AGENTS);
//        msg.setProtocol("LocalOptimization");
//        msg.addReceiver(top);
//        myAgent.send(msg);
//
//        System.out.println("Local optimization has done");
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//        myAgent.addBehaviour(new Gradient(CLASSTER,top,mqtt));



        //Фиксирование граничных параметров
        //border_nodes = communicationWithCSharp.DataFromTXT(CLASSTER, JSON_TO_AGENTS,losses);





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



    //}







}
