import java.util.ArrayList;

/**
 * Created by Eric on 2/25/2016.
 */
public class MAL_Error {
    boolean isEmpty = true;
    ArrayList<String> messages = new ArrayList<>();

    public MAL_Error(){
        isEmpty = true;
    }
    public MAL_Error(String type, String input){
        isEmpty = false; //Error object actually has errors
        /*
        Types would be "r", "v", "d", "lab"
        Input would be the part that caused the error. This could be an illegal label,
            non-existing register, etc.
         */
        if(type.equals("cmd")) { //Error due to bad command
            //WIP
        }

        else if(type.equals("param")) {
            messages.add("Too many operands!");
        }

        else if(type.equals("r")){ //Error due to a bad register
            if(!input.substring(1).matches("[a-zA-Z]+")) //Contains letters past the initial 'R'
                messages.add("Memory destinations can only have numbers!");
            if(input.charAt(0)=='R') //Register out of range
                messages.add("Register out of range!\nMAL only supports R0-R6.");
        }

        else if(type.equals("d")){ //Error due to a bad destination
            if(input.charAt(0)=='R') //Register out of range
                messages.add("Register out of range!\nMAL only supports R0-R6.");
            if(input.length()>5)
                messages.add("Destination has too many characters!\nA maximum of 5 characters is allowed,");
            if(!input.matches("[a-zA-Z]+"))
                messages.add("Memory destinations can only have letters!");
        }

        else if(type.equals("lab")){ //Error due to bad label
            if(input.length()>5)
                messages.add("Label has too many characters!\nA maximum of 5 characters is allowed,");
            if(!input.matches("[a-zA-Z]+")) //Contains letters past the initial 'R'
                messages.add("Memory destinations can only have letters!");
        }
        else { //Error is due to a bad immediate value
            messages.add("Immediate values can only be unsigned integers!"); //The only possible error case. It either is or isn't an int.
        }
    }

}
