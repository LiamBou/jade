package protocols.negociation;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CompAcheteur extends Behaviour {

    int step = 0;
    boolean accord;
    boolean rejet;
    double offreAutre, offrePrecedente, offre;
    double epsilon = 0.05;
    Negociateur monAgent;
    MessageTemplate modele;

    CompAcheteur(Negociateur monAgent, MessageTemplate modele){
        super(monAgent);
        this.monAgent = monAgent;
        this.modele = modele;
        monAgent.seuil = 110;
    }

    @Override
    public void action() {
        var msg = myAgent.receive(modele);
        if (msg != null) {
            switch (msg.getPerformative()){
                case ACLMessage.AGREE -> {
                    accord = true;
                    monAgent.println("accord trouve avec l autre sur %.2f".formatted(Double.valueOf(msg.getContent())));}
                case ACLMessage.REFUSE -> rejet = true;
                case ACLMessage.FAILURE -> rejet = true;
            }
            if(!accord && !rejet) {
                step++;
                offreAutre = Double.parseDouble(msg.getContent());
                monAgent.println("j'ai reçu une offre à %.2f".formatted(offreAutre));
                if (offreAutre <= offrePrecedente) accord = true;
                if (offreAutre > monAgent.seuil) rejet = true;
                if (!accord && !rejet) {
                    if (offrePrecedente == 0) offrePrecedente = monAgent.prixSouhaite;
                    offre = offrePrecedente * (1 + epsilon);
                    offrePrecedente = offre;
                    var reponse = msg.createReply();
                    reponse.setContent(String.valueOf(offre));
                    myAgent.send(reponse);
                    monAgent.println("je propose %.2f".formatted(offre));
                }
            }
            if(accord){
                var reponse = msg.createReply();
                reponse.setContent(String.valueOf(offreAutre));
                reponse.setPerformative(ACLMessage.AGREE);
                myAgent.send(reponse);
                monAgent.println("j'envoie mon accord");
            }
            if(rejet){
                var reponse = msg.createReply();
                reponse.setPerformative(ACLMessage.REFUSE);
                myAgent.send(reponse);
            }
            if(step == 10){
                var reponse = msg.createReply();
                reponse.setPerformative(ACLMessage.FAILURE);
                myAgent.send(reponse);
            }
        } else block();
    }

    @Override
    public boolean done() {
        if (accord)
            monAgent.println("Fin sur un accord ");
        if (rejet)
            monAgent.println("Fin sur un rejet ");
        if (step == 10)
            monAgent.println("Temps de négociation expiré");
        return step == 10 || accord || rejet;
    }
}
