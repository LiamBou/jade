package td.ticktackmoe;

import helloWorldService.gui.SimpleGui4Agent;
import jade.core.AID;
import jade.core.AgentServicesTools;
import jade.core.behaviours.ReceiverBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.awt.*;

public class PlayerAgent extends GuiAgent {
    Gui4Agent window;

    AID[] neighbourgs;

    int id;

    boolean isMyTurn;

    TickTackMoeGame game;

    @Override
    protected void setup(){
        String[] args = (String[]) this.getArguments();
        window = new Gui4Agent(this);
        id = ((args != null && args.length > 0) ? Integer.parseInt(args[0]) : -1);
        isMyTurn = false;
        game = new TickTackMoeGame();
        window.println("Player " + id + " is ready");

        AgentServicesTools.register(this, "cordiality", String.valueOf(id));
        window.mainTextArea.setBackground(Color.pink);
        if (id == 1){
            isMyTurn = true;
            window.println("It's my turn");
            window.println(game.toString());
        }else {
            window.println("Player 1 is going to play");
        }

        addBehaviour(new ReceiverBehaviour(this, -1, null, true, (a, msg)->{
            if (msg.getPerformative() == ACLMessage.INFORM){
                if (msg.getContent().equals("play")){
                    isMyTurn = true;
                    window.println("It's my turn");
                    window.println(game.toString());
                }else if (msg.getContent().equals("wait")){
                    isMyTurn = false;
                    window.println("Player 1 is going to play");
                }else if (msg.getContent().startsWith("play")){
                    String[] parts = msg.getContent().split(" ");
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    char player = parts[3].charAt(0);
                    game.play(x, y, player);
                    window.println("Player " + player + " played at " + x + " " + y);
                    window.println(game.toString());
                    if (game.isWin(player)){
                        window.println("Player " + player + " wins");
                        window.println("Game over");
                        doDelete();
                    }else if (game.isFull()){
                        window.println("It's a draw");
                        window.println("Game over");
                        doDelete();
                    }
                }
            }
        }));
    }

    /**
     * Reaction to the event transmitted by the window
     *
     * @param ev evenement
     */
    protected void onGuiEvent(GuiEvent ev) {
        switch (ev.getType()) {
            //case SimpleGui4Agent.PLAY -> sendMessage(window.lowTextArea.getText(),"lobby");
            case SimpleGui4Agent.QUITCODE -> doDelete();
        }
    }

    /**deregister to the service and close the window before leaving
     * */
    @Override
    protected void takeDown() {
        // S'effacer du service pages jaunes
        AgentServicesTools.deregisterAll(this);
        //fermer la fenetre
        window.dispose();
        //bye
        System.err.println("Agent : " + getAID().getName() + " quitte la plateforme.");
    }
}
