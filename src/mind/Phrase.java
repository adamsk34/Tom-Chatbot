package mind;

import java.util.* ;

public class Phrase
{
  private String myUnderstanding ;
  private String myMeaning ;
  private int myMatch ;
  
  private boolean isQuestion = false ;
  
  private int numBracketWords = 0 ;
  private int numNonBracketWords = 0 ;
  
  private int individual1 = -1 ;
  private int individual2 = -1 ;
  private String name = "" ;
  private String number = "" ;
  private String job = "" ;
  private String employer = "" ;
  private String school = "" ;
  private String mood = "" ;
  private String place = "" ;
  private String rel = "" ;
  private String activity = "" ;
  private String var = "" ;
  
  public Phrase ()
  {
    myUnderstanding = "" ;
    myMatch = 0 ;
  }
  public Phrase (String u, String me, int ma, boolean isQ)
  {
    myUnderstanding = u ;
    myMeaning = me ;
    myMatch = ma ;
    
    isQuestion = isQ ;
    
    countBracketNonBracket() ;
  }
  public String getMyUnderstanding ()
  {
    return myUnderstanding ;
  }
  public void setMyUnderstanding (String u)
  {
    myUnderstanding = u ;
    
    countBracketNonBracket() ;
  }
  public String getMyMeaning ()
  {
    return myMeaning ;
  }
  public void setMyMeaning (String m)
  {
    myMeaning = m ;
  }
  public void setMyMatch (int m)
  {
    myMatch = m ;
  }
  public int getMyMatch ()
  {
    return myMatch ;
  }
  public String toString ()
  {
    return myMatch + " - " + myUnderstanding + "   :-   " + myMeaning ;
  }
  
  public boolean isQuestion ()
  {
    return isQuestion ;
  }
  
  private void countBracketNonBracket ()
  {
    // check if myUnderstanding is valid
    if(myUnderstanding != null && myUnderstanding.length() > 0)
    {
      if(myUnderstanding.charAt(0) == '[')
      {
        numBracketWords = 1 ;
        numNonBracketWords = 0 ;
      }
      else
      {
        numBracketWords = 0 ;
        numNonBracketWords = 1 ;
      }
        
      for(int c = 1   ; c < myUnderstanding.length() -1   ; c++)
      {
        if(myUnderstanding.charAt(c) == ' ')
        {
          if(myUnderstanding.charAt(c+1) == '[')
            numBracketWords++ ;
          else
            numNonBracketWords++ ;
        }
      }
    }
  }// countBracketNonBracket
  
  public int getNumBracketWords ()
  {
    return numBracketWords ;
  }
  
  public int getNumNonBracketWords ()
  {
    return numNonBracketWords ;
  }
  
  public int getIndividual1 ()
  {
    return individual1 ;
  }
  public void setIndividual1 (int num)
  {
    individual1 = num ;
  }
  public int getIndividual2 ()
  {
    return individual2 ;
  }
  public void setIndividual2 (int num)
  {
    individual2 = num ;
  }
  public String getName ()
  {
    return name ;
  }
  public void setName (String str)
  {
    name = str ;
  }
  public String getNumber ()
  {
    return number ;
  }
  public void setNumber (String str)
  {
    number = str ;
  }
  public String getJob ()
  {
    return job ;
  }
  public void setJob (String str)
  {
    job = str ;
  }
  public String getEmployer ()
  {
    return employer ;
  }
  public void setEmployer (String str)
  {
    employer = str ;
  }
  public String getSchool ()
  {
    return school ;
  }
  public void setSchool (String str)
  {
    school = str ;
  }
  public String getMood ()
  {
    return mood ;
  }
  public void setMood (String str)
  {
    mood = str ;
  }
  public String getPlace ()
  {
    return place ;
  }
  public void setPlace (String str)
  {
    place = str ;
  }
  public String getRel ()
  {
    return rel ;
  }
  public void setRel (String str)
  {
    rel = str ;
  }
  public String getActivity ()
  {
    return activity ;
  }
  public void setActivity (String str)
  {
    activity = str ;
  }
  public void setVar (String str)
  {
    var = str ;
  }
  public String getVar ()
  {
    return var ;
  }
}





