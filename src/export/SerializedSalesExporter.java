package export;

import utils.Constants;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SerializedSalesExporter implements ISalesExporter {
    @Override
    public void export(long vendedorId, List<String> ventas) throws IOException {
        Path filePath = Constants.DATA_FOLDER.resolve("ventas_" + vendedorId + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            List<String> fullList = new ArrayList<>();
            fullList.add("CC;" + vendedorId);
            fullList.addAll(ventas);
            oos.writeObject(fullList);
        }
        System.out.println("Archivo serializado generado para el vendedor " + vendedorId);
    }
}
