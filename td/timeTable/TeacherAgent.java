package td.timeTable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// Agent Professeur
public class TeacherAgent extends Agent {
    private String teacherName;
    private List<TimeSlot> constraints;
    private List<TimeSlot> assignedSlots;
    private final double p1 = 0.6;
    private final double p2 = 0.4;
    private final Gson gson = new Gson();
    private int attemptCounter = 0;
    private final int maxAttempts = 1;

    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            teacherName = (String) args[0];
        }

        constraints = new ArrayList<>();
        assignedSlots = new ArrayList<>();
        initializeConstraints();

        // Comportement pour demander des créneaux
        addBehaviour(new OneShotBehaviour(this) {
            public void action() {
                requestSlots();
            }
        });

        // Comportement pour recevoir la réponse
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = receive(mt);

                if (msg != null) {
                    Type listType = new TypeToken<ArrayList<TimeSlot>>() {}.getType();
                    List<TimeSlot> proposedSlots = gson.fromJson(msg.getContent(), listType);

                    double satisfaction = calculateSatisfaction(proposedSlots);

                    if (satisfaction >= 0.8 && assignedSlots.size() < 6) {
                        // Accepter les meilleurs créneaux
                        selectBestSlots(proposedSlots);
                    }

                    // Si on n'a pas assez de créneaux, on en redemande
                    if (assignedSlots.size() < 6 && attemptCounter < maxAttempts) {
                        attemptCounter++;
                        requestSlots();
                    } else if (attemptCounter >= maxAttempts){
                        // Accept the current schedule even if not fully satisfied
                        System.out.println("Max attempts reached. Accepting current schedule for " + teacherName);
                        printSchedule();
                    } else {
                        // On a fini
                        printSchedule();
                    }
                } else {
                    block();
                }
            }
        });

        System.out.println("Agent Professeur " + teacherName + " démarré");
    }

    private void initializeConstraints() {
        switch(teacherName) {
            case "e1":
                constraints.add(new TimeSlot(1, 16));
                constraints.add(new TimeSlot(2, 14));
                break;
            case "e2":
                constraints.add(new TimeSlot(2, 10));
                constraints.add(new TimeSlot(1, 16));
                break;
            case "e3":
                constraints.add(new TimeSlot(1, 14));
                constraints.add(new TimeSlot(2, 8));
                break;
        }
    }

    private double calculateSatisfaction(List<TimeSlot> slots) {
        boolean constraint1Respected = true;
        boolean constraint2Respected = true;

        for (TimeSlot slot : slots) {
            if (conflictsWithConstraint(slot, 0)){
                constraint1Respected = false;
                System.out.println("Conflict with constraint 1: " + slot);
            }
            if (conflictsWithConstraint(slot, 1)){
                constraint2Respected = false;
                System.out.println("Conflict with constraint 2: " + slot);
            }
        }

        System.out.println("Constraint 1 respected: " + constraint1Respected);
        System.out.println("Constraint 2 respected: " + constraint2Respected);

        return p1 * (constraint1Respected ? 1 : 0) +
                p2 * (constraint2Respected ? 1 : 0);
    }

    private boolean conflictsWithConstraint(TimeSlot slot, int constraintIndex) {
        if (constraintIndex >= constraints.size()) return false;
        TimeSlot constraint = constraints.get(constraintIndex);
        System.out.println("Checking constraint " + constraint + " against slot " + slot + " result " + (slot.getDay() == constraint.getDay() && slot.getStartHour() == constraint.getStartHour()));
        return slot.getDay() == constraint.getDay() &&
                slot.getStartHour() == constraint.getStartHour();
    }

    private void selectBestSlots(List<TimeSlot> proposedSlots) {
        proposedSlots.stream()
                .filter(slot -> !conflictsWithConstraint(slot, 0))
                .filter(slot -> !conflictsWithConstraint(slot, 1))
                .limit(2)
                .forEach(slot -> {
                    assignedSlots.add(slot);
                    informSecretary(slot);
                });
    }

    private void informSecretary(TimeSlot slot) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("secretary", AID.ISLOCALNAME));
        msg.setContent(gson.toJson(slot));
        send(msg);
    }

    private void requestSlots() {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("secretary", AID.ISLOCALNAME));
        msg.setContent("request_slots");
        send(msg);
    }

    private void printSchedule() {
        System.out.println("Schedule for " + teacherName + ":");
        for (TimeSlot slot : assignedSlots) {
            System.out.println(slot);
        }
    }
}