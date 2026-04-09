package sales.model;

public class Price {

    public int dollars;
    public int cents;

    public Price(int dollars, int cents){
        if(cents >= 100){
            dollars += cents / 100;
            cents /= 100;
        }
        this.dollars = dollars;
        this.cents = cents;
    }

    public void add(Price other){
        this.dollars += other.dollars;
        this.cents += other.cents;
        if(cents >= 100){
            this.dollars += cents / 100;
            cents /= 100;
        }
    }

    public void subtract(Price other){
        this.dollars -= other.dollars;
        this.cents -= other.cents;
        if(cents < 0){
            dollars -= 1;
            cents += 100;
        }
    }

    public int compareTo(Price other){
        return this.dollars == other.dollars ? this.dollars - other.dollars : this.cents - other.cents
    }

    public String toString(){
        return "" + this.dollars + "." + this.cents;
    }
}