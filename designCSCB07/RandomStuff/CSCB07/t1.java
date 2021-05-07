import java.util.Scanner;
public class t1{
    public static void main(){
        Scanner scan = new Scanner (System.in);
        String in;
        int input;
        while (true){
            System.out.println("num");
            in = scan.nextLine();
            input = Integer.parseInt(in);
            System.out.println("num2");
            in = scan.nextLine();
            System.out.println(LCM(input, Integer.parseInt(in)));
        }
    }
    public static int LCM (int x, int y) {
        if(x>y){
            int temp = x;
            x = y;
            y = temp;
        }
        int mult = x;
        while (mult <= x*y && mult%y != 0){
            mult += x;
        }
        return mult;
    }
}