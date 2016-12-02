package android.ara_tech.es.aratech.Entidad;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Usuario {
    private String nombreUsuario;
    private String email, telefono, direccion, dni, lista2;
    private ArrayList lista;

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getLista2() {
        return lista2;
    }

    public void setLista2(String lista2) {
        this.lista2 = lista2;
    }

    public ArrayList getLista() {
        return lista;
    }

    public void setLista(ArrayList lista) {
        this.lista = lista;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
