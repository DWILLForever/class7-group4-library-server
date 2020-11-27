import java.time.Month;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Test {
  public static void main(java.lang.String[] args) {
    String s=new String("hello world");
    System.out.println(s);
  }
  static class String{
    private final java.lang.String s;
    public String(java.lang.String s){
      this.s=s;
    }
    public java.lang.String toString(){
      return s;
    }
  }
}
