import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int port = 8989;

    public static void main(String[] args) {

        while (true) {
            String json;
            try (Socket socket = new Socket(HOST, port);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("введите word или несколько через пробел:");
                String word = scanner.nextLine();
                out.println(word.toLowerCase());

                json = in.readLine();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(json);
                String prettyJsonString = gson.toJson(je);
                System.out.println(prettyJsonString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
