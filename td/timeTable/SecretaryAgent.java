package td.timeTable;

import com.google.gson.Gson;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Agent Secrétaire
public class SecretaryAgent extends Agent {
    private List<Room> rooms;
    private List<TimeSlot> allSlots;
    private final Gson gson = new Gson();
    private Map<String, Integer> teacherAssignments;

    protected void setup() {
        // Initialisation des salles
        rooms = new ArrayList<>();
        rooms.add(new Room("s1", true));
        rooms.add(new Room("s2", true));
        rooms.add(new Room("s3", false));

        // Initialisation du compteur d'affectations par professeur
        teacherAssignments = new HashMap<>();
        teacherAssignments.put("e1", 0);
        teacherAssignments.put("e2", 0);
        teacherAssignments.put("e3", 0);

        // Initialisation de tous les créneaux possibles
        initializeAllSlots();

        // Initialisation des contraintes des salles
        initializeRoomConstraints();

        // Comportement pour recevoir les demandes
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                MessageTemplate mt = MessageTemplate.or(
                        MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                        MessageTemplate.MatchPerformative(ACLMessage.INFORM)
                );
                ACLMessage msg = receive(mt);

                if (msg != null) {
                    if (msg.getPerformative() == ACLMessage.REQUEST) {
                        String teacher = msg.getSender().getLocalName();
                        List<TimeSlot> availableSlots = findAvailableSlots(teacher);

                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent(gson.toJson(availableSlots));
                        send(reply);
                    } else if (msg.getPerformative() == ACLMessage.INFORM) {
                        handleSlotSelection(msg);
                    }
                } else {
                    block();
                }
            }
        });

        System.out.println("Agent Secrétaire démarré");
    }

    private void initializeAllSlots() {
        allSlots = new ArrayList<>();
        int[] hours = {8, 10, 14, 16};
        for (int day = 1; day <= 2; day++) {
            for (int hour : hours) {
                allSlots.add(new TimeSlot(day, hour));
            }
        }
    }

    private void initializeRoomConstraints() {
        // Contraintes s1
        rooms.get(0).getUnavailableSlots().add(new TimeSlot(1, 10));

        // Contraintes s2
        rooms.get(1).getUnavailableSlots().add(new TimeSlot(2, 8));
        rooms.get(1).getUnavailableSlots().add(new TimeSlot(2, 16));

        // Contraintes s3
        rooms.get(2).getUnavailableSlots().add(new TimeSlot(1, 14));
        rooms.get(2).getUnavailableSlots().add(new TimeSlot(2, 16));
    }

    private List<TimeSlot> findAvailableSlots(String teacher) {
        List<TimeSlot> availableSlots = new ArrayList<>();

        for (TimeSlot slot : allSlots) {
            // Vérifier si au moins une salle est disponible pour ce créneau
            for (Room room : rooms) {
                if (room.isAvailable(slot)) {
                    TimeSlot newSlot = new TimeSlot(slot.getDay(), slot.getStartHour());
                    newSlot.setRoom(room);
                    newSlot.setTeacher(teacher);
                    availableSlots.add(newSlot);
                }
            }
        }

        return availableSlots;
    }

    private void bookSlot(TimeSlot slot) {
        for (Room room : rooms) {
            if (room.getName().equals(slot.getRoom().getName())) {
                room.bookSlot(slot);
                teacherAssignments.compute(slot.getTeacher(), (k, current) -> (current == null ? 1 : current + 1));
            }
        }
    }

    private void handleSlotSelection(ACLMessage msg) {
        TimeSlot selectedSlot = gson.fromJson(msg.getContent(), TimeSlot.class);
        bookSlot(selectedSlot);

        // Check if all teachers have their schedules
        boolean allScheduled = teacherAssignments.values().stream().allMatch(count -> count >= 2);
        if (allScheduled) {
            printRoomSchedules();
        }
    }

    private void printRoomSchedules() {
        for (Room room : rooms) {
            System.out.println("Schedule for room " + room.getName() + ":");
            for (TimeSlot slot : room.getBookedSlots()) {
                System.out.println("Day " + slot.getDay() + ": " + slot.getStartHour() + "h-" + slot.getEndHour() + "h" + " with " + slot.getTeacher());
            }
        }
    }
}