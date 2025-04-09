package export;

import java.io.IOException;
import java.util.List;

public interface ISalesExporter {
    void export(long vendedorId, List<String> ventas) throws IOException;
}
