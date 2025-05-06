public class JWordleDriver {    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new WordleGUI());
        WordleGUI gui = new WordleGUI(); //debug print setup
        System.out.println("Target word (for testing): " + gui.getGame().getTargetWord()); // debug print
    };
}