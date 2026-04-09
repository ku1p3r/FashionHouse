package sales.model;

import common.Serializable;

/**
 * @author Mason Hart
 */
public class Price {

    private int dollars;
    private int cents;

    public Price(int dollars, int cents){
        if(cents >= 100){
            dollars += cents / 100;
            cents /= 100;
        } else if(cents < 0){
            dollars -= 1;
            cents += 100;
        }
        this.dollars = dollars;
        this.cents = cents;
    }

    public Price(Price other){
        this.dollars = other.getDollars();
        this.cents = other.getCents();
    }

    public int getDollars(){
        return this.dollars;
    }
    public int getCents(){
        return this.cents;
    }

    public Price add(Price other){
        return new Price(
                this.dollars + other.getDollars(),
                this.cents + other.getCents()
        );
    }

    public Price subtract(Price other){
        return new Price(
                this.dollars - other.getDollars(),
                this.cents - other.getCents()
        );
    }

    public int compareTo(Price other){
        return this.dollars == other.getDollars() ? this.dollars - other.getDollars() : this.cents - other.getCents();
    }

    public String toString(){
        return "" + this.dollars + "." + this.cents;
    }
}