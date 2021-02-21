import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class communicationWithAgents extends Behaviour {

    AID test;
    double [][] border_nodes;

    public communicationWithAgents(AID test, double [][] border_nodes) {
        this.border_nodes = border_nodes;
        this.test = test;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchProtocol("Request_energy");
        ACLMessage msg = getAgent().receive(mt);

        if (msg != null) {
            System.out.println(getAgent().getLocalName()+ " Запрос о покупке энергии от "+msg.getSender().getLocalName());
            ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
            msg1.setProtocol("cost");
            msg1.addReceiver(test);
            myAgent.send(msg1);

            //myAgent.addBehaviour(new REQUEST(test,amount_of_sellers));

        }else{
            block();
        }
    }




    @Override
    public boolean done() {
        return false;
    }
}
