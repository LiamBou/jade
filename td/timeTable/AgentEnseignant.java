package td.timeTable;

import jade.core.Agent;


public class AgentEnseignant extends Agent {

    Integer[][] emploiDuTemps = new Integer[3][3];
    @Override
    protected void setup() {
        println(getLocalName() + " -> Hello, my address is " + getAID());

        if(getLocalName().equals("enseignant1")){
            println("help");
        }
    }
}
