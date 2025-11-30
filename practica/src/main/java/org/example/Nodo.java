package org.example;

public class Nodo <S> {
    private S dato;
    private Nodo <S> siguiente;

    public Nodo() {
        this.dato = null;
        this.siguiente = null;
    }

    public Nodo(S dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public boolean vacio(){
        return this.siguiente == null;
    }

    public void setSiguiente(Nodo <S> coraspe) {
        this.siguiente = coraspe;
    }

    public Nodo <S> getSiguiente() {
        return siguiente;
    }

    public S getDato() {
        return dato;
    }
}
