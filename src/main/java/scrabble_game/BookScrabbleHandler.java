package scrabble_game;
import java.io.*;

public class BookScrabbleHandler implements ClientHandler {
    BufferedReader in;
    PrintWriter out;
    DictionaryManager dm=DictionaryManager.get();

    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        try {
            in = new BufferedReader(new InputStreamReader(inFromclient));
            boolean isexsit = false ;
            String line = in.readLine();
            String first_string = line.split(",")[0];
            int commaIndex = line.indexOf(",");
            String [] newLine = line.substring(commaIndex + 1).split(",");
            if (line.startsWith("Q")) {
               isexsit =  dm.query(newLine);
            } else if (first_string.equals("C")) {
              isexsit =   dm.challenge(newLine);
            }
            out = new PrintWriter(outToClient, true);
            if (isexsit){
                out.println("true\n");
            }
            else {
                out.println("false\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            in.close();
            out.close();
        }
        catch (IOException e){
           e.printStackTrace();
        }
    }
}
