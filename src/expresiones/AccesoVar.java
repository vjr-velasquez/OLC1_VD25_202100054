/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expresiones;

import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;
import excepciones.Errores;

/**
 *
 * @author Lesther
 */
public class AccesoVar extends Instruccion {
    private String id;

    public AccesoVar(String id, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.id = id;
    }

    public Object interpretar(Arbol arbol, tablaSimbolos tabla){
        var simbolo = tabla.getVariable(this.id);
        if (simbolo == null){
            return new Errores("SEMANTICO", "Variable no existente: " + this.id, this.linea, this.col);
        }
        this.tipo = simbolo.getTipo();  // no pierdes info
        return simbolo.getValor();
    }

    
}