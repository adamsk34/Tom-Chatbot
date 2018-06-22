package world_model;

import java.util.* ;
import mind.*;

public class Thing
{
  private ArrayList<MyString> nameArr ;
  private ArrayList<Individual> ownerArr ;
  private MyString location ;
  private MyBoolean real ;
  
  public Thing ()
  {
    nameArr = new ArrayList<MyString>() ;
    ownerArr = new ArrayList<Individual>() ;
  }
  public ArrayList<MyString> getNameArr ()
  {
    return nameArr ;
  }
  public ArrayList<Individual> getOwnerArr ()
  {
    return ownerArr ;
  }
  public boolean isName (String name)
  {
    boolean result = false ;
    
    // check nameArr to see if this is my name
    for(int i = 0 ; i < nameArr.size() && !result ; i++)
    {
      if(sameUntil(nameArr.get(i).getStr(), name) &&
         nameArr.get(i).getConfidence() >= ChatBot.MATCH_MINIMUM_STATEMENT)
      {
        result = true ;
      }
    }
    
    return result ;
  }// isName
  
  public String findName (String name)
  {
    String result = null ;
    
    // check nameArr to see if this is my name
    for(int i = 0 ; i < nameArr.size() && result == null ; i++)
    {
      if(sameUntil(nameArr.get(i).getStr(), name) &&
         nameArr.get(i).getConfidence() >= ChatBot.MATCH_MINIMUM_STATEMENT)
      {
        result = nameArr.get(i).getStr() ;
      }
    }
    
    return result ;
  }// findName
  
  public boolean sameUntil (String name1, String name2)
  {
    boolean result = true ;
    
    if(name1 != null && name2 != null && name2.length() >= name1.length())
    {
      // make sure name1 isn't just a part of a word in name2
      if(name1.length() == name2.length() || !ChatBot.isLetter(name2.charAt( name1.length() )) )
      {
        for(int i = 0 ; i < name1.length() && result ; i++)
        {
          if(name1.charAt(i) != name2.charAt(i))
            result = false ;
        }// for
      }
      else
        result = false ;
    }
    else
      result = false ;
    
    return result ;
  }// sameUntil
  
  public MyString getLocation ()
  {
    return location ;
  }
  
  
  public void setNameArr (ArrayList<MyString> given)
  {
    nameArr = given ;
  }
  public void setOwnerArr (ArrayList<Individual> given)
  {
    ownerArr = given ;
  }
  public void setLocation (MyString given)
  {
    location = given ;
  }
}
