package issia23.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class  Part implements Serializable {
    String name;
    int serialNumber;
    int difficulty;
    double price;

    static List<Part> listParts= new ArrayList<>();

    public Part(String name, int serialNumber, int difficulty, double price) {
        this.name = name;
        this.serialNumber = serialNumber;
        this.difficulty = difficulty;
        this.price = price;
        listParts.add(this);
    }

    public Part(Part p, double priceVariation) {
        this(p.name, p.serialNumber, p.difficulty, p.price*priceVariation);
    }

    public String toString() {
        return String.format("Pièce{ ref. %d : %s - diff. %d - danger.  - prix %.2f€}", serialNumber, name, difficulty, price) ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        return serialNumber == part.serialNumber;
    }

    public String getName() {
        return name;
    }


    public int getSerialNumber() {
        return serialNumber;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static List<Part> getListParts() {
        return listParts;
    }
}
