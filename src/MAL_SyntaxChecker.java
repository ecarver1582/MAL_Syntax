import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Eric on 2/10/2016.
 */
public class MAL_SyntaxChecker {

    public static void main(String[] args) {
        /*String[] commands = {"MOVE,r,d","MOVEI,v,d","ADD,r,r,d","INC,r"
                ,"SUB,r,r,d","DEC,r","MUL,r,r,d","DIV,r,r,d","BEQ,r,r,lab"
                ,"BLT,r,r,lab","BGT,r,r,lab","BR,lab","END"};
                */

        ArrayList<String> labels_defined = new ArrayList<>(); //Stores all created labels
        ArrayList<String> labels_used = new ArrayList<>(); //Stores all referenced labels
        /*
        All used labels must relate to a defined label. Otherwise critical error.
        Each defined label should be used. Otherwise compiler warning (not error).
         */

        try {
            Scanner input = new Scanner(System.in);  // Reading from System.in
            System.out.print("Enter filename (no extension): ");
            String fileName = input.next();
            File file = new File(fileName+".txt");
            FileInputStream stream;
            stream = new FileInputStream(file);
            PrintWriter writer = new PrintWriter(file+"-log", "UTF-8");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String line;
            int count = 0;
            boolean hasEnd = false; //Makes sure there is at least one "END" in the program

            while ((line = reader.readLine()) != null) {
                count++;
                LineParser code = new LineParser(line);
                if(!code.line.isEmpty()) System.out.println(code.line); //Print the instruction if there is any text

                if(!code.getLabel().isEmpty()) {
                    //System.out.println("-----\t$$$"+code.getLabel());
                    labels_defined.add(code.getLabel()); //Adds the label to an array list.
                }
                if(!code.getCommand().isEmpty()) { //Triggers the command parsing
                    //System.out.println("-----\t***"+code.getCommand());
                    code.parseInstruction(line); //Triggers the command parsing
                    if(code.getCommand().equals("END"))
                        hasEnd = true;
                    if(!code.label.isEmpty())
                        labels_used.add(code.label);
                }
                if(!code.line.isEmpty() && code.getLabel().isEmpty() && code.getCommand().isEmpty()){ //If line is not empty and has no labels or commands, error.
                    System.out.println("ERROR ON LINE " + count);
                    break;
                }
            }

            System.out.println("\n------------------------------");
            System.out.println("------------------------------");
            if(hasEnd)
                System.out.println("Compilation success.\nNo errors detected!");
            else
                System.out.println("Compilation failed.\n\"END\" not seen. No errors detected otherwise.");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
