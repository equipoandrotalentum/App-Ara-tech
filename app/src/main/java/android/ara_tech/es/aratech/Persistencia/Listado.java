package android.ara_tech.es.aratech.Persistencia;

import java.util.ArrayList;

/**
 * Created by MAÑANA on 01/12/2016.
 */

public class Listado {

    private ArrayList<Integer> arrayPosiciones = new ArrayList<>();
    private static Listado instance = null;

    private Listado(){

    }

    public static Listado getInstance(){
        if(instance == null){
            instance = new Listado();
        }

        return instance;
    }

    public ArrayList<Integer> getArrayPosiciones() {
        return arrayPosiciones;
    }

    public void setArrayPosiciones(ArrayList<Integer> arrayPosiciones) {
        this.arrayPosiciones = arrayPosiciones;

    }

    public void addPosicion(Integer valor){
        arrayPosiciones.add(valor);
    }
}
