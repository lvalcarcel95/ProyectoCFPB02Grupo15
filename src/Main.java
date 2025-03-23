import java.util.Random;

public class Main {
    // Arreglo de nombres realistas para vendedores.
    private static final String[] vendorNames = {
            "María Pérez", "Juan Martínez", "Ana Gómez", "Luis Rodríguez",
            "Carmen Sánchez", "Pedro García", "Laura Díaz", "Jorge Hernández",
            "Sofía López", "Miguel Torres"
    };

    public static void main(String[] args) {
        System.out.println("Hello and welcome!");

        GenerateInfoFiles generator = new GenerateInfoFiles();
        Random random = new Random();

        // Genera un archivo de ventas para un vendedor específico.
        int ventasAleatorias = random.nextInt(100) + 1;
        String nombre = namesGenerator(random);

        // Genera un número aleatorio entre min (inclusive) y max (inclusive).
        long min = 1_000_000_000L;
        long max = 9_999_999_999L;
        long id = min + (long)(random.nextDouble() * (max - min + 1));
        generator.createSalesMenFile(ventasAleatorias, nombre, id);


        // Genera el archivo productos.csv con una cantidad aleatoria de productos.
        int cantidadProductos = random.nextInt(100) + 1;
        generator.createProductsFile(cantidadProductos);


        // Genera el archivo vendedores.csv con una cantidad aleatoria de vendedores.
        int cantidadVendedores = random.nextInt(100) + 1;
        generator.createSalesMenFile(cantidadVendedores);

        System.out.println("Proceso finalizado");
    }

    // Selecciona un nombre aleatorio del arreglo vendorNames.
    public static String namesGenerator(Random random) {
        int index = random.nextInt(vendorNames.length);
        return vendorNames[index];
    }

}
