package roomserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A class that handles communication with clients
 * @author Jed Wang
 */
public class ClientCommunication {
    /**
     * The port to communicate over
     */
    public static final int PORT = 9001;
    
    /**
     * The set of all names of clients in the chat room.  Maintained
     * so that we can check that new clients are not registering name
     * already in use.
     */
    private static HashSet<String> names = new HashSet<>();
    
    /**
     * A map of all names of clients paired to their respective Handlers.
     */
    private static HashMap<String, Handler> handlers = new HashMap<>();
    
    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for a dealing with a single client
     * and broadcasting its messages.
     */
    public static class Handler extends Thread implements Comparable<Handler> {
        /**
         * This client's name
         */
        private String name;
        
        /**
         * This client's socket
         */
        public final Socket socket;
        
        /**
         * Messaging to here
         */
        private BufferedReader in;
        
        /**
         * Message from here
         */
        private PrintWriter out;
        
        /**
         * Constructs a handler thread, squirreling away the socket.
         * All the interesting work is done in the run method.
         * @param socket the socket that receives info from the client
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Services this thread's client by repeatedly requesting a
         * screen name until a unique one has been submitted, then
         * acknowledges the name and registers the output stream for
         * the client in a global set, then repeatedly gets inputs and
         * broadcasts them.
         */
        @Override
        public void run() {
            try {
                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Request a name from this client.  Keep requesting until
                // a name is submitted that is not already used.  Note that
                // checking for the existence of a name and adding the name
                // must be done while locking the set of names.
                while(true) {
                    out.println("SUBMITNAME");
                    // notify("SUBMITNAME", false);
                    name = in.readLine();
                    // notify(name, true);
                    if(name == null) return;
                    if("".equals(name) || "null".equals(name)) continue;
                    synchronized(names) {
                        if(!names.contains(name)) {
                            names.add(name);
                            handlers.put(name, this);
                            break;
                        }
                    }
                }
                
                
                
                // Now that a successful name has been chosen, add the
                // socket's print writer to the set of all writers so
                // this client can receive broadcast messages.
                out.println("NAMEACCEPTED");
                // notify("NAMEACCEPTED", false);

                // Accept messages from this client and broadcast them.
                // Ignore other clients that cannot be broadcasted to.
                while(true) {
                    String line = in.readLine();
                    // notify(line, true);
                    if(line == null) {
                        return;
                    }
                    // handle input
                    if(line.startsWith("PING")) {
                        out.println("PING");
                    } else if(line.startsWith("CHALLENGE")) {
                        // Challenging for a match
                        String toChallenge = line.substring(9);
                        if(handlers.containsKey(toChallenge)) {
                            handlers.get(toChallenge).
                                    out.println("CHALLENGE" + name);
                        }
                    }
                }
            } catch(IOException e) {
                println(e.toString());
            } finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                if(name != null) {
                    names.remove(name);
                }
                if(handlers != null) {
                    handlers.remove(name);
                }
                out.close();
                try {
                    in.close();
                    socket.close();
                } catch(IOException e) {}
            }
        }
 
        @Override
        public String toString() {
            return name;
        }
        
        /**
         * Prints something with a carriage return afterwards
         * @param s a string to println
         */
        public void println(String s) {
            System.out.println(name + ": " + s);
        }

        @Override
        public int compareTo(Handler h) {
            return name.compareTo(h.name);
        }
        
        /**
         * Returns the client's name
         * @return the client's name
         */
        public String getClientName() {
            return name;
        }
    }
}