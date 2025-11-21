/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author ljmc2
 */
public class ConsolaGUI extends JFrame {

    private JTextArea textArea;
    private JScrollPane scrollPane;
    private Comandos comandos;
    private String comandoActual;
    private int posicionPrompt;
    private File directorioActual;

    public ConsolaGUI() {
        directorioActual = new File(System.getProperty("user.dir"));
        initComponents();
        comandos = new Comandos(textArea);
        mostrarPrompt();
    }

    private void initComponents() {

        textArea = new JTextArea();
        scrollPane = new JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Símbolo del sistema");
        setSize(900, 500);
        setLocationRelativeTo(null);

        textArea.setBackground(new Color(12, 12, 12));
        textArea.setForeground(new Color(204, 204, 204));
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setCaretColor(Color.WHITE);
        textArea.setText("Microsoft Windows [Versión 10.0.26100.7171]\n(c) Microsoft Corporation. Todos los derechos reservados.\n"
                + "Para ver la lista de comandos, ingrese help.\n\n");

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                textAreaKeyPressed(evt);
            }

            @Override
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

    //evita que se borre el texto del programa
    private void textAreaKeyPressed(KeyEvent evt) {
        int caret = textArea.getCaretPosition();

        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                evt.consume();
                procesarComando();
                break;

            case KeyEvent.VK_BACK_SPACE:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_HOME:
                if (caret <= posicionPrompt) {
                    evt.consume();
                }
                break;
            //bloquear suprimir
            case KeyEvent.VK_DELETE:
                if (caret < posicionPrompt) {
                    evt.consume();
                }
                break;

            default:
                //bloquear ctrl x
                if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_X) {
                    evt.consume();
                }
                break;
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
                    textArea.append("Uso: rm <nombre>\n"
                            + "Realizar desde padre.\n");
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
            case "help":
                comandos.mostrarAyuda();
                break;
            case "exit":
                System.exit(0);
                break;
            default:
                textArea.append("'" + comandoActual + "' no se reconoce como un comando interno o externo,"
                        + "\nprograma o archivo por lotes ejecutable.\n");
                break;
        }

        mostrarPrompt();
    }

}
