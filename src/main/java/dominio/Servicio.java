package dominio;

import java.sql.Time;

public class Servicio {
    private int idServicio;
    private String nombreServicio;
    private String descripcion;
    private double precio;
    private Time duracionMinutos;
    private int idCategoria;
    private int idEstado;

    public Servicio() {}

    public Servicio(int idServicio, String nombreServicio, String descripcion,
                    double precio, Time duracionMinutos, int idCategoria, int idEstado) {
        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracionMinutos = duracionMinutos;
        this.idCategoria = idCategoria;
        this.idEstado = idEstado;
    }

    public int getIdServicio() { return idServicio; }
    public void setIdServicio(int idServicio) { this.idServicio = idServicio; }

    public String getNombreServicio() { return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public Time getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Time duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public int getIdEstado() { return idEstado; }
    public void setIdEstado(int idEstado) { this.idEstado = idEstado; }
}