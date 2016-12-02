package android.ara_tech.es.aratech.Entidad;



import java.io.Serializable;

public class Servicio implements Serializable{

    private String nombre;
    private int edad;
    private String nacionalidad;
    private double latitud;
    private double longitud;
    private double puntuacion;
    private String descripcion;
    private int type;

    public Servicio() {
    }

    public Servicio(String nombre, int edad, String nacionalidad, double latitud, double longitud, double puntuacion, String descripcion, int type) {
        this.nombre = nombre;
        this.edad = edad;
        this.nacionalidad = nacionalidad;
        this.latitud = latitud;
        this.longitud = longitud;

        this.puntuacion = puntuacion;
        this.descripcion = descripcion;
        this.type = type;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }


    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
