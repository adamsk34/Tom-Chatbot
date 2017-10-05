import java.util.* ;

public class NLP
{
  private static ChatBot tom ;
  private static World myWorld ;
  
  private static int lastNonMaleInd = -1 ;
  private static int lastNonFemaleInd = -1 ;
  
  private static String lastWeInd = "you" ;
  
  private static ArrayList<String> malePronounArr ;
  private static ArrayList<String> femalePronounArr ;
  
  public NLP ()
  {
    // initialize male pronoun arr
    malePronounArr = new ArrayList<String>() ;
    malePronounArr.add("he") ;
    malePronounArr.add("him") ;
    malePronounArr.add("himself") ;
    malePronounArr.add("his") ;
    
    // initialize female pronoun arr
    femalePronounArr = new ArrayList<String>() ;
    femalePronounArr.add("she") ;
    femalePronounArr.add("her") ;
    femalePronounArr.add("herself") ;
    femalePronounArr.add("hers") ;
  }
  
  public void setTom (ChatBot tomGiven)
  {
    tom = tomGiven ;
  }
  
  public void setMyWorld (World givenWorld)
  {
    myWorld = givenWorld ;
  }
  
  public static void checkIfValidPhraseVars (Phrase phrase, int ind1, int ind2)
  {
    final int STD_MATCH_SUBTRACTION = 11 ;// originally 10, 11
    
    if((phrase.getMyMeaning().indexOf("[individual]") != -1 || phrase.getMyMeaning().indexOf("[individual1]") != -1 ||
        phrase.getMyMeaning().indexOf("[individual2]") != -1) &&
       (ind1 < 0 || ind1 >= myWorld.getIndividualsArr().size()))
    {
      phrase.setMyMatch( phrase.getMyMatch() - STD_MATCH_SUBTRACTION ) ;
    }
    if(phrase.getName() != null && phrase.getName().length() > 0 &&
       realName(phrase.getName()) == null)
    {
      phrase.setMyMatch( phrase.getMyMatch() - STD_MATCH_SUBTRACTION ) ;
    }
    if(phrase.getNumber() != null && phrase.getNumber().length() > 0 &&
       !realAge(phrase.getNumber()))
    {
      phrase.setMyMatch( phrase.getMyMatch() - STD_MATCH_SUBTRACTION ) ;
    }
    if(phrase.getJob() != null && phrase.getJob().length() > 0 &&
       realJob(phrase.getJob()) == null)
    {
      phrase.setMyMatch( phrase.getMyMatch() - STD_MATCH_SUBTRACTION ) ;
    }
    if(phrase.getMood() != null && phrase.getMood().length() > 0 &&
       realMood(phrase.getMood()) == null)
    {
      phrase.setMyMatch( -10000 ) ;
    }
    if(phrase.getPlace() != null && phrase.getPlace().length() > 0 &&
       realPlace(phrase.getPlace()) == null)
    {
      phrase.setMyMatch( phrase.getMyMatch() - STD_MATCH_SUBTRACTION ) ;
    }
    if(phrase.getRel() != null && phrase.getRel().length() > 0 &&
       realRel(phrase.getRel()) == null)
    {
      phrase.setMyMatch( -10000 ) ;
    }
    if(phrase.getActivity() != null && phrase.getActivity().length() > 0 &&
       realActivity(phrase.getActivity()) == null)
    {
      phrase.setMyMatch( -10000 ) ;
    }
  }// checkIfValidPhraseVars
  
  public static Phrase findBestPhrase (ArrayList<Phrase> phraseArr, boolean isQuestion)
  {
    final int WRONG_QS = 8 ;
    
    Phrase result ;
    
    for(int i = 0 ; i < phraseArr.size() ; i++)
    {
      // change match based on num [words] and ]words[
      bracketsChangeUndMatch(phraseArr.get(i)) ;
      
      // I'm checking if all of these are valid names, jobs, etc.
      checkIfValidPhraseVars(phraseArr.get(i), phraseArr.get(i).getIndividual1(), phraseArr.get(i).getIndividual2()) ;
      
      // question or statement
      if(isQuestion != phraseArr.get(i).isQuestion())
        phraseArr.get(i).setMyMatch( phraseArr.get(i).getMyMatch() -WRONG_QS ) ;
      
    }// for
    
    result = findHighestMatch(phraseArr) ;
    
    // default understanding
    if(result == null || result.getMyMatch() < tom.MATCH_MINIMUM_STATEMENT)
    {
      result = new Phrase("Huh?", null, 100, false) ;
    }
    
    return result ;
  }// findBestPhrase
  
  public static Phrase findHighestMatch (ArrayList<Phrase> phraseArr)
  {
    Phrase phrase = null ;
    
    if(phraseArr.size() > 0)
    {
      phrase = phraseArr.get(0) ;
      
      for(int i = 1 ; i < phraseArr.size() ; i++)
      {
        if(phrase.getMyMatch() < phraseArr.get(i).getMyMatch())
          phrase = phraseArr.get(i) ;
      }
    }
    
    return phrase ;
  }// findHighestMatch
  
  public static void bracketsChangeUndMatch (Phrase und)
  {
    int result = -4 ;
    
    result -= und.getNumBracketWords() ;
    result += und.getNumNonBracketWords()*2 ;
    
    und.setMyMatch( und.getMyMatch() + result ) ;
    
    if(und.getMyMatch() >= 100)
      und.setMyMatch(99) ;
    
  }// bracketsChangeUndMatch
  
  public static String realName (String name)
  {
    String result = null ;
    
    if(name != null && name.length() > 0)
    {
      result = tom.hasWord( tom.getUnisexArr(), name) ;
      if(result == null)
        result = tom.hasWord( tom.getBoysArr(), name) ;
      if(result == null)
        result = tom.hasWord( tom.getGirlsArr(), name) ;
      if(result == null)
        result = myWorld.hasNameSomeone(name) ;
    }
    
    return result ;
  }// realName
  
  public static boolean realAge (String age)
  {
    if(age != null && age.length() > 0 && (!isNumeric(age) || !reasonableAge(Integer.parseInt(age))) )
    {
      return false ;
    }
    else
      return true ;
  }// realAge
  
  public static String realJob (String job)
  {
    String result = null ;
    
    if(job != null && job.length() > 0)
    {
      result = tom.hasWord( tom.getJobsArr(), job) ;
    }
    
    return result ;
  }// realJob
  
  public static String realEmployer (String employer)
  {
    String result = null ;
    
    if(employer != null && employer.length() > 0)
    {
      result = tom.hasWord( tom.getEmployersArr(), employer) ;
    }
    
    return result ;
  }// realEmployer
  
  public static String realSchool (String school)
  {
    String result = null ;
    
    if(school != null && school.length() > 0)
    {
      result = tom.hasWord( tom.getSchoolsArr(), school) ;
    }
    
    return result ;
  }// realSchool
  
  public static String realMood (String mood)
  {
    String result = null ;
    
    if(mood != null && mood.length() > 0)
    {
      result = tom.hasMoodReturnFull(mood) ;
    }
    
    return result ;
  }// realMood
  
  public static String realPlace (String place)
  {
    String result = null ;
    
    if(place != null && place.length() > 0)
    {
      result = tom.hasWord( tom.getPlacesArr(), place) ;
    }
    
    return result ;
  }// realPlace
  
  public static String realRel (String rel)
  {
    String result = null ;
    int index ;
    
    // this real~~~ method works differently because it searches
    //  for 1 rel and returns the rel immediately after it
    
    if(rel != null && rel.length() > 0)
    {
      for(index = 0 ; index < tom.getRelsArr().size()-1 && result == null ; index += 2)
      {
        if( tom.foundIn(tom.getRelsArr().get(index), rel) )
          result = tom.getRelsArr().get(index+1) ;
      }
    }
    
    return result ;
  }// realRel
  
  public static String realActivity (String activity)
  {
    String result = null ;
    ArrayList<Activity> activityArr ;
    
    if(activity != null && activity.length() > 0)
    {
      activityArr = tom.getActivityArr() ;
      
      // loop through activityArr
      for(int i = 0 ; i < activityArr.size() && result == null ; i++)
      {
        // check if result is in present form
        result = tom.hasWord( activityArr.get(i).getPresentFormArr(), activity) ;
        
        // check if result is in past form
        if(result == null)
          result = tom.hasWord( activityArr.get(i).getPastFormArr(), activity) ;
        
        // check if result is in past participle form
        if(result == null)
          result = tom.hasWord( activityArr.get(i).getPastParticipleFormArr(), activity) ;
      }
    }
    
    return result ;
  }// realActivity
  
  public static String realVar (String var)
  {
    // check if var is the name of one of Individual's variables
    if(var != null &&
            (var.equals("name") || var.equals("placeOfBirth") || var.equals("nationality") || var.equals("job") ||
             var.equals("isStudent") || var.equals("age") || var.equals("isMale") || var.equals("mood")))
    {
      return var ;
    }
    else
      return null ;
  }// realVar
  
  public static ArrayList<Phrase> findPhrases (String line /* < user given*/, ArrayList<Interchange> phraseArr, boolean isQuestion)
  {
    ArrayList< ArrayList<String> > lineTokensArr = new ArrayList< ArrayList<String> >() ;
    ArrayList<String> lineTokens ;
    ArrayList<String> inputTokens = new ArrayList<String>() ;
    Phrase phrase = null ;
    
    ArrayList<Phrase> resultArr = new ArrayList<Phrase>() ;
    int indexInput = 0 ; // file based
    int subtraction = 0 ;
    
    final int MAX_SUB = 15 ;// originally 15
    final int INDV_SUB = 5 ;// originally 5, 4, 2
    final int P1_is_P2_MATCH_DECREASE = 10 ;// originally 10
    
    lineTokensArr = tom.tokenize(line) ;
    
    // change "we" and double lineTokensArr
    dealWithWe( lineTokensArr ) ;// change half
    
    for(int indexParallel = 0 ; indexParallel < lineTokensArr.size() ; indexParallel++)// loop through parallel lines
    {
      lineTokens = lineTokensArr.get(indexParallel) ;
      sayOneWay(lineTokensArr) ;
      
      for(int s = 0 ; s < phraseArr.size() ; s++)// loop through SM ArrayList
      {
        inputTokens = tom.tokenize(phraseArr.get(s).getInput()).get(0) ;
        phrase = new Phrase(phraseArr.get(s).getInput(), phraseArr.get(s).getOutput(), 0, isQuestion) ;
        indexInput = 0 ;
        
        for(int lStart = 0 ; lStart < lineTokens.size() && indexInput < inputTokens.size() ; lStart++)// loop through (user given) line (lStart)
        {
          phrase.setMyMatch(0) ;
          indexInput = 0 ;
          
          if(inputTokens.size() == 1)// 1 word response
            phrase.setMyMatch(-3) ;// << very specific number
          else if(inputTokens.size() == 2)// 2 word response
            phrase.setMyMatch(-4) ;// << very specific number
          else if(inputTokens.size() < 4)// very short inputs
            phrase.setMyMatch(-6) ;// << very specific number // used to be -5, -10, ->-7<-
          else if(inputTokens.size() == 4)// moderately short
            phrase.setMyMatch(-4) ;// used to be -2
          
          subtraction = 0 ;
          for(int l = lStart ; l < lineTokens.size() && indexInput < inputTokens.size() && subtraction <= MAX_SUB ; l++)// loop through (user given) line (l)
          {
            if(inputTokens.get(indexInput).charAt(0) == '[')
            {
              // set und's words
              // l += No. of steps to the word
              l += findWordAhead(phrase, lineTokens, inputTokens.get(indexInput), l, indexInput) ;
              
              // check if lineTokens.get(index) is number
              if((inputTokens.get(indexInput).equals("[number]") && isNumeric(lineTokens.get(indexInput)) && reasonableAge(Integer.parseInt(lineTokens.get(indexInput)))) ||
                 (!inputTokens.get(indexInput).equals("[number]") && !isNumeric(lineTokens.get(indexInput))) )
              {
                phrase.setMyMatch( phrase.getMyMatch() + 99/inputTokens.size() ) ;
              }
              else
              {
                subtraction += INDV_SUB ;
                indexInput-- ;// stalls indexInput because it ++ soon
              }
            }
            else if(inputTokens.get(indexInput).equals( lineTokens.get(l) ))
            {
              phrase.setMyMatch( phrase.getMyMatch() + 100/inputTokens.size() ) ;
            }
            else
            {
              subtraction += INDV_SUB ;
              indexInput-- ;// stalls indexInput because it ++ soon
            }
            
            indexInput++ ;
          }// for
        }// for
        
        phrase.setMyMatch( phrase.getMyMatch() - subtraction ) ;
        
        // make sure phrase is <100
        if(phrase.getMyMatch() > 99)
          phrase.setMyMatch(99) ;
        
        // reduce match if can't find individual1
        if((phrase.getMyMeaning().indexOf("[individual]") != -1 || phrase.getMyMeaning().indexOf("[individual1]") != -1) &&
          phrase.getIndividual1() == -1)// used to be !=
        {
          phrase.setMyMatch( phrase.getMyMatch() - INDV_SUB ) ;
        }
        // reduce match if can't find individual2
        if(phrase.getMyMeaning().indexOf("[individual2]") != -1 &&
           phrase.getIndividual2() == -1)// used to be !=
        {
          phrase.setMyMatch( phrase.getMyMatch() - INDV_SUB ) ;
        }
        
        // add phrase to resultArr
        if(phrase.getMyMatch() >= tom.MATCH_MINIMUM_STATEMENT)
        {
          resultArr.add( phrase ) ;
        }
      }// for
    }//for
    
    return resultArr ;
  }// findPhrases
  
  public static int findWordAhead (Phrase phrase, ArrayList<String> lineTokens, String bracketWord, int pos, int indexInput)
  {
    final int NUM_WORDS_CHECK = 4 ;// originally 2
    final int SUB_EACH_STEP = 5 ;// originally 5
    final int REL_MATCH_INCREASE = 5 ;// originally 5, 12, 6
    final int PERSON_MATCH_DECREASE = 0 ; // originally 1, 2, 5
    final int NAME_MATCH_INCREASE = 0 ; // originally 5, 2
    
    String tempString = null ;
    int tempInt = -1 ;
    int individual = -1 ;
    String rel = null ;
    
    boolean useLastIndividual = false ;
    
    int result = 0 ;
    
    // check what kind of bracket word it is
    if(bracketWord.equals("[individual]") || bracketWord.equals("[individual1]") || bracketWord.equals("[individual2]"))
    {
      phrase.setMyMatch( phrase.getMyMatch() - PERSON_MATCH_DECREASE ) ;
      
      useLastIndividual = useLastIndividual(phrase, bracketWord) ;
      
      // loop through lineTokens looking for a word that refers to an [individual]
      for(int i = 0 ; i < NUM_WORDS_CHECK && result == 0 && individual == -1 && lineTokens.size() > pos + i ; i++)
      {
        if(tom.hasWord(malePronounArr, lineTokens.get(pos+i)) != null)// check if M pronoun
        {
          individual = lastNonFemaleInd ;
          result = i ;
        }
        else if(tom.hasWord(femalePronounArr, lineTokens.get(pos+i)) != null)// check if F pronoun
        {
          individual = lastNonMaleInd ;
          result = i ;
        }
        else if(myWorld.hasNameSomeone( tom.fromToEnd(lineTokens, pos+i) ) != null ||                             // << Updated
                (realName( tom.fromToEnd(lineTokens, pos+i) ) != null && tom.hasWord(lineTokens, "name") == null))// << Updated
        {
          individual = myWorld.findIndividual( realName( tom.fromToEnd(lineTokens, pos+i) ) ).getReference() ;
          result = i ;
        }
      }
      
      if(individual == -1 &&
         myWorld.hasNameSomeone( lineTokens.get(pos) ) != null &&
         myWorld.findIndividual( lineTokens.get(pos) ) != null)
      {
        individual = myWorld.findIndividual( lineTokens.get(pos) ).getReference() ;
      }
      
      // loop through rels
      for(int r = 1 ; r < lineTokens.size() -pos -result && individual != -1 ; r++)
      {
        // check if rel exists after individual
        rel = realRel(  tom.fromToEnd(lineTokens, pos + result +r)  ) ;
        
        if(rel != null && rel.length() > 0)
        {
          // increase match because it is a rel if not special case
          if(!phrase.getMyMeaning().equals("[individual1].individual = [individual2]"))
            phrase.setMyMatch(  phrase.getMyMatch() + REL_MATCH_INCREASE  ) ;
          
          // find relation
          //if(the individual being refered to doesn't exist)
          if(myWorld.findByRel(rel, myWorld.findIndividual( individual )) != -1 &&
             !phrase.getMyMeaning().equals("[name].individual = [individual]"))
          {
            tempInt = individual ;// temp is one step behind individual
            individual = myWorld.findByRel(rel, myWorld.findIndividual( individual )) ;// becomes the rel
          }
          else
          {
            useLastIndividual = true ;
            
            phrase.setRel( rel ) ;
          }
        }
      }// for
      
      if(!useLastIndividual && tempInt != -1)
      {
        individual = tempInt ;// goes one step back
      }
      
      if(phrase.getMyMeaning().equals("[individual1].individual = [individual2]"))
        phrase.setMyMatch( phrase.getMyMatch() - 1 ) ;
      
      // add individual to phrase
      if(bracketWord.equals("[individual2]"))
        phrase.setIndividual2( individual ) ;
      else
        phrase.setIndividual1( individual ) ;
    }
    else if(bracketWord.equals("[name]"))
    {
      phrase.setMyMatch( phrase.getMyMatch() + NAME_MATCH_INCREASE ) ;
      
      for(int i = 0 ; i < NUM_WORDS_CHECK && result == 0 && tempString == null && lineTokens.size() > pos + i ; i++)
      {
        tempString = realName( tom.fromToEnd(lineTokens, pos+i) ) ;
        if(tempString != null)
          result = i ;
      }
      
      if(tempString == null)
        tempString = lineTokens.get(pos + result) ;
      
      phrase.setName( tempString ) ;
      
      if(realRel(  tom.fromToEnd(lineTokens, pos + result +1)  ) != null)// not a [name], it's a [individual]
        phrase.setMyMatch(-10000) ;
      
      // not an actual name
      if(tempString.equals("i") || tempString.equals("me") || tempString.equals("my") || tempString.equals("myself") || tempString.equals("mine") ||
         tempString.equals("you") || tempString.equals("your") || tempString.equals("yours") || tempString.equals("yourself"))
      {
        phrase.setMyMatch(-10000) ;
      }
    }
    else if(bracketWord.equals("[number]"))
    {
      for(int i = 0 ; i < NUM_WORDS_CHECK && result == 0 && lineTokens.size() > pos + i ; i++)
      {
        if(realAge(  lineTokens.get(pos + i)  ))
          result = i ;
      }
      
      phrase.setNumber(lineTokens.get(pos + result)) ;
    }
    else if(bracketWord.equals("[job]"))
    {
      for(int i = 0 ; i < NUM_WORDS_CHECK && result == 0 && tempString == null && lineTokens.size() > pos + i ; i++)
      {
        tempString = realJob( tom.fromToEnd(lineTokens, pos+i) ) ;
        if(tempString != null)
          result = i ;
      }
      
      if(tempString != null)
        phrase.setJob( tempString ) ;
      else
        phrase.setJob(  lineTokens.get(pos + result)  ) ;
    }
    else if(bracketWord.equals("[employer]"))
    {
      for(int i = 0 ; i < NUM_WORDS_CHECK && result == 0 && tempString == null && lineTokens.size() > pos + i ; i++)
      {
        tempString = realEmployer( tom.fromToEnd(lineTokens, pos+i) ) ;
        if(tempString != null)
          result = i ;
      }
      
      if(tempString != null)
        phrase.setEmployer( tempString ) ;
      else
        phrase.setEmployer(  lineTokens.get(pos + result)  ) ;
    }
    else if(bracketWord.equals("[school]"))
    {
      for(int i = 0 ; i < NUM_WORDS_CHECK && result == 0 && tempString == null && lineTokens.size() > pos + i ; i++)
      {
        tempString = realSchool( tom.fromToEnd(lineTokens, pos+i) ) ;
        if(tempString != null)
          result = i ;
      }
      
      if(tempString != null)
        phrase.setSchool( tempString ) ;
      else
        phrase.setSchool(  lineTokens.get(pos + result)  ) ;
    }
    else if(bracketWord.equals("[mood]"))
    {
      for(int i = 0 ; i < NUM_WORDS_CHECK && result == 0 && tempString == null && lineTokens.size() > pos + i ; i++)
      {
        tempString = realMood( tom.fromToEnd(lineTokens, pos+i) ) ;
        if(tempString != null)
          result = i ;
      }
      
      if(tempString != null)
        tempString = tom.hasMoodReturnFull(tempString) ;
      else
        tempString = lineTokens.get(pos + result) ;
      
      phrase.setMood( tempString ) ;
    }
    else if(bracketWord.equals("[place]"))
    {
      for(int i = 0 ; i < NUM_WORDS_CHECK && result == 0 && tempString == null && lineTokens.size() > pos + i ; i++)
      {
        tempString = realPlace( tom.fromToEnd(lineTokens, pos+i) ) ;
        if(tempString != null)
          result = i ;
      }
      
      if(tempString != null)
        phrase.setPlace( tempString ) ;
      else
        phrase.setPlace(  lineTokens.get(pos + result)  ) ;
    }
    else if(bracketWord.equals("[rel]"))// special case [rel]
    {
      for(int i = 0 ; i < NUM_WORDS_CHECK && result == 0 && tempString == null && lineTokens.size() > pos + i ; i++)
      {
        tempString = realRel( tom.fromToEnd(lineTokens, pos+i) ) ;
        if(tempString != null)
          result = i ;
      }
      
      if(tempString != null && tempString.length() > 0)// new && ~~~ > 0
      {
        phrase.setRel( tempString ) ;
        // special case, match increases
        phrase.setMyMatch(  phrase.getMyMatch() + REL_MATCH_INCREASE  ) ;
      }
      else// rel doesn't exist (unacceptable)
      {
        phrase.setMyMatch( 0 ) ;
      }
    }
    else if(bracketWord.equals("[activity]"))
    {
      for(int i = 0 ; i < NUM_WORDS_CHECK && result == 0 && tempString == null && lineTokens.size() > pos + i ; i++)
      {
        tempString = realActivity( tom.fromToEnd(lineTokens, pos+i) ) ;
        if(tempString != null)
          result = i ;
      }
      
      if(tempString != null)
        phrase.setActivity( tempString ) ;
      else
        phrase.setActivity(  lineTokens.get(pos + result)  ) ;
    }
    else if(bracketWord.equals("[var]"))
    {
      tempString = realVar( lineTokens.get(pos) ) ;
      
      if(tempString != null)
      {
        result = 0 ;
        phrase.setVar( tempString ) ;
      }
      else
        phrase.setMyMatch(-10000) ;
    }
    else
      System.out.println("ERROR: NLP.findWordAhead(), unknown [~~~] : phrase = " + phrase + "\tbw = " + bracketWord) ;
    
    // match goes down if we skipped words and we are not at the start of the input
    if(indexInput != 0)
      phrase.setMyMatch(  phrase.getMyMatch() - result*SUB_EACH_STEP  ) ;
    
    return result ;
  }// findWordAhead
  
  public static boolean useLastIndividual (Phrase phrase, String bracketWord)
  {
    boolean result = true ;
    
    if(phrase.getMyMeaning().equals("[name].individual = [individual]"))
    {
      result = false ;
    }/*
    else if(phrase.getMyMeaning().equals("[individual].name = [name]"))
    {
      result = false ;
    }*/
    else if(phrase.getMyMeaning().equals("[individual1].individual = [individual2]") && bracketWord.equals("[individual2]"))
    {
      result = false ;
    }
    else if(phrase.getMyMeaning().equals("twoWayRel([individual1], [individual2])"))
    {
      result = false ;
    }
    else if(phrase.getMyUnderstanding().equals("is [individual2] [individual1] [rel]"))
    {
      result = false ;
    }
    
    return result ;
  }// useLastIndividual
  
  public static void dealWithWe (ArrayList< ArrayList<String> > lineTokensArr)
  {
    ArrayList<String> lineTokens ;
    int startPos ;
    
    if(lineTokensArr != null)
    {
      startPos = lineTokensArr.size() ;
      tom.doubleArrayListStringString( lineTokensArr ) ;// double
      
      // loop through lineTokensArr
      for(int p = startPos ; p < lineTokensArr.size() ; p++)
      {
        lineTokens = lineTokensArr.get(p) ;
        
        // loop through lineTokens changing pronouns
        for(int l = 0 ; l < lineTokens.size() ; l++)
        {
          // check if word is "we"
          if(lineTokens.get(l).equals("we"))
          {
            changeWe(lineTokens, l) ;
          }
        }// for
      }// for
    }
    else
      System.out.println("ERROR: NLP.dealWithWe(), lineTokensArr == null") ;
  }// dealWithWe
  
  public static void changeWe (ArrayList<String> lineTokens, int pos)
  {
    ArrayList<String> copy ;// deep copy
    
    if(lineTokens != null && pos >= 0)
    {
      copy = tom.deepCopyArrayListString(lineTokens) ;
      
      lineTokens.set(pos, "me") ;
      
      if(pos+1 < lineTokens.size())
        lineTokens.set(pos+1, "and") ;
      else
        lineTokens.add("and") ;
      
      if(pos+2 < lineTokens.size())
        lineTokens.set(pos+2, lastWeInd) ;
      else
        lineTokens.add(lastWeInd) ;
      
      // after "me and [~]" add everything that came after "we"
      for(int i = pos+1 ; i < copy.size() ; i++)
      {
        if(i+2 < lineTokens.size())
          lineTokens.set(i+2, copy.get(i)) ;
        else
          lineTokens.add(copy.get(i)) ;
      }
    }
    else
      System.out.println("ERROR: NLP.changeWe(), invalid parameters - lineTokens == " + lineTokens + "\t\tpos == " + pos) ;
  }// changeWe
  
  public static boolean reasonableAge (int age)
  {
    return(age >= 0 && age <= 120) ;
  }
  
  public static boolean isNumeric (String str)
  {
    return(str != null && str.matches("[-+]?\\d*\\.?\\d+")) ;
  }
  
  public static Individual searchIndividuals (String name)
  {
    Individual result = null ;
    
    for(int p = 0 ; p < myWorld.getIndividualsArr().size() && result == null ; p++)
    {
      for(int n = 0 ; n < myWorld.getIndividualsArr().get(p).getNameArr().size() && result == null ; n++)
      {
        if(myWorld.getIndividualsArr().get(p).getNameArr().get(n).equals(name))
          result = myWorld.getIndividualsArr().get(p) ;
      }
    }
    
    return result ;
  }// searchIndividuals
  
  public static int isCompatable (String line, String input)
  {
    int match = 0 ;
    ArrayList< ArrayList<String> > lineTokensArr = new ArrayList< ArrayList<String> >() ;
    ArrayList<String> lineTokens = new ArrayList<String>() ;
    ArrayList<String> inputTokens = new ArrayList<String>() ;
    
    if(line.equals(input))
      match = 100 ;
    else
    {
      lineTokensArr = tom.tokenize(line) ;
      for(int p = 0 ; p < lineTokensArr.size() ; p++)
      {
        lineTokens = lineTokensArr.get(p) ;
        inputTokens = tom.tokenize(input).get(0) ;// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< inputTokens =/= 2D ArrayList... (1D)
        
        // count # of similarities
        for(int i = 0 ; i < inputTokens.size() ; i++)
        {
          boolean found = false ;
          for(int l = 0 ; l < lineTokens.size() && !found ; l++)
          {
            if(lineTokens.get(l).equals(inputTokens.get(i)))
            {
              found = true ;
              if(l+1 < lineTokens.size() && i+1 < inputTokens.size() && lineTokens.get(l+1).equals(inputTokens.get(i+1)))
                match += 100/inputTokens.size() ;
              else
                match += tom.MATCH_MINIMUM_STATEMENT/inputTokens.size() ;
            }
          }// for
        }// for
      }// for
    }// else
   
    return match ;
  }// isCompatable
  
  public static void sayOneWay (ArrayList< ArrayList<String> > tokensArr)
  {
    // two loops, 1st changing words, 2nd removing words
    
    // make sure tokensArr is valid
    if(tokensArr != null && tokensArr.size() > 0)
    {
      for(int i = 0 ; i < tokensArr.get( tokensArr.size()-1 ).size() ; i++)
      {
        if(tokensArr.get( tokensArr.size()-1 ).get(i).equals("am") ||
           tokensArr.get( tokensArr.size()-1 ).get(i).equals("are") ||
           tokensArr.get( tokensArr.size()-1 ).get(i).equals("has") ||
           tokensArr.get( tokensArr.size()-1 ).get(i).equals("was"))
        {
          tom.doubleArrayListStringString(tokensArr) ;
          
          for(int t = tokensArr.size() /2 ; t < tokensArr.size() ; t++)
          {
            if(i < tokensArr.get( t ).size())
              tokensArr.get( t ).set(i, "is") ;
            else
              System.out.println("NLP.sayOneWay(), i == " + i + "   tokensArr.get( t ).size() == " + tokensArr.get( t ).size()) ;
          }
        }
        else if(tokensArr.get( tokensArr.size()-1 ).get(i).equals("have"))
        {
          tom.doubleArrayListStringString(tokensArr) ;
          
          for(int t = tokensArr.size() /2 ; t < tokensArr.size() ; t++)
          {
            tokensArr.get( t ).set(i, "has") ;
          }
        }
        else if(tokensArr.get( tokensArr.size()-1 ).get(i).equals("an"))
        {
          tom.doubleArrayListStringString(tokensArr) ;
          
          for(int t = tokensArr.size() /2 ; t < tokensArr.size() ; t++)
          {
            tokensArr.get( t ).set(i, "a") ;
          }
        }
        else if(tokensArr.get( tokensArr.size()-1 ).get(i).equals("hate") || tokensArr.get( tokensArr.size()-1 ).get(i).equals("despise") ||
                tokensArr.get( tokensArr.size()-1 ).get(i).equals("loathe") || tokensArr.get( tokensArr.size()-1 ).get(i).equals("detest"))
        {
          tom.doubleArrayListStringString(tokensArr) ;
          
          for(int t = tokensArr.size() /2 ; t < tokensArr.size() ; t++)
          {
            tokensArr.get( t ).set(i, "dislike") ;
          }
        }
      }// for
      
      
      for(int i = 0 ; i < tokensArr.get( tokensArr.size()-1 ).size() ; i++)
      {
        if(tokensArr.get( tokensArr.size()-1 ).get(i).equals("tom") ||
           tokensArr.get( tokensArr.size()-1 ).get(i).equals("tommy") ||
           tokensArr.get( tokensArr.size()-1 ).get(i).equals("thomas"))// checking if they said tom's name
        {
          tom.doubleArrayListStringString(tokensArr) ;
          
          for(int t = tokensArr.size() /2 ; t < tokensArr.size() ; t++)
          {
            if(i < tokensArr.get( t ).size())
              tokensArr.get( t ).remove(i) ;
          }// for
        }
      }
    }
    else
      System.out.println("ERROR: NLP.sayOneWay(), tokensArr == null or ~.size() == 0") ;
  }// sayOneWay
  
  public static void interpretMeaning (Phrase phrase)
  {
    String meaning ;
    String tempString ;
    MyString tempMyString ;
    MyBoolean tempMyBoolean ;
    MyInt tempMyInt ;
    
    Individual tempInd = null ;
    
    int conf1 = 0 ;
    int conf2 = 0 ;
    int match ;
    
    if(phrase != null)
    {
      tom.checkForDefinedInd(phrase) ;
      
      meaning = phrase.getMyMeaning() ;
      match = phrase.getMyMatch() ;
      
      if(meaning != null && meaning.length() > 0)
      {
        if(meaning.equals("[individual].[var] = null"))
        {
          meaning = "[individual]." + phrase.getVar() + " = null" ;
          
          phrase.setVar("") ;
        }
        
        // check if rel was used
        if(phrase.getRel() != null && phrase.getRel().length() > 0 && meaning.indexOf("[individual2]") == -1)// "meaning.indexOf("[individual2]") == -1" used to be commented out
        {
          if(myWorld.getIndividualsArr().size() > phrase.getIndividual1())
          {
            // use the name if it is in the phrase
            if(phrase.getName() != null && phrase.getName().length() > 0)
            {
              tempInd = myWorld.findIndividual(phrase.getName(), match) ;
            }
            else// person doesn't exist yet
            {
              // create a person
              tempInd = new Individual(tom) ;
              myWorld.getIndividualsArr().add(tempInd) ;
            }
            
            
            // vvv oof
            connectByRel(phrase.getRel(), myWorld.findIndividual( phrase.getIndividual1() ), tempInd, match) ;
            
            // used to be ---> phrase.setIndividual1(  phrase.getName()  ) ;
            phrase.setIndividual1( tempInd.getReference() ) ;
          }
        }
        
        
        // check meanings
        if(meaning.equals("[individual].name = [name]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getName() != null && phrase.getName().length() > 0)
          {
            tempMyString = new MyString(phrase.getName(), match) ;
            myWorld.findIndividual( phrase.getIndividual1() ).getNameArr().add(  tempMyString  ) ;
            
            // adding first name aswell as full name is individual's nameArr
            if(phrase.getName().indexOf(" ") > 0)// eg. if "Kevin Adams" and not "Kevin"
            {
              tempMyString = new MyString(phrase.getName().substring(0, phrase.getName().indexOf(" ")), match) ;
              myWorld.findIndividual( phrase.getIndividual1() ).getNameArr().add(  tempMyString  ) ;
              
              
            }
            
            //printAllIndividualsNames() ;
            
            // male or female
            tom.maleOrFemale( myWorld.getIndividualsArr().get(0), phrase.getName() ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[name].individual = [individual]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getName() != null && phrase.getName().length() > 0)
          {
            tempMyString = new MyString(phrase.getName(), match) ;
            myWorld.findIndividual( phrase.getIndividual1() ).getNameArr().add(  tempMyString  ) ;
            
            // adding first name aswell as full name is individual's nameArr
            if(phrase.getName().indexOf(" ") > 0)// eg. if "Kevin Adams" and not "Kevin"
            {
              // vvv oof
              tempMyString = new MyString(phrase.getName().substring(0, phrase.getName().indexOf(" ")), match) ;
              myWorld.findIndividual(  phrase.getIndividual1()  ).getNameArr().add(  tempMyString  ) ;
            }
            
            //printAllIndividualsNames() ;
            
            // male or female
            tom.maleOrFemale( myWorld.getIndividualsArr().get(0), phrase.getName() ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual1].individual = [individual2]"))
        {
          // check if rel was used
          if(phrase.getRel() != null && phrase.getRel().length() > 0)
          {
            if(phrase.getIndividual1() < myWorld.getIndividualsArr().size() &&
               phrase.getIndividual2() < myWorld.getIndividualsArr().size() &&
               myWorld.findByRel(phrase.getRel(), myWorld.findIndividual( phrase.getIndividual1() )) == -1)
            {
              // vvv oof
              connectByRel(phrase.getRel(), myWorld.findIndividual( phrase.getIndividual1() ), myWorld.getIndividualsArr().get( phrase.getIndividual2() ), match) ;
            }
          }
        }
        else if(meaning.equals("[user].name = [name]"))
        {
          if(phrase.getName() != null && phrase.getName().length() > 0)
          {
            // individualsArr.get(0) = the user
            tempMyString = new MyString(phrase.getName(), match) ;
            myWorld.getIndividualsArr().get(0).getNameArr().add(  tempMyString  ) ;
            
            // male or female
            tom.maleOrFemale( myWorld.getIndividualsArr().get(0), phrase.getName() ) ;
            
            //printAllIndividualsNames() ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("new individual([name])"))
        {
          // set phrase.individual1 to the individual with the name [name]
          tempInd = myWorld.findIndividual(phrase.getName(), match) ;
          
          if(tempInd != null)
          {
            phrase.setIndividual1( tempInd.getReference() ) ;
          }
        }
        else if(meaning.equals("new male([name])"))
        {
          // set phrase.individual1 to the individual with the name [name]
          tempInd = myWorld.findIndividual(phrase.getName(), match) ;
          
          if(tempInd != null)
          {
            phrase.setIndividual1( tempInd.getReference() ) ;
            
            // make male
            tempInd.setIsMale( new MyBoolean(true, match) ) ;
          }
        }
        else if(meaning.equals("new female([name])"))
        {
          // set phrase.individual1 to the individual with the name [name]
          tempInd = myWorld.findIndividual(phrase.getName(), match) ;
          
          if(tempInd != null)
          {
            phrase.setIndividual1( tempInd.getReference() ) ;
            
            // make female
            tempInd.setIsMale( new MyBoolean(false, match) ) ;
          }
        }
        else if(meaning.equals("[individual].placeOfBirth = [place]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getPlace() != null && phrase.getPlace().length() > 0)
          {
            tempMyString = new MyString(phrase.getPlace(), match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setPlaceOfBirth(  tempMyString  ) ;
            
            //System.out.println( myWorld.findIndividual(  phrase.getIndividual1()  ).getPlaceOfBirth() ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].job = [job]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getJob() != null && phrase.getJob().length() > 0)
          {
            tempMyString = new MyString(phrase.getJob(), match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setJob(  tempMyString  ) ;
            
            //System.out.println( myWorld.findIndividual(  phrase.getIndividual1()  ).getJob() ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].employer = [employer]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getEmployer() != null && phrase.getEmployer().length() > 0)
          {
            tempMyString = new MyString(phrase.getEmployer(), match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setEmployer(  tempMyString  ) ;
            
            // if you have an employer then you can't have no job
            if(myWorld.findIndividual( phrase.getIndividual1() ).getJob() != null &&
               myWorld.findIndividual( phrase.getIndividual1() ).getJob().getStr() == null &&
               myWorld.findIndividual( phrase.getIndividual1() ).getJob().getConfidence() >= tom.MATCH_MINIMUM_STATEMENT)
            {
              myWorld.findIndividual( phrase.getIndividual1() ).getJob().setConfidence(50) ;// << magic number
              
              
              
              // * !!!!! It might be better if tom ask for clarification !!!!! *
            }
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].school = [school]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getSchool() != null && phrase.getSchool().length() > 0)
          {
            tempMyString = new MyString(phrase.getSchool(), match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setSchool(  tempMyString  ) ;
            
            //System.out.println( myWorld.findIndividual(  phrase.getIndividual1()  ).getSchool() ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].school = null"))
        {
          if(phrase.getIndividual1() != -1)
          {
            tempMyBoolean = new MyBoolean(false, match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setSchool( new MyString(null, match) ) ;
            
            //System.out.println( myWorld.findIndividual( phrase.getIndividual1() ).getIsStudent()  ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].age = [number]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getNumber() != null && phrase.getNumber().length() > 0)
          {
            tempMyInt = new MyInt(Integer.parseInt(phrase.getNumber()), match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setAge(  tempMyInt  ) ;
            
            //System.out.println( myWorld.findIndividual(  phrase.getIndividual1()  ).getAge() ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].isMale = true"))
        {
          if(phrase.getIndividual1() !=-1)
          {
            tempMyBoolean = new MyBoolean(true, match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setIsMale( tempMyBoolean ) ;
            
            //System.out.println( myWorld.findIndividual( phrase.getIndividual1() ).getIsMale()  ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].isMale = false"))
        {
          if(phrase.getIndividual1() != -1)
          {
            tempMyBoolean = new MyBoolean(false, match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setIsMale( tempMyBoolean ) ;
            
            //System.out.println( myWorld.findIndividual( phrase.getIndividual1() ).getIsMale()  ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].location = [place]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getPlace() != null && phrase.getPlace().length() > 0)
          {
            tempMyString = new MyString(phrase.getPlace(), match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setLocation(  tempMyString  ) ;
            
            //System.out.println( myWorld.findIndividual(  phrase.getIndividual1()  ).getLocation() ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].nationality = [place]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getPlace() != null && phrase.getPlace().length() > 0)
          {
            tempMyString = new MyString(phrase.getPlace(), match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setNationality(  tempMyString  ) ;
            
            //System.out.println( myWorld.findIndividual(  phrase.getIndividual1()  ).getNationality() ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].isMood = [mood].num"))
        {
          if(phrase.getIndividual1() != -1 && phrase.getMood() != null && phrase.getMood().length() > 0)
          {
            tempString = tom.hasMoodReturnFull(phrase.getMood()) ;
            
            if(tempString != null && tempString.length() >= 4)
            {
              if(isNumeric( tempString.substring(0,1) ))
              {
                tempMyInt = new MyInt(  Character.getNumericValue( tempString.charAt(0) ), match  ) ;
                
                myWorld.findIndividual(  phrase.getIndividual1()  ).setMyMood( tempMyInt ) ;
              }
              else
                System.out.println("ERROR: NLP.interpretMeaning(), tempString.substring(0,1) == " + tempString.substring(0,1)) ;
            }
            else
              System.out.println("ERROR: NLP.interpretMeaning(), tempString == " + tempString) ;
            /*
             if(  tom.hasWord(tom.getGoodMoodsArr(), phrase.getMood()) != null  )// good mood
             {
             tempMyBoolean = new MyBoolean(true, match) ;
             myWorld.findIndividual(  phrase.getIndividual1()  ).setMyMood( tempMyBoolean ) ;
             }
             else  if(  tom.hasWord(tom.getBadMoodsArr(), phrase.getMood()) != null  )// bad mood
             {
             tempMyBoolean = new MyBoolean(false, match) ;
             myWorld.findIndividual(  phrase.getIndividual1()  ).setMyMood( tempMyBoolean ) ;
             }*/
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].isMood = 10 -[mood].num"))
        {
          if(phrase.getIndividual1() != -1)
          {
            tempString = tom.hasWord(tom.getGoodMoodsArr(), phrase.getMood()) ;
            
            if(tempString == null || tempString.length() < 4)
              tempString = tom.hasWord(tom.getBadMoodsArr(), phrase.getMood()) ;
            
            if(tempString != null && tempString.length() >= 4)
            {
              if(isNumeric( tempString.substring(0,1) ))
              {
                tempMyInt = new MyInt(  10 - Character.getNumericValue(tempString.charAt(0)), match  ) ;
                
                myWorld.findIndividual(  phrase.getIndividual1()  ).setMyMood( tempMyInt ) ;
              }
              else
                System.out.println("ERROR: NLP.interpretMeaning(), tempString.substring(0,1) == " + tempString.substring(0,1)) ;
            }
            else
              System.out.println("ERROR: NLP.interpretMeaning(), tempString == " + tempString) ;
            
            /*
             if(  tom.hasWord(tom.getGoodMoodsArr(), phrase.getMood()) != null  )// good mood
             {
             tempMyBoolean = new MyBoolean(false, match) ;
             myWorld.findIndividual(  phrase.getIndividual1()  ).setMyMood( tempMyBoolean ) ;
             }
             else if(  tom.hasWord(tom.getBadMoodsArr(), phrase.getMood()) != null  )// bad mood
             {
             tempMyBoolean = new MyBoolean(true, match) ;
             myWorld.findIndividual(  phrase.getIndividual1()  ).setMyMood( tempMyBoolean ) ;
             }
             */
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].isFamous = true"))
        {
          if(phrase.getIndividual1() != -1)
          {
            tempMyBoolean = new MyBoolean(true, match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setIsFamous( tempMyBoolean ) ;
          }
          else
            System.out.println("ERROR: NLP.findByRel(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].isFamous = false"))
        {
          if(phrase.getIndividual1() != -1)
          {
            tempMyBoolean = new MyBoolean(false, match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setIsFamous( tempMyBoolean ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].isAlive = true"))
        {
          if(phrase.getIndividual1() != -1)
          {
            tempMyBoolean = new MyBoolean(true, match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setIsAlive( tempMyBoolean ) ;
          }
          else
            System.out.println("ERROR: NLP.findByRel(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].isAlive = false"))
        {
          if(phrase.getIndividual1() != -1)
          {
            tempMyBoolean = new MyBoolean(false, match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setIsAlive( tempMyBoolean ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].isPerson = true"))
        {
          if(phrase.getIndividual1() != -1)
          {
            tempMyBoolean = new MyBoolean(true, match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setIsPerson( tempMyBoolean ) ;
          }
          else
            System.out.println("ERROR: NLP.findByRel(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].isPerson = false"))
        {
          if(phrase.getIndividual1() != -1)
          {
            tempMyBoolean = new MyBoolean(false, match) ;
            myWorld.findIndividual(  phrase.getIndividual1()  ).setIsPerson( tempMyBoolean ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual1].age = [individual2].age"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getIndividual2() != -1)
          {
            conf1 = myWorld.findIndividual(  phrase.getIndividual1()  ).getAge().getConfidence() ;
            conf2 = myWorld.findIndividual(  phrase.getIndividual2()  ).getAge().getConfidence() ;
            
            // whoever's age Tom is more confident about is the age of the other
            if(conf1 > conf2)
            {
              tempMyInt = new MyInt(myWorld.findIndividual(  phrase.getIndividual1()  ).getAge().getNum(), conf1) ;
              myWorld.findIndividual(  phrase.getIndividual2()  ).setAge(  tempMyInt  ) ;
            }
            else
            {
              tempMyInt = new MyInt(myWorld.findIndividual(  phrase.getIndividual2()  ).getAge().getNum(), conf2) ;
              myWorld.findIndividual(  phrase.getIndividual1()  ).setAge(  tempMyInt  ) ;
            }
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("rel([individual1], [individual2])"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getIndividual2() != -1)
          {
            // related to eachother
            connectByRel( phrase.getRel(), myWorld.findIndividual(phrase.getIndividual1()), myWorld.findIndividual(phrase.getIndividual2()), match ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("twoWayRel([individual1], [individual2])"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getIndividual2() != -1)
          {
            // make sure it's a two way rel
            if(!oneWayRel( phrase.getRel() ))
            {
              // related to eachother
              connectByRel( phrase.getRel(), myWorld.findIndividual(phrase.getIndividual1()), myWorld.findIndividual(phrase.getIndividual2()), match ) ;
            }
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("coworker([individual1], [individual2])"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getIndividual2() != -1)
          {
            // work with eachtother
            connectByRel( "coworker", myWorld.findIndividual(phrase.getIndividual1()), myWorld.findIndividual(phrase.getIndividual2()), match ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual1].likesIndividuals = [individual2]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getIndividual2() != -1)
          {
            // add to likesIndividualsArr not already there
            if(!myWorld.findIndividual(phrase.getIndividual1()).getLikesIndividualsArr().contains(  myWorld.findIndividual(phrase.getIndividual2())  ))
              myWorld.findIndividual(phrase.getIndividual1()).getLikesIndividualsArr().add(  myWorld.findIndividual(phrase.getIndividual2())  ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual1].likesIndividuals != [individual2]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getIndividual2() != -1)
          {
            // remove individual from likesIndividualsArr if there (omg I know how this looks)
            tom.removeFromArrayListIndividual(myWorld.findIndividual(phrase.getIndividual1()).getLikesIndividualsArr(), myWorld.findIndividual(phrase.getIndividual2())) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].likesActivities = [activity]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getActivity() != null && phrase.getActivity().length() > 0)
          {
            // add to likesActivitiesArr not already there
            if(!myWorld.findIndividual(phrase.getIndividual1()).getLikesActivitiesArr().contains(  myWorld.findActivity(phrase.getActivity())  ))
              myWorld.findIndividual(phrase.getIndividual1()).getLikesActivitiesArr().add(  myWorld.findActivity(phrase.getActivity())  ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].likesActivities != [activity]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getActivity() != null && phrase.getActivity().length() > 0)
          {
            // remove activity from likesActivitiesArr if there (omg I know how this looks)
            tom.removeFromArrayListActivity(myWorld.findIndividual(phrase.getIndividual1()).getLikesActivitiesArr(), myWorld.findActivity(phrase.getActivity())) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].isDoing = [activity]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getActivity() != null && phrase.getActivity().length() > 0)
          {
            // add to doingArr if not already there
            if(!myWorld.findIndividual(phrase.getIndividual1()).getDoingArr().contains(  myWorld.findActivity(phrase.getActivity())  ))
              myWorld.findIndividual(phrase.getIndividual1()).getDoingArr().add(  myWorld.findActivity(phrase.getActivity())  ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].isDoing != [activity]"))
        {
          if(phrase.getIndividual1() != -1 &&
             phrase.getActivity() != null && phrase.getActivity().length() > 0)
          {
            // remove activity from doingArr if there (omg I know how this looks)
            tom.removeFromArrayListActivity(myWorld.findIndividual(phrase.getIndividual1()).getDoingArr(), myWorld.findActivity(phrase.getActivity())) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].name = null"))
        {
          // do nothing
        }
        else if(meaning.equals("[individual].placeOfBirth = null"))
        {
          if(phrase.getIndividual1() != -1)
          {
            myWorld.findIndividual( phrase.getIndividual1() ).setPlaceOfBirth( new MyString(null, match) ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].nationality = null"))
        {
          if(phrase.getIndividual1() != -1)
          {
            myWorld.findIndividual( phrase.getIndividual1() ).setNationality( new MyString(null, match) ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].location = null"))
        {
          if(phrase.getIndividual1() != -1)
          {
            myWorld.findIndividual( phrase.getIndividual1() ).setLocation( new MyString(null, match) ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].job = null"))
        {
          if(phrase.getIndividual1() != -1)
          {
            myWorld.findIndividual( phrase.getIndividual1() ).setJob( new MyString(null, match) ) ;
            
            // if you have no job then you can't have an employer
            myWorld.findIndividual( phrase.getIndividual1() ).setEmployer( new MyString(null, match) ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].employer = null"))
        {
          if(phrase.getIndividual1() != -1)
          {
            myWorld.findIndividual( phrase.getIndividual1() ).setEmployer( new MyString(null, match) ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].age = null"))
        {
          if(phrase.getIndividual1() != -1)
          {
            myWorld.findIndividual( phrase.getIndividual1() ).setAge( new MyInt(18, 1) ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else if(meaning.equals("[individual].mood = null"))
        {
          if(phrase.getIndividual1() != -1)
          {
            myWorld.findIndividual( phrase.getIndividual1() ).setMyMood( new MyInt(6, 0) ) ;
          }
          else
            System.out.println("ERROR: NLP.interpretMeaning(), this is not understood (should go in ArrayList of not understood phrases)") ;
        }
        else
        {
          System.out.println("ERROR: NLP.interpretMeaning(), unknown meaning == " + meaning) ;
        }
        
        // check sex of individual1
        if(phrase.getIndividual1() != -1 && myWorld.getIndividualsArr().size() < phrase.getIndividual1())
        {
          // use [name] if possible
          if(phrase.getName() != null && phrase.getName().length() > 0)
          {
            tom.maleOrFemale( myWorld.findIndividual( phrase.getIndividual1() ), phrase.getName() ) ;
          }
          else
          {
            tom.maleOrFemale( myWorld.findIndividual( phrase.getIndividual1() ), myWorld.findIndividual( phrase.getIndividual1() ).getNameArr().get(0).getStr()  ) ;
          }
        }
        // check sex of individual2
        if(phrase.getIndividual2() != -1 && myWorld.getIndividualsArr().size() < phrase.getIndividual2())
        {
          // use [name] if possible
          if(phrase.getName() != null && phrase.getName().length() > 0)
          {
            tom.maleOrFemale( myWorld.getIndividualsArr().get( phrase.getIndividual2() ), phrase.getName() ) ;
          }
          else
          {
            tom.maleOrFemale(  myWorld.getIndividualsArr().get( phrase.getIndividual2() ), myWorld.getIndividualsArr().get( phrase.getIndividual2() ).getNameArr().get(0).getStr()  ) ;
          }
        }
      }
      else
        System.out.println("ERROR: NLP.interpretMeaning(), invalid meaning == " + meaning) ;
    }
    else
      System.out.println("ERROR: NLP.interpretMeaning(), phrase == null") ;
    
    setLastMaleFemale(phrase.getIndividual1(), phrase.getIndividual2()) ;
  }// interpretMeaning
  
  public static void setLastMaleFemale (int ind1, int ind2)
  {
    int maleConf1 ;
    int maleConf2 ;
    
    // check if ind1 and ind2 are valid
    if(ind1 >= 0 && ind1 < myWorld.getIndividualsArr().size() && myWorld.getIndividualsArr().get( ind1 ) != null)
    {
      // both inds are not tom or user
      if(ind1 != 0 && ind1 != 1 && ind2 != 0 && ind2 != 1 &&
         ind2 >= 0 && ind2 < myWorld.getIndividualsArr().size() && myWorld.getIndividualsArr().get( ind2 ) != null)
      {
        // get maleConfs for both inds
        maleConf1 = myWorld.getIndividualsArr().get( ind1 ).maleConfidence() ;
        maleConf2 = myWorld.getIndividualsArr().get( ind2 ).maleConfidence() ;
        
        if(maleConf1 < maleConf2)
        {
          if(!myWorld.getIndividualsArr().get( ind2 ).isFemale() && ind2 >= 0)// ind2 is more male
          {
            lastNonFemaleInd = ind2 ;
          }
          else if(!myWorld.getIndividualsArr().get( ind1 ).isMale() && ind1 >= 0)// ind1 is more female
          {
            lastNonMaleInd = ind1 ;
          }
        }
        else
        {
          if(!myWorld.getIndividualsArr().get( ind1 ).isFemale() && ind1 >= 0)// ind1 is more male
          {
            lastNonFemaleInd = ind1 ;
          }
          else if(!myWorld.getIndividualsArr().get( ind2 ).isMale() && ind2 >= 0)// ind2 is more female
          {
            lastNonMaleInd = ind2 ;
          }
        }
      }
      else if(ind1 != 0 && ind1 != 1)// only individual1 is valid
      {
        if(!myWorld.getIndividualsArr().get( ind1 ).isFemale())// ind1 is maybe male
        {
          lastNonFemaleInd = ind1 ;
        }
        if(!myWorld.getIndividualsArr().get( ind1 ).isMale())// ind1 is maybe female
        {
          lastNonMaleInd = ind1 ;
        }
      }
    }
    else
      System.out.println("ERROR: NLP.setLastMaleFemale(), invalid ind1... ind1 == " + ind1) ;
    
  }// setLastMaleFemale
  
  public static boolean oneWayRel (String rel)
  {
    return(rel.equals("father") || rel.equals("mother") || rel.equals("parent") ||
           rel.equals("child") || rel.equals("son") || rel.equals("daughter")) ;
  }
  
  public static void likeEachother (Individual ind1, Individual ind2)
  {
    // like eachother
    if(!ind1.getLikesIndividualsArr().contains( ind2 ))
      ind1.getLikesIndividualsArr().add( ind2 ) ;
    if(!ind2.getLikesIndividualsArr().contains( ind1 ))
      ind2.getLikesIndividualsArr().add( ind1 ) ;
  }
  
  public static void connectByRel (String rel, Individual ind1, Individual ind2, int match)
  {
    if(rel != null && ind1 != null && ind2 != null)
    {
      if(rel.equals("friend"))
      {
        // put in getFriendArr if not already there
        if(!ind1.getFriendArr().contains( ind2 ))
          ind1.getFriendArr().add( ind2 ) ;
        if(!ind2.getFriendArr().contains( ind1 ))
          ind2.getFriendArr().add( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
      }
      else if(rel.equals("coworker"))
      {
        // put in getCoworkerArr if not already there
        if(!ind1.getCoworkerArr().contains( ind2 ))
          ind1.getCoworkerArr().add( ind2 ) ;
        if(!ind2.getCoworkerArr().contains( ind1 ))
          ind2.getCoworkerArr().add( ind1 ) ;
      }
      else if(rel.equals("date"))
      {
        // put in dateArr if not already there
        if(!ind1.getDateArr().contains( ind2 ))
          ind1.getDateArr().add( ind2 ) ;
        if(!ind2.getDateArr().contains( ind1 ))
          ind2.getDateArr().add( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
      }
      else if(rel.equals("boyfriend"))
      {
        // put in dateArr if not already there
        if(!ind1.getDateArr().contains( ind2 ))
          ind1.getDateArr().add( ind2 ) ;
        if(!ind2.getDateArr().contains( ind1 ))
          ind2.getDateArr().add( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
        ind2.setIsMale( new MyBoolean(true, match) ) ;
      }
      else if(rel.equals("girlfriend"))
      {
        // put in dateArr if not already there
        if(!ind1.getDateArr().contains( ind2 ))
          ind1.getDateArr().add( ind2 ) ;
        if(!ind2.getDateArr().contains( ind1 ))
          ind2.getDateArr().add( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
        ind2.setIsMale( new MyBoolean(false, match) ) ;
      }
      else if(rel.equals("father"))
      {
        if(ind2 != ind1.getParent1() && ind2 != ind1.getParent2())
        {
          if(ind1.getParent1() == null)// new father
          {
            ind1.setParent1( ind2 ) ;
            ind2.setIsMale( new MyBoolean(true, match) ) ;
            
            likeEachother(ind1, ind2) ;
          }
          else if(ind1.getParent2() == null)// new father
          {
            ind1.setParent2( ind2 ) ;
            ind2.setIsMale( new MyBoolean(true, match) ) ;
            
            likeEachother(ind1, ind2) ;
          }
          else if(ind1.getParent1().isMale())// new father
          {
            ind1.setParent1( ind2 ) ;
            ind2.setIsMale( new MyBoolean(true, match) ) ;
            
            likeEachother(ind1, ind2) ;
          }
          else if(ind1.getParent2().isMale())// new father
          {
            ind1.setParent2( ind2 ) ;
            ind2.setIsMale( new MyBoolean(true, match) ) ;
            
            likeEachother(ind1, ind2) ;
          }
          else
            System.out.println("ERROR: NLP.connectByRel(), unknown neither parent is male, we need a father") ;
        }
        else
          System.out.println("ERROR: NLP.connectByRel(), already father") ;
        
        // put in childArr if not already there
        if(!ind2.getChildArr().contains( ind1 ))
          ind2.getChildArr().add( ind1 ) ;
      }
      else if(rel.equals("mother"))
      {
        if(ind2 != ind1.getParent1() && ind2 != ind1.getParent2())
        {
          if(ind1.getParent1() == null)// new mother
          {
            ind1.setParent1( ind2 ) ;
            ind2.setIsMale( new MyBoolean(false, match) ) ;
            
            likeEachother(ind1, ind2) ;
          }
          else if(ind1.getParent2() == null)// new mother
          {
            ind1.setParent2( ind2 ) ;
            ind2.setIsMale( new MyBoolean(false, match) ) ;
            
            likeEachother(ind1, ind2) ;
          }
          else if(ind1.getParent1().isFemale())// new mother
          {
            ind1.setParent1( ind2 ) ;
            ind2.setIsMale( new MyBoolean(false, match) ) ;
            
            likeEachother(ind1, ind2) ;
          }
          else if(ind1.getParent2().isFemale())// new mother
          {
            ind1.setParent2( ind2 ) ;
            ind2.setIsMale( new MyBoolean(false, match) ) ;
            
            likeEachother(ind1, ind2) ;
          }
          else
            System.out.println("ERROR: NLP.connectByRel(), unknown neither parent is female, we need a mother") ;
        }
        else
          System.out.println("ERROR: NLP.connectByRel(), already mother") ;
        
        // put in childArr if not already there
        if(!ind2.getChildArr().contains( ind1 ))
          ind2.getChildArr().add( ind1 ) ;
      }
      else if(rel.equals("parent"))
      {
        // set as parent
        if(ind1.getParent1() == null && (ind2.getParent2() == null || !ind2.getParent2().equals( ind1 )) )
          ind1.setParent1( ind2 ) ;
        else if(ind1.getParent2() == null && (ind2.getParent1() == null || !ind2.getParent1().equals( ind1 )) )
          ind1.setParent2( ind2 ) ;
        
        // put in childArr if not already there
        if(!ind2.getChildArr().contains( ind1 ))
          ind2.getChildArr().add( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
      }
      else if(rel.equals("husband"))
      {
        ind1.setSpouse( ind2 ) ;
        ind2.setIsMale( new MyBoolean(true, match) ) ;
        ind2.setSpouse( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
      }
      else if(rel.equals("wife"))
      {
        ind1.setSpouse( ind2 ) ;
        ind2.setIsMale( new MyBoolean(false, match) ) ;
        ind2.setSpouse( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
      }
      else if(rel.equals("spouse"))
      {
        ind1.setSpouse( ind2 ) ;
        ind2.setSpouse( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
      }
      else if(rel.equals("son"))
      {
        // put in childArr if not already there
        if(!ind1.getChildArr().contains( ind2 ))
          ind1.getChildArr().add( ind2 ) ;
        
        // set as parent
        if(ind2.getParent1() == null && (ind2.getParent2() == null || !ind2.getParent2().equals( ind1 )) )
          ind2.setParent1( ind1 ) ;
        else if(ind2.getParent2() == null && (ind2.getParent1() == null || !ind2.getParent1().equals( ind1 )) )
          ind2.setParent2( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
        ind2.setIsMale( new MyBoolean(true, match) ) ; ;
      }
      else if(rel.equals("daughter"))
      {
        // put in childArr if not already there
        if(!ind1.getChildArr().contains( ind2 ))
          ind1.getChildArr().add( ind2 ) ;
        
        // set as parent
        if(ind2.getParent1() == null && (ind2.getParent2() == null || !ind2.getParent2().equals( ind1 )) )
          ind2.setParent1( ind1 ) ;
        else if(ind2.getParent2() == null && (ind2.getParent1() == null || !ind2.getParent1().equals( ind1 )) )
          ind2.setParent2( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
        ind2.setIsMale( new MyBoolean(false, match) ) ;
      }
      else if(rel.equals("child"))
      {
        // put in childArr if not already there
        if(!ind1.getChildArr().contains( ind2 ))
          ind1.getChildArr().add( ind2 ) ;
        
        // set as parent
        if(ind2.getParent1() == null && (ind2.getParent2() == null || !ind2.getParent2().equals( ind1 )) )
          ind2.setParent1( ind1 ) ;
        else if(ind2.getParent2() == null && (ind2.getParent1() == null || !ind2.getParent1().equals( ind1 )) )
          ind2.setParent2( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
      }
      else if(rel.equals("brother"))
      {
        // put in siblingArr if not already there
        if(!ind1.getSiblingArr().contains( ind2 ))
          ind1.getSiblingArr().add( ind2 ) ;
        if(!ind2.getSiblingArr().contains( ind1 ))
          ind2.getSiblingArr().add( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
        ind2.setIsMale( new MyBoolean(true, match) ) ;
      }
      else if(rel.equals("sister"))
      {
        // put in siblingArr if not already there
        if(!ind1.getSiblingArr().contains( ind2 ))
          ind1.getSiblingArr().add( ind2 ) ;
        if(!ind2.getSiblingArr().contains( ind1 ))
          ind2.getSiblingArr().add( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
        ind2.setIsMale( new MyBoolean(false, match) ) ;
      }
      else if(rel.equals("sibling"))
      {
        // put in siblingArr if not already there
        if(!ind1.getSiblingArr().contains( ind2 ))
          ind1.getSiblingArr().add( ind2 ) ;
        if(!ind2.getSiblingArr().contains( ind1 ))
          ind2.getSiblingArr().add( ind1 ) ;
        
        likeEachother(ind1, ind2) ;
      }
      else
        System.out.println("ERROR: NLP.connectByRel(), unknown rel : |" + rel + "|") ;
    }
    else
      System.out.println("ERROR: NLP.connectByRel(), rel == null || ind1 == null || ind2 == null") ;
  }// connectByRel
  
}// NLP
