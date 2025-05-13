public class JWordleDriver {    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Create and set up the window.
            WordleGUI gui = new WordleGUI(); //debug print setup
        //System.out.println("Target word (for testing): " + gui.getGame().getTargetWord()); //debug print
        });
    };
} 