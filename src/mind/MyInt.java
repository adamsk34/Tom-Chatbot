package mind;

public class MyInt
{
  int num ;
  int confidence ;
  
  public MyInt (int numGiven, int confidenceGiven)
  {
    num = numGiven ;
    confidence = confidenceGiven ;
  }
  
  public void setConfidence (int confidenceGiven)
  {
    confidence = confidenceGiven ;
  }
  
  public int getConfidence ()
  {
    return confidence ;
  }
  
  public int getNum ()
  {
    return num ;
  }
  
  public String toString ()
  {
    return "\"" + num + " - " + confidence + "\"" ;
  }
}
