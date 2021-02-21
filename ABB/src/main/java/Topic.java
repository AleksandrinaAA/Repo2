import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;

import jade.core.messaging.TopicManagementHelper;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import mqtt.MyMqttClient;


public class Topic extends OneShotBehaviour {

    String [] CLASSTER;
    MyMqttClient mqtt;

    public Topic(String [] CLASSTER,MyMqttClient mqtt){
        this.CLASSTER=CLASSTER;
        this.mqtt = mqtt;
    }



    public String s;

    public int amount_of_sellers;
    private String tp;
    @Override
    public void action() {


        tp = "Topic";

        AID top = createTopic(tp);    //доступ к агенту AID-указатель на агента
        AID test = subscribeTopic(tp);
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Auction");
        dfd.addServices(sd);

        DFAgentDescription[] foundAgents = new DFAgentDescription[0];
        try {
            foundAgents = DFService.search(getAgent(), dfd);
            for (int i = 0; i < foundAgents.length; ++i) {

                System.out.println(foundAgents[i].getName().getLocalName());
                if(!foundAgents[i].getName().getLocalName().equals(getAgent().getLocalName())){
                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                    AID aid = new AID(foundAgents[i].getName().getLocalName(), false);
                    msg.setContent(tp);
                    msg.setProtocol("SUB");
                    msg.addReceiver(aid);
                    myAgent.send(msg);
                }

        }

        } catch (FIPAException e) {
            e.printStackTrace();
        }


        myAgent.addBehaviour(new Master(CLASSTER,top,mqtt));



    }

    private AID subscribeTopic(String topicName) {
        TopicManagementHelper topicHelper;
        AID jadeTopic = null;
        try {
            topicHelper = (TopicManagementHelper)
                    myAgent.getHelper(TopicManagementHelper.SERVICE_NAME);
            jadeTopic = topicHelper.createTopic(topicName);
            topicHelper.register(jadeTopic);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return jadeTopic;
    }


    private AID createTopic(String topicName) {
        TopicManagementHelper topicHelper;
        AID jadeTopic = null;
        try {
            topicHelper = (TopicManagementHelper) getAgent().getHelper(TopicManagementHelper.SERVICE_NAME);
            jadeTopic = topicHelper.createTopic(topicName);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return jadeTopic;
    }


}







