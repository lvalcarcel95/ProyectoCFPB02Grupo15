package app;

import utils.Constants;
import export.CsvSalesExporter;
import export.ISalesExporter;
import export.SerializedSalesExporter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GenerateInfoFiles {

    private static final Random random = new Random();

    private static final String[] vendorNames = {
            "Maria Perez", "Juan Martinez", "Ana Gomez", "Luis Rodriguez",
            "Carmen Sanchez", "Pedro Garcia", "Laura Díaz", "Jorge Hernandez",
            "Sofía Lopez", "Miguel Torres"
    };

    private static final String[] productNames = {
            "Celular", "Tablet", "Computador", "Monitor", "Impresora",
            "Teclado", "Mouse", "Audifonos", "Camara", "Router"
    };

    private static final Map<Long, String> vendedoresMap = new HashMap<>();
    private static final Set<String> productosValidos = new HashSet<>();

    public static void main(String[] args) {
        try {
            createProductsFile(10);
            createSalesManInfoFile(10);

            cargarVendedores();
            cargarProductos();

            for (long id : vendedoresMap.keySet()) {
                // Exportar en CSV
                exportarVentas(new CsvSalesExporter(), 5 + random.nextInt(5), id);
                // Exportar en .ser
                exportarVentas(new SerializedSalesExporter(), 5 + random.nextInt(5), id);
            }

            System.out.println("Archivos de prueba generados exitosamente");
        } catch (Exception e) {
            System.err.println("Error al generar archivos de prueba, detalle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createSalesManInfoFile(int quantitySellers) throws IOException {
        Path filePath = Constants.DATA_FOLDER.resolve("vendedores.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("TipoDocumento;NumeroDocumento;Nombres;Apellidos\n");
            Set<Long> ids = new HashSet<>();
            for (int i = 0; i < quantitySellers; i++) {
                long id;
                do { id = 100000 + random.nextInt(900000); }
                while (!ids.add(id));
                String[] name = vendorNames[i % vendorNames.length].split(" ");
                writer.write(String.format("%s;%d;%s;%s\n",
                        Constants.DOCUMENT_TYPE, id, name[0], name[1]));
            }
            System.out.println("Archivo vendedores generado exitosamente");
        }
    }

    public static void createProductsFile(int quantityProducts) throws IOException {
        Path filePath = Constants.DATA_FOLDER.resolve("productos.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("ProductoId;Nombre;Precio\n");
            Set<String> ids = new HashSet<>();
            for (int i = 0; i < quantityProducts; i++) {
                String pid;
                do { pid = "P" + (100 + random.nextInt(900)); }
                while (!ids.add(pid));
                int price = 10000 + random.nextInt(990000);
                writer.write(String.format("%s;%s;%d\n",
                        pid, productNames[i % productNames.length], price));
            }
            System.out.println("Archivo productos generado exitosamente");
        }
    }

    /** Método único para exportar ventas con cualquier estrategia */
    public static void exportarVentas(ISalesExporter exporter, int cantidad, long vendedorId)
            throws IOException {
        if (!vendedoresMap.containsKey(vendedorId)) {
            System.err.println("Vendedor no registrado: " + vendedorId);
            return;
        }
        List<String> ventas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            String pid;
            do { pid = "P" + (100 + random.nextInt(900)); }
            while (!productosValidos.contains(pid));
            int qty = 1 + random.nextInt(20);
            ventas.add(pid + ";" + qty);
        }
        exporter.export(vendedorId, ventas);
    }

    public static void cargarVendedores() throws IOException {
        Path path = Constants.DATA_FOLDER.resolve("vendedores.csv");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.readLine(); // salto header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(";");
                long id = Long.parseLong(p[1]);
                vendedoresMap.put(id, p[2] + " " + p[3]);
            }
        }
    }

    public static void cargarProductos() throws IOException {
        Path path = Constants.DATA_FOLDER.resolve("productos.csv");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                productosValidos.add(line.split(";")[0]);
            }
        }
    }

    public static Map<Long, String> getVendedoresMap() {
        return Collections.unmodifiableMap(vendedoresMap);
    }
}
