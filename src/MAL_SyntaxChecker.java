import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Eric on 2/10/2016.
 */
public class MAL_SyntaxChecker {

    public static void main(String[] args) {

        ArrayList<String> labels_defined = new ArrayList<>(); //Stores all created labels
        ArrayList<String> labels_used = new ArrayList<>(); //Stores all referenced labels
        MAL_Error error; //Object that holds errors found in instructions
        error = new MAL_Error();
        /*
        All used labels must relate to a defined label. Otherwise critical error.
        Each defined label should be used. Otherwise compiler warning (not error).
         */

        try {
            Scanner input = new Scanner(System.in);  // Reading from System.in
            System.out.print("Enter filename (no extension): ");
            String fileName = input.next();
            File file = new File(fileName+".mal");
            FileInputStream stream;
            stream = new FileInputStream(file);
            PrintWriter writer = new PrintWriter(fileName+".log", "UTF-8");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            writer.write(fileName+".mal");
            writer.write("MAL Compiler Output\n");
            Date sysDate = new Date();
            writer.write("Generated: "+ sysDate.toString());
            writer.write("\nCS 3210");
            writer.write("\nEric Carver");
            writer.write("\n---------------------------------------------");

            String line;
            int count = 0;
            boolean hasEnd = false; //Makes sure there is at least one "END" in the program

            while ((line = reader.readLine()) != null) {
                count++;
                LineParser code = new LineParser(line);
                if(!code.line.isEmpty()) writer.write("\n"+count + ".\t" + code.line); //Print the instruction if there is any text

                if(!code.getLabel().isEmpty()) {
                    //System.out.println("-----\t$$$"+code.getLabel());
                    labels_defined.add(code.getLabel()); //Adds the label to an array list.
                }
                if(!code.getCommand().isEmpty()) { //Triggers the command parsing
                    //System.out.println("-----\t***"+code.getCommand());
                    if(code.getCommand().equals("END")) //Allows the END command to ignore the parser
                        hasEnd = true;
                    else error = code.parseInstruction(code.getCommand()); //Triggers the command parsing
                    if(!error.isEmpty)
                        break;
                    if(!code.label.isEmpty())
                        labels_used.add(code.label);
                }
                if(!code.line.isEmpty() && code.getLabel().isEmpty() && code.getCommand().isEmpty()){ //If line is not empty and has no labels or commands, error.
                    System.out.println("ERROR ON LINE " + count);
                    error = code.parseInstruction(code.getCommand());
                    break;
                }
            }

            writer.write("\n------------------------------");
            writer.write("------------------------------\n");
            for (int i = 0; i < error.messages.size(); i++)
                writer.write(error.messages.get(i)+"\n");
            if(hasEnd)
                writer.write("Compilation success.\nNo errors detected!");
            else if (error.messages.isEmpty())
                writer.write("Compilation failed.\n\"END\" not seen. No errors detected otherwise.");
            else
                writer.write("Compilation failed with " + error.messages.size() + " errors.");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
