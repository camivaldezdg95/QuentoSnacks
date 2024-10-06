package utils;

import java.util.Scanner;


/*
 La clase Utils proporciona métodos de utilidad para la validación de entradas y otras operaciones comunes.
 Incluye métodos para validar que las entradas sean enteros válidos y cadenas no vacías,
 así como para mostrar listas de productos.
 */

public class Utils {

    /**
     * Obtiene un entero válido del usuario, asegurando que la entrada sea un número entero.
     *
     * @param scanner El objeto Scanner para leer la entrada del usuario.
     * @param mensaje El mensaje a mostrar al usuario.
     * @return El número entero válido ingresado por el usuario.
     */
    public static int obtenerEnteroValido(Scanner scanner, String mensaje) {
        int numero = -1;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            try {
                numero = Integer.parseInt(entrada);
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
            }
        }
        return numero;
    }


    /**
     * Obtiene una cadena no vacía del usuario, asegurando que la entrada no sea una cadena vacía.
     *
     * @param scanner El objeto Scanner para leer la entrada del usuario.
     * @param mensaje El mensaje a mostrar al usuario.
     * @return La cadena no vacía ingresada por el usuario.
     */
    public static String obtenerStringNoVacio(Scanner scanner, String mensaje) {
        String entrada = "";
        while (entrada.isEmpty()) {
            System.out.print(mensaje);
            entrada = scanner.nextLine();
            if (entrada.isEmpty()) {
                System.out.println("La entrada no puede estar vacía. Por favor, ingrese un valor válido.");
            }
        }
        return entrada;
    }


    /**
     * Confirma una acción con el usuario, solicitando una respuesta 's' o 'n'.
     *
     * @param scanner El objeto Scanner para leer la entrada del usuario.
     * @param mensaje El mensaje a mostrar al usuario.
     * @return true si el usuario ingresa 's', false en caso contrario.
     */
    public static boolean confirmar(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        String input = scanner.next();
        scanner.nextLine(); // Consumir la nueva línea
        return input.equalsIgnoreCase("s");
    }


    /**
     * Muestra una lista de productos con sus IDs y precios.
     * Esta es una lista fija y no extrae datos de la base de datos.
     */
    public static void mostrarListaProductos() {
        System.out.println("--- Productos con Stock ---");
        System.out.println("1012 - Papas fritas clásicas - $700");
        System.out.println("1316 - Papas fritas sabor ketchup - $800");
        System.out.println("1326 - Papas fritas sabor jamón - $900");
        System.out.println("1357 - Papas fritas sabor limón - $800");
        System.out.println("3108 - Mega queso - $800");
        System.out.println("6002 - Quento mix - $800");
        System.out.println("9121 - Nachos sabor choclo y manteca - $600");
        System.out.println("9130 - Nachos picantes sabor jalapeño - $1000");
    }
}


