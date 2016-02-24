import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Eric on 2/17/2016.
 */
public class LineParser {
    String line;
    String tempLine;
    ArrayList<String> labels = new ArrayList<>();

    public LineParser(String input){
        line = input;
        line = stripComments(line); //Strip comments off of the line
        if(!isEmpty(line)) line = stripLeading(line); //Strips leading spaces
    }

    public String getCommand() {
        tempLine = line;
        if (!isEmpty(tempLine) && getLabel().isEmpty()) { //If the line itself isn't empty AND it doesn't contain a label
            int index = tempLine.indexOf(" ");
            if (index == -1)
                index = tempLine.indexOf("\t"); //Gets chars before first space/tab (already stripped of leading spaces)
            if (index != -1 && isLegalCommand(tempLine.substring(0, index))) //Testing just the "command" to see if it is legal.
                return tempLine.substring(0, index); //If no spaces exist, check for a tab
            else return "";
        } else return "";
    }

    public boolean isLegalCommand(String command) {
        return Arrays.asList(commands).contains(command);
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

    public String getLabel() { //Returns empty string if none exists
        tempLine = line;
        int colonPos = tempLine.indexOf(":");
        if(colonPos!=-1) {
            tempLine = tempLine.substring(0, colonPos);
            if (isLegalLabel(tempLine)) { //5 chars + colon
                labels.add(tempLine);
                return tempLine;
            }
            else return "";
        }
        else return "";
    }

    public boolean isLegalLabel(String label) {
        return (label.length()<5 && label.matches("[a-zA-Z]+"));
    }


    public Boolean isEmpty(String line) {
        Boolean non_space = false;
        for(int i=0; i<line.length(); i++){ //Counts backwards from the end of the line

            if(line.charAt(i) != ' ' && line.charAt(i) != '\t')
                non_space = true; //Flag set to true when any non "space" character is found
        }
        return !non_space;
    }

    String[] commands = {"MOVE","MOVEI","ADD","INC"
            ,"SUB","DEC","MUL","DIV","BEQ"
            ,"BLT","BGT","BR","END"};
    /*
    r = register
    d = destination (memory)
    v = immediate value
    lab= label
    */
    String[] arguments = {
            "r,d",      //MOVE
            "v,d",      //MOVEI
            "r,r,d",    //ADD
            "r",        //INC
            "r,r,d",    //SUB
            "r",        //DEC
            "r,r,d",    //MUL
            "r,r,d",    //DIV
            "r,r,lab",  //BEW
            "r,r,lab",  //BLT
            "r,r,lab",  //BGT
            "lab",      //BR
            ""};        //END (no args)
        /*
        Commands are in same order as the arguments.
        commands[5] will have arguments: arguments[5];
         */

}
