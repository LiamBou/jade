package issia23.behaviours;

import issia23.agents.RepairCoffeeAgent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CafeRepondreUtilisateur extends ContractNetResponder {

    RepairCoffeeAgent monAgent;

    public CafeRepondreUtilisateur(RepairCoffeeAgent a, MessageTemplate model) {
        super(a, model);
        monAgent = a;
    }

    //function triggered by a PROPOSE msg : send back the ranking
    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        monAgent.println("~".repeat(40));
        int hasard = (int)(Math.random()*8) - 4;
        monAgent.println(cfp.getSender().getLocalName() + " proposes this options: " + cfp.getContent());
        ACLMessage answer = cfp.createReply();
        if(hasard<=0 )answer.setPerformative(ACLMessage.REFUSE);
        else answer.setPerformative(ACLMessage.PROPOSE);

//                String choice = makeItsChoice(cfp.getContent());
        answer.setContent(String.valueOf(hasard));
        return answer;
    }

    /**proposals in the form option1,option2,option3,option4,.....
     * * he returns his choice by ordering the options and giving their positions
     * @param offres list of proposals in the form of option1,option2,option3,option4
     * @return orderly choice in the form of option2_1,option4_2,option3_3,option1_4
     * */
    private String makeItsChoice(String offres) {
        ArrayList<String> choice = new ArrayList<>(List.of(offres.split(",")));
        Collections.shuffle(choice);
        StringBuilder sb = new StringBuilder();
        String pref = ">";
        for (String s : choice) sb.append(s).append(pref);
        String proposition  = sb.substring(0, sb.length()-1);
        monAgent.println("I propose this ranking: " + proposition);
        return proposition;
    }

    //function triggered by a ACCEPT_PROPOSAL msg : the polling station agent  accept the vote
    //@param cfp : the initial cfp message
    //@param propose : the proposal I sent
    //@param accept : the acceptation sent by the auctioneer
    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        monAgent.println("=".repeat(15));
        monAgent.println(" I proposed " + propose.getContent());
        monAgent.println(cfp.getSender().getLocalName() + " accepted my poposam and sent the result:  " + accept.getContent());
        ACLMessage msg = accept.createReply();
        msg.setPerformative(ACLMessage.INFORM);
        msg.setContent("ok !");
        return msg;
    }

    //function triggered by a REJECT_PROPOSAL msg : the auctioneer rejected my vote !
    //@param cfp : the initial cfp message
    //@param propose : the proposal I sent
    //@param accept : the reject sent by the auctioneer
    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        monAgent.println("=".repeat(10));
        monAgent.println("PROPOSAL REJECTED");
        monAgent.println(cfp.getSender().getLocalName() + " asked to repair elt no " + cfp.getContent());
        monAgent.println(" I proposed " + propose.getContent());
        monAgent.println(cfp.getSender().getLocalName() + " refused ! with this message: " + reject.getContent());
    }


};
