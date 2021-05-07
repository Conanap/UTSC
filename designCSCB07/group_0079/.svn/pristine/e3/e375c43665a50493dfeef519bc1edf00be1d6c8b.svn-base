public class MountainBike extends Bicycle {
    // public MountainBike(int min, int max, SuspensionType t)
    // How can you reuse the constructor of the superclass?
    // You'll need to define a private variable to keep track of SuspensionType
    // don't forget to add getters/setters
    private SuspensionType susp;
    public MountainBike(int minGear, int maxGear, SuspensionType suspType) {
        super(minGear, maxGear);
        this.susp = suspType;
    }
    public SuspensionType getSuspensionType(){
        return this.susp;
    }
    public void printDescription() {
        // override me!
        System.out.println("\nBike is " + " in gear " + this.getGear()
               + "and travelling at a speed of " + this.getSpeed() + " and suspension "
               + this.susp);
    }

}
