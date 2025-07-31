package td.timeTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Classe pour les salles
class Room implements Serializable {
    private final String name;
    private final boolean hasProjector;
    private List<TimeSlot> unavailableSlots;
    private List<TimeSlot> bookedSlots;

    public Room(String name, boolean hasProjector) {
        this.name = name;
        this.hasProjector = hasProjector;
        this.unavailableSlots = new ArrayList<>();
        this.bookedSlots = new ArrayList<>();
    }

    public String getName() { return name; }
    public boolean hasProjector() { return hasProjector; }
    public List<TimeSlot> getUnavailableSlots() { return unavailableSlots; }
    public List<TimeSlot> getBookedSlots() { return bookedSlots; }

    public boolean isAvailable(TimeSlot slot) {
        return unavailableSlots.stream().noneMatch(s -> s.equals(slot)) &&
                bookedSlots.stream().noneMatch(s -> s.equals(slot));
    }

    public void bookSlot(TimeSlot slot) {
        bookedSlots.add(slot);
    }
}