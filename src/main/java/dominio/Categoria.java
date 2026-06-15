package dominio;

public class Categoria
{
    private  int idCategoria;
    private  String NombreCategoria;
    private int idEstado;

    public Categoria()
    {

    }
    @Override
    public String toString() {
        return NombreCategoria;
    }

    public Categoria(int idCategoria, String nombreCategoria, int idEstado) {
        this.idCategoria = idCategoria;
        NombreCategoria = nombreCategoria;
        this.idEstado = idEstado;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return NombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        NombreCategoria = nombreCategoria;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }
}
