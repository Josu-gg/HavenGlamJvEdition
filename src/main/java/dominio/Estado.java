package dominio;

public class Estado
{
    private int idEstado;
    private String  NombreEstado;
    private  String TipoEstado;

    public Estado()
    {

    }

    public Estado(int idEstado, String nombreEstado, String tipoEstado) {
        this.idEstado = idEstado;
        NombreEstado = nombreEstado;
        TipoEstado = tipoEstado;
    }
    @Override
    public String toString() {
        return NombreEstado;
    }
    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombreEstado() {
        return NombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        NombreEstado = nombreEstado;
    }

    public String getTipoEstado() {
        return TipoEstado;
    }

    public void setTipoEstado(String tipoEstado) {
        TipoEstado = tipoEstado;
    }
}
