import java.util.* ;
import java.io.* ;

public class MeaningsIntoFile
{
  public static void main (String[] args)  throws IOException
  {
    File file1 = new File("SMs.txt") ;
    File file2 = new File("final.txt") ;
    
    boolean isMeaningLine = false ;
    
    ArrayList<String> meanings = new ArrayList<String>() ;
    
    BufferedReader in = null ;
    PrintWriter out = null ;
    
    try
    {
      in = (new BufferedReader(new FileReader(file1))) ;
      out = (new PrintWriter(new FileWriter(file2))) ;
      String line ;
      while((line = in.readLine()) != null)
      {
        if(line.length() > 0)
        {
          if(isMeaningLine)
          {
            if(!search(meanings, line))
            {
              meanings.add(line) ;
              System.out.println(line) ;
              out.write(line) ;
            }
            isMeaningLine = false ;
          }
          else
            isMeaningLine = true ;
        }
      }
    }
    finally
    {
      if(in != null)
        in.close() ;
      if(out != null)
        out.close() ;
    }
  }
  
  private static boolean search (ArrayList<String> list, String key)
  {
    boolean result = false ;
    
    for(int i = 0 ; i < list.size() && !result ; i++)
    {
      if(list.get(i).equals(key))
        result = true ;
    }
    
    return result ;
  }// search
}













