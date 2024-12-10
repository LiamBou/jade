package issia23.agents;

import issia23.data.Part;
import jade.core.AgentServicesTools;
import jade.gui.AgentWindowed;
import jade.gui.SimpleWindow4Agent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SparePartsStoreAgent extends AgentWindowed {
    List<Part> parts;

    @Override
    public void setup(){
        this.window = new SimpleWindow4Agent(getLocalName(),this);
        this.window.setBackgroundTextColor(Color.cyan);
        println("hello, do you want a piece of something ?");

        //registration to the yellow pages (Directory Facilitator Agent)
        AgentServicesTools.register(this, "repair", "SparePartsStore");
        println("I'm just registered as a Spare Parts Store");


        //distributors have 3 examples of some parts
        parts = new ArrayList<>();
        var allParts = Part.getListParts();
        var nb = allParts.size();
        var nbStock = (int)(nb*.6);
        for(int i=0; i<nbStock; i++) {
            var rand = (int)(Math.random()*nb);
            for(int j=0; j<3; j++)
                parts.add(allParts.get(rand));
        }
        println("i have the following parts : ");
        for(var p:parts) println(p.getName() + " ");

    }

}
