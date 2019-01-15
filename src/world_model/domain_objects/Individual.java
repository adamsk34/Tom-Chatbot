package world_model.domain_objects;

import java.util.* ;
import mind.*;

public class Individual extends Thing
{
  public static final int DEF_SCHOOL_CONF = 70 ;
  public static final int DEF_ISMALE_CONF = 50 ;
  public static final int DEF_ISFAMOUS_CONF = 70 ;
  public static final int DEF_ISALIVE_CONF = 90 ;
  public static final int DEF_ISPERSON_CONF = 99 ;
  
  private int reference ;
  
  private MyString placeOfBirth ;
  private MyString nationality ;
  private MyString job ;
  private MyString employer ;
  private MyString school = new MyString(null, DEF_SCHOOL_CONF) ;
  private MyInt age = new MyInt(18, 1) ;
  private MyBoolean isMale = new MyBoolean(false, DEF_ISMALE_CONF) ;
  private Individual parent1 ;
  private Individual parent2 ;
  private Individual spouse ;
  private MyInt myMood = new MyInt(5, 50) ;
  private ArrayList<Individual> childArr ;
  private ArrayList<Individual> siblingArr ;
  private ArrayList<Individual> friendArr ;
  private ArrayList<Individual> coworkerArr ;
  private ArrayList<Individual> dateArr ;
  private ArrayList<Individual> likesIndividualsArr ;
  private ArrayList<Activity> likesActivitiesArr ;
  private ArrayList<Activity> doingArr ;
  private MyBoolean isFamous = new MyBoolean(false, DEF_ISFAMOUS_CONF) ;
  private MyBoolean isAlive = new MyBoolean(true, DEF_ISALIVE_CONF) ;
  private MyBoolean isPerson = new MyBoolean(true, DEF_ISPERSON_CONF) ;
  
  private Chatbot tom ;
  
  public Individual (Chatbot tomGiven)
  {
    tom = tomGiven ;
    
    setReference() ;
    
    initializeArrs() ;
    
    likesIndividualsArr.add(this) ;
  }
  public Individual (Chatbot tomGiven, String name, int confidence)
  {
    tom = tomGiven ;
    
    setReference() ;
    
    initializeArrs() ;
    
    getNameArr().add( new MyString(name,confidence) ) ;
    likesIndividualsArr.add(this) ;
  }
  
  private void setReference ()
  {
    reference = tom.getNextRef() ;
  }
  
  public void initializeArrs ()
  {
    childArr = new ArrayList<Individual>() ;
    siblingArr = new ArrayList<Individual>() ;
    friendArr = new ArrayList<Individual>() ;
    coworkerArr = new ArrayList<Individual>() ;
    dateArr = new ArrayList<Individual>() ;
    likesIndividualsArr = new ArrayList<Individual>() ;
    likesActivitiesArr = new ArrayList<Activity>() ;
    doingArr = new ArrayList<Activity>() ;
  }
  
  public boolean equals (Individual given)
  {
    boolean result = true ;
    
    // compare names only
    if(getNameArr().size() == given.getNameArr().size())
    {
      for(int i = 0 ; i < getNameArr().size() && result ; i++)
      {
        if(!getNameArr().get(i).equals( given.getNameArr().get(i) ))
          result = false ;
      }
    }
    else
      result = false ;
    
    return result ;
  }// equals
  
  public String bestName ()
  {
    String result = null ;
    int topMatch = 0 ;
    int currMatch = 0 ;
    
    topMatch = 0 ;
    // find name with highest match, < 100
    for(int i = 0 ; i < getNameArr().size() ; i++)
    {
      currMatch = getNameArr().get(i).getConfidence() ;
      if(currMatch > topMatch && currMatch < 100)
      {
        topMatch = currMatch ;
        // tempMyString is the best choice of name
        result = getNameArr().get(i).getStr() ;
      }
    }
    
    return result ;
  }
  
  public boolean searchArrayListIndividual (ArrayList<Individual> arr, Individual key)
  {
    boolean found = false ;
    
    if(arr != null)
    {
      for(int i = 0 ; i < arr.size() && !found && key != null ; i++)
      {
        if(arr.get(i) == key)
        {
          found = true ;
        }
      }
    }
    else
      System.out.println("ERROR: Individual.searchArrayListIndividual(), arr == null") ;
    
    return found ;
  }// searchArrayListIndividual
  
  public boolean searchArrayListActivity (ArrayList<Activity> arr, Activity key)
  {
    boolean found = false ;
    
    if(arr != null)
    {
      for(int i = 0 ; i < arr.size() && !found && key != null ; i++)
      {
        if(arr.get(i) == key)
        {
          found = true ;
        }
      }
    }
    else
      System.out.println("ERROR: Individual.searchArrayListActivity(), arr == null") ;
    
    return found ;
  }// searchArrayListActivity
  
  public ArrayList<String> findRelations (Individual ind)
  {
    ArrayList<String> result = new ArrayList<String>() ;
    
    // check child relation
    if(  searchArrayListIndividual(childArr, ind)  )
    {
      if(ind.isMale())
        result.add("son") ;
      else if(ind.isFemale())
        result.add("daughter") ;
      else
        result.add("child") ;
    }
    
    // check sibling relation
    if(  searchArrayListIndividual(siblingArr, ind)  )
    {
      if(ind.isMale())
        result.add("brother") ;
      else if(ind.isFemale())
        result.add("sister") ;
      else
        result.add("sibling") ;
    }
    
    // check friend relation
    if(  searchArrayListIndividual(friendArr, ind)  )
    {
      result.add("friend") ;
    }
    
    // check coworker relation
    if(  searchArrayListIndividual(coworkerArr, ind)  )
    {
      result.add("coworker") ;
    }
    
    // check date relationship
    if(  searchArrayListIndividual(dateArr, ind)  )
    {
      if(ind.isMale())
        result.add("boyfriend") ;
      else if(ind.isFemale())
        result.add("girlfriend") ;
      else
        result.add("partner") ;
    }
    
    // check parent1
    if(parent1 == ind)
    {
      if(ind.isMale())
        result.add("father") ;
      else if(ind.isFemale())
        result.add("mother") ;
      else
        result.add("parent") ;
    }
    // check parent2
    if(parent2 == ind)
    {
      if(ind.isMale())
        result.add("father") ;
      else if(ind.isFemale())
        result.add("mother") ;
      else
        result.add("parent") ;
    }
    
    return result ;
  }// findRelations
  
  public boolean areRelated (String rel, Individual ind)
  {
    boolean result = false ;
    
    if(rel.equals("child")  ||  (rel.equals("son") && ind.isMale())  ||  (rel.equals("daughter") && ind.isFemale()))
    {
      if(searchArrayListIndividual(  childArr, ind  ))
        result = true ;
    }
    else if(rel.equals("sibling")  ||  (rel.equals("brother") && ind.isMale())  ||  (rel.equals("sister") && ind.isFemale()))
    {
      if(searchArrayListIndividual(  siblingArr, ind  ))
        result = true ;
    }
    else if(rel.equals("friend"))
    {
      if(searchArrayListIndividual(  friendArr, ind  ))
        result = true ;
    }
    else if(rel.equals("coworker"))
    {
      if(searchArrayListIndividual(  coworkerArr, ind  ))
        result = true ;
    }
    else if(rel.equals("date")  ||  (rel.equals("boyfriend") && ind.isMale())  ||  (rel.equals("girlfriend") && ind.isFemale()))
    {
      if(searchArrayListIndividual(  dateArr, ind  ))
        result = true ;
    }
    else if(rel.equals("parent")  ||  (rel.equals("father") && ind.isMale())  ||  (rel.equals("mother") && ind.isFemale()))
    {
      if(parent1 == ind || parent2 == ind)
        result = true ;
    }
    else
      System.out.println("ERROR: Individual.areRelated(), unknown rel, rel == " + rel) ;
    
    return result ;
  }// areRelated
  
  public int maleConfidence ()
  {
    int result ;
    
    result = isMale.getConfidence() ;
    
    if(!isMale.getBool())
      result = 100 - result ;
    
    return result ;
  }// maleConfidence
  
  public int getReference ()
  {
    return reference ;
  }
  
  public MyString getPlaceOfBirth ()
  {
    return placeOfBirth ;
  }
  public MyString getJob ()
  {
    return job ;
  }
  public MyString getEmployer ()
  {
    return employer ;
  }
  public MyString getSchool ()
  {
    return school ;
  }
  public MyInt getAge ()
  {
    return age ;
  }
  public MyBoolean getIsMale ()
  {
    return isMale ;
  }
  public MyString getNationality ()
  {
    return nationality ;
  }
  public Individual getParent1 ()
  {
    return parent1 ;
  }
  public Individual getParent2 ()
  {
    return parent2 ;
  }
  public Individual getSpouse ()
  {
    return spouse ;
  }
  public MyInt getMyMood ()
  {
    return myMood ;
  }
  
  public ArrayList<Individual> getFriendArr ()
  {
    return friendArr ;
  }
  public ArrayList<Individual> getChildArr ()
  {
    return childArr ;
  }
  public ArrayList<Individual> getSiblingArr ()
  {
    return siblingArr ;
  }
  public ArrayList<Individual> getCoworkerArr ()
  {
    return coworkerArr ;
  }
  public ArrayList<Individual> getDateArr ()
  {
    return dateArr ;
  }
  public ArrayList<Individual> getLikesIndividualsArr ()
  {
    return likesIndividualsArr ;
  }
  public ArrayList<Activity> getLikesActivitiesArr ()
  {
    return likesActivitiesArr ;
  }
  public ArrayList<Activity> getDoingArr ()
  {
    return doingArr ;
  }
  public MyBoolean getIsFamous ()
  {
    return isFamous ;
  }
  public MyBoolean getIsAlive ()
  {
    return isAlive ;
  }
  public MyBoolean getIsPerson ()
  {
    return isPerson ;
  }
  
  public void setPlaceOfBirth (MyString given)
  {
    placeOfBirth = given ;
  }
  public void setJob (MyString given)
  {
    job = given ;
  }
  public void setEmployer (MyString given)
  {
    employer = given ;
  }
  public void setSchool (MyString given)
  {
    school = given ;
  }
  public void setAge (MyInt given)
  {
    age = given ;
  }
  public void setIsMale (MyBoolean given)
  {
    isMale = given ;
  }
  public void setNationality (MyString given)
  {
    nationality = given ;
  }
  public void setParent1 (Individual given)
  {
    parent1 = given ;
  }
  public void setParent2 (Individual given)
  {
    parent2 = given ;
  }
  public void setSpouse (Individual given)
  {
    spouse = given ;
  }
  public void setMyMood (MyInt given)
  {
    myMood = given ;
  }
  public void setFriendArr (ArrayList<Individual> given)
  {
    friendArr = given ;
  }
  public void setChildArr (ArrayList<Individual> given)
  {
    childArr = given ;
  }
  public void setSiblingArr (ArrayList<Individual> given)
  {
    siblingArr = given ;
  }
  public void setCoworkerArr (ArrayList<Individual> given)
  {
    coworkerArr = given ;
  }
  public void setDateArr (ArrayList<Individual> given)
  {
    dateArr = given ;
  }
  public void setLikesIndividualsArr (ArrayList<Individual> given)
  {
    likesIndividualsArr = given ;
  }
  public void setLikesActivitiesArr (ArrayList<Activity> given)
  {
    likesActivitiesArr = given ;
  }
  public void setDoingArr (ArrayList<Activity> given)
  {
    doingArr = given ;
  }
  public void setIsFamous (MyBoolean given)
  {
    isFamous = given ;
  }
  public void setIsAlive (MyBoolean given)
  {
    isAlive = given ;
  }
  public void setIsPerson (MyBoolean given)
  {
    isPerson = given ;
  }
  
  public boolean isMale ()
  {
    return(isMale.getBool() && isMale.getConfidence() > Chatbot.MATCH_MINIMUM_STATEMENT) ;
  }
  public boolean isFemale ()
  {
    return(!isMale.getBool() && isMale.getConfidence() > Chatbot.MATCH_MINIMUM_STATEMENT) ;
  }
  
  public String toString ()
  {
    return "IndRef" + reference ;
  }
  
  public void print ()
  {
    System.out.println("- - - - - - - - - - - - - - - - - - - -") ;
    System.out.println("                Individual:\n") ;
    System.out.println("           reference == " + reference) ;
    System.out.println("             nameArr == " + getNameArr()) ;
    System.out.println("        placeOfBirth == " + placeOfBirth) ;
    System.out.println("         nationality == " + nationality) ;
    System.out.println("            location == " + getLocation()) ;
    System.out.println("                 job == " + job) ;
    System.out.println("            employer == " + employer) ;
    System.out.println("              school == " + school) ;
    System.out.println("                 age == " + age) ;
    System.out.println("              isMale == " + isMale) ;
    System.out.println("             parent1 == " + parent1) ;
    System.out.println("             parent2 == " + parent2) ;
    System.out.println("              spouse == " + spouse) ;
    System.out.println("              myMood == " + myMood) ;
    System.out.println("            childArr == " + childArr) ;
    System.out.println("          siblingArr == " + siblingArr) ;
    System.out.println("           friendArr == " + friendArr) ;
    System.out.println("         coworkerArr == " + coworkerArr) ;
    System.out.println("             dateArr == " + dateArr) ;
    System.out.println(" likesIndividualsArr == " + likesIndividualsArr) ;
    System.out.println("  likesActivitiesArr == " + likesActivitiesArr) ;
    System.out.println("            doingArr == " + doingArr) ;
    System.out.println("            isFamous == " + isFamous) ;
    System.out.println("             isAlive == " + isAlive) ;
    System.out.println("            isPerson == " + isPerson) ;
    System.out.println("\n- - - - - - - - - - - - - - - - - - - -\n\n\n") ;
  }
}













