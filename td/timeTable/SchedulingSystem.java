package td.timeTable;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

// Main pour lancer le système
public class SchedulingSystem {
    public static void main(String[] args) {
        // Création du conteneur principal JADE
        jade.core.Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.GUI, "true");

        ContainerController cc = rt.createMainContainer(p);

        try {
            // Création de l'agent secrétaire
            AgentController secretaryAgent = cc.createNewAgent(
                    "secretary",
                    SecretaryAgent.class.getName(),
                    null
            );
            secretaryAgent.start();

            // Création des agents professeurs
            String[] teachers = {"e1", "e2", "e3"};
            for (String teacher : teachers) {
                AgentController teacherAgent = cc.createNewAgent(
                        teacher,
                        TeacherAgent.class.getName(),
                        new Object[]{teacher}
                );
                teacherAgent.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
