// We think too much and feel too little. More than machinery we need humanity.

import java.util.* ;
import java.io.* ;

public class ChatBot
{
  private static NLP myNLP ;
  private static World myWorld ;
  
  private static ArrayList<Interchange> arrQA ; // questions with associated answer
  private static ArrayList<Interchange> arrQYN ;// yes/or questions
  private static ArrayList<Interchange> arrQM ; // questions with associated meaning
  private static ArrayList<Interchange> arrSM ; // statements with associated meaning
  private static ArrayList<Interchange> arrMyQ ; // tom's questions for the user
  private static String fileNameQA = "QAs.txt" ;
  private static String fileNameQM = "QMs.txt" ;
  private static String fileNameSM = "SMs.txt" ;
  private static String fileNameMyQ = "myQs.txt" ;
  
  private String fileNameJobs = "Jobs.txt" ;
  private String fileNameEmployers = "Employers.txt" ;
  private String fileNameSchools = "Schools.txt" ;
  private String fileNameBadMoods = "MoodsBad.txt" ;
  private String fileNameGoodMoods = "MoodsGood.txt" ;
  private String fileNameBoys = "NamesBoy.txt" ;
  private String fileNameGirls = "NamesGirl.txt" ;
  private String fileNameUnisex = "NamesUnisex.txt" ;
  private String fileNamePlaces = "Places.txt" ;
  private String fileNameRels = "Rels.txt" ;
  private String fileNameActivities = "Activities.txt" ;
  private String fileNameAbbreviations = "Abbreviations.txt" ;
  
  // consider putting these in a new class
  // lists of all known jobs, employers, schools, etc.
  private ArrayList<String> jobsArr      = new ArrayList<String>() ;
  private ArrayList<String> employersArr = new ArrayList<String>() ;
  private ArrayList<String> schoolsArr   = new ArrayList<String>() ;
  private ArrayList<String> badMoodsArr  = new ArrayList<String>() ;
  private ArrayList<String> goodMoodsArr = new ArrayList<String>() ;
  private ArrayList<String> boysArr      = new ArrayList<String>() ;
  private ArrayList<String> girlsArr     = new ArrayList<String>() ;
  private ArrayList<String> unisexArr    = new ArrayList<String>() ;
  private ArrayList<String> placesArr    = new ArrayList<String>() ;
  
  private ArrayList<Interchange> relsArr = new ArrayList<Interchange>() ;// all known relationships
  
  private static ArrayList<Activity> activityArr = new ArrayList<Activity>() ;// all known activities
  
  private ArrayList<Abbreviation> abbreviationArr = new ArrayList<Abbreviation>() ;// all known abbreviations
  
  private static ArrayList<Phrase> myQuestions ;// tom's Qs that he asks user
  
  private boolean printInds = false ;// if true, tom prints all Individuals in his world, every time he talks
  
  private static int origSizeArrSM = 0 ;
  
  private static final int MATCH_MINIMUM_QUESTION = 100000 ;// originally 70, 80, 100000
  public static final int MATCH_MINIMUM_STATEMENT = 80 ;// originally 80, 70
  private static final int NAME_TO_GENDER_CONFIDENCE = 85 ;// originally 85
  
  public ChatBot () throws IOException
  {
    System.out.println("Hello, I'm Tom! Enter a query below: (\"quit\" or \'q\' to quit ; \"world\" or \'w\' to see my world model)");
    
    myNLP = new NLP() ;
    myWorld = new World() ;
    
    myNLP.setTom(this) ;
    myNLP.setMyWorld(myWorld) ;
    
    myWorld.setTom(this) ;
    myWorld.setMyNLP(myNLP) ;
    
    fillMyQuestions() ;
    
    // opens every file and initializes corresponding arrays
    readAllFiles() ;
    
    // create user and Tom as Individuals
    createUser() ;
    createTom() ;
  }// ChatBot constructor
  
  public void printStats ()
  {
    System.out.println("Printing stats...\n" +
                       "# of  QAs: " + arrQA.size() + "\n" +
                       "# of QYNs: " + arrQYN.size() + "\n" +
                       "# of  QMs: " + arrQM.size() + "\n" +
                       "# of  SMs: " + arrSM.size() + "\n" +
                         "# of myQs: " + arrMyQ.size()) ;
  }
  
  public String sendToTom (String line /*<< the line the user enters*/)
  {
    Interchange temp ;
    
    String response = "" ;
    
    Phrase phrase = null ;
    
    boolean isStatement = false ;
    boolean isQuestion = false ;
    
    // check if the user entered anything
    if(line.length() > 0)
    {
      // check punctuation to see if it is likely a statement or a question
      if(line.charAt( line.length()-1 ) == '.' || line.charAt( line.length()-1 ) == '!')
      {
        isStatement = true ;
        isQuestion = false ;
      }
      else if(line.charAt( line.length()-1 ) == '?')
      {
        isQuestion = true ;
        isStatement = false ;
      }
      else
      {
        // unsure whether it is a phrase or a question.
        isStatement = false ;
        isQuestion = false ;
      }
      
      line = simplifyLine(line) ;
      
      if(!isQuitLine(line) && !isWorldLine(line))
      {
        phrase = listenToUser(line, isQuestion) ;
        response = respondToUser(phrase) ;
      }
      else if(isWorldLine(line))
      {
        printInds = !printInds ;
      }
      
      if(!isQuitLine(line) && printInds)
        printAllIndividuals() ;
    }
    
    return response ;
  }// sendToTom
  
  public static Phrase listenToUser (String line, boolean isQuestion)
  {
    ArrayList<Phrase> phraseArr ;
    Phrase phrase = null ;
    ArrayList<Interchange> interArr ;
    
    // fill interArr
    interArr = new ArrayList<Interchange>() ;
    interArr.addAll( arrQM ) ;
    interArr.addAll( arrQYN ) ;
    
    // fill phraseArr with appropriate phrases (statements)
    phraseArr = NLP.findPhrases(line, arrSM, false) ;
    
    // append appropriate phrases (questions)
    phraseArr.addAll( NLP.findPhrases(line, interArr, true) ) ;
    
    // compare phrases and pick the best one
    phrase = NLP.findBestPhrase(phraseArr, isQuestion) ;
    
    return phrase ;
  }// listenToUser
  
  public static String respondToUser (Phrase phrase)
  {
    String response = "" ;
    
    final int SHORT_RESPONSE = 15 ;
    
    // interpret meaning if there is one
    if(phrase != null && phrase.getMyMeaning() != null)
    {
      if(phrase.isQuestion())
      {
        response = myWorld.answerQuestion(phrase) ;
        
        // if(tom doesn't know the answer and it is in regards to the user)
        if(response.equals("I don't know.") && phrase.getIndividual1() == 0 && phrase.getIndividual2() == -1)
        {
          // turn the question back around at the user
          myQuestions.add(phrase) ;
        }
      }
      else
      {
        NLP.interpretMeaning(phrase) ;
      }
    }
    
    //System.out.println(phrase + "   ind1 = " + phrase.getIndividual1() + "   ind2 = " + phrase.getIndividual2()) ;
    
    if(myQuestions == null)
      myQuestions = new ArrayList<Phrase>() ;
    if(response == null)
      response = "" ;
    
    // check if tom created individuals that aren't in phrase
    checkForExtraInds(phrase) ;
    
    // check if tom should remove new SMs
    while(arrSM.size() > origSizeArrSM)
    {
      arrSM.remove( origSizeArrSM ) ;
    }
    
    if(arrSM.size() != origSizeArrSM)
      System.out.println("ERROR: ChatBot.sendToTom(), arrSM.size() = " + arrSM.size() + "   origSizeArrSM = " + origSizeArrSM) ;
    
    // ask user a question
    if(myQuestions.size() > 0 && response.length() <= SHORT_RESPONSE)// ask a question if response is short.
    {
      if(response.length() > 0)
        response += " " ;
      
      response += askUserQuestion() ;
    }
    
    return response ;
  }// respondToUser
  
  public static void checkForExtraInds (Phrase phrase)
  {
    // check if tom created individuals when he shouldn't have
    for(int i = myWorld.getOrigNumInds() ; i < myWorld.getIndividualsArr().size() ; i++)
    {
      if(myWorld.getIndividualsArr().get(i).getReference() != phrase.getIndividual1() &&
         myWorld.getIndividualsArr().get(i).getReference() != phrase.getIndividual2())
      {
        myWorld.getIndividualsArr().remove(i) ;
      }
      else if(i >= 2)// not user/tom
      {
        addStandardIndQuestions(i) ;
      }
    }
    
    myWorld.setOrigNumInds() ;
  }// checkForExtraInds
  
  public static void printAllIndividuals ()
  {
    for(int i = 0 ; i < myWorld.getIndividualsArr().size() ; i++)
    {
      if(myWorld.getIndividualsArr().get(i) != null)
      {
        myWorld.getIndividualsArr().get(i).print() ;
      }
      else
        System.out.println("ERROR: ChatBot.printAllIndividuals(), individualsArr.get(i) == null") ;
    }
  }// printAllIndividuals
  
  public void createUser ()
  {
    Individual user = new Individual(this) ;
    
    myWorld.getIndividualsArr().add(user) ;
    // set user's many names
    user.getNameArr().add( new MyString("i", 100) ) ;
    user.getNameArr().add( new MyString("me", 100) ) ;
    user.getNameArr().add( new MyString("my", 100) ) ;
    user.getNameArr().add( new MyString("myself", 100) ) ;
    user.getNameArr().add( new MyString("mine", 100) ) ;
    
    // change details
    user.setIsAlive( new MyBoolean(true, 100) ) ;
    user.getDoingArr().add(  myWorld.findActivity("thinking")  ) ;
    user.getDoingArr().add(  myWorld.findActivity("chatting")  ) ;
    user.getDoingArr().add(  myWorld.findActivity("talking")  ) ;
  }// createUser
  
  public void createTom ()
  {
    Individual tom = new Individual(this) ;
    
    myWorld.getIndividualsArr().add(tom) ;
    // set Tom's many names
    tom.getNameArr().add( new MyString("tom", 99) ) ;
    tom.getNameArr().add( new MyString("tommy", 99) ) ;
    tom.getNameArr().add( new MyString("thomas", 99) ) ;
    tom.getNameArr().add( new MyString("you", 100) ) ;
    tom.getNameArr().add( new MyString("your", 100) ) ;
    tom.getNameArr().add( new MyString("yours", 100) ) ;
    tom.getNameArr().add( new MyString("yourself", 100) ) ;
    
    // change details
    tom.setLocation( new MyString("your computer", 100) ) ;
    tom.setNationality( new MyString("your computer", 100) ) ;
    tom.setJob( new MyString("ChatBot", 100) ) ;
    tom.setSchool( new MyString(null, 100) ) ;
    tom.setIsMale( new MyBoolean(true, 100) ) ;
    tom.setMyMood( new MyInt(7, 100) ) ;
    tom.getFriendArr().add( myWorld.getIndividualsArr().get(0) ) ;
    myWorld.getIndividualsArr().get(0).getFriendArr().add( tom ) ;// changing user's details
    tom.getLikesIndividualsArr().add( myWorld.getIndividualsArr().get(0) ) ;
    
    tom.getDoingArr().add(  myWorld.findActivity("thinking")  ) ;
    tom.getDoingArr().add(  myWorld.findActivity("chatting")  ) ;
    tom.getDoingArr().add(  myWorld.findActivity("talking")  ) ;
    
    tom.getLikesActivitiesArr().add(  myWorld.findActivity("thinking")  ) ;
    tom.getLikesActivitiesArr().add(  myWorld.findActivity("chatting")  ) ;
    tom.getLikesActivitiesArr().add(  myWorld.findActivity("talking")  ) ;
    
    tom.setIsFamous( new MyBoolean(false, 100) ) ;
    tom.setIsAlive( new MyBoolean(true, 100) ) ;
    tom.setIsPerson( new MyBoolean(false, 100) ) ;
  }// createTom
  
  public int getNextRef ()
  {
    return myWorld.getIndividualsArr().size() ;
  }
  
  public void readAllFiles () throws IOException
  {
    try {
      readQAsFile() ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read QAs file.") ;
    }
    try {
      readQMsFile() ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read QMs file.") ;
    }
    try {
      readSMsFile() ;
      
      fillArrQYN() ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read SMs file.") ;
    }
    try {
      readMyQsFile() ;
      
      fillArrQYN() ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read myQs file.") ;
    }
    try {
      readFileRels() ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read rels file.") ;
    }
    try {
      readFileActivities() ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read Activities file.") ;
    }
    try {
      readFileAbbreviations() ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read Abbreviations file.") ;
    }
    try {
      readFileFillList(fileNameJobs, jobsArr) ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read a file.") ;
    }
    try {
      readFileFillList(fileNameEmployers, employersArr) ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read a file.") ;
    }
    try {
      readFileFillList(fileNameSchools, schoolsArr) ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read a file.") ;
    }
    try {
      readFileFillList(fileNameBadMoods, badMoodsArr) ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read a file.") ;
    }
    try {
      readFileFillList(fileNameGoodMoods, goodMoodsArr) ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read a file.") ;
    }
    try {
      readFileFillList(fileNameBoys, boysArr) ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read a file.") ;
    }
    try {
      readFileFillList(fileNameGirls, girlsArr) ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read a file.") ;
    }
    try {
      readFileFillList(fileNameUnisex, unisexArr) ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read a file.") ;
    }
    try {
      readFileFillList(fileNamePlaces, placesArr) ;
    }
    catch(Exception IOException) {
      System.out.println("Failed to read a file.") ;
    }
  }// readAllFiles
  
  public static void fillMyQuestions ()
  {
    myQuestions = new ArrayList<Phrase>() ;
    
    addMyQuestion("[individual].nationality", 0) ;
    addMyQuestion("[individual].age", 0) ;
    
    addStandardIndQuestions(0) ;
    
    addMyQuestion("[individual].mood", 0) ;
  }// fillMyQuestions
  
  public static void addStandardIndQuestions (int ind)
  {
    addMyQuestion("[individual].school", ind) ;
    addMyQuestion("[individual].job", ind) ;
    addMyQuestion("maleOrFemale([individual])", ind) ;
    addMyQuestion("[individual].name", ind) ;
  }// addStandardIndQuestions
  
  public static void addMyQuestion (String meaning, int ind)
  {
    Phrase phrase ;
    
    phrase = new Phrase(null, meaning, 0, true) ;
    
    checkForDefinedInd(phrase) ;
    
    phrase.setIndividual1(ind) ;
    myQuestions.add( phrase ) ;
  }// addMyQuestion
  
  public static String askUserQuestion ()
  {
    String result = "" ;
    String meaning ;
    String bracketWord ;
    String varWord ;
    String varEquals ;
    Phrase phrase ;
    int qChoice ;
    
    qChoice = myQuestions.size() -1 ;
    
    // qChoice becomes a question w/o an answer or -1
    while(qChoice >= 0 && myWorld.questionHasAnswer( myQuestions.get(qChoice) ))
    {
      if(myWorld.questionHasAnswer( myQuestions.get(qChoice) ))
      {
        myQuestions.remove(qChoice--) ;
      }
    }
    
    if(qChoice >= 0)
    {
      phrase = myQuestions.get( qChoice ) ;
      
      if(phrase != null)
      {
        // find a random question with the meaning
        phrase.setMyUnderstanding( randFindUnderstanding(  arrMyQ, phrase.getMyMeaning()  ) ) ;
        
        // check if returned null/""
        if(phrase.getMyUnderstanding() != null && phrase.getMyUnderstanding().length() > 0)
        {
          // so tom won't ask the same question again
          myQuestions.remove(qChoice) ;
          
          result = makeQuestion(phrase) + "?" ;
        }
        
        // add SMs which are the user's answer
        // -----
        // ex) t: "what's your name?"
        //     u: "it's kevin"
        meaning = phrase.getMyMeaning() ;
        
        if(meaning != null)
        {
          if(meaning.indexOf(".") != -1)
          {
            varWord = meaning.substring(meaning.indexOf(".") +1, meaning.length()) ;
            bracketWord = findBracketWord(varWord) ;
            
            varEquals = bracketWord ;
            
            if(varWord.equals("mood"))
            {
              varWord = "isMood" ;
              varEquals += ".num" ;
              
              // "not sad"
              addSM("not " + bracketWord, "[individualsArr.get(" + phrase.getIndividual1() + ")]."+varWord+" = 10 -"+varEquals) ;
              addSM("not too " + bracketWord, "[individualsArr.get(" + phrase.getIndividual1() + ")]."+varWord+" = 10 -"+varEquals) ;
              addSM("not half " + bracketWord, "[individualsArr.get(" + phrase.getIndividual1() + ")]."+varWord+" = 10 -"+varEquals) ;
              addSM("not all " + bracketWord, "[individualsArr.get(" + phrase.getIndividual1() + ")]."+varWord+" = 10 -"+varEquals) ;
              addSM("not that " + bracketWord, "[individualsArr.get(" + phrase.getIndividual1() + ")]."+varWord+" = 10 -"+varEquals) ;
              addSM("not all that " + bracketWord, "[individualsArr.get(" + phrase.getIndividual1() + ")]."+varWord+" = 10 -"+varEquals) ;
              addSM("not very " + bracketWord, "[individualsArr.get(" + phrase.getIndividual1() + ")]."+varWord+" = 10 -"+varEquals) ;
              
              //System.out.println(arrSM.get(arrSM.size()-1)) ;
            }
            
            addSM(bracketWord, "[individualsArr.get(" + phrase.getIndividual1() + ")]."+varWord+" = "+varEquals) ;
            
            addSM("it is "+bracketWord, "[individualsArr.get(" + phrase.getIndividual1() + ")]."+varWord+" = "+varEquals) ;
            
            addSM("it was "+bracketWord, "[individualsArr.get(" + phrase.getIndividual1() + ")]."+varWord+" = "+varEquals) ;
          }
        }
        else
          System.out.println("ERROR: ChatBot.askUserQuestion(), meaning == null") ;
      }
      else
        System.out.println("ERROR: ChatBot.askUserQuestion(), phrase == null") ;
    }
    else
      System.out.println("ERROR: ChatBot.askUserQuestion(), qChoice == 0") ;
    
    return result ;
  }// askUserQuestion
  
  public static String findBracketWord (String varWord)
  {
    String result ;
    
    if(varWord.equals("placeOfBirth") || varWord.equals("nationality") || varWord.equals("location"))
      result = "[place]" ;
    else if(varWord.equals("age"))
      result = "[number]" ;
    else
    {
      result = "["+varWord+"]" ;
    }
    
    return result ;
  }// findBracketWord
  
  public static void addSM (String in, String out)
  {
    Interchange inter ;
    
    inter = new Interchange(in) ;
    inter.setOutput(out) ;
    arrSM.add(inter) ;
  }// addSM
  
  public static String makeQuestion (Phrase phrase)
  {
    String result = "" ;
    String refer ;
    
    Individual ind1 ;
    Individual ind2 ;
    
    int charsLong ;
    
    if(phrase != null)
    {
      result = phrase.getMyUnderstanding() ;
      
      if(result != null)
      {
        if(phrase.getIndividual1() >= 0 && phrase.getIndividual1() < myWorld.getIndividualsArr().size())
        {
          if(result.indexOf("is [individual") != -1)
          {
            ind1 = myWorld.findIndividual(phrase.getIndividual1()) ;
            
            if(phrase.getIndividual1() == 0)// user
              refer = "are you" ;
            else if(phrase.getIndividual1() == 1)// tom
              refer = "am i" ;
            else if(ind1.bestName() != null)// tom knows their name and will use it
              refer = "is " + ind1.bestName() ;
            else if(ind1.isMale())// male
              refer = "is he" ;
            else if(ind1.isFemale())// female
              refer = "is she" ;
            else// default
              refer = "are they" ;
            
            // "is [individual]" is 15 chars long btw.
            if(result.indexOf("is [individual") == result.indexOf("is [individual]"))
              charsLong = 15 ;
            else
              charsLong = 16 ;
            
            result = result.substring(0, result.indexOf("is [individual")) +
              refer +
              result.substring(result.indexOf("is [individual") +charsLong, result.length()) ;
          }
          
          if(result.indexOf("do [individual") != -1)
          {
            ind1 = myWorld.findIndividual(phrase.getIndividual1()) ;
            
            if(phrase.getIndividual1() == 0)// user
              refer = "do you" ;
            else if(phrase.getIndividual1() == 1)// tom
              refer = "do i" ;
            else if(ind1.bestName() != null)// tom knows their name and will use it
              refer = "does " + ind1.bestName() ;
            else if(ind1.isMale())// male
              refer = "does he" ;
            else if(ind1.isFemale())// female
              refer = "does she" ;
            else// default
              refer = "do they" ;
            
            // "do [individual]" is 15 chars long btw.
            if(result.indexOf("do [individual") == result.indexOf("do [individual]"))
              charsLong = 15 ;
            else
              charsLong = 16 ;
            
            result = result.substring(0, result.indexOf("do [individual")) +
              refer +
              result.substring(result.indexOf("do [individual") +charsLong, result.length()) ;
          }
          
          while(result.indexOf("[individual") != -1)
          {
            ind1 = myWorld.findIndividual(phrase.getIndividual1()) ;
            
            if(phrase.getIndividual1() == 0)// user
              refer = "you" ;
            else if(phrase.getIndividual1() == 1)// tom
              refer = "i" ;
            else if(ind1.bestName() != null)// tom knows their name and will use it
              refer = ind1.bestName() ;
            else if(ind1.isMale())// male
              refer = "he" ;
            else if(ind1.isFemale())// female
              refer = "she" ;
            else// default
              refer = "they" ;
            
            // "[individual]" is 12 chars long btw.
            if(result.indexOf("[individual") == result.indexOf("[individual]"))
              charsLong = "[individual]".length() ;
            else
              charsLong = "[individual2]".length() ;
            
            result = result.substring(0, result.indexOf("[individual"))
                     + refer +
                     result.substring(result.indexOf("[individual") +charsLong, result.length()) ;
          }
        }
      }
      else
        System.out.println("ERROR: ChatBot.makeQuestion(), result == null") ;
    }
    else
      System.out.println("ERROR: ChatBot.makeQuestion(), phrase == null") ;
    return result ;
  }// makeQuestion
  
  public static String randFindUnderstanding (ArrayList<Interchange> arr, String meaning)
  {
    String result = null ;
    ArrayList<String> undArr = new ArrayList<String>() ;
    int rnd ;
    
    // fills up undArr with every understanding that corresponds to meaning
    for(int i = 0 ; i < arr.size() ; i++)
    {
      if(arr.get(i).getOutput().equals(meaning))
      {
        undArr.add( arr.get(i).getInput() ) ;
      }
    }
    
    // randomly chooses an understanding from undArr
    if(undArr.size() > 0)
    {
      rnd = (int)(Math.random() * undArr.size()) ;
      
      result = undArr.get(rnd) ;
    }
    
    return result ;
  }// randFindUnderstanding
  
  public static void checkForDefinedInd (Phrase phrase)
  {
    String meaning ;
    String tempString ;
    
    if(phrase != null)
    {
      meaning = phrase.getMyMeaning() ;
      
      if(meaning != null)
      {
        // check for [individualsArr.get(0/1)] in lieu of [individual1/2]
        if(meaning.indexOf("[individualsArr.get(") != -1)
        {
          tempString = meaning ;
          
          if(phrase.getIndividual1() == -1)
          {
            if(findNumber( meaning.substring(20, meaning.length()) ) != -1)// [individualsArr.get(0)]
            {
              phrase.setIndividual1( findNumber( meaning.substring(20, meaning.length()) ) ) ;
              
              // replace [individualsArr.get(X)] with [individual1/2]
              meaning = tempString.substring(0, meaning.indexOf("[individualsArr.get(")) +
                "[individual" ;
              
              if(phrase.getIndividual2() != -1)
                meaning += "1" ;
              
              meaning += "]" + tempString.substring(meaning.indexOf("[individualsArr.get(") +24, tempString.length()) ;
            }
            else
              System.out.println("ERROR: ChatBot.checkForDefinedInd(), meaning.charAt(20) == " + meaning.charAt(20)) ;
          }
          else
          {
            if(findNumber( meaning.substring(20, meaning.length()) ) != -1)// [individualsArr.get(0)]
            {
              phrase.setIndividual2( findNumber( meaning.substring(20, meaning.length()) ) ) ;
              
              // replace [individualsArr.get(X)] with [individual1/2]
              meaning = tempString.substring(0, meaning.indexOf("[individualsArr.get(")) +
                "[individual2" + "]" +
                tempString.substring(meaning.indexOf("[individualsArr.get(") +24, tempString.length()) ;
            }
            else
              System.out.println("ERROR: ChatBot.checkForDefinedInd(), meaning.charAt(20) == " + meaning.charAt(20)) ;
          }
        }
        
        phrase.setMyMeaning(meaning) ;
      }
      else
        System.out.println("ERROR: ChatBot.checkForDefinedInd(), meaning == null") ;
    }
    else
      System.out.println("ERROR: ChatBot.checkForDefinedInd(), phrase == null") ;
  }// checkForDefinedInd
  
  public static int findNumber (String str)
  {
    int result = -1 ;
    
    for(int i = 0 ; i < str.length() && myNLP.isNumeric( str.substring(i,i+1) ) ; i++)
    {
      if(result == -1)
        result = 0 ;
      else
        result *= 10 ;
      
      result += Integer.parseInt( str.substring(i,i+1) ) ;
    }
    
    return result ;
  }// findNumber
  
  public static String hasWord (ArrayList<String> arr, String key)
  {
    return hasWord(arr, key, 0) ;
  }// hasWord
  
  public static String hasWord (ArrayList<String> arr, String key, int start)
  {
    String result = null ;
    
    for(int i = 0 ; i < arr.size() && result == null ; i++)
    {
      if(start < arr.get(i).length() &&
         foundIn(arr.get(i).substring(start, arr.get(i).length()), key) )
      {
        // make sure result is the full word
        if(key.length() == arr.get(i).length() - start ||
           !isLetter(key.charAt( arr.get(i).length() - start )))
        {
          result = arr.get(i).substring(start, arr.get(i).length()) ;
        }
      }
    }
    
    return result ;
  }// hasWord
  
  public String hasMoodReturnFull (String key)
  {
    String result = null ;
    int start = 2 ;
    
    if(key != null && key.length() > 0)
    {
      // if starts with num
      if(myNLP.isNumeric( key.substring(0,1) ))
        key = key.substring(2, key.length()) ;
      
      // check good moods
      for(int i = 0 ; i < goodMoodsArr.size() && result == null ; i++)
      {
        if(start < goodMoodsArr.get(i).length() &&
           foundIn(goodMoodsArr.get(i).substring(start, goodMoodsArr.get(i).length()), key) )
        {
          // make sure result is the full word
          if(key.length() == goodMoodsArr.get(i).length() - start ||
             !isLetter(key.charAt( goodMoodsArr.get(i).length() - start )))
          {
            result = goodMoodsArr.get(i) ;
          }
        }
      }// for
      
      // check bad moods
      for(int i = 0 ; i < badMoodsArr.size() && result == null ; i++)
      {
        if(start < badMoodsArr.get(i).length() &&
           foundIn(badMoodsArr.get(i).substring(start, badMoodsArr.get(i).length()), key) )
        {
          // make sure result is the full word
          if(key.length() == badMoodsArr.get(i).length() - start ||
             !isLetter(key.charAt( badMoodsArr.get(i).length() - start )))
          {
            result = badMoodsArr.get(i) ;
          }
        }
      }// for
    }
    else
      System.out.println("ERROR: ChatBot.hasMoodReturnFull(), key == " + key) ;
    
    return result ;
  }// hasMoodReturnFull
  
  public static boolean isLetter (char ch)
  {
    boolean result = false ;
    
    // check lowercase
    if((int)ch >= (int)'a' && (int)ch <= (int)'z')
      result = true ;
    
    // check uppercase
    if((int)ch >= (int)'A' && (int)ch <= (int)'Z')
      result = true ;
    
    return result ;
  }// isLetter
  
  public static boolean isVowel (char ch)
  {
    return(ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') ;
  }
  
  public static String simplifyLine (String line)
  {
    line = line.toLowerCase() ;
    line = removeChars(line, "?.,!()") ;
    
    return line ;
  }// readFromUser
  
  public static String removeChars(String line, String chars)
  {
    String result = "" ;
    boolean badChar = false ;
    
    for(int i = 0 ; i < line.length() ; i++)
    {
      badChar = false ;
      for(int j = 0 ; j < chars.length() && !badChar ; j++)
      {
        if(line.charAt(i) == chars.charAt(j))
          badChar = true ;
      }
      
      if(!badChar)
        result += line.charAt(i) ;
    }
    
    return result ;
  }// removeChars
  
  public static boolean isQuitLine (String line)
  {
    boolean result = false ;
    
    if(line != null)
    {
      result = (line.equals("quit") || line.equals("q")) ;
    }
    
    return result ;
  }// isQuitLine
  
  public static boolean isWorldLine (String line)
  {
    boolean result = false ;
    
    if(line != null)
    {
      result = (line.equals("world") || line.equals("w")) ;
    }
    
    return result ;
  }// isWorldLine
  
  public static String findAnswer (String line)
  {
    String response = null ;
    int matchMinimum = MATCH_MINIMUM_QUESTION ;
    int match = 0 ;
    
    if(!isQuitLine(line))
    {
      for(int i = 0 ; i < arrQA.size() && match < 100 ; i++)
      {
        match = NLP.isCompatable(line, arrQA.get(i).getInput().toLowerCase()) ;
        
        if(match >= matchMinimum)
        {
          response = arrQA.get(i).getOutput() ;
          matchMinimum = match+1 ;
        }
      }
    }
    else// quitting
    {
      response = "Goodbye." ;
    }
    
    return response ;
  }// findResponse
  
  public static String fromToEnd (ArrayList<String> userLineTokens, int pos)
  {
    String result = "" ;
    
    for(int i = pos ; i < userLineTokens.size() ; i++)
    {
      result += userLineTokens.get(i) ;
      if(i < userLineTokens.size() -1)
        result += " " ;
    }
    return result ;
  }// fromToEnd
  
  public ArrayList< ArrayList<String> > tokenize (String str)
  {
    ArrayList< ArrayList<String> > tokensArr = new ArrayList< ArrayList<String> >() ;
    StringTokenizer tokenizer = new StringTokenizer(str) ;
    
    Abbreviation abb = null ;
    
    String bothHalves = "" ;
    String half1 = "" ;
    String half2 = "" ;
    
    int origArrSize ;
    
    tokensArr.add( new ArrayList<String>() ) ;
    
    while(tokenizer.hasMoreTokens())
    {
      origArrSize = tokensArr.size() ;
      half1 = tokenizer.nextToken() ;
      half2 = "" ;
      bothHalves = "" ;
      
      // change abbreviations into two words
      if(half1.indexOf('\'') >= 0)
      {
        bothHalves = half1 ;
        // quadruple tokensArr
        doubleArrayListArrayListString(tokensArr) ;
        doubleArrayListArrayListString(tokensArr) ;
        
        abb = searchAbbreviations(half1) ;
        
        if(abb != null)
        {
          half1 = abb.getHalf1() ;
          half2 = abb.getHalf2() ;
        }
        else if(half1.length() > 2 && half1.charAt(  half1.length()-2  ) == '\'' && half1.charAt(  half1.length()-1  ) == 's')// 's
        {
          half1 = half1.substring(0, half1.length()-2) ;
          half2 = "is" ;
        }
        else
        {
          System.out.println("ERROR: ChatBot.tokenize(), unknown apostrophe. |" + str + "|") ;
        }
      }
      else if(half1.equals("gonna"))
      {
        half1 = "going" ;
        half2 = "to" ;
      }
      else if(half1.equals("gotta"))
      {
        half1 = "must" ;
      }
      
      if(half1 != null && half1.length() > 0)// 1st half
      {
        for(int i = 0 ; i < tokensArr.size() ; i++)
        {
          if(bothHalves != null && bothHalves.length() > 0 && i < origArrSize && i%2 == 0)
            tokensArr.get(i).add(bothHalves) ;
          else
            tokensArr.get(i).add(half1) ;
        }
      }
      if(half2 != null && half2.length() > 0)// 2nd half
      {
        for(int i = origArrSize*2 ; i < tokensArr.size() ; i++)
        {
          tokensArr.get(i).add(half2) ;
        }
      }
    }// while
    
    //System.out.println(tokensArr) ;
    
    return tokensArr ;
  }// tokenize
  
  public Abbreviation searchAbbreviations (String word)
  {
    Abbreviation result = null ;
    
    for(int i = 0 ; i < abbreviationArr.size() ; i++)
    {
      if(word.equals(  abbreviationArr.get(i).getWord()  ))
        result = abbreviationArr.get(i) ;
    }
    
    return result ;
  }
  
  public static void doubleArrayListArrayListString (ArrayList< ArrayList<String> > tokensArr)
  {
    int originalSize ;
    
    if(tokensArr != null && tokensArr.size() > 0)
    {
      originalSize = tokensArr.size() ;
      
      for(int i = 0 ; i < originalSize ; i++)
      {
        tokensArr.add(  deepCopyArrayListString(tokensArr.get(i))  ) ;
      }
    }
    else
      System.out.println("ERROR: ChatBot.doubleArrayListStringString(), invalid tokensArr") ;
  }// doubleArrayListArrayListString
  
  public static ArrayList<String> deepCopyArrayListString (ArrayList<String> tokens)
  {
    ArrayList<String> result = new ArrayList<String>() ;
    
    for(int i = 0 ; i < tokens.size() ; i++)
    {
      result.add(  tokens.get(i)  ) ;
    }
    
    return result ;
  }// deepCopyArrayListString
  
  public static void fillArrQYN ()
  {
    arrQYN = new ArrayList<Interchange>() ;
    String tempInput = null ;
    String[] wordsArr = {"is", "was", "do", "does"} ;
    
    if(arrSM != null)
    {
      // loop through arrSM
      for(int i = 0 ; i < arrSM.size() ; i++)
      {
        for(int w = 0 ; w < wordsArr.length ; w++)
        {
          // move wordsArr[w] to front
          tempInput = moveWordsToFront(arrSM.get(i).getInput(), wordsArr[w]) ;
          if(tempInput != null)
          {
            addInterchangeArrQYN( tempInput, i ) ;
            
            tempInput = null ;
          }
        }// for
        
        // add "do you think " to front
        tempInput = "do you think " + arrSM.get(i).getInput() ;
        addInterchangeArrQYN( tempInput, i ) ;
        
        // add "do you believe " to front
        tempInput = "do you believe " + arrSM.get(i).getInput() ;
        addInterchangeArrQYN( tempInput, i ) ;
        
        // add "is it true " to front
        tempInput = "is it true " + arrSM.get(i).getInput() ;
        addInterchangeArrQYN( tempInput, i ) ;
        
        // add "is you sure " to front
        tempInput = "is you sure " + arrSM.get(i).getInput() ;
        addInterchangeArrQYN( tempInput, i ) ;
        
        // add "do " to front
        tempInput = "do " + arrSM.get(i).getInput() ;
        addInterchangeArrQYN( tempInput, i ) ;
        
        // add "so " to front
        tempInput = "so " + arrSM.get(i).getInput() ;
        addInterchangeArrQYN( tempInput, i ) ;
        
        // add "surely " to front
        tempInput = "surely " + arrSM.get(i).getInput() ;
        addInterchangeArrQYN( tempInput, i ) ;
        
        // add "does " to front
        tempInput = "does " + arrSM.get(i).getInput() ;
        addInterchangeArrQYN( tempInput, i ) ;
        
        // add " right" to the end
        tempInput = arrSM.get(i).getInput() + " right" ;
        addInterchangeArrQYN( tempInput, i ) ;
        
        // add "yes or no " to the beginning
        tempInput = "yes or no " + arrSM.get(i).getInput() ;
        addInterchangeArrQYN( tempInput, i ) ;
        
        // add " yes or no" to the end
        tempInput = arrSM.get(i).getInput() + " yes or no" ;
        addInterchangeArrQYN( tempInput, i ) ;
      }// for
    }
    else
      System.out.println("ERROR: ChatBot.fillArrQYN(), arrSM == null") ;
  }// fillArrQYN
  
  public static void addInterchangeArrQYN (String input, int pos)
  {
    Interchange inter = new Interchange( input ) ;
    inter.setOutput(arrSM.get( pos ).getOutput()) ;
    arrQYN.add( inter ) ;
  }// addInterchangeArrQYN
  
  public static String moveWordsToFront (String str, String word)
  {
    String result = null ;
    int wordSize = 0 ;
    
    if(str != null && str.length() > 3)
    {
      wordSize = word.length() ;
      
      // look for "wordsArr[w]
      for(int i = 0 ; i < str.length() - wordSize ; i++)// loop through str
      {
        if(str.substring(i, i + wordSize).equals(word))// found "is"
        {
          result = word + " " + str.substring(0, i) + str.substring(i + wordSize+1, str.length()) ;
        }
      }// for
    }
    else
      System.out.println("ERROR: ChatBot.moveIsAndWasToFront(), invalid str") ;
    
    return result ;
  }// moveIsAndWasToFront
  
  public static void readQAsFile () throws IOException
  {
    BufferedReader in = null ;
    boolean isQuestion = true ;
    Interchange curr = null ;
    
    arrQA = new ArrayList<Interchange>() ;
    
    try
    {
      in = new BufferedReader(new FileReader(fileNameQA)) ;
      
      String line ;
      while((line = in.readLine()) != null)
      {
        if(line.length() != 0)
        {
          if(isQuestion)
          {
            curr = new Interchange(line) ;
            arrQA.add(curr) ;
            
            isQuestion = false ;
          }
          else
          {
            curr.setOutput(line) ;
            isQuestion = true ;
          }
        }
      }
    }
    finally
    {
      if(in != null)
      {
        in.close() ;
      }
    }
  }// readQAsFile
  
  public static void readQMsFile () throws IOException
  {
    BufferedReader in = null ;
    boolean isQuestion = true ;
    Interchange curr = null ;
    
    arrQM = new ArrayList<Interchange>() ;
    
    try
    {
      in = new BufferedReader(new FileReader(fileNameQM)) ;
      
      String line ;
      while((line = in.readLine()) != null)
      {
        if(line.length() != 0)
        {
          if(isQuestion)
          {
            curr = new Interchange(line) ;
            arrQM.add(curr) ;
            
            isQuestion = false ;
          }
          else
          {
            curr.setOutput(line) ;
            isQuestion = true ;
          }
        }
      }
    }
    finally
    {
      if(in != null)
      {
        in.close() ;
      }
    }
  }// readQMsFile
  
  public static void readSMsFile () throws IOException
  {
    BufferedReader in = null ;
    boolean isPhrase = true ;
    Interchange curr = null ;
    
    arrSM = new ArrayList<Interchange>() ;
    
    try
    {
      in = new BufferedReader(new FileReader(fileNameSM)) ;
      
      String line ;
      while((line = in.readLine()) != null)
      {
        if(line.length() != 0)
        {
          if(isPhrase)
          {
            curr = new Interchange(line) ;
            arrSM.add(curr) ;
            
            isPhrase = false ;
          }
          else
          {
            curr.setOutput(line) ;
            isPhrase = true ;
          }
        }
      }
      
      if(arrSM != null)
        origSizeArrSM = arrSM.size() ;
      else
        System.out.println("ERROR: ChatBot.readSMsFile(), arrSM == null") ;
    }
    finally
    {
      if(in != null)
      {
        in.close() ;
      }
    }
  }// readSMsFile
  
  public static void readMyQsFile () throws IOException
  {
    BufferedReader in = null ;
    boolean isQuestion = true ;
    Interchange curr = null ;
    
    arrMyQ = new ArrayList<Interchange>() ;
    
    try
    {
      in = new BufferedReader(new FileReader(fileNameMyQ)) ;
      
      String line ;
      while((line = in.readLine()) != null)
      {
        if(line.length() != 0)
        {
          if(isQuestion)
          {
            curr = new Interchange(line) ;
            arrMyQ.add(curr) ;
            
            isQuestion = false ;
          }
          else
          {
            curr.setOutput(line) ;
            isQuestion = true ;
          }
        }
      }
    }
    finally
    {
      if(in != null)
      {
        in.close() ;
      }
    }
  }// readMyQsFile
  
  public void readFileRels () throws IOException
  {
    BufferedReader in = null ;
    String[] tokens ;
    Interchange curr ;
    
    try
    {
      in = new BufferedReader(new FileReader(fileNameRels)) ;
      
      String line ;
      while((line = in.readLine()) != null)
      {
        if(line.length() != 0)
        {
          curr = new Interchange(line) ;
          
          line = in.readLine() ;
          if(line != null && line.length() != 0)
          {
            curr.setOutput(line) ;
            
            // add curr to relsArr
            relsArr.add(curr) ;
          }
          else
            curr = null ;
        }
        else
          curr = null ;
      }// while
    }
    finally
    {
      if(in != null)
      {
        in.close() ;
      }
    }
  }// readFileRels
  
  public void readFileActivities () throws IOException
  {
    BufferedReader in = null ;
    String[] tokens ;
    Activity curr ;
    
    try
    {
      in = new BufferedReader(new FileReader(fileNameActivities)) ;
      
      String line ;
      while((line = in.readLine()) != null)
      {
        if(line.length() != 0)
        {
          // turn line into curr's vars
          tokens = line.split("\t");
          if(tokens != null && tokens.length == 6)
          {
            curr = new Activity() ;
            
            curr.addPresent(tokens[1]) ;
            curr.addPresent(tokens[4]) ;
            curr.addPresent(tokens[5]) ;
            
            curr.addPast(tokens[2]) ;
            
            curr.addPastParticiple(tokens[3]) ;
            
            // put curr in activityArr
            activityArr.add(curr) ;
          }
          else
            System.out.println("ERROR: ChatBot.readFileActivities(), tokens.length == " + tokens[0]) ;
        }
      }// while
    }
    finally
    {
      if(in != null)
      {
        in.close() ;
      }
    }
  }// readFileActivities
  
  public void readFileAbbreviations () throws IOException
  {
    BufferedReader in = null ;
    String[] tokens ;
    Abbreviation curr ;
    
    try
    {
      in = new BufferedReader(new FileReader(fileNameAbbreviations)) ;
      
      String line ;
      while((line = in.readLine()) != null)
      {
        if(line.length() != 0)
        {
          curr = new Abbreviation(line) ;
          
          line = in.readLine() ;
          if(line != null && line.length() != 0 && line.indexOf(" ") != -1)
          {
            curr.setHalf1( line.substring(0, line.indexOf(" ")) ) ;
            curr.setHalf2( line.substring(line.indexOf(" ") +1, line.length()) ) ;
            
            // add curr to relsArr
            abbreviationArr.add(curr) ;
          }
          else
            curr = null ;
        }
        else
          curr = null ;
      }// while
    }
    finally
    {
      if(in != null)
      {
        in.close() ;
      }
    }
  }// readFileAbbreviations
  
  public void readFileFillList (String fileName, ArrayList<String> arr) throws IOException
  {
    BufferedReader in = null ;
    
    try
    {
      in = new BufferedReader(new FileReader(fileName)) ;
      
      String line ;
      while((line = in.readLine()) != null)
      {
        if(line.length() != 0)
        {
          arr.add(line) ;
        }
      }
    }
    finally
    {
      if(in != null)
      {
        in.close() ;
      }
    }
  }// readFileFillList
  
  public static boolean foundIn (String inputLine, String key)
  {
    boolean result = true ;
    
    if(inputLine.length() <= key.length())
    {
      for(int i = 0 ; i < inputLine.length() && result ; i++)
      {
        if(inputLine.charAt(i) != key.charAt(i))
          result = false ;
      }
    }
    else
      result = false ;
    
    return result ;
  }// foundIn
  
  public static void printAllIndividualsNames ()
  {
    System.out.println("individualsArr.size() == " + myWorld.getIndividualsArr().size()) ;
    
    for(int i = 0 ; i < myWorld.getIndividualsArr().size() ; i++)
    {
      if(myWorld.getIndividualsArr().get(i) != null)
        System.out.println(myWorld.getIndividualsArr().get(i).getNameArr()) ;
      else
        System.out.println("ERROR: ChatBot.printAllIndividuals(), individualsArr.get(" + i + ") == null") ;
    }
  }// printAllIndividuals
  
  public static void removeFromArrayListIndividual (ArrayList<Individual> arr, Individual p)
  {
    if(arr != null)
    {
      for(int i = 0 ; i < arr.size() ; i++)
      {
        if(arr.get(i) == p)
          arr.remove(i) ;
      }
    }
  }// removeFromArrayListIndividual
  
  public static void removeFromArrayListActivity (ArrayList<Activity> arr, Activity a)
  {
    if(arr != null)
    {
      for(int i = 0 ; i < arr.size() ; i++)
      {
        if(arr.get(i) == a)
          arr.remove(i) ;
      }
    }
  }// removeFromArrayListActivity
  
  public void maleOrFemale (Individual individualGiven, String name)
  {
    if(!individualGiven.isMale() && !individualGiven.isFemale())
    {
      if(hasWord( boysArr, name ) != null)
      {
        individualGiven.setIsMale( new MyBoolean(true, NAME_TO_GENDER_CONFIDENCE) ) ;
      }
      else if(hasWord( girlsArr, name ) != null)
      {
        individualGiven.setIsMale( new MyBoolean(false, NAME_TO_GENDER_CONFIDENCE) ) ;
      }
    }
  }// maleOrFemale
  
  public ArrayList<String> getUnisexArr ()
  {
    return unisexArr ;
  }
  
  public ArrayList<String> getBoysArr ()
  {
    return boysArr ;
  }
  
  public ArrayList<String> getGirlsArr ()
  {
    return girlsArr ;
  }
  
  public ArrayList<String> getJobsArr ()
  {
    return jobsArr ;
  }
  
  public ArrayList<String> getEmployersArr ()
  {
    return employersArr ;
  }
  
  public ArrayList<String> getSchoolsArr ()
  {
    return schoolsArr ;
  }
  
  public ArrayList<String> getGoodMoodsArr ()
  {
    return goodMoodsArr ;
  }
  
  public ArrayList<String> getBadMoodsArr ()
  {
    return badMoodsArr ;
  }
  
  public ArrayList<String> getPlacesArr ()
  {
    return placesArr ;
  }
  
  public ArrayList<Interchange> getRelsArr ()
  {
    return relsArr ;
  }
  
  public ArrayList<Activity> getActivityArr ()
  {
    return activityArr ;
  }
  
  public ArrayList<Interchange> getArrSM ()
  {
    return arrSM ;
  }
  
}// class ChatBot


















