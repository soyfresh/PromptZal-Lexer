/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

import PromptZalLenguaje.TipoToken;
import Registros.RegistroError;
import Registros.RegistroToken;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dar333n
 */
public class Lexer {
    
    private char[] texto;
    private int posicion;
    private int fila;
    private int columna;

    private List<RegistroToken> tokens;
    private List<RegistroError> errores;
    
    public Lexer(String codigoFuente) {
        this.texto = codigoFuente.toCharArray();
        this.posicion = 0;
        this.fila = 1;
        this.columna = 1;
        this.tokens = new ArrayList<>();
        this.errores = new ArrayList<>();
    }

    public List<RegistroToken> getTokens() {
        return tokens;
    }

    public List<RegistroError> getErrores() {
        return errores;
    }
    /*
    Metodo que se llama para analizar todo el texto
    */
    public void analizar() {
        while (!finDeArchivo()) {
            leerToken();
        }
    }
    
    //revisa si el archivo aun no termina
    private boolean finDeArchivo() {
    return posicion >= texto.length;
    }

    //caracter de la posicion actual del texto .pz
    private char actual() {
        return texto[posicion];
    }

    //ver el siguiente caracter
    private char siguiente() {
        if (posicion + 1 >= texto.length) {
            return '\0';
        }
        return texto[posicion + 1];
    }

    //avanzar a la siguiente columna
    private char avanzar() {
        char c = texto[posicion];
        posicion = posicion + 1;

        if (c == '\n') {//si es un salto de linea, salta a siguiente fila y reinicia columna
            fila = fila + 1;
            columna = 1;
        } else {
            columna = columna + 1;
        }

        return c;
    }
    
    
    /*
    metodos para saber se es una letra o numero
    */
    private boolean esDigito(char c) {
        return c >= '0' && c <= '9';
    }
    
    private boolean esLetra(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
    
    
    /*
    metodos para reconocer identificadores
    */
    private boolean esInicioIdentificador(char c) {
        return esLetra(c) || c == '_';
    }

    private boolean esParteIdentificador(char c) {
        return esLetra(c) || esDigito(c) || c == '_';
    }
    
    
    //define el tipo de token que es
    private void leerToken() {
        char c = actual();

        if (c == ' ' || c == '\t') {
            avanzar();
            return;
        }

        if (c == '\n') {
            avanzar();
            return;
        }

        if (c == '/' && siguiente() == '/') {
            leerComentarioDeLinea();
            return;
        }

        if (c == '/' && siguiente() == '*') {
            leerComentarioDeBloque();
            return;
        }

        if (c == '@') {
            leerDirectiva();
            return;
        }

        if (c == '"') {
            leerCadena();
            return;
        }

        if (esDigito(c)) {
            leerNumero();
            return;
        }

        if (esInicioIdentificador(c)) {
            leerIdentificadorOPalabraClave();
            return;
        }

        if (c == '-' && siguiente() == '>') {
            avanzar();
            avanzar();
            tokens.add(new RegistroToken("->", TipoToken.CONECTOR, fila, columna));
            return;
        }

        switch (c) {
            case '=':
                avanzar();
                tokens.add(new RegistroToken("=", TipoToken.OP_ASIGNACION, fila, columna));
                return;
            case '+':
                avanzar();
                tokens.add(new RegistroToken("+", TipoToken.OP_CONCATENACION, fila, columna));
                return;
            case '{':
                avanzar();
                tokens.add(new RegistroToken("{", TipoToken.LLAVE_ABRE, fila, columna));
                return;
            case '}':
                avanzar();
                tokens.add(new RegistroToken("}", TipoToken.LLAVE_CIERRA, fila, columna));
                return;
            case '(':
                avanzar();
                tokens.add(new RegistroToken("(", TipoToken.PARENTESIS_ABRE, fila, columna));
                return;
            case ')':
                avanzar();
                tokens.add(new RegistroToken(")", TipoToken.PARENTESIS_CIERRA, fila, columna));
                return;
            case ',':
                avanzar();
                tokens.add(new RegistroToken(",", TipoToken.COMA, fila, columna));
                return;
        }

        //si no es ninguno se toma como error
        int filaError = fila;
        int columnaError = columna;
        char caracterMalo = avanzar();
        errores.add(new RegistroError(String.valueOf(caracterMalo), "Caracter no reconocido", filaError, columnaError));
    }
    
    
    private void leerComentarioDeLinea() {
        avanzar();
        avanzar();

        while (!finDeArchivo() && actual() != '\n') {
            avanzar();
        }
    }
    
    
    private void leerComentarioDeBloque() {
        int filaInicio = fila;
        int columnaInicio = columna;

        avanzar();
        avanzar();

        boolean cerrado = false;

        while (!finDeArchivo()) {
            if (actual() == '*' && siguiente() == '/') {
                avanzar();
                avanzar();
                cerrado = true;
                break;
            }
            avanzar();
        }

        if (!cerrado) {
            errores.add(new RegistroError("/* ...", "Comentario de bloque sin cerrar", filaInicio, columnaInicio));
        }
    }
    
    
    private void leerDirectiva() {
        int filaInicio = fila;
        int columnaInicio = columna;

        avanzar();

        if (!esLetra(actual())) {
            errores.add(new RegistroError("@", "Caracter no reconocido (directiva incompleta)", filaInicio, columnaInicio));
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append('@');

        while (!finDeArchivo() && esLetra(actual())) {
            sb.append(avanzar());
        }

        String lexema = sb.toString();
        String nombre = lexema.substring(1);

        if (nombre.equals("modelo") || nombre.equals("rol") || nombre.equals("formato")) {
            tokens.add(new RegistroToken(lexema, TipoToken.DIRECTIVA, filaInicio, columnaInicio));
        } else {
            errores.add(new RegistroError(lexema, "Directiva no reconocida", filaInicio, columnaInicio));
        }
    }
    
    
    private void leerCadena() {
        int filaInicio = fila;
        int columnaInicio = columna;

        avanzar();

        StringBuilder sb = new StringBuilder();
        sb.append('"');

        boolean cerrada = false;

        while (!finDeArchivo()) {
            char c = actual();

            if (c == '"') {
                sb.append(avanzar());
                cerrada = true;
                break;
            }

            if (c == '\n') {
                break;
            }

            sb.append(avanzar());
        }

        String lexema = sb.toString();

        if (cerrada) {
            tokens.add(new RegistroToken(lexema, TipoToken.CADENA, filaInicio, columnaInicio));
        } else {
            errores.add(new RegistroError(lexema, "Cadena sin cerrar", filaInicio, columnaInicio));
        }
    }
    
    private void leerNumero() {
        int filaInicio = fila;
        int columnaInicio = columna;

        StringBuilder sb = new StringBuilder();

        while (!finDeArchivo() && esDigito(actual())) {
            sb.append(avanzar());
        }

        boolean esDecimal = false;

        if (!finDeArchivo() && actual() == '.' && esDigito(siguiente())) {
            esDecimal = true;
            sb.append(avanzar()); // consume el '.'

            while (!finDeArchivo() && esDigito(actual())) {
                sb.append(avanzar());
            }
        }

        String lexema = sb.toString();

        if (esDecimal) {
            tokens.add(new RegistroToken(lexema, TipoToken.DECIMAL, filaInicio, columnaInicio));
        } else {
            tokens.add(new RegistroToken(lexema, TipoToken.ENTERO, filaInicio, columnaInicio));
        }
    }
    
    private void leerIdentificadorOPalabraClave() {
        int filaInicio = fila;
        int columnaInicio = columna;

        StringBuilder sb = new StringBuilder();

        while (!finDeArchivo() && esParteIdentificador(actual())) {
            sb.append(avanzar());
        }

        String lexema = sb.toString();

        if (lexema.equals("AGENTE") || lexema.equals("contexto") || lexema.equals("variable")
                || lexema.equals("EJECUTAR") || lexema.equals("EXPORTAR")) {
            tokens.add(new RegistroToken(lexema, TipoToken.PALABRA_RESERVADA, filaInicio, columnaInicio));
            return;
        }

        if (lexema.equals("PREGUNTAR") || lexema.equals("GENERAR") || lexema.equals("RESUMIR")
                || lexema.equals("ANALIZAR") || lexema.equals("TRADUCIR") || lexema.equals("CLASIFICAR")
                || lexema.equals("EXTRAER")) {
            tokens.add(new RegistroToken(lexema, TipoToken.COMANDO_IA, filaInicio, columnaInicio));
            return;
        }

        if (lexema.equals("SOBRE") || lexema.equals("DESDE") || lexema.equals("EN") || lexema.equals("COMO")) {
            tokens.add(new RegistroToken(lexema, TipoToken.CONECTOR, filaInicio, columnaInicio));
            return;
        }

        if (lexema.equals("CARGAR")) {
            tokens.add(new RegistroToken(lexema, TipoToken.FUNCION, filaInicio, columnaInicio));
            return;
        }

        tokens.add(new RegistroToken(lexema, TipoToken.IDENTIFICADOR, filaInicio, columnaInicio));
    }
    
    
}
