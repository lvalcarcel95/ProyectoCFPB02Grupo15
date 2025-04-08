import java.io.IOException;
import java.util.Map;
import java.util.Scanner;


/**
 * Clase principal encargada de ejecutar la lógica del programa:
 * 1. Generar archivos de prueba.
 * 2. Procesar y validar archivos de ventas (CSV y serializados).
 **/
public class Main {

    /**
     * Método principal de ejecución.
     * ejecuta los métodos de generación de datos de prueba de la clase GenerateInfoFiles.
     * @param args no se utilizan argumentos desde consola.
     **/
    public static void main(String[] args) {
        try {
            ejecutarMenuGeneracionArchivosPrueba();
            System.out.println("Procesamiento finalizado exitosamente");

        } catch (Exception e) {
            System.err.println("Error inesperado, detalle: " + e.getMessage());
        }
    }

    /**
     * Ejecuta la generación de archivos de prueba pseudoaleatorios.
     * Este método llama a los métodos definidos en la clase GenerateInfoFiles.
     **/
    private static void ejecutarMenuGeneracionArchivosPrueba() {

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("****************************************************");
            System.out.println("************ Menu creación de archivos *************");
            System.out.println("****************************************************");
            System.out.println("**** 1 - Crear archivo de productos              ****");
            System.out.println("**** 2 - Crear archivo de información vendedores ****");
            System.out.println("**** 3 - Crear archivos de ventas                ****");
            System.out.println("**** 4 - Procesar y visualizar archivos          ****");
            System.out.println("****************************************************");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    GenerateInfoFiles.createProductsFile(5);
                    break;
                case 2:
                    GenerateInfoFiles.createSalesManInfoFile(5);
                    break;
                case 3:
                    GenerateInfoFiles.cargarVendedores();
                    GenerateInfoFiles.cargarProductos();
                    for (Map.Entry<Long, String> vendedor : GenerateInfoFiles.getVendedoresMap().entrySet()) {
                        long id = vendedor.getKey();
                        GenerateInfoFiles.createSalesMenFile(3, id);
                        GenerateInfoFiles.createSerializedSalesFile(3, id);
                    }
                    break;
                case 4:
                    GenerateInfoFiles.cargarVendedores();
                    GenerateInfoFiles.cargarProductos();
                    GenerateInfoFiles.procesarArchivosVentas();
                    GenerateInfoFiles.procesarArchivosVentasSerializados();
                    GenerateInfoFiles.visualizarArchivosSerializados();
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error generando o procesando archivos de prueba, detalle: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }

    }
}

