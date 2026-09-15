/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package Main;

import AnalizadorLexico.Lexer;
import Registros.RegistroError;
import Registros.RegistroToken;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author dar333n
 */
public class Main {
    /*
    public static Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
    public static List<RegistroToken> tokens;
    public static List<RegistroError> errores;
    

        System.out.println("=====================================");
        System.out.println("  PromptZal - Analizador Lexico");
        System.out.println("=====================================");

        boolean continuar = true;

        while (continuar) {

            String rutaArchivoPz = pedirRutaArchivoValida();

            String rutaCarpetaHtml = pedirRutaCarpetaValida();

            procesarArchivo(rutaArchivoPz, rutaCarpetaHtml);

            continuar = preguntarQueHacer(sc);
        }

        System.out.println("Programa finalizado.");
        sc.close();
    }

    public static String pedirRutaArchivoValida() {
        while (true) {
            System.out.println();
            System.out.print("Ingrese la ruta del archivo .pz: ");
            String ruta = sc.nextLine().trim();

            File archivo = new File(ruta);
            if (archivo.exists() && archivo.isFile()) {
                return ruta;
            } else {
                System.out.println("ERROR: El archivo especificado no existe o la ruta es invalida. Intente de nuevo.");
            }
        }
    }

    public static String pedirRutaCarpetaValida() {
        while (true) {
            System.out.print("Ingrese la ruta de la carpeta para guardar los reportes HTML: ");
            String rutaCarpeta = sc.nextLine().trim();

            if (rutaCarpeta.isEmpty()) {
                System.out.println("ERROR: La ruta no puede estar vacia. Intente de nuevo.");
                continue;
            }

            File carpeta = new File(rutaCarpeta);

            if (carpeta.isDirectory()) {
                return rutaCarpeta;
            } else {
                System.out.println("ERROR: La ruta existe pero no es una carpeta valida.");
            }
        }
    }

    public static boolean preguntarQueHacer(Scanner sc) {
        boolean opcionValida=false;  
        while(!opcionValida){
            mostrarOpciones();
            String opcion = sc.nextLine().trim();
            switch (opcion) {
                case "1":
                    return true;
                case "2":
                    return false;
                default:
                    System.out.println("Opcion invalida, elija una de las opciones que se muestran en pantalla.");
                    opcionValida=false;
            }
        }
        return false;
    }

    public static void mostrarOpciones(){
        System.out.println("=====================================");
        System.out.println("  PromptZal - Analizador Lexico");
        System.out.println("=====================================");
        System.out.println(" ");
        System.out.print("1. Analizar otro archivo\n");
        System.out.print("2. Salir\n");
        System.out.print("Seleccione una opcion: ");
    }

    
    public static void procesarArchivo(String rutaPz, String carpetaHtml) {
        File archivo = new File(rutaPz);

        if (!archivo.exists()) {
            System.out.println("ERROR: el archivo no existe.");
            return;
        }

        String codigoFuente;
        try {
            codigoFuente = new String(Files.readAllBytes(archivo.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("ERROR al leer el archivo: " + e.getMessage());
            return;
        }

        Lexer lexer = new Lexer(codigoFuente);

        tokens = lexer.getTokens();
        errores = lexer.getErrores();

        imprimirTablaTokens(tokens);
        imprimirTablaErrores(errores);
        
        generarReporteHTML(tokens, errores, carpetaHtml);
    }

    public static void generarReporteHTML(List<RegistroToken> tokens, List<RegistroError> errores, String carpetaDestino) {
        File carpeta = new File(carpetaDestino);
        if (!carpeta.exists()) {
            System.out.println("la crpeta no existe, no se pudo generar el archivo HTML");
        }

        File archivoHtml = new File(carpeta, "reporte_analisis.html");

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoHtml, StandardCharsets.UTF_8))) {
            pw.println("<!DOCTYPE html>");
            pw.println("<html lang=\"es\">");
            pw.println("<head>");
            pw.println("    <meta charset=\"UTF-8\">");
            pw.println("    <title>Reportes - PromptZal</title>");
            pw.println("    <style>");
            pw.println("        body { font-family: Arial, sans-serif; margin: 30px; }");
            pw.println("        h1, h2 { color: #333; }");
            pw.println("        table { width: 100%; border-collapse: collapse; margin-bottom: 30px; }");
            pw.println("        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }");
            pw.println("        th { background-color: #f2f2f2; }");
            pw.println("    </style>");
            pw.println("</head>");
            pw.println("<body>");

            pw.println("    <h1>Reportes</h1>");

            //TOKENS RECONOCIDOS
            pw.println("    <h2>Tokens Reconocidos</h2>");
            pw.println("    <table>");
            pw.println("        <tr><th>No.</th><th>Lexema</th><th>Tipo</th><th>Fila</th><th>Columna</th></tr>");

            for (int i = 0; i < tokens.size(); i++) {
                RegistroToken t = tokens.get(i);
                pw.println("        <tr>");
                pw.println("            <td>" + (i + 1) + "</td>");
                pw.println("            <td>" + t.getLexema() + "</td>");
                pw.println("            <td>" + t.getTipo() + "</td>");
                pw.println("            <td>" + t.getFila() + "</td>");
                pw.println("            <td>" + t.getColumna() + "</td>");
                pw.println("        </tr>");
            }
            pw.println("    </table>");

            //ERRORES LÉXICOS
            pw.println("    <h2>Errores Léxicos</h2>");

            if (errores.isEmpty()) {
                pw.println("    <p>No se encontraron errores lexicos.</p>");
            } else {
                pw.println("    <table>");
                pw.println("        <tr><th>No.</th><th>Lexema / Carácter</th><th>Descripción</th><th>Fila</th><th>Columna</th></tr>");

                for (int i = 0; i < errores.size(); i++) {
                    RegistroError e = errores.get(i);
                    pw.println("        <tr>");
                    pw.println("            <td>" + (i + 1) + "</td>");
                    pw.println("            <td>" + e.getLexema() + "</td>");
                    pw.println("            <td>" + e.getDescripcion() + "</td>");
                    pw.println("            <td>" + e.getFila() + "</td>");
                    pw.println("            <td>" + e.getColumna() + "</td>");
                    pw.println("        </tr>");
                }
                pw.println("    </table>");
            }

            pw.println("</body>");
            pw.println("</html>");

            System.out.println("\nReporte HTML generado exitosamente en: " + archivoHtml.getAbsolutePath());

        } catch (IOException e) {
            System.out.println("ERROR al generar el reporte HTML: " + e.getMessage());
        }
    }

    public static void imprimirTablaTokens(List<RegistroToken> tokens) {
        System.out.println();
        System.out.println("-------------------------------------------- TOKENS RECONOCIDOS ----------------------------------------------");
        System.out.printf("%-20s %-50s %-20s %-15s %-15s%n", "No.", "Lexema", "Tipo", "Fila", "Col");
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        
        for (int i = 0; i < tokens.size(); i++) {
            RegistroToken t = tokens.get(i);
            int numero = i + 1;
            System.out.printf("%-20d %-50s %-20s %-15d %-15d%n",
                    numero, t.getLexema(), t.getTipo(), t.getFila(), t.getColumna());
        }

        System.out.println("Total de tokens: " + tokens.size());
    }


    public static void imprimirTablaErrores(List<RegistroError> errores) {
        System.out.println();
        System.out.println("-------------------------------------------- ERRORES LEXICOS --------------------------------------------------");

        if (errores.isEmpty()) {
            System.out.println("No se encontraron errores lexicos.");
            return;
        }

        System.out.printf("%-50s %-100s %-10s %-6s%n", "Lexema/Caracter", "Descripcion", "Fila", "Col");
        System.out.println("---------------------------------------------------------------------------------------------------------------");

        for (RegistroError e : errores) {
            System.out.printf("%-20s %-100s %-10d %-6d%n",
                    e.getLexema(), e.getDescripcion(), e.getFila(), e.getColumna());
        }

        System.out.println("Total de errores: " + errores.size());
    }
 */   
}
    



