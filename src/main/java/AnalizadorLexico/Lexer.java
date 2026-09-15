/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

import PromptZalLenguaje.TipoToken;
import Registros.RegistroError;
import Registros.RegistroToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dar333n
 */
public class Lexer {

    private static final Map<String, TipoToken> PALABRAS_RESERVADAS = new HashMap<>();
    private static final Map<String, TipoToken> COMANDOS = new HashMap<>();
    private static final Map<String, TipoToken> FUNCIONES_SISTEMA = new HashMap<>();
    private static final Map<String, TipoToken> CONECTORES = new HashMap<>();
    private static final Map<String, TipoToken> TODAS_LAS_PALABRAS = new HashMap<>();

    static {
        PALABRAS_RESERVADAS.put("AGENTE", TipoToken.PALABRA_RESERVADA);
        PALABRAS_RESERVADAS.put("contexto", TipoToken.PALABRA_RESERVADA);
        PALABRAS_RESERVADAS.put("variable", TipoToken.PALABRA_RESERVADA);
        PALABRAS_RESERVADAS.put("EJECUTAR", TipoToken.PALABRA_RESERVADA);
        PALABRAS_RESERVADAS.put("EXPORTAR", TipoToken.PALABRA_RESERVADA);

        COMANDOS.put("PREGUNTAR", TipoToken.COMANDO);
        COMANDOS.put("GENERAR", TipoToken.COMANDO);
        COMANDOS.put("RESUMIR", TipoToken.COMANDO);
        COMANDOS.put("ANALIZAR", TipoToken.COMANDO);
        COMANDOS.put("TRADUCIR", TipoToken.COMANDO);
        COMANDOS.put("CLASIFICAR", TipoToken.COMANDO);
        COMANDOS.put("EXTRAER", TipoToken.COMANDO);

        FUNCIONES_SISTEMA.put("CARGAR", TipoToken.FUNCION_SISTEMA);

        CONECTORES.put("SOBRE", TipoToken.CONECTOR);
        CONECTORES.put("DESDE", TipoToken.CONECTOR);
        CONECTORES.put("EN", TipoToken.CONECTOR);
        CONECTORES.put("COMO", TipoToken.CONECTOR);

        TODAS_LAS_PALABRAS.putAll(PALABRAS_RESERVADAS);
        TODAS_LAS_PALABRAS.putAll(COMANDOS);
        TODAS_LAS_PALABRAS.putAll(FUNCIONES_SISTEMA);
        TODAS_LAS_PALABRAS.putAll(CONECTORES);
    }
    
    private static final String DIRECTIVAS_VALIDAS = "modelo,rol,formato,";
    
    private String flujoCaracteres;
    private int posicion;
    private int fila;
    private int columna;

    private List<RegistroToken> tokens;
    private List<RegistroError> errores;
    
    public Lexer(String codigoFuente) {
        this.flujoCaracteres = codigoFuente;
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
    **
    METODOS DEL CURSOR
    **
    */
    private boolean finDeArchivo() {
        return posicion >= flujoCaracteres.length();
    }

    private char actual() {
        return flujoCaracteres.charAt(posicion);
    }
    
    private char avanzar() {
        char c = flujoCaracteres.charAt(posicion);
        posicion++;
        if (c == '\n') {
            fila++;
            columna = 1;
        } else {
            columna++;
        }
        return c;
    }

    private char siguiente() {
        return (posicion + 1 < flujoCaracteres.length()) ? flujoCaracteres.charAt(posicion + 1) : '\0';
    }

    private boolean esInicioIdentificador(char c) {
        return Character.isLetter(c) || c == '_';
    }

    private boolean esParteIdentificador(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }
    
    
    /*
    **
    ANALIZADOR LEXICO
    **
    */
    public void AnalizarFlujo() {
        while(!finDeArchivo()){ //ESTADO INICIAL q0 -  en cada while se inicia un nuevo analisis volviendo al estado inicial
            char c = actual();

        /*
        **
        TRANSICIONES - los if son las transiciones(ramas) del AFD hacia los mini-AFD 
        cada if representa AFD con sus propia gramatica, siendo tambien en este punto el estado inicial
            
        transición q0 --categoria(c)--> qX hacia un sub-autómata.
        **
        */
        
            if (c == ' ' || c == '\t') {
                avanzar();
            } else if (c == '\n') {
                avanzar();
            } else if (c == '/') {
                comentario();
            } else if (c == '@') {
                directiva();
            } else if (c == '"') {
                cadena();
            } else if (Character.isDigit(c)) {
                numero();
            } else if (esInicioIdentificador(c)) {
                identificadorOPalabraClave();
            } else if (c == '-') {
                flechaOError();
            } else if (c == '=') {
                emitirSimple(TipoToken.ASIGNACION, "=");
            } else if (c == '+') {
                emitirSimple(TipoToken.CONCATENACION, "+");
            } else if (c == '{') {
                emitirSimple(TipoToken.LLAVE_ABRE, "{");
            } else if (c == '}') {
                emitirSimple(TipoToken.LLAVE_CIERRA, "}");
            } else if (c == '(') {
                emitirSimple(TipoToken.PARENTESIS_ABRE, "(");
            } else if (c == ')') {
                emitirSimple(TipoToken.PARENTESIS_CIERRA, ")");
            } else {
                // Carácter no reconocido por ninguna rama del AFD: recuperación de errores.
                int filaInicio = fila;
                int columnaInicio = columna;
                
                errores.add(new RegistroError(String.valueOf(c), "CARACTER_NO_RECONOCIDO", filaInicio, columnaInicio));
                avanzar(); 
            }
        }
    }
 
    
    /*
    **
    mini-AFD 
    **
    */
   
    // Símbolos de un solo carácter: =, +, {, }, (, )
     private void emitirSimple(TipoToken tipo, String lexema) {
        int filaInicio = fila;
        int columnaInicio = columna;
        
        avanzar();
        tokens.add(new RegistroToken(lexema, tipo, filaInicio, columnaInicio));
    }   
        
    // qCOM: comentarios de línea (//) y de bloque (/* */). No generan token.
    private void comentario() {
        int filaInicio = fila;
        int columnaInicio = columna;
        
        avanzar();

        if (!finDeArchivo() && actual() == '/') {
            while (!finDeArchivo() && actual() != '\n') {
                avanzar();
            }
        } else if (!finDeArchivo() && actual() == '*') {
            avanzar(); 
            boolean cerrado = false;
            while (!finDeArchivo()) {
                if (actual() == '*' && siguiente() == '/') {
                    avanzar();
                    avanzar();
                    cerrado = true;
                    break;
                }
                if (actual() == '\n') {
                    avanzar();
                } else {
                    avanzar();
                }
            }
            if (!cerrado) {
                errores.add(new RegistroError("/*", "COMENTARIO_SIN_CERRAR", filaInicio, columnaInicio));
            }
        } else {
            errores.add(new RegistroError("/", "SIMBOLO_INVALIDO", filaInicio, columnaInicio));
        }
    }
    
    
    //directivas @modelo, @rol, @formato
    //qDIR0 --'@'--> qDIR1 --letra--> qDIR2 --(letra|dígito)*--> ACEPTA
    private void directiva() {
        int filaInicio = fila;
        int columnaInicio = columna;
        
        StringBuilder lexemaCompleto = new StringBuilder();
        StringBuilder lexema = new StringBuilder();
        
        lexemaCompleto.append(actual());
        avanzar();

        if (finDeArchivo() || !Character.isLetter(actual())) {
            errores.add(new RegistroError(lexemaCompleto.toString(), "DIRECTIVA_MAL_FORMADA", filaInicio, columnaInicio));
            return;
        }
        
        while (!finDeArchivo() && (Character.isLetterOrDigit(actual()))) {
            lexema.append(actual());
            avanzar();
        }
        
        if (DIRECTIVAS_VALIDAS.contains(lexema.toString() + ",")) {
            lexemaCompleto.append(lexema);
            tokens.add(new RegistroToken(lexemaCompleto.toString(),TipoToken.DIRECTIVA, filaInicio, columnaInicio));
        } else {
            errores.add(new RegistroError(lexema.toString(), "DIRECTIVA_DESCONOCIDA", filaInicio, columnaInicio));
        }
    }
    
    
    
    //qSTR0 --'"'--> qSTR1 (bucle: cualquier char != '"' y != '\n') --'"'--> ACEPTA
    private void cadena() {
        int filaInicio = fila;
        int columnaInicio = columna;
        
        StringBuilder lexema = new StringBuilder();
        lexema.append(actual());
        avanzar();

        //qSTR0 --'"'--> qSTR1 (bucle: cualquier char != '"' y != '\n')
        while (!finDeArchivo()) {
            
            if (actual() != '"' && actual() != '\n') {
                lexema.append(actual());
                avanzar();
            }
        }

        if (finDeArchivo() || actual() == '\n') {
            errores.add(new RegistroError(lexema.toString(), "CADENA_SIN_CERRAR", filaInicio, columnaInicio));
            return; 
        }
        
        //qSTR1 (bucle: cualquier char != '"' y != '\n') --'"'--> ACEPTA
        lexema.append(actual());
        avanzar();
        tokens.add(new RegistroToken(lexema.toString(), TipoToken.CADENA, filaInicio, columnaInicio));
    }
    
    
    
    //enteros y decimales
    //qNUM0 --dígito--> qNUM1 (bucle dígito) --ACEPTA ENTERO
    //qNUM0 --dígito--> qNUM1 (bucle dígito) --'.'--> qDEC0 --dígito--> qDEC1 (bucle dígito) --ACEPTA DECIMAL
    private void numero() {
        int filaInicio = fila;
        int columnaInicio = columna;
        
        StringBuilder lexema = new StringBuilder();

        while (!finDeArchivo() && Character.isDigit(actual())) {
            lexema.append(actual());
            avanzar();
        }

        //transición qNUM1 --'.'--> qDEC0
        if (!finDeArchivo() && actual() == '.' && siguiente() != '\0' && Character.isDigit(siguiente())) {
            lexema.append('.');
            avanzar();

            //qDEC0 --dígito--> qDEC1 (bucle dígito)
            while (!finDeArchivo() && Character.isDigit(actual())) {
                lexema.append(actual());
                avanzar();
            }
            tokens.add(new RegistroToken(lexema.toString(), TipoToken.DECIMAL, filaInicio, columnaInicio));
            
        //ACEPTA ENTERO
        } else {
            tokens.add(new RegistroToken(lexema.toString(), TipoToken.ENTERO, filaInicio, columnaInicio));
        }
    }
    
    
    //qARR: flecha ->
    //qARR0 --'-'--> qARR1 --'>'--> ACEPTA FLECHA
    private void flechaOError() {
        int filaInicio = fila;
        int columnaInicio = columna;
        
        avanzar();
        if (!finDeArchivo() && actual() == '>') {
            avanzar();
            tokens.add(new RegistroToken("->", TipoToken.CONECTOR, filaInicio, columnaInicio));
        } else {
            errores.add(new RegistroError("-", "SIMBOLO_INVALIDO", filaInicio, columnaInicio));
        }
    }
    
    
    //qID: identificadores y palabras reservadas
    //  qID0 --(letra|'_')--> qID1 --(letra|dígito|'_')*--> ACEPTA
    private void identificadorOPalabraClave() {
        int filaInicio = fila;
        int columnaInicio = columna;

        StringBuilder lexema = new StringBuilder();

        // qID0 -> qID1
        lexema.append(actual());
        avanzar();

        // qID1: bucle (quedarse en el mismo estado mientras siga siendo válido)
        while (!finDeArchivo() && esParteIdentificador(actual())) {
            lexema.append(actual());
            avanzar();
        }

        String palabra = lexema.toString();
        TipoToken tipo = TODAS_LAS_PALABRAS.get(palabra);
        if (tipo == null) {
            tipo = TipoToken.IDENTIFICADOR;
        }
        tokens.add(new RegistroToken(palabra, tipo, filaInicio, columnaInicio));
    }  
}
