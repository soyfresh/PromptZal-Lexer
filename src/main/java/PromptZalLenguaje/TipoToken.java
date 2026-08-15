/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PromptZalLenguaje;

/**
 *
 * @author dar333n
 */
public class TipoToken {

    public enum tipoToken {
        DIRECTIVA, PALABRA_RESERVADA, COMANDO_IA, CONECTOR, IDENTIFICADOR,
        CADENA, ENTERO, DECIMAL, OP_ASIGNACION, OP_CONCATENACION,
        LLAVE_ABRE, LLAVE_CIERRA, PARENTESIS_ABRE, PARENTESIS_CIERRA,
        COMA, FUNCION
    }
}
