package dao;

import model.Cliente;


/*
 La interfaz IClienteDAO define los métodos para realizar operaciones CRUD en la entidad Cliente.
 Esta interfaz proporciona una abstracción para la gestión de clientes en el sistema.
 */

public interface IClienteDAO {
    Cliente buscarCliente(int idCliente);
    int crearCliente(Cliente cliente);
}
