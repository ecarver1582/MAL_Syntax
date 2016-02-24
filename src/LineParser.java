import java.util.ArrayList;

/**
 * Created by Eric on 2/17/2016.
 */
public class LineParser {
    String line;
    String line_original;
    ArrayList<String> labels;

    public LineParser(String input){
        line = input;
        line_original = input;
        line = stripComments(line);
        if(!isEmpty(line)) line = stripLeading(line); //Strips leading spaces
    }

    public String getCommand() {
        if (!isEmpty(line) && getLabel(line).isEmpty()) {
            int index = line.indexOf(" ");
            if (index==-1) index = line.indexOf("\t"); //Gets chars before 2nd space/tab
            return line.substring(0, index);
        }
        else return "";
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

    public String getLabel(String line) { //Returns empty string if none exists
        int colonPos = line.indexOf(":");
        if(colonPos!=-1) {
            line = line.substring(0, colonPos);
            if (line.length() > 1 && line.length() <= 6) { //5 chars + colon
                labels.add(line);
                return line;
            }
            else return "";
        }
        else return "";
    }

    public Boolean isEmpty(String line) {
        Boolean non_space = false;
        for(int i=0; i<line.length(); i++){ //Counts backwards from the end of the line

            if(line.charAt(i) != ' ' && line.charAt(i) != '\t')
                non_space = true; //Flag set to true when any non "space" character is found
        }
        return !non_space;
    }
}
