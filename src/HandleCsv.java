import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

public class HandleCsv {

    @Option(name="-d",usage="create language files inside directories, e.g. en.xml ends up in en/en.xml")
    private boolean doDirectories;

    @Option(name="-i", usage="Specify the encoding for the inputfile. The encoding should be a valid java encoding, eg UTF-8")        // no usage
    private String inputEncoding = "macintosh";

    // receives other command line parameters than options
    @Argument
    private List<String> arguments = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		new HandleCsv().doMain(args);
	}

	public void doMain(String args[]) throws IOException {
		CmdLineParser parser = new CmdLineParser(this);

		// if you have a wider console, you could increase the value;
		// here 80 is also the default
		parser.setUsageWidth(80);

		try {
			// parse the arguments.
			parser.parseArgument(args);

			// you can parse additional arguments if you want.
			// parser.parseArgument("more","args");

			// after parsing arguments, you should check
			// if enough arguments are given.
			if (arguments.isEmpty())
				throw new CmdLineException("No argument is given");

		} catch (CmdLineException e) {
			// if there's a problem in the command line,
			// you'll get this exception. this will report
			// an error message.
			System.out.println(e.getMessage());
			System.out.println("java SampleMain [options...] arguments...");
			// print the list of available options
			parser.printUsage(System.out);
			System.out.println();

			// print option sample. This is useful some time
			System.out.println("  Example: java SampleMain"
					+ parser.printExample(null));

			return;
		}

		// access non-option arguments
		//System.out.println("other arguments are:");
		//for (String s : args)
		//	System.out.println(s);
		String InputFile = args[args.length-1];
		//System.out.println(recursive);

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(InputFile), inputEncoding));
			System.out.println("Reading " + InputFile + " " + inputEncoding);
			String strLine = br.readLine();
			String[] languages = strLine.split(";");

			br.mark(60000);
			for (int i = 1; i < languages.length; i++) {
				String filename = languages[i] + ".xml";
				if (doDirectories) {
					filename = languages[i] +"/" + languages[i] + ".xml";
					
				}
				FileWriter FileOutStream = new FileWriter(filename);

				System.out.println("Writing " + languages[i] + ".xml" + " "
						+ FileOutStream.getEncoding());
				BufferedWriter out = new BufferedWriter(FileOutStream);
				out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				out.write("<resources>\n");
				while ((strLine = br.readLine()) != null)

					if (!strLine.equalsIgnoreCase("")) {
						String[] values = strLine.split(";");

						String translation = "";

						if (values.length == 1) {
							translation = values[0];
						} else if (values.length <= i) {
							translation = values[1];
						} else {
							translation = values[i];
						}

						out.write("  <string name=\"" + values[0] + "\">"
								+ translation + "</string>\n");
					}
				out.write("</resources>\n");
				out.flush();
				out.close();
				br.reset();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found (or something else)");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
