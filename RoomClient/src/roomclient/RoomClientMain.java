package roomclient;

import java.io.IOException;
import java.util.Scanner;

/**
 * The main class for this client
 * @author Jed Wang
 */
public class RoomClientMain {
    /**
     * The main method
     * @param args the command line arguments
     * @throws java.io.IOException if something goes wrong
     */
    public static void main(String[] args) throws IOException {
        ServerCommunication sc = new ServerCommunication();
        Scanner input = new Scanner(System.in);
        while(true) {
            String line = input.nextLine();
            switch(line) {
                case "EXIT":
                    sc.exitGame();
                    break;
                case "END":
                    System.exit(0);
                    break;
                case "PRINTALL":
                    sc.printAllClients();
                    break;
                default:
                    sc.out.println(line);
                    break;
            }
        }
    }
}