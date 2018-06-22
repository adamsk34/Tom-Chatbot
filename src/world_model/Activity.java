package world_model;

import java.util.* ;

public class Activity
{
  private ArrayList<String> presentFormArr = null ;
  private ArrayList<String> pastFormArr = null ;
  private ArrayList<String> pastParticipleFormArr = null ;
  
  private Activity next = null ;
  
  public void addPresent (String str)
  {
    // initialize arr if necessary
    if(presentFormArr == null && str != null)
      presentFormArr = new ArrayList<String>() ;
    
    addArr(presentFormArr, str) ;
  }// addPresent
  
  public void addPast (String str)
  {
    // initialize arr if necessary
    if(pastFormArr == null && str != null)
      pastFormArr = new ArrayList<String>() ;
    
    addArr(pastFormArr, str) ;
  }// addPast
  
  public void addPastParticiple (String str)
  {
    // initialize arr if necessary
    if(pastParticipleFormArr == null && str != null)
      pastParticipleFormArr = new ArrayList<String>() ;
    
    addArr(pastParticipleFormArr, str) ;
  }// addPastParticiple
  
  private void addArr (ArrayList<String> arr, String str)
  {
    String[] tokens ;
    
    if(str != null)
    {
      tokens = str.split("/");
      for(String token : tokens)
      {
        arr.add(token) ;
      }
    }
    else
      System.out.println("ERROR: Activity.addArr(), str == null") ;
  }// addArr
  
  public ArrayList<String> getPresentFormArr ()
  {
    return presentFormArr ;
  }
  
  public ArrayList<String> getPastFormArr ()
  {
    return pastFormArr ;
  }
  
  public ArrayList<String> getPastParticipleFormArr ()
  {
    return pastParticipleFormArr ;
  }
  
  public boolean searchPresentFormArr (String key)
  {
    return searchArr(presentFormArr, key) ;
  }
  
  public boolean searchPastFormArr (String key)
  {
    return searchArr(pastFormArr, key) ;
  }
  
  public boolean searchPastParticipleFormArr (String key)
  {
    return searchArr(pastParticipleFormArr, key) ;
  }
  
  private boolean searchArr (ArrayList<String> arr, String key)
  {
    boolean found = false ;
    
    if(arr != null)
    {
      // linear search
      for(int i = 0 ; i < arr.size() && !found ; i++)
      {
        if(arr.get(i).equals(key))
          found = true ;
      }
    }// if it is null that is perfectly fine
    
    return found ;
  }// searchArr
  
  public boolean isActivity (String key)
  {
    boolean found = false ;
    
    // check if key is present form
    found = searchPresentFormArr(key) ;
    
    // check if key is past form
    if(!found)
      found = searchPastFormArr(key) ;
    
    // check if key is past participle form
    if(!found)
      found = searchPastParticipleFormArr(key) ;
    
    return found ;
  }// isActivity
  
  public Activity getNext ()
  {
    return next ;
  }
  
  public void setNext (Activity n)
  {
    next = n ;
  }
}// class Activity











