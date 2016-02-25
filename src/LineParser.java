import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Eric on 2/17/2016.
 */
public class LineParser {
    String line;

    public LineParser(String input){
        line = input;
        line = stripComments(line); //Strip comments off of the line
        if(!isEmpty(line)) line = stripLeading(line); //Strips leading spaces
    }

    private String stripComments(String line) {
        int comment_pos = line.indexOf(";"); //Searches for a comment
        if(comment_pos!=-1) line = line.substring(0, comment_pos);
        return line;
    }

    public String stripLeading(String line) {
        while(line.charAt(0) == ' ' || line.charAt(0) == '\t')
            line = line.substring(1);
        return line;
    }

    public String getCommand() {
        String cmd = line;
        if (!isEmpty(cmd) && !getLabel().isEmpty()) {
            cmd = cmd.substring(cmd.indexOf(":")+1); //If there is a label, ignore it.
        }
        if (!isEmpty(cmd)) { //If the line itself isn't empty AND it doesn't contain a label
            cmd = stripLeading(cmd);
            if(cmd.equals("END")) //Case of "END" with no following spaces
                return cmd;
            int index = cmd.indexOf(" ");
            if (index != -1 && isLegalCommand(cmd.substring(0, index))) //Testing just the "command" to see if it is legal.
                return cmd.substring(0, index); //If no spaces exist, check for a tab
            else return "";
        }
        return "";
    }

    public boolean isLegalCommand(String command) {
        for(int i=0;i<commands.length;i++) {
            if(command.equals(commands[i][0])) //Checks to see if the command given is in the list of commands
                return true;
        }
        return false;
    }

    public String getLabel() { //Returns empty string if none/invalid
        String lab = line;
        int colonPos = lab.indexOf(":");
        if(colonPos!=-1) {
            lab = lab.substring(0, colonPos);
            if (isLegalLabel(lab)) { //5 chars + colon
                return lab;
            }
            else return ""; //Invalid label, but it does exist
        }
        else return ""; //No label present
    }

    public boolean isLegalLabel(String label) {
        return (label.length()<=5 && label.matches("[a-zA-Z]+"));
        //Returns true if and only if the label contains only letters and is less than six characters
    }


    public Boolean isEmpty(String line) {
        Boolean non_space = false;
        for(int i=0; i<line.length(); i++){ //Counts backwards from the end of the line
            if(line.charAt(i) != ' ' && line.charAt(i) != '\t')
                non_space = true; //Flag set to true when any non "space" character is found
        }
        return !non_space;
    }

    String[][] commands = {
            /*
            * Commands are followed by their required "parameters"
            * r = register
            * d = destination
            * v = immediate value
            * lab = label
            * */
            {"MOVE","r","d"},
            {"MOVEI","v","d"},
            {"ADD","r","r","d"},
            {"INC","r"},
            {"SUB","r","r","d"},
            {"DEC","r"},
            {"MUL","r","r","d"},
            {"DIV","r","r","d"},
            {"BEQ","r","r","lab"},
            {"BLT","r","r","lab"},
            {"BGT","r","r","lab"},
            {"BR","lab"},
            {"END"}            //END (no args)
    };

    public void parseCommand(String cmd, String line)
    {

    }

    String[] registers = {
                "R0",
                "R1",
                "R2",
                "R3",
                "R4",
                "R5",
                "R6",
                "R7",
        };

    public boolean isLegalRegister(String reg)
    {
        return Arrays.asList(registers).contains(reg);
    } //Returns true if the "register" is in the above list
}
