import java.util.* ;
import java.io.* ;
import mind.*;

public class App
{
  public static void main (String[] args) throws IOException
  {
    Scanner scanner = new Scanner(System.in) ;
    Chatbot tom = new Chatbot() ;
    String userInput = "" ;// the line the user enters
    String response ;// the response Tom gives back
    
    while(!tom.isQuitLine(userInput))
    {
      userInput = scanner.nextLine() ;
      
      response = tom.sendToTom(userInput) ;

      if(response != null)
        System.out.println(response) ;
      else
        System.out.println("ERROR: App.main(), Tom responded with null") ;
    }
    
    tom.printStats() ;
  }
}
