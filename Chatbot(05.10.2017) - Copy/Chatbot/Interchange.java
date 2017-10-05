
public class Interchange
{
  private String input ;
  private String output ;
  
  public Interchange ()
  {
    input = "" ;
    output = "" ;
  }
  public Interchange (String in)
  {
    input = in ;
    output = "" ;
  }
  public void setOutput (String out)
  {
    output = out ;
  }
  public String getInput ()
  {
    return input ;
  }
  public String getOutput ()
  {
    return output ;
  }
  public void print ()
  {
    System.out.println("Input : " + input) ;
    System.out.println("Output: " + output + "\n") ;
  }
  public String toString ()
  {
    return("Input : " + input + "\nOutput: " + output + "\n\n") ;
  }
}// class Interchange