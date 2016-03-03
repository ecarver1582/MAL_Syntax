import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Eric on 2/17/2016.
 */
public class LineParser {
    String line="";
    String label = ""; //Will pass the label branched to in the instruction if one exists
    MAL_Error error = new MAL_Error();

    public LineParser(String input){ //Code that gets run when LineParser is created.
        line = input;
        line = stripComments(line); //Strip comments off of the line
        line = stripSpaces(line); //Strips leading spaces
    }

    private String stripComments(String line) {
        int comment_pos = line.indexOf(";"); //Searches for a comment
        if(comment_pos!=-1) line = line.substring(0, comment_pos);
        return line;
    }

    public String stripSpaces(String line) {
        return line.trim();
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
            * Commands are followed by their required "parameters" (args)
            * r = register
            * d = destination
            * v = immediate value
            * lab = label
            * */
            {"MOVE","d","d"},
            {"MOVEI","v","d"},
            {"ADD","d","d","d"},
            {"INC","d"},
            {"SUB","d","d","d"},
            {"DEC","d"},
            {"MUL","d","d","d"},
            {"DIV","d","d","d"},
            {"BEQ","d","d","lab"},
            {"BLT","d","d","lab"},
            {"BGT","d","d","lab"},
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
            if(commands[i][0].equals(cmd)){
                cmd_row = i;
            }
        }

        boolean hasError;
        String param = ""; //Will store each parameter one at a time.
        if(!getLabel().isEmpty()) instruction = stripSpaces(instruction.substring(instruction.indexOf(" "))); //Skip the label if one exists
        if(instruction.contains(" "))
            instruction = stripSpaces(instruction.substring(instruction.indexOf(" "))); //Skip past the "command" so that line has only parameters
        else
            return new MAL_Error("gen", param);

        /*
        * This is the meat of the program. The following for loop goes through each arg to see if it is legal
        * If not legal, it will return an error message which will then be printed out.
         */
        for(int i=1; i<commands[cmd_row].length; i++) { //For each parameter required by the command
            if(instruction.contains(",")) { //If it is not the last  param, grab the next parameter.
                param = stripSpaces(instruction.substring(0, instruction.indexOf(",")));
            }

            else { //Last parameter
                param = stripSpaces(instruction);
                if (param.isEmpty())
                    return new MAL_Error("param_few","");
            }

            if (commands[cmd_row][i].equals("r")) { //Parameter is supposed to be a register
                hasError = !isLegalRegister(param);
                if (hasError) {
                    return new MAL_Error(commands[cmd_row][i], param);
            }
                //If no error exists, param is valid. Move on!
            }

            else if(commands[cmd_row][i].equals("d")) { //Parameter is supposed to be a destination
                hasError = (!isLegalRegister(param) && !isLegalLabel(param)); //Checks if destination is a register
                if(hasError) {
                    return new MAL_Error(commands[cmd_row][i], param);
                }
            }

            else if(commands[cmd_row][i].equals("v")) { //Parameter is supposed to be an immediate value
                try {
                    int num = Integer.parseInt(param); //Attempts to convert string to int
                }
                catch (NumberFormatException e) { //Not a number
                    return new MAL_Error(commands[cmd_row][i], param);
                }
            }

            else { //Parameter is supposed to be a label
                hasError = !isLegalLabel(param);
                if(hasError) {
                    return new MAL_Error(commands[cmd_row][i], param);
                }
                label = param;
            }

            //System.out.println("Param"+i+":"+param);
            if(instruction.contains(",")) //If there is a new parameter, grab it.
                instruction = stripSpaces(instruction.substring(instruction.indexOf(",")+1)); //Move to the next parameter
            else if(!instruction.contains(",") && i==commands[cmd_row].length-2)
                return new MAL_Error("param_few","");
        }//End for-loop for each parameter
        if(instruction.contains(",")) {
            return new MAL_Error("param_few","");
        }
        if(instruction.contains(",")) //Extra parameter detected
            return new MAL_Error("param_extra","");

        return error;
    } //End parseInstruction()

    String[] registers = {"R0","R1","R2","R3","R4","R5","R6","R7"}; //All legal registers

    public boolean isLegalRegister(String reg)
    {
        return Arrays.asList(registers).contains(reg);
    } //Returns true if the "register" is in the above list
}
