import java.io.* ;

public class TxtFileToLowerCase
{
  public static void main (String[] args)  throws IOException
  {
    File file1 = new File("Places.txt");
    File file2 = new File("final.txt");
    char CharCounter = 0;
    BufferedReader in = null ;
    PrintWriter out = null ;
    
    try
    {
      in = (new BufferedReader(new FileReader(file1)));
      out = (new PrintWriter(new FileWriter(file2)));
      int ch;
      while ((ch = in.read()) != -1)
      {
        if (Character.isUpperCase(ch))
        {
          ch=Character.toLowerCase(ch);// convert assign variable
        }
        out.write(ch);
      }
    }
    finally
    {
      if(in != null)
        in.close();
      if(out != null)
        out.close();
    }
  }
}
