import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import model.CalculationType;
import model.ModeParameters;
import model.SchemeElement;
import model.SchemeRegim;
import mqtt.MyMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttClient;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import paho.mqtt.client as mqtt

public class WaitForRequestFromMaster extends Behaviour {


    String JSON_TO_AGENTS;


    MqttClient MQTT;

    String [] CLASSTER;
    AID top;
    MyMqttClient mqtt;
    boolean exit = false;

    DataStore ds =  new DataStore();

    public WaitForRequestFromMaster(String [] CLASSTER, AID top,MyMqttClient mqtt){
        this.CLASSTER=CLASSTER;
        this.top=top;
        this.mqtt=mqtt;
    }

    String losses;


    @SneakyThrows
    public void action() {


        MessageTemplate mt = MessageTemplate.MatchProtocol("Slave");
        ACLMessage msg = getAgent().receive(mt);

        if (msg != null) {

            System.out.println(getAgent().getLocalName()+ " получил запрос на расчет потерь "+msg.getSender().getLocalName());
            System.out.println(msg.getContent());


            String aa = msg.getContent();

            try {
                losses = OptimizationWithLimits(CLASSTER, msg.getContent(),mqtt,ds);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
            msg1.setProtocol("Gradient");
            msg1.setContent(losses);
            msg1.addReceiver(top);
            myAgent.send(msg1);
            System.out.println(getAgent().getLocalName()+ " отправил ответ о расчете потерь "+msg.getSender().getLocalName());

            exit = true;

        }else{
            block();

        }

    }





    @Override
    public boolean done() {
        return exit;
    }

    @SneakyThrows
    @Override
    public int onEnd() {
        myAgent.addBehaviour(new WaitForRequestFromMaster(CLASSTER,top,mqtt));

        return 1;
    }


    public String OptimizationWithLimits(String[] CLASSTER, String JSON_TO_AGENTS, MyMqttClient mqtt, DataStore ds) throws IOException, InterruptedException, MqttException {

        String Claster = CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1];
        ModeParameters mp = new ModeParameters();
        //Работа с файлом соседа
        ObjectMapper objectMapperr = new ObjectMapper();
        try {
            mp = objectMapperr.readValue(JSON_TO_AGENTS, ModeParameters.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //mp.setType(CalculationType.Slave);

        mp.getRegim().setRegimTemplate(CLASSTER[0]);
        mp.getRegim().setAncTemplate(CLASSTER[1]);
        mp.getRegim().setTransTemplate(CLASSTER[2]);
        mp.getRegim().setRegim(CLASSTER[3]);
        mp.getRegim().setAnc(CLASSTER[4]);
        mp.getRegim().setTrans(CLASSTER[5]);
        //mp.getRegim().setLosses(CLASSTER[6]);


        //String JSON_TO_CSHARP = mp.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        String JSON_TO_CSHARP = objectMapper.writeValueAsString(mp);

        mqtt.sendMessage(Claster+"ToCSharp",JSON_TO_CSHARP);

        //mqtt.unsubscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");


        //Process process = new ProcessBuilder("C:\\PathToExe\\MyExe.exe").start();
        //process.destroy();


        //MQTT.publish(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava",new byte[0],0,true);

        //MQTT.subscribe();
        mqtt.publishMessage(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");
        mqtt.unsubscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");

        mqtt.subscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava", new IMqttMessageListener() {

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(getAgent().getLocalName()+ " получил ответ с С# о расчете потерь с параметрами ");
                System.out.println(mqttMessage.toString());

                byte[] payload = mqttMessage.getPayload();
                String AAA = new String(payload, "UTF-8");

                ds.setJSON_TO_JAVA_2(new String(payload, "UTF-8"));

                mqtt.unsubscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");
            }


        });


        while(ds.getJSON_TO_JAVA_2() == null){
            Thread.sleep(300);
        }
        /*

        mqtt.subscribe(Claster+"ToJava", new IMqttMessageListener() {

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(mqttMessage.toString());
                JSON_TO_JAVA[0] = mqttMessage.toString();
            }
        });

        */

        //Оптимизация с учётом ограничений
        //JSON_TO_AGENTS = communicationWithCSharp.AnswerFromCSharp(CLASSTER);
        //JSON_TO_AGENTS = communicationWithCSharp.RequestToCSharp(CLASSTER,1);


        //Считываем потери

        ModeParameters mp1 = new ModeParameters();
        ObjectMapper objectMapperr11 = new ObjectMapper();
        try {
            mp1 = objectMapperr11.readValue(ds.getJSON_TO_JAVA_2(), ModeParameters.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String TOAGENT = objectMapper.writeValueAsString(mp1);

        //return mp1.regim.losses;

        return TOAGENT;
    }





}
