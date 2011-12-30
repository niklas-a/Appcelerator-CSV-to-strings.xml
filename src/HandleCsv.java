import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class HandleCsv
{
  public static void main(String[] args)
  {
    String InputFile = args[0];
    try
    {
      BufferedReader br = new BufferedReader(
        new InputStreamReader(new FileInputStream(InputFile), 
        "macintosh"));
      System.out.println("Reading " + InputFile + " " + "macintosh");
      String strLine = br.readLine();
      String[] languages = strLine.split(";");

      br.mark(60000);
      for (int i = 1; i < languages.length; i++)
      {
        FileWriter FileOutStream = new FileWriter(languages[i] + ".xml");

        System.out.println("Writing " + languages[i] + ".xml" + " " + FileOutStream.getEncoding());
        BufferedWriter out = new BufferedWriter(FileOutStream);
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        out.write("<resources>\n");
        while ((strLine = br.readLine()) != null)
          if (!strLine.equalsIgnoreCase("")) {
            String[] values = strLine.split(";");

            String translation = "";

            if (values.length == 1) {
              translation = values[0];
            }
            else if (values.length <= i) {
              translation = values[1];
            }
            else {
              translation = values[i];
            }

            out.write("  <string name=\"" + values[0] + "\">" + 
              translation + "</string>\n");
          }
        out.write("</resources>\n");
        out.flush();
        out.close();
        br.reset();
      }
    }
    catch (FileNotFoundException e) {
      System.out.println("File not found (or something else)");
      e.printStackTrace();
      System.exit(0);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }
} 