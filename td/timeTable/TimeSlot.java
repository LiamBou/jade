package td.timeTable;

import java.io.Serializable;
import java.util.Objects;

// Classe pour les créneaux horaires
class TimeSlot implements Serializable {
    private final int day;
    private final int startHour;
    private final int endHour;
    private Room room;
    private String teacher;
    private String group;

    public TimeSlot(int day, int startHour) {
        this.day = day;
        this.startHour = startHour;
        this.endHour = startHour + 2;
    }

    public int getDay() { return day; }
    public int getStartHour() { return startHour; }
    public int getEndHour() { return endHour; }
    public Room getRoom() { return room; }
    public String getTeacher() { return teacher; }
    public String getGroup() { return group; }

    public void setRoom(Room room) { this.room = room; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
    public void setGroup(String group) { this.group = group; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return day == timeSlot.day &&
                startHour == timeSlot.startHour &&
                endHour == timeSlot.endHour;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, startHour, endHour);
    }

    @Override
    public String toString() {
        return String.format("Jour %d: %dh-%dh", day, startHour, endHour);
    }
}
