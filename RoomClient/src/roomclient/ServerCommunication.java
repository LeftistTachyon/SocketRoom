package roomclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * A client to server communication
 * @author Jed Wang
 */
public class ServerCommunication {
    /**
     * The socket connection into this
     */
    private BufferedReader in;
    
    /**
     * The socket connection out of this
     * Should be private
     */
    protected PrintWriter out;
    
    /**
     * Whether this client is in a game
     */
    private boolean inGame;
    
    /**
     * A {@code Set} of all clients
     */
    private static final Set<String> ALL_CLIENTS = new HashSet<>();
    
    /**
     * Standard constructor.
     */
    public ServerCommunication() {
        inGame = false;
        new Thread() {
            @Override
            public void run() {
                try {
                    run_();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }
    
    /**
     * Connects to the server then enters the processing loop
     * @throws IOException if something goes wrong
     */
    private void run_() throws IOException {
        // Make connection and initialize streams
        String serverAddress;
        Socket socket = null;
        do {
            serverAddress = getServerAddress();
            try {
                socket = new Socket(serverAddress, 9001);
            } catch (ConnectException | NoRouteToHostException | UnknownHostException ex) {
                //JOptionPane.showMessageDialog(cf, ex.getMessage(),
                //        "Connection Error", JOptionPane.ERROR_MESSAGE);
                Object[] options = {"Reenter IP Adress", "Exit"};
                int returned = JOptionPane.showOptionDialog(null, ex.getMessage(), 
                        "Connection Error", JOptionPane.OK_CANCEL_OPTION, 
                        JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                if(returned != JOptionPane.OK_OPTION) {
                    // cf.dispose();
                    System.exit(0);
                    return;
                }
            }
        } while(socket == null);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        // Process all messages from server, according to the protocol.
        
        int temp = 0;
        String _name;
        while(true) {
            // Reading input from the server
            String line = null;
            try {
                line = in.readLine();
            } catch (SocketException se) {
                JOptionPane.showMessageDialog(null, 
                        "You have been disconnected from the server.", 
                        "Disconnected", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
            if(line == null) {
                // Welp, looks like the server left
                return;
            }
            
            if(line.equals("PING")) {
                out.println("PING");
            } else if(line.startsWith("NEWCLIENT")) {
                System.out.println("new client: " + line.substring(9));
                // add a client to the pool
                boolean added = ALL_CLIENTS.add(line.substring(9));
                if(!added) System.err.println("WTF a client connected "
                        + "with a duplicate name");
            } else {
                if(inGame) {
                    if(line.equals("EXIT")) {
                        System.err.println("The other person has left "
                                + "the match.");
                        inGame = false;
                    } else if(line.startsWith("NM")) {
                        String message = line.substring(2);
                        while(message.length() > 90) {
                            System.out.println("          " + message.substring(0, 90));
                            message = message.substring(90);
                        }
                        String spaces = "          ";
                        for(int i = 0; i < 90 - message.length(); i++) {
                            spaces += " ";
                        }
                        System.out.println(spaces + message);
                    }
                } else {
                    if(line.startsWith("SUBMITNAME")) {
                        // submit your name, duh
                        _name = getName(temp++ == 0);
                        out.println(_name);
                        System.out.println(_name);
                    } else if(line.startsWith("NAMEACCEPTED")) {
                        // the server has accepted your name
                        temp = 0;
                        // init stuff
                    } else if(line.startsWith("CHALLENGE_C")) {
                        // I'm being challenged!
                        String challenger = line.substring(11);
                        int choice = JOptionPane.showConfirmDialog(null,
                                challenger + " has challenged you!\nDo you accept?",
                                "Challenge", JOptionPane.YES_NO_OPTION, 
                                JOptionPane.INFORMATION_MESSAGE);
                        // whether I accept the challenge
                        boolean accepted = choice == JOptionPane.YES_OPTION;
                        out.println("CHALLENGE_R" + challenger + " " + accepted);
                    } else if(line.startsWith("CHALLENGE_R")) {
                        inGame = Boolean.parseBoolean(line.substring(11));
                    }
                }
            }
        }
    }
    
    /**
     * Prompt for and return the address of the server.
     */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            null,
            "Enter IP Address of the Server:",
            "Welcome to Socket Room",
            JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Prompt for and return the desired screen name.
     * @param again whether this method needs to state not to enter the same name again
     */
    private String getName(boolean again) {
        String s = null;
        do {
            s = JOptionPane.showInputDialog(
                null,
                    again?"Choose a screen name (no spaces):":"Choose a different screen name (no spaces):",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
            if(s == null) System.exit(0);
        } while(s.contains(" ") || "".equals(s));
        return s;
    }
    
    /**
     * Prints all connected clients.
     */
    public void printAllClients() {
        for(String name : ALL_CLIENTS) {
            System.out.println(name);
        }
    }
    
    /**
     * Exits the current game.
     */
    public void exitGame() {
        out.println("EXIT");
        inGame = false;
    }
}