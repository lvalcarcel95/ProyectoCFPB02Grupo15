package export;

import utils.Constants;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvSalesExporter implements ISalesExporter {
    @Override
    public void export(long vendedorId, List<String> ventas) throws IOException {
        Path filePath = Constants.DATA_FOLDER.resolve("ventas_" + vendedorId + ".csv");
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("ProductoId;Cantidad\n");
            for (String venta : ventas) {
                writer.write(venta + "\n");
            }
        }
        System.out.println("Archivo CSV generado para el vendedor " + vendedorId);
    }
}