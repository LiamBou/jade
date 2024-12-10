package issia23.data;

import java.io.Serializable;
import java.util.Random;


public enum ProductType implements Serializable {
    Cafetiere( 3, 40d),
    LaveLinge( 3,  200d),
    SourisOrdi( 2,  40d),
    Aspirateur( 3,  100d),
    LaveVaisselle( 2,  200d);

    public double getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(double standardPrice) {
        this.standardPrice = standardPrice;
    }

    public int getNbParts() {
        return nbParts;
    }

    public void setNbParts(int nbParts) {
        this.nbParts = nbParts;
    }

    double standardPrice;
    int nbParts;

    private final Random rand = new Random();

    ProductType(  int nbParts,  double standardPrice){
        this.nbParts = nbParts;
        this.standardPrice = standardPrice;
    }

/*    @Override
    public String toString() {
        return String.format("%s,  difficulty=%d, transportable=%b, danger=%b " +
                "\n\tnb smallParts=%d\n\tnb bigParts=%d",
                this.name(), difficulty, transportable, danger, nbSmallParts, nbBigParts);
    }
*/



}
