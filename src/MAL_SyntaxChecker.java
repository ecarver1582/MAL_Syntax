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

            while ((line = reader.readLine()) != null) {
                LineParser code = new LineParser(line);
                System.out.println(code.line);
                if(!code.getCommand().isEmpty()) System.out.println("******" + code.getCommand());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
