public class Bicycle extends Vehicle {
    private int gear;
    private long speed;
    private int minimumGear;
    private int maximumGear;
    
    public Bicycle() {
        this.minimumGear = 0;
        this.maximumGear = 0;
        this.speed = 0L;
        this.gear = 0;
    }
    public Bicycle(int minG, int maxG) {
        this.minimumGear = minG;
        this.maximumGear = maxG;
        this.gear = minG;
        this.speed = 0L;
    }
    public void setGear(int gear) {
        if(gear <= this.maximumGear && gear >= minimumGear)
            this.gear = gear;
    }
    public int getGear() {
        return this.gear;
    }
    public void setSpeed(long speed) {
        this.speed = speed;
    }
    public long getSpeed() {
        return this.speed;
    }
    public void printDescription() {

        System.out.println("\nBike is " + "in gear " + this.gear
                + " and travelling at a speed of " + this.speed + ". ");
        // What happens when gear and speed are not set?
        // What kind of checks can you put in place to take care of non-?

    }

    final public void hitTheBreaks() {
        System.out.println("Break!");
    }

}
