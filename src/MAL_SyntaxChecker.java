import java.io.*;
import java.util.*;

/**
 * Created by Eric on 2/10/2016.
 */
public class MAL_SyntaxChecker {

    public static void main(String[] args) {

        ArrayList<String> labels_defined = new ArrayList<>(); //Stores all created labels
        ArrayList<String> labels_used = new ArrayList<>(); //Stores all referenced labels
        MAL_Error error; //Object that holds errors found in instructions
        error = new MAL_Error(); //Any compilation errors will be stored here
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

            writer.write(fileName+".mal\n");
            writer.write("MAL Compiler Output\n");
            Date sysDate = new Date();
            writer.write("Generated: "+ sysDate.toString());
            writer.write("\nCS 3210");
            writer.write("\nEric Carver");
            writer.write("\n---------------------------------------------");

            String line;
            int count = 0;
            int num_errors = 0;
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

                    else error = code.parseInstruction(code.getCommand()); //Triggers the command parsing which returns errors if any exist

                    if(!code.label.isEmpty())
                        labels_used.add(code.label);
                }
                if(!code.line.trim().isEmpty() && code.getLabel().isEmpty() && code.getCommand().isEmpty() && !code.line.trim().equals("")){ //If line is not empty and has no labels or commands, error.
                    //error = new MAL_Error("gen", "");
                }

                //Done with this iteration of the while loop. Now, print out errors:

                if(!error.isEmpty) { //Print out all errors, if any.
                    writer.write("\n"); //New line for error
                    for (int i = 0; i < error.messages.size(); i++) {
                        writer.write(error.messages.get(i) + "\n");
                        num_errors++;
                    }
                }
            }

            writer.write("\n------------------------------");
            writer.write("------------------------------\n");

            //Below block of code removes duplicates by creating a set out of the array list (sets don't allow duplicates)
            Set removeDups = new LinkedHashSet(labels_defined);
            Set removeDups2 = new LinkedHashSet(labels_used);
            labels_defined.clear();
            labels_used .clear();
            labels_defined.addAll(removeDups);
            labels_defined.addAll(removeDups2);

            if(!labels_defined.isEmpty()){ //Prints out labels defined in the program
                writer.write("Labels defined: ");
                for(int i=0; i<labels_defined.size(); i++) {
                    writer.write(labels_defined.get(i));
                    if (i != labels_defined.size()-1)
                        writer.write(", ");
                }
                writer.write("\n");
            }

            boolean hasLabelError = false;
            int label_errors=0; //Used to count number of label errors
            if(!labels_used.isEmpty()) { //Check if
                for(int i=0; i<labels_defined.size(); i++) {
                    if(!labels_defined.contains(labels_used.get(i))) {
                        writer.write("\n**Error: Label " + labels_used.get(i) + " is not defined!");
                        hasLabelError = true;
                        label_errors++;
                    }
                }
            }

            writer.write("\n");
            if(hasEnd && num_errors==0 && !hasLabelError)
                writer.write("Compilation success.\nNo errors detected!");
            else if (error.messages.isEmpty() && num_errors==0 && !hasLabelError)
                writer.write("Compilation failed.\n\"END\" not seen. No errors detected otherwise.");
            else
                writer.write("Compilation failed with " + (num_errors+label_errors) + " errors.");

            writer.close();
            //End of program
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
