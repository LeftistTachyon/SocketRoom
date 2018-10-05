package roomclient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * A window that displays the GUI
 * @author Oracle, Jed Wang
 */
public class LobbyWindow extends JFrame {
    /**
     * The ServerCommunication to alert when an action occurs
     */
    private final ServerCommunication toAlert;
    
    /** 
     * Creates new form LobbyWindow
     * @param toAlert the ServerCommunication to report events to
     */
    public LobbyWindow(ServerCommunication toAlert) {
        this.toAlert = toAlert;
        
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        infoPanel = new JPanel();
        chatSP = new JScrollPane();
        chatTP = new JTextPane();
        playerListSP = new JScrollPane();
        playerListTP = new JTextPane();
        playerLabel = new JLabel();
        chatLabel = new JLabel();
        chatTextField = new JTextField();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("LobbyWindow");

        infoPanel.setBackground(new Color(255, 255, 255));
        infoPanel.setBorder(BorderFactory.createLineBorder(
                new Color(130, 135, 144)));
        infoPanel.setPreferredSize(new Dimension(0, 150));
        
        chatTP.setEditable(false);
        chatSP.setViewportView(chatTP);

        playerListTP.setEditable(false);
        playerListSP.setViewportView(playerListTP);

        playerLabel.setFont(new Font("Segoe UI Semilight", 0, 20)); // NOI18N
        playerLabel.setText("Players");

        chatLabel.setFont(new Font("Segoe UI Semilight", 0, 20)); // NOI18N
        chatLabel.setText("Lobby Chat");

        chatTextField.setFont(new Font("Segoe UI", 0, 11)); // NOI18N
        chatTextField.addActionListener(this::sendMessage);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(infoPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addComponent(playerLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                            .addComponent(playerListSP, GroupLayout.Alignment.LEADING))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(chatSP)
                            .addComponent(chatLabel, GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
                            .addComponent(chatTextField))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(playerLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chatLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(playerListSP)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chatSP, GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chatTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(infoPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>
    
    /**
     * Invoked when the client hits the ENTER key after typing in the box.
     * @param evt a description of the event
     */
    private void sendMessage(ActionEvent evt) {                                              
        String message = chatTextField.getText();
        chatTextField.setText("");
        
    }
    
    /**
     * Creates and shows a LobbyWindow
     * @param toAlert the ServerCommunication to report events to
     * @return the LobbyWindow which is on screen
     */
    public static LobbyWindow run(ServerCommunication toAlert) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.LookAndFeelInfo[] installedLookAndFeels = 
                    UIManager.getInstalledLookAndFeels();
            for (UIManager.LookAndFeelInfo installedLookAndFeel : installedLookAndFeels) {
                if ("Nimbus".equals(installedLookAndFeel.getName())) {
                    UIManager.setLookAndFeel(installedLookAndFeel.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        //</editor-fold>

        /* Create and display the form */
        LobbyWindow lw = new LobbyWindow(toAlert);
        EventQueue.invokeLater(() -> {
            lw.setResizable(true);
            lw.setVisible(true);
        });
        return lw;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Variables declaration - do not modify">
    private JLabel chatLabel;
    private JScrollPane chatSP;
    private JTextPane chatTP;
    private JTextField chatTextField;
    private JPanel infoPanel;
    private JLabel playerLabel;
    private JScrollPane playerListSP;
    private JTextPane playerListTP;
    //</editor-fold>
}
