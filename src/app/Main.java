// src/app/Main.java
package app;

import app.GenerateInfoFiles;
import export.CsvSalesExporter;
import export.ISalesExporter;
import export.SerializedSalesExporter;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        try {
            ejecutarMenuGeneracionArchivosPrueba();
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

        // Procesamiento y visualización
        //GenerateInfoFiles.procesarArchivosVentas();
        //GenerateInfoFiles.procesarArchivosVentasSerializados();
        //GenerateInfoFiles.visualizarArchivosSerializados();

        System.out.println("Archivos de prueba generados y validados correctamente");
    }
}
