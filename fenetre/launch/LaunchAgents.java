package fenetre.launch;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;

public class LaunchAgents {
	 public static void main(String[] args) {
	  // preparer les arguments pout le conteneur JADE
	  Properties prop = new ExtendedProperties();
	  // demander la fenetre de controle
	  prop.setProperty(Profile.GUI, "true");
	  // nommer les agencesVoyages.agents
	  prop.setProperty(Profile.AGENTS, "a:fenetre.agencesVoyages.agents.AgentDirectEmissionButton;"
	  		+ "b:fenetre.agencesVoyages.agents.AgentReceptionClassique;c:fenetre.agencesVoyages.agents.AgentReceptionClassique;d:fenetre.agencesVoyages.agents.AgentReceptionClassique");
	  // creer le profile pour le conteneur principal
	  ProfileImpl profMain = new ProfileImpl(prop);
	  // lancer le conteneur principal
	  Runtime rt = Runtime.instance();
	  rt.createMainContainer(profMain);
	  }
	}
