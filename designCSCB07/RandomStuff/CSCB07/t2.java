public class t2{
    private static int count=0;
    public t2(){
        count++;
    }
    
    public void eat(int food) throws tException, t2Exception{
        if(food == 0)
            throw new tException();
        else if(food == 1)
            throw new t2Exception();
        else
            System.out.println("ate #" + food);
    }
    
    public static int count(){
        return count;
    }
}