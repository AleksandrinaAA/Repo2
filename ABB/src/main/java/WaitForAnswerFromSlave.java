import com.fasterxml.jackson.core.JsonProcessingException;
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

public class WaitForAnswerFromSlave extends Behaviour {

    String path;
    boolean exit = false;

    MyMqttClient mqtt;

    String  [] CLASSTER;

    AID top;

    DataStore ds = new DataStore();

    public WaitForAnswerFromSlave(String  [] CLASSTER, AID top, MyMqttClient mqtt){
        this.mqtt=mqtt;
        this.CLASSTER=CLASSTER;
        this.top=top;
    }



    @SneakyThrows
    @Override
    public void action() {

        MessageTemplate mt = MessageTemplate.MatchProtocol("Gradient");
        ACLMessage msg = getAgent().receive(mt);
        if (msg != null) {

            System.out.println(getAgent().getLocalName()+ " получил ответ о потерях от " + msg.getSender().getLocalName());

            String Claster = CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length-1];

            String KK = msg.getContent();

            String Content = parseFromAgent(CLASSTER,msg.getContent());


            mqtt.sendMessage(Claster+"ToCSharp",Content);


            //ds.setJSON_TO_JAVA_1(msg.getContent());

            //mqtt.sendMessage("LocalTopic",msg.getContent());

            System.out.println(getAgent().getLocalName()+ " отправляю потери своему Си на градиент:  "+ msg.getContent());
            myAgent.addBehaviour(new WaitGradientResultFromCSharp(CLASSTER, top,mqtt));

            exit=true;

            /*
            mqtt.subscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava", new IMqttMessageListener() {

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println("Agent 1" + mqttMessage.toString());

                    byte[] payload = mqttMessage.getPayload();
                    String JSON_TO_AGENTS = new String(payload, "UTF-8");

                    //Сохраняем данные для второго агента (новый запрос)
                    ds.setJSON_TO_JAVA_1(JSON_TO_AGENTS);

                    exit = true;
                }

            });


             */
        }else{
            //System.out.println("gdhsjsj");
            block();
        }
    }



    @Override
    public boolean done() {

        System.out.println(exit);

        return exit;

    }

    @SneakyThrows
    @Override
    public int onEnd() {

//        System.out.println("Agent 1 закончил поведение");
//        System.out.println(ds.getJSON_TO_JAVA_1());

        try {
            myAgent.addBehaviour(new Gradient(CLASSTER,top,mqtt,ds.getJSON_TO_JAVA_1()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }



    private String parseFromAgent(String[] CLASSTER, String enter) throws JsonProcessingException {

        String Claster = CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1];

        ModeParameters mp = new ModeParameters();
        //Работа с файлом соседа
        ObjectMapper objectMapperr = new ObjectMapper();
        try {
            mp = objectMapperr.readValue(enter, ModeParameters.class);

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

        ObjectMapper objectMapper = new ObjectMapper();
        String JSON_TO_CSHARP = objectMapper.writeValueAsString(mp);




        return JSON_TO_CSHARP;
    }



}
