package view;

import controller.PedidoController;
import controller.ClienteController;
import controller.ProductoController;

import model.Cliente;
import model.DetalleItem;
import model.DetallePedido;
import model.Pedido;
import model.Producto;
import utils.Utils;

import java.sql.Connection;
import java.util.*;



/**
 * La clase Menu maneja la interacción del usuario con el sistema a través de un menú de selección.
 * Permite crear, listar, ver, modificar y eliminar pedidos, así como una pequeña y necesaria interacción con clientes y productos.
 */

public class Menu {
    private final PedidoController pedidoController;
    private final ClienteController clienteController;
    private final ProductoController productoController;
    private final Scanner scanner;
    private final Connection conexion;

    public Menu(Connection conexion) {
        this.conexion = conexion;
        this.pedidoController = new PedidoController(conexion);
        this.clienteController = new ClienteController(conexion);
        this.productoController = new ProductoController(conexion);
        this.scanner = new Scanner(System.in);
    }

    // Muestra menú principal.
    public void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("------ Quento Snacks ------");
            System.out.println("--- Menu Principal ---");
            System.out.println("1. Pedidos");
            System.out.println("2. Stock");
            System.out.println("3. Cliente");
            System.out.println("4. Depósito");
            System.out.println("5. Usuarios");
            System.out.println("6. Salir");
            opcion = Utils.obtenerEnteroValido(scanner, "Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    mostrarMenuPedidos();
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                    System.out.println("Módulo en construcción...\n");
                    break;
                case 6:
                    System.out.println("Saliendo...\n");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.\n");
            }
        } while (opcion != 6);
    }

    // Muestra el menú de gestión de pedidos
    public void mostrarMenuPedidos() {
        int opcion;
        do {
            System.out.println("------ Menú de Pedidos ------");
            System.out.println("1. Crear Pedido");
            System.out.println("2. Listar Pedidos");
            System.out.println("3. Ver Pedido");
            System.out.println("4. Modificar Pedido");
            System.out.println("5. Eliminar Pedido");
            System.out.println("6. Volver al Menú Principal");
            System.out.println("7. Salir");
            opcion = Utils.obtenerEnteroValido(scanner, "Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    crearPedido();
                    break;
                case 2:
                    listarPedidos();
                    break;
                case 3:
                    verPedido();
                    break;
                case 4:
                    modificarPedido();
                    break;
                case 5:
                    eliminarPedido();
                    break;
                case 6:
                    mostrarMenuPrincipal();
                    break;
                case 7:
                    System.out.println("Saliendo...\n");
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Intente nuevamente.\n");
            }
        } while (opcion != 6);
    }


    // Crea un nuevo pedido, solicitando los datos necesarios para crearlo en la BD.
    public void crearPedido() {
        System.out.println("----------- Crear Pedido -----------");

        int idCliente = Utils.obtenerEnteroValido(scanner, "Ingrese ID del Cliente: ");
        Cliente cliente = clienteController.buscarCliente(idCliente);
        if (cliente == null) {
            System.out.println("\nCliente no encontrado. Ingrese datos del nuevo cliente.");

            String nombre = Utils.obtenerStringNoVacio(scanner, "Nombre: ");
            String apellido = Utils.obtenerStringNoVacio(scanner, "Apellido: ");
            String direccion = Utils.obtenerStringNoVacio(scanner, "Dirección: ");
            String telefono = Utils.obtenerStringNoVacio(scanner, "Teléfono: ");

            // Mostrar datos ingresados y confirmar
            if (!Utils.confirmar(scanner, "¿Desea confirmar la creación del cliente? (s/n): ")) {
                System.out.println("Creación del cliente cancelada.\n");
                return; // Salir SIN crear el pedido
            }

            cliente = new Cliente(0, nombre, apellido, direccion, telefono);
            int idGenerado = clienteController.crearCliente(cliente);
            if (idGenerado != -1) {
                cliente.setIdCliente(idGenerado);
                System.out.println("\nCliente creado con éxito. ID: " + idGenerado);
            } else {
                System.out.println("\nError al crear el cliente. \n");
                return;
            }
        }

        System.out.println("\nCliente: " + cliente.getNombre() + " " + cliente.getApellido());

        List<DetalleItem> detalleItems = new ArrayList<>();
        while (true) {
            System.out.print("Ingrese ID del Producto (o 'salir' para finalizar, o 'listar' para ver productos): ");
            String input = scanner.nextLine(); // Cambiado a nextLine para consistencia
            if (input.equalsIgnoreCase("salir")) {
                break;
            } else if (input.equalsIgnoreCase("listar")) {
                Utils.mostrarListaProductos();
                continue;
            }

            int idProducto;
            try {
                idProducto = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("ID de producto inválido. Por favor, ingrese un número.\n");
                continue;
            }

            Producto producto = productoController.buscarProducto(idProducto);
            if (producto == null) {
                System.out.println("ID de producto incorrecto. Ingrese un ID válido.\n");
                continue;
            }

            int cantidad = Utils.obtenerEnteroValido(scanner, "Ingrese Cantidad: ");

            double precioUnitario = producto.getPrecioUnitario();
            double precioTotal = cantidad * precioUnitario;

            detalleItems.add(new DetalleItem(0, 0, idProducto, cantidad, precioUnitario, precioTotal));
        }

        if (detalleItems.isEmpty()) {
            System.out.println("No se puede crear un pedido sin productos. Pedido cancelado.\n");
            return;
        }

        //Resumen -
        System.out.println("\nResumen del Pedido:");
        for (DetalleItem item : detalleItems) {
            Producto producto = productoController.buscarProducto(item.getIdProducto());
            System.out.println("\nProducto: " + producto.getDescripcion() +
                    "\n | Cantidad: " + item.getCantidad() +
                    "\n | Precio Unitario: $" + item.getPrecioUnitario() +
                    "\n | Precio Total: $" + item.getPrecioTotal());
        }

        Pedido pedido = new Pedido(0, cliente.getIdCliente(), new java.sql.Timestamp(System.currentTimeMillis()), "pendiente", 1, 0, new DetallePedido(0, 0, detalleItems));
        double total = pedidoController.calcularTotal(pedido);
        pedido.setPrecioTotalPedido(total);

        // Mostrar total del pedido
        System.out.println("----------------------------------------------");
        System.out.println("Total del Pedido: $" + total);
        System.out.println("----------------------------------------------");

        if (Utils.confirmar(scanner, "¿Desea confirmar la creación del pedido? (s/n): ")) {
            pedidoController.crearPedido(pedido);
            System.out.println("Pedido creado con éxito. Total: $" + total + "\n");
        } else {
            System.out.println("Creación del pedido cancelada. \n");
        }
    }


    // Lista todos los pedidos registrados en el sistema.
    public void listarPedidos() {
        System.out.println("---------- Listado de Pedidos ----------");
        List<Pedido> pedidos = pedidoController.obtenerTodosLosPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.\n");
        } else {
            for (Pedido pedido : pedidos) {
                System.out.println("ID Pedido: " + pedido.getIdPedido() +
                        " | ID Cliente: " + pedido.getIdCliente() +
                        " | Fecha: " + pedido.getFecha() +
                        " | Estado: " + pedido.getEstado() +
                        " | ID Depósito: " + pedido.getIdDeposito() +
                        " | Precio Total: $" + pedido.getPrecioTotalPedido());
            }
            System.out.println();
        }
    }

    // ve un único pedido, recibido por ID.
    public void verPedido() {
        System.out.println("---------- Ver Pedido ----------");

        int idPedido = Utils.obtenerEnteroValido(scanner, "Ingrese ID del Pedido: ");
        Pedido pedido = pedidoController.obtenerPedidoPorId(idPedido);

        if (pedido == null) {
            System.out.println("No existe un pedido con el ID indicado.\n");
        } else {
            System.out.println("\nPedido " + idPedido + ":");
            System.out.println("ID: " + pedido.getIdPedido());
            System.out.println("Cliente: " + pedido.getIdCliente());
            System.out.println("Estado: " + pedido.getEstado());
            System.out.println("Productos:");

            if (pedido.getDetalle() != null) {
                for (DetalleItem item : pedido.getDetalle().getDetalleItems()) {
                    Producto producto = productoController.buscarProducto(item.getIdProducto());
                    System.out.println(producto.getDescripcion() + " x " + item.getCantidad() + " : $" + item.getPrecioTotal());
                }

                System.out.println("Estado: " + pedido.getEstado()+ "\n");
                System.out.println("Total: $" + pedido.getPrecioTotalPedido() + "\n");

            } else {
                System.out.println("No hay detalles para este pedido.\n");
            }
        }
    }

     // Modifica un pedido existente, permitiendo cambiar su estado o los productos y cantidades.
    public void modificarPedido() {
        System.out.println("---------- Modificar Pedido ----------");

        int idPedido = Utils.obtenerEnteroValido(scanner, "Ingrese ID del Pedido a modificar: ");
        Pedido pedido = pedidoController.obtenerPedidoPorId(idPedido);

        if (pedido == null) {
            System.out.println("No existe un pedido con el ID indicado.\n");
        } else {
            System.out.println("\nSeleccione el campo a modificar:");
            System.out.println("1. Estado del Pedido");
            System.out.println("2. Productos y Cantidades");

            int opcion = Utils.obtenerEnteroValido(scanner, "Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    System.out.println("Estados disponibles: pendiente, en proceso, terminado, cancelado");
                    String nuevoEstado = Utils.obtenerStringNoVacio(scanner, "Ingrese el nuevo estado: ");
                    if (!nuevoEstado.equalsIgnoreCase("pendiente") && !nuevoEstado.equalsIgnoreCase("en proceso") && !nuevoEstado.equalsIgnoreCase("terminado") && !nuevoEstado.equalsIgnoreCase("cancelado")) {
                        System.out.println("Estado inválido. Modificación cancelada.\n");
                    } else {
                        pedidoController.actualizarEstadoPedido(idPedido, nuevoEstado);
                        System.out.println("Estado del pedido actualizado.\n");
                    }
                    break;
                case 2:
                    System.out.println("Ingrese los nuevos productos y cantidades:");
                    List<DetalleItem> detalleItems = new ArrayList<>();
                    while (true) {
                        System.out.print("Ingrese ID del Producto (o 'listar' para ver productos, o 'salir' para finalizar): ");
                        String input = scanner.nextLine();
                        if (input.equalsIgnoreCase("salir")) {
                            break;
                        } else if (input.equalsIgnoreCase("listar")) {
                            Utils.mostrarListaProductos();
                            continue;
                        }
                        int idProducto;
                        try {
                            idProducto = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.println("ID de producto inválido. Por favor, ingrese un número.");
                            continue;
                        }

                        Producto producto = productoController.buscarProducto(idProducto);
                        if (producto == null) {
                            System.out.println("Producto no encontrado.");
                            continue;
                        }

                        int cantidad = Utils.obtenerEnteroValido(scanner, "Ingrese Cantidad: ");

                        double precioUnitario = producto.getPrecioUnitario();
                        double precioTotal = precioUnitario * cantidad;
                        detalleItems.add(new DetalleItem(0, pedido.getDetalle().getIdDetallePedido(), idProducto, cantidad, precioUnitario, precioTotal));
                    }
                    DetallePedido detallePedido = new DetallePedido(pedido.getDetalle().getIdDetallePedido(), idPedido, detalleItems);
                    pedido.setDetalle(detallePedido);
                    pedido.setPrecioTotalPedido(pedidoController.calcularTotal(pedido));
                    pedidoController.actualizarProductosPedido(pedido);
                    System.out.println("Productos del pedido actualizados.\n");
                    break;
                default:
                    System.out.println("Opción no válida.\n");
            }
        }
    }

    //Elimina un pedido existente, partiendo del ID.
    public void eliminarPedido() {
        System.out.println("---------- Eliminar Pedido ----------");

        int idPedido = Utils.obtenerEnteroValido(scanner, "Ingrese ID del Pedido a eliminar: ");
        Pedido pedido = pedidoController.obtenerPedidoPorId(idPedido);

        if (pedido == null) {
            System.out.println("No existe un pedido con el ID indicado.\n");
        } else {
            if (Utils.confirmar(scanner, "¿Está seguro de que desea eliminar el pedido? (s/n): ")) {
                pedidoController.eliminarPedido(idPedido);
                System.out.println("Pedido eliminado con éxito.\n");
            } else {
                System.out.println("Operación cancelada.\n");
            }
        }
    }

}