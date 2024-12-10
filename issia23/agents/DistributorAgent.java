package issia23.agents;

import issia23.data.Product;
import jade.core.AgentServicesTools;
import jade.gui.AgentWindowed;
import jade.gui.SimpleWindow4Agent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DistributorAgent extends AgentWindowed {
    Set<Product> products;

    @Override
    public void setup(){
        this.window = new SimpleWindow4Agent(getLocalName(),this);
        this.window.setBackgroundTextColor(Color.GRAY);
        println("hello, do you want a new object  ?");

        //registration to the yellow pages (Directory Facilitator Agent)
        AgentServicesTools.register(this, "repair", "distributor");
        println("I'm just registered as a Distributor");

        products = new HashSet<>();
        var allProducts = Product.getListProducts();
        var nb = allProducts.size();
        var nbStock = (int)(nb*.8);
        for(int i=0; i<nbStock; i++) {
            var rand = (int)(Math.random()*nb);
            products.add(allProducts.get(rand));
        }
        println("i have the following products : ");
        for(var p:products) println(p.getName() + " ");

    }

}
