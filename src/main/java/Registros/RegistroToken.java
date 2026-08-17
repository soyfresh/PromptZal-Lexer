/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Registros;

import PromptZalLenguaje.TipoToken;

/**
 *
 * @author dar333n
 */
public class RegistroToken {

    private String lexema;
    private TipoToken tipo;
    private int fila;
    private int columna;

    public RegistroToken(String lexema, TipoToken tipo, int fila, int columna) {
        this.lexema = lexema;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
    }

    public String getLexema() {
        return lexema;
    }

    public TipoToken getTipo() {
        return tipo;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
    
    
}
