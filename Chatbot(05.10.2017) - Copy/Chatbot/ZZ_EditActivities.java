import java.util.* ;
import java.io.* ;

public class ZZ_EditActivities
{
  public static void main (String[] args) throws IOException
  {
    File fileIn = new File("ActivitiesOriginal.txt");
    File fileOut = new File("Activities.txt");
    char CharCounter = 0;
    BufferedReader in = null ;
    PrintWriter out = null ;
    
    try
    {
      in = (new BufferedReader(new FileReader(fileIn)));
      out = (new PrintWriter(new FileWriter(fileOut)));
      int ch;
      while ((ch = in.read()) != -1)
      {
        if(ch < 48 || ch > 57)// checking if ch is '0' through '9'
        {
          out.write(ch);
        }
      }
    }
    finally
    {
      if(in != null)
        in.close();
      if(out != null)
        out.close();
    }
  }// main
}// class