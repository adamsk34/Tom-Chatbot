package mind;

public class MyBoolean
{
  boolean bool ;
  int confidence ;
  
  public MyBoolean (boolean boolGiven, int confidenceGiven)
  {
    bool = boolGiven ;
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
  
  public boolean getBool ()
  {
    return bool ;
  }
  
  public String toString ()
  {
    return "\"" + bool + " - " + confidence + "\"" ;
  }
}
