/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab5progra2;

/**
 *
 * @author ljmc2
 */
public class Lab5Progra2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ConsolaGUI().setVisible(true);
            }
        });
    }

}
