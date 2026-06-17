package dominio;

public class Usuario
{
    private int idUsuario;
    private String correo;
    private String contra;
    private int idEstado;

    public Usuario()
    {
    }

    public Usuario(int idUsuario, String correo, String contra, int idEstado) {
        this.idUsuario = idUsuario;
        this.correo = correo;
        this.contra = contra;
        this.idEstado = idEstado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }
}
