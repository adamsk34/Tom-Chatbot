import java.util.* ;
import java.io.* ;

public class AI_Tom
{
  public static void main (String[] args) throws IOException
  {
    Scanner scanner = new Scanner(System.in) ;
    ChatBot tom = new ChatBot() ;
    String line = "" ;// the line the user enters
    String response ;// the response Tom gives back
    
    while(!tom.isQuitLine(line))
    {
      line = scanner.nextLine() ;
      
      response = tom.sendToTom(line) ;
      
      //System.out.println("\"" + line + "\"") ;
      
      if(response != null)
        System.out.println(response) ;
      else
        System.out.println("ERROR: AI_Tom.main(), Tom responded with null") ;
    }
    
    tom.printStats() ;
  }
}