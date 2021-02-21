import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import mqtt.MyMqttClient;

import java.io.IOException;


public class SUBSC extends Behaviour {
    AID test1;


    public int count =0;
    boolean Over=false;

    String [] CLASSTER;
    MyMqttClient mqtt;

    public SUBSC(String [] CLASSTER,MyMqttClient mqtt){
        this.CLASSTER=CLASSTER;
        this.mqtt = mqtt;
    }

    /*
    @SneakyThrows
    public void onStart() {
        communicationWithCSharp.RequestToCSharpLocalOptimization(CLASSTER,mqtt,myAgent);
    }
    */

    @SneakyThrows
    @Override
    public void action() {



        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchProtocol("SUB"));
        ACLMessage msg = getAgent().receive(mt);

        if (msg != null) {
            System.out.println("Принял сообщение " + myAgent.getLocalName()+" ОТ "+msg.getSender().getLocalName());
            test1 = subscribeTopic(msg.getContent());

            /*
            ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
            AID aid = new AID(msg.getSender().getLocalName(), false);
            msg2.setProtocol("subscribe");
            msg2.addReceiver(aid);
            //msg2.addReceiver(test);
            myAgent.send(msg2);
            */
            myAgent.addBehaviour(new Slave(CLASSTER,test1, mqtt));

        } else {
            block();
        }


        //myAgent.addBehaviour(new SellerProcess(time,test1,test2));



    }

    @Override
    public boolean done() {
        return false;
    }

    private AID subscribeTopic (String topicName){
        TopicManagementHelper topicHelper;
        AID jadeTopic = null;
        try {
            topicHelper = (TopicManagementHelper) getAgent().getHelper(TopicManagementHelper.SERVICE_NAME);
            jadeTopic = topicHelper.createTopic(topicName);
            topicHelper.register(jadeTopic);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return jadeTopic;
    }
}

