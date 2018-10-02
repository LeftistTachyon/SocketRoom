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
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
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
     */
    private PrintWriter out;
    
    /**
     * A {@code Set} of all clients
     */
    private static final Set<Client> ALL_CLIENTS = new HashSet<>();
    
    /**
     * Standard constructor.
     * @throws IOException if something goes wrong
     */
    public ServerCommunication() throws IOException {
        run();
    }
    
    /**
     * Connects to the server then enters the processing loop
     * @throws IOException if something goes wrong
     */
    private void run() throws IOException {
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
        String _name = null;
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
            
            if(line.startsWith("PING")) {
                out.println("PING");
            } else if(line.startsWith("SUBMITNAME")) {
                // submit your name, duh
                _name = getName(temp++ == 0);
                out.println(_name);
                System.out.println(_name);
            } else if(line.startsWith("NAMEACCEPTED")) {
                // the server has accepted your name
                temp = 0;
                // init stuff
            } else if(line.startsWith("NEWCLIENT")) {
                // add a client to the pool
                Scanner reader = new Scanner(line.substring(10));
                boolean added = ALL_CLIENTS.add(new Client(reader.next()));
                if(!added) System.err.println("WTF a client connected "
                        + "with a duplicate name");
            } else if(line.startsWith("CHALLENGE")) {
                // I'm being challenged!
                String challenger = line.substring(9);
                int choice = JOptionPane.showConfirmDialog(null, 
                        challenger + " has challenged you!\nDo you accpet?", 
                        "Challenge", JOptionPane.YES_NO_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE);
                // whether I accept the challenge
                if(choice == JOptionPane.YES_OPTION) {
                    out.println("true");
                } else {
                    out.println("false");
                }
            } else if(line.startsWith("CONNECT")) {
                // Time to connect for a match!
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
     * A class that represents a client.
     */
    public static class Client implements Comparable<Client> {
        /**
         * This client's name
         */
        private final String name;

        /**
         * Creates a new client with a given name
         * @param name the name of this client
         */
        public Client(String name) {
            this.name = name;
        }

        @Override
        public int compareTo(Client c) {
            return name.compareToIgnoreCase(c.name);
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Client) {
                Client cc = (Client) obj;
                return name.equalsIgnoreCase(cc.name);
            } else return super.equals(obj);
        }

        @Override
        public int hashCode() { 
            // NOTE: when adding another field, be sure to refresh/remake the 
            // hashcode function
            int hash = 5;
            hash = 31 * hash + Objects.hashCode(this.name);
            return hash;
        }
    }
}