import jade.core.Agent;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import lombok.SneakyThrows;
import mqtt.MyMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class Agents extends Agent {

    @SneakyThrows
    @Override
    protected void setup() {


        System.out.println(getLocalName());
        registerAgent();
        //Вывод русских символов
        //System.setProperty("console.encoding","Cp866");
        System.setProperty("console.encoding","utf-8");

        MyMqttClient mqtt = new MyMqttClient("tcp://localhost:1883");

        if (getLocalName().equals("Agent1")) {
            String[] CLASSTER = new String[7];

            CLASSTER[0] = "C:\\Users\\Степан\\Desktop\\Opt_Agents_1\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\rej23.rg2";
            CLASSTER[1] = "C:\\Users\\Степан\\Desktop\\Opt_Agents_1\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\анцапфы.anc";
            CLASSTER[2] = "C:\\Users\\Степан\\Desktop\\Opt_Agents_1\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\трансформаторы.trn";

            CLASSTER[3] = "C:\\Users\\Степан\\Desktop\\Opt_Agents_1\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\rej23.rg2";
            CLASSTER[4] = "C:\\Users\\Степан\\Desktop\\Opt_Agents_1\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\anc23.anc";
            CLASSTER[5] = "C:\\Users\\Степан\\Desktop\\Opt_Agents_1\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\trn23.trn";
            CLASSTER[6] = "0";



            //Process process1 = new ProcessBuilder("C:\\Users\\Степан\\Desktop\\Opt_Agents_1\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\Rastr_Optimizer.exe").start();


//            while (true){
//                mqtt.sendMessage("/test","hello from java");
//                //Thread.sleep(2000);
//            }

            /*
            CLASSTER[0] = "rej23.rg2";
            CLASSTER[1] = "анцапфы.anc";
            CLASSTER[2] = "трансформаторы.trn";

            CLASSTER[3] = "rej23.rg2";
            CLASSTER[4] = "anc23.anc";
            CLASSTER[5] = "trn23.trn";
            */



            addBehaviour(new Topic(CLASSTER,mqtt));

        } else if (getLocalName().equals("Agent2")) {
            String[] CLASSTER = new String[7];

            CLASSTER[0] = "C:\\Users\\Степан\\Desktop\\Opt_Agents_2\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\rej22.rg2";
            CLASSTER[1] = "C:\\Users\\Степан\\Desktop\\Opt_Agents_2\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\анцапфы.anc";
            CLASSTER[2] = "C:\\Users\\Степан\\Desktop\\Opt_Agents_2\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\трансформаторы.trn";

            CLASSTER[3] = "C:\\Users\\Степан\\Desktop\\Opt_Agents_2\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\rej22.rg2";
            CLASSTER[4] = "C:\\Users\\Степан\\Desktop\\Opt_Agents_2\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\anc22.anc";
            CLASSTER[5] = "C:\\Users\\Степан\\Desktop\\Opt_Agents_2\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\trn22.trn";
            CLASSTER[6] = "0";

            //Process process2 = new ProcessBuilder("C:\\Users\\Степан\\Desktop\\Opt_Agents_2\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\Rastr_Optimizer.exe").start();

            /*
            CLASSTER[0] = "rej22.rg2";
            CLASSTER[1] = "анцапфы.anc";
            CLASSTER[2] = "трансформаторы.trn";

            CLASSTER[3] = "rej22.rg2";
            CLASSTER[4] = "anc22.anc";
            CLASSTER[5] = "trn22.trn";

             */
            addBehaviour(new SUBSC(CLASSTER, mqtt));


        }
    }


    private void registerAgent () {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Auction");
        sd.setName("Auction" + getLocalName());
        dfd.addServices(sd);


        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}