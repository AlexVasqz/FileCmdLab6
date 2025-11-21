package lab5progra2;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsolaGUI extends JFrame {
    
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private Comandos comandos;
    private String comandoActual;
    private int posicionPrompt;
    private File directorioActual;
    
    public ConsolaGUI() {
        directorioActual = new File(System.getProperty("user.home"));
        initComponents();
        comandos = new Comandos(textArea);
        mostrarPrompt();
    }
    
    private void initComponents() {
        
        textArea = new JTextArea();
        scrollPane = new JScrollPane();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Administrator: Command Prompt");
        setSize(900, 500);
        setLocationRelativeTo(null);
        
        textArea.setBackground(new Color(12, 12, 12));
        textArea.setForeground(new Color(204, 204, 204));
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setCaretColor(Color.WHITE);
        textArea.setText("Microsoft Windows [Version 10.0.22621.521]\n(c) Microsoft Corporation. All rights reserved.\n\n");
        
        textArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                textAreaKeyPressed(evt);
            }
            
            public void keyTyped(KeyEvent evt) {
                textAreaKeyTyped(evt);
            }
        });
        
        scrollPane.setViewportView(textArea);
        
        getContentPane().add(scrollPane);
    }
    
    private void mostrarPrompt() {
        String prompt = directorioActual.getAbsolutePath() + ">";
        textArea.append(prompt);
        posicionPrompt = textArea.getText().length();
        comandoActual = "";
        textArea.setCaretPosition(textArea.getText().length());
    }
    
    private void textAreaKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            evt.consume();
            procesarComando();
        } else if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (textArea.getCaretPosition() <= posicionPrompt) {
                evt.consume();
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT || 
                   evt.getKeyCode() == KeyEvent.VK_HOME) {
            if (textArea.getCaretPosition() <= posicionPrompt) {
                evt.consume();
            }
        }
    }
    
    private void textAreaKeyTyped(KeyEvent evt) {
        if (textArea.getCaretPosition() < posicionPrompt) {
            evt.consume();
            textArea.setCaretPosition(textArea.getText().length());
        }
    }
    
    private void procesarComando() {
        comandoActual = textArea.getText().substring(posicionPrompt).trim();
        textArea.append("\n");
        
        if (comandoActual.isEmpty()) {
            mostrarPrompt();
            return;
        }
        
        String[] partes = comandoActual.split(" ", 2);
        String comando = partes[0].toLowerCase();
        String argumento = partes.length > 1 ? partes[1] : "";
        
        switch (comando) {
            case "mkdir":
                if (!argumento.isEmpty()) {
                    comandos.crearCarpeta(directorioActual, argumento);
                } else {
                    textArea.append("Uso: mkdir <nombre>\n");
                }
                break;
            case "mfile":
                if (!argumento.isEmpty()) {
                    comandos.crearArchivo(directorioActual, argumento);
                } else {
                    textArea.append("Uso: mfile <nombre.ext>\n");
                }
                break;
            case "rm":
                if (!argumento.isEmpty()) {
                    comandos.eliminar(directorioActual, argumento);
                } else {
                    textArea.append("Uso: rm <nombre>\n");
                }
                break;
            case "cd":
                if (argumento.equals("...")) {
                    File nuevoDir = comandos.irAtras(directorioActual);
                    if (nuevoDir != null) {
                        directorioActual = nuevoDir;
                    }
                } else if (!argumento.isEmpty()) {
                    File nuevoDir = comandos.cambiarDirectorio(directorioActual, argumento);
                    if (nuevoDir != null) {
                        directorioActual = nuevoDir;
                    }
                } else {
                    textArea.append("Uso: cd <nombre_carpeta> o cd ...\n");
                }
                break;
            case "...":
                File nuevoDir = comandos.irAtras(directorioActual);
                if (nuevoDir != null) {
                    directorioActual = nuevoDir;
                }
                break;
            case "dir":
                comandos.listar(directorioActual);
                break;
            case "date":
                comandos.mostrarFecha();
                break;
            case "time":
                comandos.mostrarHora();
                break;
            case "wr":
                if (!argumento.isEmpty()) {
                    String[] partesWr = argumento.split(" ", 2);
                    if (partesWr.length == 2) {
                        comandos.escribirArchivo(directorioActual, partesWr[0], partesWr[1]);
                    } else {
                        String nombreArchivo = partesWr[0];
                        String texto = JOptionPane.showInputDialog(this, "Ingrese el texto para " + nombreArchivo + ":");
                        if (texto != null) {
                            comandos.escribirArchivo(directorioActual, nombreArchivo, texto);
                        }
                    }
                } else {
                    textArea.append("Uso: wr <archivo.ext> <texto>\n");
                }
                break;
            case "rd":
                if (!argumento.isEmpty()) {
                    comandos.leerArchivo(directorioActual, argumento);
                } else {
                    textArea.append("Uso: rd <archivo.ext>\n");
                }
                break;
            case "cls":
                textArea.setText("");
                break;
            case "help":
                comandos.mostrarAyuda();
                break;
            case "exit":
                System.exit(0);
                break;
            default:
                textArea.append("'" + comandoActual + "' is not recognized as an internal or external command,\noperable program or batch file.\n");
                break;
        }
        
        mostrarPrompt();
    }
    
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConsolaGUI().setVisible(true);
            }
        });
    }
}

