package mind;

public class MyString
{
  String str ;
  int confidence ;
  
  public MyString (String strGiven, int confidenceGiven)
  {
    str = strGiven ;
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
  
  public String getStr ()
  {
    return str ;
  }
  
  public String toString ()
  {
    return "\"" + str + " - " + confidence + "\"" ;
  }
}
