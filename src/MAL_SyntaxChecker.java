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

        ArrayList labels = new ArrayList(); //Stores all seen labels


        try {
            Scanner input = new Scanner(System.in);  // Reading from System.in
            System.out.print("Enter filename (no extension): ");
            String fileName = input.next();
            File file = new File(fileName);
            FileInputStream stream;
            stream = new FileInputStream(file);
            PrintWriter writer = new PrintWriter(file+"-log", "UTF-8");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                count++;
                LineParser code = new LineParser(line);
                if(!code.line.isEmpty()) System.out.println(code.line);
                //if(!code.getLabel().isEmpty()) System.out.println(code.getLabel());
                //if(!code.getCommand().isEmpty()) System.out.println(code.getCommand());
                if(code.getLabel().isEmpty() && code.getCommand().isEmpty()){
                    System.out.println("ERROR ON LINE " + count);
                    break;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
