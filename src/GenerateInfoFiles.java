import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GenerateInfoFiles {
    private static final String DATA_FOLDER = "datos/";
    private static final Random random = new Random();

    // Arreglo de nombres realistas para vendedores.
    private static final String[] vendorNames = {
            "María Pérez", "Juan Martínez", "Ana Gómez", "Luis Rodríguez",
            "Carmen Sánchez", "Pedro García", "Laura Díaz", "Jorge Hernández",
            "Sofía López", "Miguel Torres"
    };


    // Método para generar el archivo de vendedores.
    public void createSalesMenFile(int cantidadVendedores) {

        String filePath = DATA_FOLDER + "vendedores.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ID;Nombre\n");
            Set<Long> salesmenIds = new HashSet<>();

            for (int i = 0; i < cantidadVendedores; i++) {
                long id;
                do {
                    id = 100000 + random.nextInt(900000);
                } while (salesmenIds.contains(id));
                salesmenIds.add(id);

                // Selecciona un nombre realista del arreglo (si hay más vendedores, se cicla a través de los nombres)
                String nombre = vendorNames[i % vendorNames.length];
                writer.write(id + ";" + nombre + "\n");
            }
            System.out.println("Archivo vendedores.csv generado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo vendedores.csv: " + e.getMessage());
        }
    }


    // Método para generar el archivo de productos
    public void createProductsFile(int cantidadProductos) {

        String filePath = DATA_FOLDER + "productos.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ID;Nombre;Precio\n");
            Set<String> products = new HashSet<>();

            for (int i = 0; i < cantidadProductos; i++) {
                String idProduct;
                do {
                    idProduct = "P" + (100 + random.nextInt(900));
                } while (products.contains(idProduct));
                products.add(idProduct);

                // Se asegura que el precio sea positivo
                int precio = 10000 + random.nextInt(990000);
                writer.write(idProduct + ";Producto " + (i + 1) + ";" + precio + "\n");
            }
            System.out.println("Archivo productos.csv generado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo productos.csv: " + e.getMessage());
        }
    }

    // Método para generar el archivo de ventas
    public void createSalesMenFile(int ventasAleatorias, String nombre, long id) {

        String filePath = DATA_FOLDER + "ventas_" + id + ".csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("IDProducto;Cantidad\n");

            for (int i = 0; i < ventasAleatorias; i++) {
                String idProducto = "P" + (100 + random.nextInt(900));

                // Se asegura que la cantidad sea positiva
                int cantidad = 1 + random.nextInt(50);
                writer.write(idProducto + ";" + cantidad + "\n");
            }

            System.out.println("Archivo ventas_" + id + ".csv generado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo ventas_" + id + ".csv: " + e.getMessage());
        }
    }
}
