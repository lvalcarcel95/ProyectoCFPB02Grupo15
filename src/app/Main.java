// src/app/Main.java
package app;

import app.GenerateInfoFiles;
import export.CsvSalesExporter;
import export.ISalesExporter;
import export.SerializedSalesExporter;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.HashMap;
import java.io.BufferedReader;
import java.util.List;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import utils.Constants;

public class Main {

    public static void main(String[] args) {
        try {

            ejecutarMenuGeneracionArchivosPrueba();
            procesarArchivosVentas();
            procesarArchivosVentasSerializados();


            System.out.println("Procesamiento finalizado exitosamente");
        } catch (Exception e) {
            System.err.println("Error inesperado, detalle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void ejecutarMenuGeneracionArchivosPrueba() throws IOException, ClassNotFoundException {
        // Generación de vendedores y productos
        GenerateInfoFiles.createProductsFile(5);
        GenerateInfoFiles.createSalesManInfoFile(5);

        // Carga de datos en memoria
        GenerateInfoFiles.cargarVendedores();
        GenerateInfoFiles.cargarProductos();

        // Estrategias de exportación
        ISalesExporter csvExporter = new CsvSalesExporter();
        ISalesExporter serExporter = new SerializedSalesExporter();

        Random rnd = new Random();
        for (Map.Entry<Long, String> entry : GenerateInfoFiles.getVendedoresMap().entrySet()) {
            long id = entry.getKey();

            // Exportar 3 ventas aleatorias en CSV y .ser
            GenerateInfoFiles.exportarVentas(csvExporter, 3, id);
            GenerateInfoFiles.exportarVentas(serExporter, 3, id);
        }
        System.out.println("Archivos de prueba generados y validados correctamente");
    }


    public static void procesarArchivosVentas() throws IOException {
        Path folder = Constants.DATA_FOLDER;
        Map<Long, Integer> ventasPorVendedor = new HashMap<>();

        try (var files = Files.list(folder)) {
            files.filter(path -> path.getFileName().toString().startsWith("ventas_") && path.getFileName().toString().endsWith(".csv"))
                    .forEach(path -> {
                        try (BufferedReader reader = Files.newBufferedReader(path)) {
                            String line;
                            int total = 0;
                            boolean isFirstLine = true;

                            while ((line = reader.readLine()) != null) {

                                if (isFirstLine) {
                                    isFirstLine = false; // saltamos la cabecera
                                    continue;
                                }
                                String[] parts = line.split(";");
                                if (parts.length == 2) {
                                    int cantidad = Integer.parseInt(parts[1]);
                                    total += cantidad;
                                }
                            }
                            // Obtener el ID del nombre del archivo, por ejemplo ventas_123456.csv
                            String fileName = path.getFileName().toString();
                            long vendedorId = Long.parseLong(fileName.substring(7, fileName.indexOf(".csv")));
                            ventasPorVendedor.put(vendedorId, total);
                        } catch (IOException e) {
                            System.err.println("Error leyendo archivo " + path.getFileName() + ": " + e.getMessage());
                        }
                    });
        }

        // Mostrar resumen
        System.out.println("\nResumen de ventas por vendedor:");
        ventasPorVendedor.forEach((id, totalVentas) -> {
            String nombre = GenerateInfoFiles.getVendedoresMap().getOrDefault(id, "Desconocido");
            System.out.println("Vendedor " + nombre + " (" + id + "): " + totalVentas + " productos vendidos");
        });
    }


    private static void procesarArchivosVentasSerializados() throws IOException, ClassNotFoundException {
        Path folder = Constants.DATA_FOLDER;
        Map<String, Integer> unidadesPorProducto = new HashMap<>();

        try (var files = Files.list(folder)) {
            files.filter(path -> path.getFileName().toString().startsWith("ventas_") && path.getFileName().toString().endsWith(".ser"))
                    .forEach(path -> {
                        try (var ois = new java.io.ObjectInputStream(Files.newInputStream(path))) {
                            List<String> ventas = (List<String>) ois.readObject();
                            for (String venta : ventas) {
                                String[] parts = venta.split(";");
                                if (parts.length == 2) {
                                    String productoId = parts[0];
                                    int cantidad = Integer.parseInt(parts[1]);
                                    unidadesPorProducto.merge(productoId, cantidad, Integer::sum);
                                }
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            System.err.println("Error leyendo archivo serializado " + path.getFileName() + ": " + e.getMessage());
                        }
                    });
        }

        // Mostrar resumen
        System.out.println("\nResumen de unidades vendidas por producto:");
        unidadesPorProducto.forEach((productoId, unidades) -> {
            System.out.println("Producto " + productoId + ": " + unidades + " unidades vendidas");
        });
    }

}
