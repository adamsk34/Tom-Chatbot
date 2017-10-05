import java.util.* ;

public class Label
{
  private ArrayList<String> wordsArr = new ArrayList<String>() ;
  
  public Label (ArrayList<String> wordsGiven)
  {
    wordsArr = wordsGiven ;
  }
  public boolean equals(ArrayList<String> wordsGiven)
  {
    boolean result = true ;
    
    if(wordsArr.size() != wordsGiven.size())
      result = false ;
    else
    {
      for(int i = 0 ; i < wordsArr.size() && result ; i++)
      {
        if( !wordsArr.get(i).equals(  wordsGiven.get(i)  ) )
          result = false ;
      }
    }
    
    return result ;
  }// equals
  public boolean equals(String word)
  {
    return(wordsArr.size() != 1 || !wordsArr.get(0).equals( word )) ;
  }// equals
}
