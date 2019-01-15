package world_model;

import java.util.* ;
import mind.*;
import world_model.domain_objects.*;

public class WorldManager
{
  private static Chatbot tom ;
  private static NLP myNLP ;
  private int origNumInds = 2 ;
  
  private static ArrayList<Individual> individualsArr ;
  
  public WorldManager ()
  {
    individualsArr = new ArrayList<Individual>() ;
  }
  
  public void setTom (Chatbot tomGiven)
  {
    tom = tomGiven ;
  }
  
  public void setMyNLP (NLP givenNLP)
  {
    myNLP = givenNLP ;
  }
  
  public static ArrayList<Individual> getIndividualsArr ()
  {
    return individualsArr ;
  }
  
  public void setOrigNumInds ()
  {
    if(individualsArr != null)
      origNumInds = individualsArr.size() ;
    else
      System.out.println("ERROR: World.setOrigNumInds(), setindividualsArr == null") ;
  }
  public int getOrigNumInds ()
  {
    return origNumInds ;
  }
  
  public static String hasNameSomeone (String name)
  {
    String result = null ;
    
    if(individualsArr != null)
    {
      for(int i = 0 ; i < individualsArr.size() && result == null ; i++)
      {
        if(individualsArr.get(i).isName( name ))
        {
          result = individualsArr.get(i).findName( name ) ;
        }
      }// for
    }
    else
      System.out.println("ERROR: World.hasNameSomeone(), individualsArr == null") ;
    
    return result ;
  }// hasNameSomeone
  
  public static Individual findIndividual (String name)
  {
    return findIndividual(name, tom.MATCH_MINIMUM_STATEMENT) ;
  }// findIndividual
  
  public static Individual findIndividual (String name, final int DEFAULT_CONFIDENCE)
  {
    Individual result = null ;
    
    // check if name is invalid
    if(name != null && name.length() > 0)
    {
      // find someone with this name
      for(int i = 0 ; i < individualsArr.size() && result == null ; i++)
      {
        if(individualsArr.get(i).isName( name ))
          result = individualsArr.get(i) ;
      }
      
      // check if not found
      if(result == null && name != null)
      {
        result = new Individual(tom, name, DEFAULT_CONFIDENCE) ;
        individualsArr.add(result) ;
        
        tom.maleOrFemale(result, name) ;
      }
    }
    else
      System.out.println("ERROR: World.findIndividual(), invalid name \"" + name + "\"") ;
    
    return result ;
  }// findIndividual
  
  public static Individual findIndividual (int ind)
  {
    Individual result = null ;
    
    if(ind >= 0 && ind < individualsArr.size())
    {
      result = individualsArr.get( ind ) ;
    }
    
    return result ;
  }
  
  public static Activity findActivity (String key)
  {
    Activity result = null ;
    ArrayList<Activity> activityArr ;
    
    if(tom.getActivityArr() != null)
    {
      activityArr = tom.getActivityArr() ;
      
      // loop through activityArr
      for(int i = 0 ; i < activityArr.size() && result == null ; i++)
      {
        if(activityArr.get(i).isActivity( key ))
        {
          // result becomes the corresponding Activity
          result = activityArr.get(i) ;
        }
      }
    }
    
    return result ;
  }// findActivity
  
  public static int findByRel (String rel, Individual individual)
  {
    int result = -1 ;
    
    if(individual != null)
    {
      // check rel
      if(rel.equals("friend"))
      {
        // assumes you mean last one in list
        if(individual.getFriendArr() != null && individual.getFriendArr().size() > 0)
        {
          result = individual.getFriendArr().get(  individual.getFriendArr().size() -1  ).getReference() ;
        }
      }
      else if(rel.equals("coworker"))
      {
        // assumes you mean last one in list
        if(individual.getCoworkerArr() != null && individual.getCoworkerArr().size() > 0)
        {
          result = individual.getCoworkerArr().get(  individual.getCoworkerArr().size() -1  ).getReference() ;
        }
      }
      else if(rel.equals("date"))
      {
        // assumes you mean last one in list
        if(individual.getDateArr() != null && individual.getDateArr().size() > 0)
        {
          result = individual.getDateArr().get(  individual.getDateArr().size() -1  ).getReference() ;
        }
      }
      else if(rel.equals("boyfriend"))
      {
        // assumes you mean the 1st male date
        if(individual.getDateArr() != null)
        {
          for(int i = 0 ; i < individual.getDateArr().size() ; i++)
          {
            if(individual.getDateArr().get(i).isMale())
            {
              result = individual.getDateArr().get(i).getReference() ;
            }
          }
        }
      }
      else if(rel.equals("girlfriend"))
      {
        // assumes you mean the 1st female date
        if(individual.getDateArr() != null)
        {
          for(int i = 0 ; i < individual.getDateArr().size() ; i++)
          {
            if(individual.getDateArr().get(i).isFemale())
            {
              result = individual.getDateArr().get(i).getReference() ;
            }
          }
        }
      }
      else if(rel.equals("father"))
      {
        if(individual.getParent1() != null && individual.getParent1().isMale())
        {
          result = individual.getParent1().getReference() ;
        }
        else if(individual.getParent2() != null && individual.getParent2().isMale())
        {
          result = individual.getParent2().getReference() ;
        }
      }
      else if(rel.equals("mother"))
      {
        if(individual.getParent1() != null && individual.getParent1().isFemale())
        {
          result = individual.getParent1().getReference() ;
        }
        else if(individual.getParent2() != null && individual.getParent2().isFemale())
        {
          result = individual.getParent2().getReference() ;
        }
      }
      else if(rel.equals("parent"))
      {
        if(individual.getParent1() != null)
        {
          result = individual.getParent1().getReference() ;
        }
        else if(individual.getParent2() != null)
        {
          result = individual.getParent2().getReference() ;
        }
      }
      else if(rel.equals("husband"))
      {
        if(individual.getSpouse() != null && individual.getSpouse().isMale())
        {
          result = individual.getSpouse().getReference() ;
        }
      }
      else if(rel.equals("wife"))
      {
        if(individual.getSpouse() != null && individual.getSpouse().isFemale())
        {
          result = individual.getSpouse().getReference() ;
        }
      }
      else if(rel.equals("spouse"))
      {
        if(individual.getSpouse() != null)
        {
          result = individual.getSpouse().getReference() ;
        }
      }
      else if(rel.equals("son"))
      {
        // assumes you mean the 1st male child
        if(individual.getChildArr() != null)
        {
          for(int i = 0 ; i < individual.getChildArr().size() ; i++)
          {
            if(individual.getChildArr().get(i).isMale())
            {
              result = individual.getChildArr().get(i).getReference() ;
            }
          }
        }
      }
      else if(rel.equals("daughter"))
      {
        // assumes you mean the 1st female child
        if(individual.getChildArr() != null)
        {
          for(int i = 0 ; i < individual.getChildArr().size() ; i++)
          {
            if(individual.getChildArr().get(i).isFemale())
            {
              result = individual.getChildArr().get(i).getReference() ;
            }
          }
        }
      }
      else if(rel.equals("child"))
      {
        // assumes you mean last one in list
        if(individual.getChildArr() != null && individual.getChildArr().size() > 0)
        {
          result = individual.getChildArr().get(  individual.getChildArr().size() -1  ).getReference() ;
        }
      }
      else if(rel.equals("brother"))
      {
        // assumes you mean the 1st male sibling
        if(individual.getSiblingArr() != null)
        {
          for(int i = 0 ; i < individual.getSiblingArr().size() ; i++)
          {
            if(individual.getSiblingArr().get(i).isMale())
            {
              result = individual.getSiblingArr().get(i).getReference() ;
            }
          }
        }
      }
      else if(rel.equals("sister"))
      {
        // assumes you mean the 1st male sibling
        if(individual.getSiblingArr() != null)
        {
          for(int i = 0 ; i < individual.getSiblingArr().size() ; i++)
          {
            if(individual.getSiblingArr().get(i).isFemale())
            {
              result = individual.getSiblingArr().get(i).getReference() ;
            }
          }
        }
      }
      else if(rel.equals("sibling"))
      {
        // assumes you mean last one in list
        if(individual.getSiblingArr() != null && individual.getSiblingArr().size() > 0)
        {
          result = individual.getSiblingArr().get(  individual.getSiblingArr().size() -1  ).getReference() ;
        }
      }
      else
        System.out.println("ERROR: NLP.findByRel(), unknown rel : |" + rel + "|") ;
    }
    else
      System.out.println("ERROR: NLP.findByRel(), individual == null") ;
    
    return result ;
  }// findByRel
  
  public static boolean questionHasAnswer (Phrase phrase)
  {
    Individual ind1 = null ;
    
    boolean result = false ;
    String meaning ;
    
    int match ;
    
    if(phrase != null)
    {
      meaning = phrase.getMyMeaning() ;
      match = phrase.getMyMatch() ;
      
      if(phrase.getIndividual1() != -1)
        ind1 = findIndividual(  phrase.getIndividual1()  ) ;
      
      // check meanings
      if(meaning != null && meaning.length() > 0)
      {
        if(meaning.equals("[individual].name"))
        {
          result = (ind1.bestName() != null) ;// true if unknown name
        }
        else if(meaning.equals("[individual].placeOfBirth"))
        {
          if(ind1.getPlaceOfBirth() != null)
            result = (ind1.getPlaceOfBirth().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT) ;
        }
        else if(meaning.equals("[individual].nationality"))
        {
          if(ind1.getNationality() != null)
            result = (ind1.getNationality().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT) ;
        }
        else if(meaning.equals("[individual].location"))
        {
          if(ind1.getLocation() != null)
            result = (ind1.getLocation().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT) ;
        }
        else if(meaning.equals("[individual].job"))
        {
          if(ind1.getJob() != null)
            result = (ind1.getJob().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT) ;
        }
        else if(meaning.equals("[individual].school"))
        {
          if(ind1.getSchool() != null)
            result = (ind1.getSchool().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT) ;
        }
        else if(meaning.equals("[individual].age"))
        {
          if(ind1.getAge() != null)
            result = (ind1.getAge().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT) ;
        }
        else if(meaning.equals("maleOrFemale([individual])"))
        {
          if(ind1.getIsMale() != null)
            result = (ind1.getIsMale().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT) ;
        }
        else if(meaning.equals("[individual].mood"))
        {
          if(ind1.getMyMood() != null)
            result = (ind1.getMyMood().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT) ;
        }
        //else if(meaning.equals("[individual].isDoing"))
        //{
        //  // I don't know what I might put here
        //}
      }
      else
        System.out.println("World.questionHasAnswer(), meaning == " + meaning) ;
    }
    else
      System.out.println("World.questionHasAnswer(), phrase == null") ;
    
    return result ;
  }// questionHasAnswer
  
  public static String answerQuestion (Phrase phrase)
  {
    Individual ind1 = null ;
    Individual ind2 = null ;
    
    String result = null ;
    String meaning ;
    
    int match ;
    
    if(phrase != null)
    {
      tom.checkForDefinedInd(phrase) ;
      
      meaning = phrase.getMyMeaning() ;
      match = phrase.getMyMatch() ;
      
      // check meanings
      if(meaning != null && meaning.length() > 0)
      {
        if(phrase.getIndividual1() != -1)
          ind1 = findIndividual(  phrase.getIndividual1()  ) ;
        
        if(phrase.getIndividual2() != -1)
          ind2 = findIndividual(  phrase.getIndividual2()  ) ;
        
        // check for [var]
        if(meaning.equals("[individual].[var]"))
        {
          meaning = "[individual]." + phrase.getVar() ;
          
          phrase.setVar("") ;
        }
        
        // see what meaning is
        if(meaning.equals("describe([individual])"))
        {
          result = sayDescription( ind1 ) ;
        }
        else if(meaning.equals("[individual].name"))
        {
          result = sayNameIs( ind1, "" ) ;
        }
        else if(meaning.equals("[individual].placeOfBirth"))
        {
          result = sayPlaceOfBirth( ind1 ) ;
        }
        else if(meaning.equals("[individual].nationality"))
        {
          result = sayNationality( ind1 ) ;
        }
        else if(meaning.equals("[individual].location"))
        {
          result = sayLocation( ind1 ) ;
        }
        else if(meaning.equals("[individual].job"))
        {
          result = sayJob( ind1 ) ;
        }
        else if(meaning.equals("[individual].employer"))
        {
          result = sayEmployer( ind1 ) ;
        }
        else if(meaning.equals("[individual].school"))
        {
          result = saySchool( ind1 ) ;
        }
        else if(meaning.equals("[individual].age"))
        {
          result = sayAge( ind1 ) ;
        }
        else if(meaning.equals("maleOrFemale([individual])"))
        {
          result = sayMaleOrFemale( ind1 ) ;
        }
        else if(meaning.equals("[individual].mood"))
        {
          result = sayMood( ind1 ) ;
        }
        else if(meaning.equals("[tom].mood"))// tom's mood
        {
          ind1 = individualsArr.get(1) ;
          result = sayMood( ind1 ) ;
        }
        else if(meaning.equals("[individual].likesIndividual"))
        {
          result = sayWhoIndLikes(ind1) ;
        }
        else if(meaning.equals("[individual].likesActivity"))
        {
          result = sayActivityIndLikes(ind1) ;
        }
        else if(meaning.equals("[individual].isDoing"))
        {
          result = sayWhatIndDoes(ind1) ;
        }
        else if(meaning.equals("[tom].isDoing"))
        {
          ind1 = individualsArr.get(1) ;
          result = sayWhatIndDoes(ind1) ;
        }
        else if(meaning.equals("[individual].name = [name]"))
        {
          if(ind1 != null)
          {
            if(ind1.isName(  phrase.getName()  ))
            {
              result = "Yes, " + sayNameIs( ind1, phrase.getName() ).toLowerCase() ;
            }
            else
              result = "No, I don't think so." ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[name].individual = [individual]"))
        {
          if(ind1 != null)
          {
            if(ind1.isName(  phrase.getName()  ))
            {
              result = "Yes, " + sayNameIs( ind1, phrase.getName() ).toLowerCase() ;
            }
            else
              result = "No, I don't think so." ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual1].individual = [individual2]"))
        {
          result = "I'm not sure" ;
        }
        else if(meaning.equals("[user].name = [name]"))
        {
          if(individualsArr.get(0).isName(  phrase.getName()  ))// check user
            result = "Yes it is." ;
          else
            result = "No, I don't think so." ;
        }
        else if(meaning.equals("[individual].placeOfBirth = [place]"))
        {
          if(ind1 != null)
          {
            if(ind1.getPlaceOfBirth() != null &&
               phrase.getPlace().equals(  ind1.getPlaceOfBirth().getStr()  ) &&
               ind1.getPlaceOfBirth().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
            {
              result = "Yes, " + sayPlaceOfBirth( ind1 ).toLowerCase() ;
            }
            else
              result = "No, I don't think so." ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].job = [job]"))
        {
          if(ind1 != null)
          {
            if(ind1.getJob() != null && ind1.getJob().getStr() != null &&
               ind1.getJob().getStr().equals(  phrase.getJob()  ) &&
               ind1.getJob().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
            {
              result = "Yes, " + sayJob( ind1 ).toLowerCase() ;
            }
            else
              result = "No, I don't think so." ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].employer = [employer]"))
        {
          if(ind1 != null)
          {
            if(ind1.getEmployer() != null && ind1.getEmployer().getStr() != null &&
               ind1.getEmployer().getStr().equals(  phrase.getEmployer()  ) &&
               ind1.getEmployer().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
            {
              result = "Yes, " + sayEmployer( ind1 ).toLowerCase() ;
            }
            else
              result = "No, I don't think so." ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].school = [school]"))
        {
          if(ind1 != null)
          {
            if(ind1.getSchool() != null && ind1.getSchool().getStr() != null &&
               ind1.getSchool().getStr().equals(  phrase.getSchool()  ) &&
               ind1.getSchool().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
            {
              result = "Yes, " + saySchool( ind1 ).toLowerCase() ;
            }
            else
              result = "No, I don't think so." ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].school = null"))
        {
          if(ind1 != null && ind1.getSchool().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
          {
            if(ind1.getSchool().getStr() == null)
              result = "No, " ;
            else
              result = "Yes, " ;
            
            result += saySchool( ind1 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].age = [number]"))
        {
          if(ind1 != null && ind1.getAge().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
          {
            if(ind1.getAge().getNum() == Integer.parseInt( phrase.getNumber() ))
              result = "Yes, " ;
            else
              result = "No, " ;
            
            result += sayAge( ind1 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].isMale = true"))
        {
          if(ind1 != null)
          {
            if(ind1.isMale())// man
            {
              result = "Yes, " + sayMaleOrFemale( ind1 ).toLowerCase() ;
            }
            else if(ind1.isFemale())// woman
            {
              result = "No, " + sayMaleOrFemale( ind1 ).toLowerCase() ;
            }
            else
              result = "I don't know." ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].isMale = false"))
        {
          if(ind1 != null)
          {
            if(ind1.isMale())// man
            {
              result = "No, " + sayMaleOrFemale( ind1 ).toLowerCase() ;
            }
            else if(ind1.isFemale())// woman
            {
              result = "Yes, " + sayMaleOrFemale( ind1 ).toLowerCase() ;
            }
            else
              result = "I don't know." ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].location = [place]"))
        {
          if(ind1 != null && ind1.getLocation() != null &&
             ind1.getLocation().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
          {
            if(ind1.getLocation() != null &&
             phrase.getPlace().equals(  ind1.getLocation().getStr()  ))
            {
              result = "Yes, " ;
            }
            else
            {
              result = "No, " ;
            }
            
            result += sayLocation( ind1 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].nationality = [place]"))
        {
          if(ind1 != null && ind1.getNationality() != null &&
             ind1.getNationality().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
          {
            if(ind1.getNationality() != null &&
             phrase.getPlace().equals(  ind1.getNationality().getStr()  ))
            {
              result = "Yes, " ;
            }
            else
            {
              result = "No, " ;
            }
            
            result += sayNationality( ind1 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].isMood = [mood].num"))
        {
          if(ind1 != null && ind1.getMyMood().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT &&
             phrase.getMood() != null && phrase.getMood().length() > 0)
          {
            if(ind1.getMyMood().getNum() >= Integer.parseInt(phrase.getMood().substring(0,1)) -1 &&
               ind1.getMyMood().getNum() <= Integer.parseInt(phrase.getMood().substring(0,1)) +1)// buffer zone
              result = "Yes, " ;
            else
              result = "No, " ;
            
            result += sayMood( ind1 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].isMood = 10 -[mood].num"))
        {
          if(ind1 != null && ind1.getMyMood().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
          {
            if(ind1.getMyMood().getNum() >= Integer.parseInt(phrase.getMood().substring(0,1)) -1 &&
               ind1.getMyMood().getNum() <= Integer.parseInt(phrase.getMood().substring(0,1)) +1)// buffer zone
              result = "No, " ;
            else
              result = "Yes, " ;
            
            result += sayMood( ind1 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].isFamous = true"))
        {
          if(ind1 != null && ind1.getIsFamous().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
          {
            if(ind1.getIsFamous().getBool())
              result = "Yes, " ;
            else
              result = "No, " ;
            
              result += sayFamous( ind1 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].isFamous = false"))
        {
          if(ind1 != null && ind1.getIsFamous().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
          {
            if(ind1.getIsFamous().getBool())
              result = "No, " ;
            else
              result = "Yes, " ;
            
              result += sayFamous( ind1 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].isAlive = true"))
        {
          if(ind1 != null && ind1.getIsAlive().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
          {
            if(ind1.getIsAlive().getBool())
              result = "Yes, " ;
            else if(!ind1.getIsAlive().getBool())
              result = "No, " ;
            
            result += sayIfAlive( ind1 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].isAlive = false"))
        {
          if(ind1 != null && ind1.getIsAlive().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
          {
            if(ind1.getIsAlive().getBool())
              result = "No, " ;
            else if(!ind1.getIsAlive().getBool())
              result = "Yes, " ;
            
            result += sayIfAlive( ind1 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].isPerson = true"))
        {
          if(ind1 != null && ind1.getIsPerson().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
          {
            if(ind1.getIsPerson().getBool())
              result = "Yes, " ;
            else if(!ind1.getIsPerson().getBool())
              result = "No, " ;
            
            result += sayIfPerson( ind1 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].isPerson = false"))
        {
          if(ind1 != null && ind1.getIsPerson().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
          {
            if(ind1.getIsPerson().getBool())
              result = "No, " ;
            else if(!ind1.getIsPerson().getBool())
              result = "Yes, " ;
            
            result += sayIfPerson( ind1 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual1].age = [individual2].age"))
        {
          if(ind1 != null && ind2 != null &&
             ind1.getAge().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT &&
             ind2.getAge().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
          {
            if(ind1.getAge().getNum() == ind2.getAge().getNum())
              result = "Yes, " ;
            else
              result = "No, " ;
            
            result += sayIfSameAge( ind1, ind2 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("rel([individual1], [individual2])"))
        {
          if(ind1 != null && ind2 != null)
          {
            if(ind1.areRelated(phrase.getRel(), ind2))
            {
              result = "Yes, " ;
            }
            else
            {
              result = "No, " ;
            }
            result += sayIfRel( phrase.getRel(), ind1, ind2 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("coworker([individual1], [individual2])"))
        {
          if(ind1 != null && ind2 != null)
          {
            if(ind1.areRelated("coworker", ind2))
            {
              result = "Yes, " ;
            }
            else
            {
              result = "No, " ;
            }
            result += sayIfRel( "coworker", ind1, ind2 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual1].likesIndividuals = [individual2]"))
        {
          if(ind1 != null && ind2 != null)
          {
            if(  ind1.searchArrayListIndividual(  ind1.getLikesIndividualsArr(), ind2  )  )
            {
              result = "Yes, " ;
            }
            else
            {
              result = "No, " ;
            }
            result += sayIfLikesIndividual( ind1, ind2 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual1].likesIndividuals != [individual2]"))
        {
          if(ind1 != null && ind2 != null)
          {
            if(  ind1.searchArrayListIndividual(  ind1.getLikesIndividualsArr(), ind2  )  )
            {
              result = "No, " ;
            }
            else
            {
              result = "Yes, I think " ;
            }
            result += sayIfLikesIndividual( ind1, ind2 ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].likesActivities = [activity]"))
        {
          if(ind1 != null)
          {
            if(  ind1.searchArrayListActivity(  ind1.getLikesActivitiesArr(), findActivity(phrase.getActivity())  )  )
            {
              result = "Yes, " ;
            }
            else
            {
              result = "No, " ;
            }
            result += sayIfLikesActivity( ind1, findActivity(phrase.getActivity()) ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].likesActivities != [activity]"))
        {
          if(ind1 != null)
          {
            if(  ind1.searchArrayListActivity(  ind1.getLikesActivitiesArr(), findActivity(phrase.getActivity())  )  )
            {
              result = "No, " ;
            }
            else
            {
              result = "Yes, " ;
            }
            result += sayIfLikesActivity( ind1, findActivity(phrase.getActivity()) ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].isDoing = [activity]"))
        {
          if(ind1 != null)
          {
            if(  ind1.searchArrayListActivity(  ind1.getDoingArr(), findActivity(phrase.getActivity())  )  )
            {
              result = "Yes, " ;
            }
            else
            {
              result = "No, " ;
            }
            result += sayIfDoing( ind1, findActivity(phrase.getActivity()) ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else if(meaning.equals("[individual].isDoing != [activity]"))
        {
          if(ind1 != null)
          {
            if(  ind1.searchArrayListActivity(  ind1.getDoingArr(), findActivity(phrase.getActivity())  )  )
            {
              result = "No, " ;
            }
            else
            {
              result = "Yes, " ;
            }
            result += sayIfDoing( ind1, findActivity(phrase.getActivity()) ).toLowerCase() ;
          }
          else
            result = "I don't know." ;
        }
        else
          System.out.println("ERROR: World.answerQuestion(), invalid meaning == " + meaning) ;
      }
      else
        System.out.println("ERROR: World.answerQuestion(), invalid meaning == " + meaning) ;
      
      result = /*match + " - " + */result/* + "   :-   " + phrase.getMyUnderstanding()*/ ;
    }
    
    // make sure it ends with punctuation
    if(result != null && result.length() > 0)
    {
      if(result.charAt( result.length() -1 ) != '.' &&
         result.charAt( result.length() -1 ) != '!')
        result += "." ;
    }
    
    return result ;
  }// answerQuestion
  
  public static String sayDescription (Individual ind)
  {
    String result = "I don't really know who that is." ;
    String indRefer = "they" ;
    ArrayList<String> relations = null ;
    
    if(ind != null)
    {
      if(ind.bestName() != null)
      {
        indRefer = ind.bestName() ;
        
        if(indRefer.equals("tom"))
          indRefer = "I" ;
      }
      else
      {
        if(ind.isMale())
          indRefer = "he" ;
        else if(ind.isFemale())
          indRefer = "she" ;
      }
      
      // default ind
      if(ind == individualsArr.get(0))// user
        result = "I don't really know much about you." ;
      else if(ind.isMale())
        result = "I don't really know much about him." ;
      else if(ind.isFemale())
        result = "I don't really know much about her." ;
      
      relations = individualsArr.get(0).findRelations(ind) ;
      
      // check relations
      if(relations != null && relations.size() > 0 && ind != individualsArr.get(0))// make sure ind is not the user
      {
        if(relations.size() == 1)
        {
          if(ind == individualsArr.get(1))// check if ind is tom
            result = "I am your " + relations.get(0) + "." ;
          else
            result = indRefer + " is your " + relations.get(0) + "." ;
        }
        else
        {
          if(ind == individualsArr.get(1))// check if ind is tom
            result = "I am your " ;
          else
            result = indRefer + " is your " ;
          
          // ex: "joe is your friend, brother, and coworker."
          for(int i = 0 ; i < relations.size() ; i++)
          {
            if(i < relations.size() -1)
            {
              result += relations.get(i) + ", " ;
            }
            else// last relation
            {
              result += "and " + relations.get(i) + "." ;
            }
          }
        }
      }
      else if(ind.getJob() != null && ind.getJob().getStr().length() > 0 && ind.getJob().getConfidence() > tom.MATCH_MINIMUM_STATEMENT)// job
      {
        if(tom.isVowel(ind.getJob().getStr().charAt(0)))
        {
          if(ind == individualsArr.get(0))// user
            result = "You are an " + ind.getJob().getStr() + "." ;
          else if(ind == individualsArr.get(1))// tom
            result = "I am an " + ind.getJob().getStr() + "." ;
          else// not user and not tom
            result = indRefer + " is an " + ind.getJob().getStr() + "." ;
        }
        else
        {
          if(ind == individualsArr.get(0))// user
            result = "You are a " + ind.getJob().getStr() + "." ;
          else if(ind == individualsArr.get(1))// tom
            result = "I am a " + ind.getJob().getStr() + "." ;
          else// not user
            result = indRefer + " is a " + ind.getJob().getStr() + "." ;
        }
      }
      else if(ind.getSchool() != null && ind.getSchool().getConfidence() > tom.MATCH_MINIMUM_STATEMENT)// student
      {
        if(ind == individualsArr.get(0))// user
          result = "You are a student." ;
        else if(ind == individualsArr.get(1))// tom
          result = "I am a student." ;
        else// not user
          result = indRefer + " is a student." ;
      }
    }
    
    return result ;
  }// sayDescription
  
  public static String sayNameIs (Individual ind, String name)
  {
    String refer = "Their" ;
    String result = null ;
    
    if(ind != null)
    {
      if(!ind.isName(name))
        name = ind.bestName() ;
      
      if(name != null && name.length() > 0)
      {
        // rephrase slightly
        if(ind == individualsArr.get(0))// user
          refer = "Your" ;
        else if(ind == individualsArr.get(1))// tom
          refer = "My" ;
        else if(ind.isMale())// man
          refer = "His" ;
        else if(ind.isFemale())// woman
          refer = "Her" ;
        
        result = refer + " name is " + name + "." ;
      }
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayNameIs
  
  public static String sayPlaceOfBirth (Individual ind)
  {
    String result = null ;
    String refer ;
    String name = null ;
    
    if(ind != null)
    {
      name = ind.bestName() ;
      
      if(ind.getPlaceOfBirth() != null && ind.getPlaceOfBirth().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
      {
        if(ind.getPlaceOfBirth().getStr() != null)
        {
          if(ind.getPlaceOfBirth().getStr().length() > 0)// valid place of birth
          {
            // rephrase slightly
            if(individualsArr.get(0) == ind)// user
              refer = "You were" ;
            else if(individualsArr.get(1) == ind)// tom
              refer = "I was" ;
            else if(name != null)// tom knows their name and will use it
              refer = name + " was" ;
            else if(ind.isMale())// man
              refer = "He was" ;
            else if(ind.isFemale())// woman
              refer = "She was" ;
            else
              refer = "They were" ;
            
            result = refer + " born in " + ind.getPlaceOfBirth().getStr() + "." ;
          }
          else// "" place of birth
          {
            if(ind == individualsArr.get(1))// tom
            {
              result = "Nowhere." ;
            }
            else
              result = "I don't know." ;
          }
        }
        else
        {
          // rephrase slightly
           if(individualsArr.get(0) == ind)// user
             refer = "You have " ;
           else if(individualsArr.get(1) == ind)// tom
             refer = "I have " ;
           else if(name != null)// tom knows their name and will use it
             refer = name + " has " ;
           else if(ind.isMale())// man
             refer = "He has " ;
           else if(ind.isFemale())// woman
             refer = "She has " ;
           else
             refer = "They have " ;
           
           result = refer + "no place of birth." ;
        }
      }
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayPlaceOfBirth
  
  public static String sayNationality (Individual ind)
  {
    String result = null ;
    String refer = "They live" ;
    String name ;
    
    if(ind != null)
    {
      name = ind.bestName() ;
      
      if(ind.getNationality() != null && ind.getNationality().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
      {
        if(ind.getNationality().getStr() != null)
        {
          if(ind.getNationality().getStr().length() > 0)// valid nationality
          {
            // rephrase slightly
            if(ind == individualsArr.get(0))// user
              refer = "You live" ;
            else if(ind == individualsArr.get(1))// tom
              refer = "I live" ;
            else
            {
              if(name != null)// i know their name and will use it
                refer = name ;
              else if(ind.isMale())// man
                refer = "He" ;
              else if(ind.isFemale())// women
                refer = "She" ;
              
              if(!ind.getIsAlive().getBool() && ind.getIsAlive().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)// is alive
                result += " lived" ;
              else
                result += " lives" ;
            }
            
            result = refer + " in " + ind.getNationality().getStr() + "." ;
          }
          else// "" nationality
          {
            result = "I don't know." ;
          }
        }
        else
        {
          // rephrase slightly
          if(individualsArr.get(0) == ind)// user
            refer = "You have " ;
          else if(individualsArr.get(1) == ind)// tom
            refer = "I have " ;
          else if(name != null)// tom knows their name and will use it
            refer = name + " has " ;
          else if(ind.isMale())// man
            refer = "He has " ;
          else if(ind.isFemale())// woman
            refer = "She has " ;
          else
            refer = "They have " ;
          
          result = refer + "no nationality." ;
        }
      }
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayNationality
  
  public static String sayLocation (Individual ind)
  {
    String result = null ;
    String refer = "They're" ;
    String name ;
    
    if(ind != null)
    {
      name = ind.bestName() ;
      
      if(ind.getLocation() != null && ind.getLocation().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
      {
        if(ind.getLocation().getStr() != null)
        {
          if(ind.getLocation().getStr().length() > 0)// valid location
          {
            // rephrase slightly
            if(individualsArr.get(0) == ind)// user
              refer = "You're" ;
            else if(individualsArr.get(1) == ind)// tom
              refer = "I'm" ;
            else if(name != null)// tom knows their name and will use it
              refer = name + " is" ;
            else if(ind.isMale())// man
              refer = "He is" ;
            else if(ind.isFemale())// woman
              refer = "She is" ;
            
            result = refer + " in " + ind.getLocation().getStr() + "." ;
          }
          else// "" location
          {
            if(ind == individualsArr.get(1))// tom
            {
              result = "Nowhere." ;
            }
            else
              result = "I don't know." ;
          }
        }
        else
        {
          // rephrase slightly
          if(individualsArr.get(0) == ind)// user
            refer = "You are " ;
          else if(individualsArr.get(1) == ind)// tom
            refer = "I am " ;
          else if(name != null)// tom knows their name and will use it
            refer = name + " is " ;
          else if(ind.isMale())// man
            refer = "He is " ;
          else if(ind.isFemale())// woman
            refer = "She is " ;
          else
            refer = "They are " ;
          
          result = refer + "nowhere." ;
        }
      }
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayLocation
  
  public static String sayJob (Individual ind)
  {
    String result = null ;
    String refer ;
    String name ;
    
    if(ind != null)
    {
      name = ind.bestName() ;
      
      if(ind.getJob() != null && ind.getJob().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
      {
        if(ind.getJob().getStr() != null)
        {
          if(ind.getJob().getStr().length() > 0)// valid job
          {
            // rephrase slightly
            if(individualsArr.get(0) == ind)// user
              refer = "You're" ;
            else if(individualsArr.get(1) == ind)// tom
              refer = "I'm" ;
            else if(name != null)// tom knows their name and will use it
              refer = name + " is" ;
            else if(ind.isMale())// man
              refer = "He is" ;
            else if(ind.isFemale())// woman
              refer = "She is" ;
            else
              refer = "They're" ;
            
            result = refer + " a" ;
            
            // "an" if job starts with vowel.
            if(tom.isVowel(ind.getJob().getStr().charAt(0)))
              result += "n" ;
            result += " " + ind.getJob().getStr() + "." ;
          }
          else// "" job
          {
            if(ind == individualsArr.get(1))// tom
            {
              result = "I guess I don't have one." ;
            }
            else
              result = "I don't know." ;
          }
        }
        else
        {
          // rephrase slightly
          if(individualsArr.get(0) == ind)// user
            refer = "You have " ;
          else if(individualsArr.get(1) == ind)// tom
            refer = "I have " ;
          else if(name != null)// tom knows their name and will use it
            refer = name + " has " ;
          else if(ind.isMale())// man
            refer = "He has " ;
          else if(ind.isFemale())// woman
            refer = "She has " ;
          else
            refer = "They have " ;
          
          result = refer + "no job." ;
        }
      }
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayJob
  
  public static String sayEmployer (Individual ind)
  {
    String result = null ;
    String refer ;
    String name ;
    
    if(ind != null)
    {
      name = ind.bestName() ;
      
      if(ind.getEmployer() != null && ind.getEmployer().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
      {
        if(ind.getEmployer().getStr() != null)
        {
          if(ind.getEmployer().getStr().length() > 0)// valid employer
          {
            // rephrase slightly
            if(individualsArr.get(0) == ind)// user
              refer = "You work " ;
            else if(individualsArr.get(1) == ind)// tom
              refer = "I work " ;
            else if(name != null)// tom knows their name and will use it
              refer = name + " works " ;
            else if(ind.isMale())// man
              refer = "He works " ;
            else if(ind.isFemale())// woman
              refer = "She works " ;
            else
              refer = "They work " ;
            
            result = refer + "for " ;
            
            result += ind.getEmployer().getStr() + "." ;
          }
          else// "" employer
          {
            if(ind == individualsArr.get(1))// tom
            {
              result = "I guess I don't have one." ;
            }
            else
              result = "I don't know." ;
          }
        }
        else
        {
          // rephrase slightly
          if(individualsArr.get(0) == ind)// user
            refer = "You don't " ;
          else if(individualsArr.get(1) == ind)// tom
            refer = "I don't " ;
          else if(name != null)// tom knows their name and will use it
            refer = name + " doesn't " ;
          else if(ind.isMale())// man
            refer = "He doesn't " ;
          else if(ind.isFemale())// woman
            refer = "She doesn't " ;
          else
            refer = "They don't " ;
          
          result = refer + "have an employer." ;
        }
      }
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayEmployer
  
  public static String sayAge (Individual ind)
  {
    String result = null ;
    String refer = "They're" ;
    String name ;
    
    if(ind != null)
    {
      name = ind.bestName() ;
      
      if(ind.getAge() != null &&
         ind.getAge().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)// known age
      {
        if(individualsArr.get(0) == ind)// user
          refer = "You're" ;
        else if(individualsArr.get(1) == ind)// tom
          refer = "I'm" ;
        else if(name != null)// tom knows their name and will use it
          refer = name + " is" ;
        else if(ind.isMale())// man
          refer = "He is" ;
        else if(ind.isFemale())// woman
          refer = "She is" ;
        
        result = refer + " " + ind.getAge().getNum() + " years old." ;
      }
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayAge
  
  public static String sayMaleOrFemale (Individual ind)
  {
    String result = null ;
    String refer = "They're" ;
    String name ;
    
    if(ind != null)
    {
      name = ind.bestName() ;
      
      if(individualsArr.get(0) == ind)// user
        refer = "You're" ;
      else if(individualsArr.get(1) == ind)// tom
        refer = "I am" ;
      else if(name != null)// tom knows their name and will use it
        refer = name + "is" ;
      else if(ind.isMale())// man
        refer = "He is" ;
      else if(ind.isFemale())// woman
        refer = "She is" ;
      
      result = refer ;
      
      if(ind.isMale())// man
        result = refer + " a man." ;
      else if(ind.isFemale())// woman
        result = refer + " a woman." ;
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayMaleOrFemale
  
  public static String saySchool (Individual ind)
  {
    String result = null ;
    String refer ;
    String name ;
    
    if(ind != null)
    {
      name = ind.bestName() ;
      
      if(ind.getSchool() != null && ind.getSchool().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
      {
        if(ind.getSchool().getStr() != null)
        {
          if(ind.getSchool().getStr().length() > 0)// valid school
          {
            // rephrase slightly
            if(individualsArr.get(0) == ind)// user
              refer = "You're " ;
            else if(individualsArr.get(1) == ind)// tom
              refer = "I'm " ;
            else if(name != null)// tom knows their name and will use it
              refer = name + " is " ;
            else if(ind.isMale())// man
              refer = "He is " ;
            else if(ind.isFemale())// woman
              refer = "She is " ;
            else
              refer = "They're " ;
            
            result = refer + "a student at " + ind.getSchool().getStr() + "." ;
          }
          else// "" school
          {
            if(ind == individualsArr.get(1))// tom
            {
              result = "I guess I'm not a student." ;
            }
            else
              result = "I don't know." ;
          }
        }
        else
        {
          // rephrase slightly
          if(individualsArr.get(0) == ind)// user
            refer = "You are " ;
          else if(individualsArr.get(1) == ind)// tom
            refer = "I am " ;
          else if(name != null)// tom knows their name and will use it
            refer = name + " is " ;
          else if(ind.isMale())// man
            refer = "He is " ;
          else if(ind.isFemale())// woman
            refer = "She is " ;
          else
            refer = "They are " ;
          
          result = refer + "not a student." ;
        }
      }
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// saySchool
  
  public static String sayMood (Individual ind)
  {
    String result = null ;
    String refer = "They're" ;
    String name ;
    
    if(ind != null)
    {
      name = ind.bestName() ;
      
      result = "" ;
      
      if(individualsArr.get(0) == ind)// user
        refer = "you're" ;
      else if(individualsArr.get(1) == ind)// tom
        refer = "I'm" ;
      else if(name != null)// tom knows their name and will use it
        refer = name + "is" ;
      
      else if(ind.isMale())// man
        refer = "he is" ;
      else if(ind.isFemale())// woman
        refer = "she is" ;
      
      if(ind.getMyMood().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
      {
        result += refer ;
        
        if(ind.getMyMood().getNum() == 0)// suicidal
        {
          result = "Right now, " + result ;
          result += " feeling suicidal." ;
        }
        else if(ind.getMyMood().getNum() == 1 ||
                ind.getMyMood().getNum() == 2)// awful
        {
          result = "Right now, " + result ;
          result += " feeling awful." ;
        }
        else if(ind.getMyMood().getNum() == 3 ||
                ind.getMyMood().getNum() == 4)// bad
        {
          result = "Right now, " + result ;
          result += " not feeling very good." ;
        }
        else if(ind.getMyMood().getNum() == 5 ||
                ind.getMyMood().getNum() == 6)// okay
        {
          result += " doing okay." ;
        }
        else if(ind.getMyMood().getNum() == 7)// good
        {
          result += " doing well." ;
        }
        else if(ind.getMyMood().getNum() >= 8)// great
        {
          result += " feeling great." ;
        }
        else
          result = "I don't know." ;
      }
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayMood
  
  public static String sayWhoIndLikes (Individual ind)
  {
    String result = "" ;
    String name = null ;
    
    Individual ind2 ;
    
    if(ind != null && ind.getLikesIndividualsArr() != null && ind.getLikesIndividualsArr().size() > 0)
    {
      // get the last Individual in arr
      ind2 = ind.getLikesIndividualsArr().get(  ind.getLikesIndividualsArr().size() -1  ) ;
      
      if(ind2 != null)
      {
        if(ind2 == individualsArr.get(0))// user
          name = "you" ;
        else if(ind2 == individualsArr.get(1))// tom
          name = "me" ;
        else
          name = ind2.bestName() ;
        
        if(name != null)
        {
          if(ind == individualsArr.get(0))// user
            result += "You like " ;
          else if(ind == individualsArr.get(1))// tom
            result += "I like " ;
          else if(ind.bestName() != null)// tom knows their name and will use it
            result += ind.bestName() + " likes " ;
          else if(ind.isMale())// man
            result += "He likes " ;
          else if(ind.isFemale())// woman
            result += "She likes " ;
          else
            result += "They like " ;
          
          result += name + "." ;
        }
        else
          result = "I don't know." ;
      }
      else
        System.out.println("ERROR: World.sayWhoIndLikes(), ind2 == null") ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayWhoIndLikes
  
  public static String sayActivityIndLikes (Individual ind)
  {
    String result = "" ;
    String presentForm = null ;
    Activity activity = null ;
    
    if(ind != null && ind.getLikesActivitiesArr() != null && ind.getLikesActivitiesArr().size() > 0)
    {
      // get the last activity in arr
      activity = ind.getLikesActivitiesArr().get(  ind.getLikesActivitiesArr().size() -1  ) ;
      
      if(activity != null && activity.getPresentFormArr() != null && activity.getPresentFormArr().size() > 0)
      {
        presentForm = activity.getPresentFormArr().get(0) ;
        
        if(presentForm != null)
        {
          if(ind == individualsArr.get(0))// user
            result += "You like " ;
          else if(ind == individualsArr.get(1))// tom
            result += "I like " ;
          else if(ind.bestName() != null)// tom knows their name and will use it
            result += ind.bestName() + " likes " ;
          else if(ind.isMale())// man
            result += "He likes " ;
          else if(ind.isFemale())// woman
            result += "She likes " ;
          else
            result += "They like " ;
          
          result += "to " + presentForm + "." ;
        }
      }
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayActivityIndLikes
  
  public static String sayWhatIndDoes (Individual ind)
  {
    String result = "" ;
    String presentForm = null ;
    
    Activity act ;
    
    if(ind != null && ind.getDoingArr() != null && ind.getDoingArr().size() > 0)
    {
      // gets the last activity that is in the doingArr
      act = ind.getDoingArr().get(  ind.getDoingArr().size() -1  ) ;
      
      if(act != null && act.getPresentFormArr() != null && act.getPresentFormArr().size() > 0)
      {
        // gets the last present form which should be the "ing" one
        presentForm = act.getPresentFormArr().get(  act.getPresentFormArr().size() -1  ) ;
        
        if(presentForm != null)
        {
          if(ind == individualsArr.get(0))// user
            result = "You're " ;
          else if(ind == individualsArr.get(1))// tom
            result = "I'm " ;
          else if(ind.bestName() != null)// tom knowns their name and will use it
            result = ind.bestName() + " is " ;
          else if(ind.isMale())// man
            result = "He is " ;
          else if(ind.isFemale())// woman
            result = "She is " ;
          else
            result = "They are " ;
          
          result += presentForm ;
        }
        else
          result = "I don't know." ;
      }
      else
        System.out.println("ERROR: World.sayWhatIndDoes(), invalid act, act == " + act) ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayWhatIndDoes
  
  public static String sayFamous (Individual ind)
  {
    String result = null ;
    String refer = "They're" ;
    String name ;
    
    if(ind != null)
    {
      name = ind.bestName() ;
      
      if(individualsArr.get(0) == ind)// user
        refer = "You're" ;
      else if(individualsArr.get(1) == ind)// tom
        refer = "I'm" ;
      else if(name != null)// tom knows their name and will use it
        refer = name + " is" ;
      else if(ind.isMale())// man
        refer = "He is" ;
      else if(ind.isFemale())// woman
        refer = "She is" ;
      
      if(ind.getIsFamous().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
      {
        if(ind.getIsFamous().getBool())// man
          result = refer + " famous." ;
        else// woman
          result = refer + " not famous." ;
      }
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayFamous
  
  public static String sayIfAlive (Individual ind)
  {
    String result = null ;
    String refer = "They're" ;
    String name ;
    
    if(ind != null)
    {
      name = ind.bestName() ;
      
      if(individualsArr.get(0) == ind)// user
        refer = "You're" ;
      else if(individualsArr.get(1) == ind)// tom
        refer = "I'm" ;
      else if(name != null)// tom knows their name and will use it
        refer = name + "is" ;
      else if(ind.isMale())// man
        refer = "He is" ;
      else if(ind.isFemale())// woman
        refer = "She is" ;
      
      if(ind.getIsAlive().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
      {
        if(ind.getIsAlive().getBool())// man
          result = refer + " alive." ;
        else// woman
          result = refer + " dead." ;
      }
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayIfAlive
  
  public static String sayIfPerson (Individual ind)
  {
    String result = null ;
    String refer = "They're" ;
    String name ;
    
    if(ind != null)
    {
      name = ind.bestName() ;
      
      if(individualsArr.get(0) == ind)// user
        refer = "You're" ;
      else if(individualsArr.get(1) == ind)// tom
        refer = "I'm" ;
      else if(name != null)// tom knows their name and will use it
        refer = name + "is" ;
      else if(ind.isMale())// man
        refer = "He is" ;
      else if(ind.isFemale())// woman
        refer = "She is" ;
      
      if(ind.getIsPerson().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
      {
        if(ind.getIsPerson().getBool())// man
          result = refer ;
        else// woman
          result = refer + " not" ;
        
        result += " a person." ;
      }
      else
        result = "I don't know." ;
    }
    else
      result = "I don't know." ;
    
    return result ;
  }// sayIfPerson
  
  public static String sayIfSameAge(Individual ind1, Individual ind2)
  {
    String result = "" ;
    
    if(ind1 == individualsArr.get(1) || ind2 == individualsArr.get(1))// tom
      result = "We " ;
    else if(ind1 == individualsArr.get(0) || ind2 == individualsArr.get(0))// tom
      result = "You " ;
    else
      result = "They " ;
    
    result += "are " ;
    
    if(ind1.getAge().getNum() != ind2.getAge().getNum())
      result += "not " ;
    
    result += "the same age." ;
    
    return result ;
  }// sayIfSameAge
  
  public static String sayIfRel (String rel, Individual ind1, Individual ind2 )
  {
    String result = "" ;
    
    if(rel != null && ind1 != null && ind2 != null)
    {
      if(ind2 == individualsArr.get(0))// user
        result += "You're " ;
      else if(ind2 == individualsArr.get(1))// tom
        result += "I'm " ;
      else if(ind2.bestName() != null)// tom knows their name and with use it
        result = ind2.bestName() + " is " ;
      else if(ind2.isMale())// man
        result = "He is " ;
      else if(ind2.isFemale())// woman
        result = "She is " ;
      else
        result = "They are " ;
      
      if(!ind1.areRelated(rel, ind2))
        result += "not " ;
      
      if(ind1 == individualsArr.get(0))// user
        result += "your " ;
      else if(ind1 == individualsArr.get(1))// tom
        result += "my " ;
      else if(ind1.bestName() != null)// tom knows their name and with use it
        result += ind1.bestName() + "'s " ;
      else if(ind1.isMale())// man
        result += "his " ;
      else if(ind1.isFemale())// woman
        result += "her " ;
      else
        result += "their " ;
      
      result += rel ;
    }
    else
      System.out.println("ERROR: World.sayIfRel(), rel || ind1 || ind2 == null") ;
    
    return result ;
  }// sayIfRel
  
  public static String sayIfLikesIndividual (Individual ind1, Individual ind2)
  {
    String result ;
    
    if(individualsArr.get(0) == ind1)// user
      result = "You do" ;
    else if(individualsArr.get(1) == ind1)// tom
      result = "I do" ;
    else if(ind1.bestName() != null)// tom knows their name and with use it
      result = ind1.bestName() + " does" ;
    else if(ind1.isMale())// man
      result = "He does" ;
    else if(ind1.isFemale())// woman
      result = "She does" ;
    else
      result = "They do" ;
    
    if(!ind1.searchArrayListIndividual(  ind1.getLikesIndividualsArr(), ind2  ))
      result += "n't" ;
    
    result += " like " ;
    
    if(individualsArr.get(0) == ind2)// user
      result += "you." ;
    else if(individualsArr.get(1) == ind2)// tom
      result += "me." ;
    else if(ind2.bestName() != null)// tom knows their name and with use it
      result += ind2.bestName() + "." ;
    else if(ind2.isMale())// man
      result += "him." ;
    else if(ind2.isFemale())// woman
      result += "her." ;
    else
      result += "them." ;
    
    return result ;
  }// sayIfLikesIndividual
  
  public static String sayIfLikesActivity (Individual ind, Activity activity)
  {
    String result ;
    
    if(individualsArr.get(0) == ind)// user
      result = "You do" ;
    else if(individualsArr.get(1) == ind)// tom
      result = "I do" ;
    else if(ind.bestName() != null)// tom knows their name and with use it
      result = ind.bestName() + " does" ;
    else if(ind.isMale())// man
      result = "He does" ;
    else if(ind.isFemale())// woman
      result = "She does" ;
    else
      result = "They do" ;
    
    if(!ind.searchArrayListActivity(  ind.getLikesActivitiesArr(), activity  ))
      result += "n't" ;
    
    result += " like to " ;
    
    if(activity.getPresentFormArr() != null && activity.getPresentFormArr().size() > 0)
    {
      result += activity.getPresentFormArr().get(0) + "." ;
    }
    else
      System.out.println("ERROR: World.sayIfLikesActivity(), activity.getPresentFormArr() == " + activity.getPresentFormArr()) ;
    
    return result ;
  }// sayIfLikesActivity
  
  public static String sayIfDoing (Individual ind, Activity activity)
  {
    String result ;
    
    if(individualsArr.get(0) == ind)// user
      result = "You're " ;
    else if(individualsArr.get(1) == ind)// tom
      result = "I'm " ;
    else if(ind.bestName() != null)// tom knows their name and with use it
      result = ind.bestName() + " is " ;
    else if(ind.isMale())// man
      result = "He is " ;
    else if(ind.isFemale())// woman
      result = "She is " ;
    else
      result = "They are " ;
    
    if(!ind.searchArrayListActivity(  ind.getDoingArr(), activity  ))
      result += "not " ;
    
    if(activity.getPresentFormArr() != null && activity.getPresentFormArr().size() > 0)
    {
      // gets the last present form which should be the "ing" one
      result += activity.getPresentFormArr().get(  activity.getPresentFormArr().size()-1  ) + "." ;
    }
    else
      System.out.println("ERROR: World.sayIfLikesActivity(), activity.getPresentFormArr() == " + activity.getPresentFormArr()) ;
    
    return result ;
  }// sayIfDoing
}

