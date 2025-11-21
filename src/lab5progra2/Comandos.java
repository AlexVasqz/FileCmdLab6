/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab5progra2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextArea;

/**
 *
 * @author ljmc2
 */
public class Comandos {

    private JTextArea consola;

    public Comandos(JTextArea consola) {
        this.consola = consola;
    }

    public void crearCarpeta(File dirActual, String nombre) {
        File dir = new File(dirActual, nombre);
        if (dir.mkdir()) {
            consola.append("Carpeta creada: " + dir.getName() + "\n");
        } else {
            consola.append("No se pudo crear la carpeta.\n");
        }
    }

    public void crearArchivo(File dirActual, String nombre) {
        File archivo = new File(dirActual, nombre);
        try {
            if (archivo.createNewFile()) {
                consola.append("Archivo creado: " + archivo.getName() + "\n");
            } else {
                consola.append("El archivo ya existe.\n");
            }
        } catch (IOException e) {
            consola.append("Error al crear el archivo: " + e.getMessage() + "\n");
        }
    }

    public void eliminar(File dirActual, String nombre) {
        File item = new File(dirActual, nombre);
        if (item.exists()) {
            if (item.isDirectory()) {
                eliminarCarpeta(item);
            }
            if (item.delete()) {
                consola.append("Eliminado: " + item.getName() + "\n");
            } else {
                consola.append("No se pudo eliminar.\n");
            }
        } else {
            consola.append("El archivo o carpeta no existe.\n");
        }
    }

    private void eliminarCarpeta(File dir) {
        File[] archivos = dir.listFiles();
        if (archivos != null) {
            for (File f : archivos) {
                if (f.isDirectory()) {
                    eliminarCarpeta(f);
                }
                f.delete();
            }
        }
    }

    public void listar(File dirActual) {
        File[] archivos = dirActual.listFiles();
        consola.append("Contenido de: " + dirActual.getAbsolutePath() + "\n");
        if (archivos != null) {
            for (File f : archivos) {
                String tipo = f.isDirectory() ? "<DIR>" : "<FILE>";
                consola.append(tipo + "\t" + f.getName() + "\n");
            }
        }
    }

    public File cambiarDirectorio(File dirActual, String nombre) {
        File dir = new File(nombre).isAbsolute() ? new File(nombre) : new File(dirActual, nombre);
        if (dir.exists() && dir.isDirectory()) {
            return dir;
        } else {
            consola.append("No se encontró la carpeta.\n");
            return null;
        }
    }

    public File irAtras(File dirActual) {
        File padre = dirActual.getParentFile();
        return padre != null ? padre : dirActual;
    }

    public void mostrarFecha() {
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        consola.append("Fecha actual: " + fecha + "\n");
    }

    public void mostrarHora() {
        String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
        consola.append("Hora actual: " + hora + "\n");
    }

    public void escribirArchivo(File dirActual, String nombre, String texto) {
        File archivo = new File(dirActual, nombre);
        if (archivo.exists() && archivo.isFile()) {
            try (FileWriter writer = new FileWriter(archivo, true)) { // append
                writer.write(texto + "\n");
                consola.append("Texto escrito en " + archivo.getName() + "\n");
            } catch (IOException e) {
                consola.append("Error al escribir: " + e.getMessage() + "\n");
            }
        } else {
            consola.append("Archivo no encontrado.\n");
        }
    }

    public void leerArchivo(File dirActual, String nombre) {
        File archivo = new File(dirActual, nombre);
        if (archivo.exists() && archivo.isFile()) {
            consola.append("Contenido de " + archivo.getName() + ":\n");
            try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = lector.readLine()) != null) {
                    consola.append(linea + "\n");
                }
            } catch (IOException e) {
                consola.append("Error al leer: " + e.getMessage() + "\n");
            }
        } else {
            consola.append("Archivo no encontrado.\n");
        }
    }

    public void mostrarAyuda() {
        consola.append("Lista de comandos del CMD:\n");
        consola.append("----------------------------------\n");
        consola.append("mkdir <nombre>         : Crea una carpeta.\n");
        consola.append("mfile <nombre>         : Crea un archivo.\n");
        consola.append("rm <nombre>            : Elimina un archivo o carpeta.\n");
        consola.append("dir                    : Lista contenido del directorio actual.\n");
        consola.append("cd <carpeta>           : Cambia a la carpeta especificada.\n");
        consola.append("cd ...                 : Va al directorio padre.\n");
        consola.append("date                   : Muestra la fecha actual.\n");
        consola.append("time                   : Muestra la hora actual.\n");
        consola.append("wr <archivo> <texto>   : Escribe texto en el archivo.\n");
        consola.append("rd <archivo>           : Lee el contenido del archivo.\n");
        consola.append("help                   : Muestra esta lista de comandos.\n");
        consola.append("exit                   : Cierra el programa.\n");
        consola.append("\n");
    }

}
