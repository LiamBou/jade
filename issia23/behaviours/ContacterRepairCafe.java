package issia23.behaviours;

import issia23.agents.UserAgent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.ArrayList;
import java.util.List;

public class ContacterRepairCafe  extends ContractNetInitiator{
    UserAgent monAgent;
    public ContacterRepairCafe(UserAgent a, ACLMessage msg) {
        super(a, msg);
        monAgent = a;
//        reset();
        monAgent.println("I am in the contacter repair cafe behaviour");
    }

    //function triggered by a PROPOSE msg
    // @param propose     the received propose message
    // @param acceptances the list of ACCEPT/REJECT_PROPOSAL to be sent back.
    //                    list that can be modified here or at once when all the messages are received
    @Override
    public void handlePropose(ACLMessage propose, List<ACLMessage> acceptations) {
        monAgent.println("Agent %s proposes %s ".formatted(propose.getSender().getLocalName(), propose.getContent()));
    }

    //function triggered by a REFUSE msg
    @Override
    protected void handleRefuse(ACLMessage refuse) {
        monAgent.println("REFUSE ! I received a refuse from " + refuse.getSender().getLocalName());
    }

    //function triggered when all the responses are received (or after the waiting time)
    //@param theirVotes the list of message sent by the voters
    //@param myAnswers the list of answers for each voter
    @Override
    protected void handleAllResponses(List<ACLMessage> theirVotes, List<ACLMessage> myAnswers) {
        ArrayList<ACLMessage> listeProposals = new ArrayList<>(theirVotes);
        //we keep only the proposals only
        listeProposals.removeIf(v -> v.getPerformative() != ACLMessage.PROPOSE);
        myAnswers.clear();

        ACLMessage bestProposal =null;
        ACLMessage bestAnswer =null;
        var bestPrice = Integer.MAX_VALUE;

        for (ACLMessage proposal : listeProposals) {
            //by default, we build a accept answer for each proposal
            var answer = proposal.createReply();
            answer.setPerformative(ACLMessage.REJECT_PROPOSAL);
            myAnswers.add(answer);
            var content = Integer.parseInt(proposal.getContent());
            monAgent.println(proposal.getSender().getLocalName() + " has proposed " + content);
            if (content<bestPrice){
                bestPrice = content;
                bestProposal = proposal;
                bestAnswer = answer;
            }

        }

        if (bestProposal !=null)
        {
            bestProposal.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            monAgent.println("I choose the proposal of " + bestProposal.getSender().getLocalName());
        }

        monAgent.println("-".repeat(40));

    }

    //function triggered by a INFORM msg : a voter accept the result
    // @Override
    protected void handleInform(ACLMessage inform) {
        monAgent.println("the vote is accepted by " + inform.getSender().getLocalName());
    }


};