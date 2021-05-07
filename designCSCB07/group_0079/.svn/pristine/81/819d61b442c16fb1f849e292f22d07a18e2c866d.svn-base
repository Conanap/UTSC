public class MoPed  extends Bicycle{
    private int percentCharged;

    public MoPed (int minG, int maxG, int charge) {
        super(minG, maxG);
        this.setCharge(charge);
    }
    public void setCharge(int charge) {
        if(charge <= 100 && charge >= 0)
            this.percentCharged = charge;
    }
    public int getCharge() {
        return this.percentCharged;
    }
    public void printDescription() {
        // override me!
        System.out.println("\nBike is " + " in gear " + this.getGear()
               + "and travelling at a speed of " + this.getSpeed() + " and charge "
               + this.percentCharged);
    }
}
