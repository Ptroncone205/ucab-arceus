package org.example;

public class ListaGenerica <S> {
    private Nodo <S> cabeza;

    public ListaGenerica() {
        cabeza = null;
    }

    public Nodo <S> getCabeza() {
        return cabeza;
    }

    public void setCabeza(Nodo <S> cabeza) {
        this.cabeza = cabeza;
    }

    public boolean vacia(){
        return cabeza == null;
    }

    public void agregar(S dato){
        Nodo <S> aux = new Nodo<>(dato);
        aux.setSiguiente(null);
        if (vacia()){
            cabeza = aux;
        }else {
            Nodo<S> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(aux);
        }
    }

    public void eliminar(S dato){
        if (vacia()){
            System.out.println("tolon");
        }else {
            Nodo<S> actual = cabeza;
            while (actual.getSiguiente().getDato()!=dato || actual.getSiguiente()!=null) {
                actual = actual.getSiguiente();
            }
            if (actual.getSiguiente()==null) {
                System.out.println("deja de trolear papa");
            }else{
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
            }

        }
    }
}
