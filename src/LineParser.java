import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Eric on 2/17/2016.
 */
public class LineParser {
    String line;
    String label = ""; //Will pass the label branched to in the instruction if one exists

    public LineParser(String input){ //Code that gets run when LineParser is created.
        line = input;
        line = stripComments(line); //Strip comments off of the line
        if(!isEmpty(line)) line = stripSpaces(line); //Strips leading spaces
    }

    private String stripComments(String line) {
        int comment_pos = line.indexOf(";"); //Searches for a comment
        if(comment_pos!=-1) line = line.substring(0, comment_pos);
        return line;
    }

    public String stripSpaces(String line) {
        if(line.isEmpty()) //No need to strip trailing spaces if it is already empty.
            return line;
        while(line.charAt(0) == ' ' || line.charAt(0) == '\t') //Strip leading spaces
            line = line.substring(1);
        if(line.isEmpty()) //No need to strip trailing spaces if it is already empty.
            return line;
        for(int i=line.length(); i>0; i++) {
            if(line.charAt(0) == ' ' || line.charAt(0) == '\t') //Strip trailing spaces
                line = line.substring(0, i);
            else break;
        }
        return line;
    }

    public String getCommand() {
        String cmd = line;
        if (!isEmpty(cmd) && !getLabel().isEmpty()) {
            cmd = cmd.substring(cmd.indexOf(":")+1); //If there is a label, ignore it.
        }
        if (!isEmpty(cmd)) { //If the line itself isn't empty AND it doesn't contain a label
            cmd = stripSpaces(cmd);
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

    public MAL_Error parseInstruction(String cmd) //Determines if all "parameters" are met/valid
    {
        /*Get the command passed from the getCommand() method
        Then, skip past the command so that only the parameters are left.
         */
        String instruction = line; //Create an editable version of the instruction
        int cmd_row = 0; //Guaranteed to have a legitimate command.
        for(int i=0; i<commands.length; i++) {
            if(commands[i][0].equals(cmd))
                cmd_row = i;
        }

        boolean hasError = false;
        String param = ""; //Will store each parameter one at a time.
        MAL_Error error;
        instruction = stripSpaces(instruction.substring(instruction.indexOf(" "))); //Skip past the "command" so that line has only parameters
        for(int i=1; i<commands[cmd_row].length; i++) { //For each parameter required by the command
            if(instruction.contains(",")) { //If it is not the last  param, grab the next parameter.
                param = stripSpaces(instruction.substring(0, instruction.indexOf(",")));
            }

            if (commands[cmd_row][i].equals("r")) { //Parameter is supposed to be a register
                hasError = isLegalRegister(param);
                if (hasError) {
                    System.out.println("Failed at register");
                    return new MAL_Error(commands[cmd_row][i], param);
            }
                //If no error exists, param is valid. Move on!
            }

            else if(commands[cmd_row][i].equals("d")) { //Parameter is supposed to be a destination
                hasError = isLegalRegister(param); //Checks if destination is a register
                if(!hasError) hasError = isLegalLabel(param); //If not, check if destination is <=5 letters (only letters)
                if(hasError) {
                    System.out.println("Failed at destination");
                    return new MAL_Error(commands[cmd_row][i], param);
                }
            }

            else if(commands[cmd_row][i].equals("v")) { //Parameter is supposed to be an immediate value
                try {
                    int num = Integer.parseInt(param);
                }
                catch (NumberFormatException e) { //Not a number
                    System.out.println("Failed at immediate value");
                    return new MAL_Error(commands[cmd_row][i], param);
                }
            }

            else { //Parameter is supposed to be a label
                hasError = isLegalLabel(param);
                if(hasError) {
                    System.out.println("Failed at label");
                    return new MAL_Error(commands[cmd_row][i], param);
                }
                label = param;
            }

            if(instruction.contains(",")) //If there is a new parameter, grab it.
                instruction = stripSpaces(instruction.substring(instruction.indexOf(","))); //Move to the next parameter
        }//End for-loop for each parameter
        return null;
    } //End parseInstruction()

    String[] registers = {"R0","R1","R2","R3","R4","R5","R6","R7"}; //All legal registers

    public boolean isLegalRegister(String reg)
    {
        return Arrays.asList(registers).contains(reg);
    } //Returns true if the "register" is in the above list
}
