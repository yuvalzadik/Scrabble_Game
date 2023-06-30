package scrabble_game;
import java.io.*;

public class BookScrabbleHandler implements ClientHandler {
    DictionaryManager dm=DictionaryManager.get();

    /**
     * The handleClient function is responsible for handling the client's request.
     * It reads the input from the client and sends back a response to it.
     *
     * @param  inFromclient Read the input from the client
     * @param outToClient Send data back to the client
     */
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inFromclient));
            boolean isExist = false ;
            String line = in.readLine();
            if(line.equals("connectionCheck")) return;
            String first_string = line.split(",")[0];
            int commaIndex = line.indexOf(",");
            String [] newLine = line.substring(commaIndex + 1).split(",");
            if (line.startsWith("Q")) {
               isExist =  dm.query(newLine);
            } else if (first_string.equals("C")) {
              isExist =   dm.challenge(newLine);
            }
            PrintWriter out = new PrintWriter(outToClient, true);
            if (isExist){
                out.println("true\n");
            }
            else {
                out.println("false\n");
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The close function is used to close the connection between the client and server.
     * This function is called when a user logs out of their account or closes the program.

     */
    @Override
    public void close() {

    }
}
