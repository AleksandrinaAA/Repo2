import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.SneakyThrows;
import mqtt.MyMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;

public class FirstGrad extends OneShotBehaviour {


    String[] CLASSTER;

    String JSON_TO_AGENTS;

    AID top;



    private boolean exit = false;

    DataStore ds = new DataStore();

    MyMqttClient mqtt;

    boolean exx = false;


    public FirstGrad(String[] CLASSTER, AID top, MyMqttClient mqtt) {
        this.top = top;
        this.CLASSTER = CLASSTER;
        this.mqtt = mqtt;
    }


    @SneakyThrows
    @Override
    public void onStart() {
        try {
            communicationWithCSharp.RequestToCSharpGradient(CLASSTER, mqtt, myAgent);
            System.out.println(getAgent().getLocalName() + " First Gradient has done");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @SneakyThrows
    @Override
    public void action() {


        mqtt.publishMessage(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");


        mqtt.unsubscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");

        mqtt.subscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava", new IMqttMessageListener() {

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(getAgent().getLocalName() + " получил ответ с С# после firstGrad");
                System.out.println(mqttMessage.toString());

                byte[] payload = mqttMessage.getPayload();
                String JSON_TO_AGENTS = new String(payload, "UTF-8");



                //System.out.println("Отправил в топик к другому агенту \n"+ JSON_TO_AGENTS);

                exx = true;

                myAgent.addBehaviour(new Gradient(CLASSTER,top,mqtt,JSON_TO_AGENTS));

                ds.setJSON_TO_JAVA_1(JSON_TO_AGENTS);


                //myAgent.addBehaviour(new FirstGrad(CLASSTER, top, mqtt));

                //myAgent.addBehaviour(new Gradient(CLASSTER,top,mqtt,JSON_TO_AGENTS));


                //myAgent.addBehaviour(new Gradient(CLASSTER,top,mqtt,JSON_TO_AGENTS));

                mqtt.unsubscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");
            }

        });



//        if(exx){
//            myAgent.addBehaviour(new Gradient(CLASSTER,top,mqtt,JSON_TO_AGENTS));
//        }





/*
        try {

            //Градиент
            //communicationWithCSharp.RequestToCSharpGradient(CLASSTER, mqtt, myAgent);

            myAgent.addBehaviour(new Gradient(CLASSTER,top,mqtt,JSON_TO_AGENTS));

        } catch (IOException e) {
            e.printStackTrace();
        }

*/

    }
}
