package model;


/*
Representa un usuario que interactúa con el sistema.
Un usuario tiene un ID, nombre, apellido, email, rol y el ID del depósito asociado.
Los usuarios pueden crear, modificar y eliminar pedidos si tienen los roles necesarios.
 */
public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private String contrasena;
    private int idDeposito;

    public Usuario(int idUsuario, String nombre, String apellido, String email, String rol, String contrasena, int idDeposito) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
        this.contrasena = contrasena;
        this.idDeposito = idDeposito;
    }

    public Usuario() {
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getIdDeposito() {
        return idDeposito;
    }

    public void setIdDeposito(int idDeposito) {
        this.idDeposito = idDeposito;
    }
}
