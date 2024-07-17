package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class MT940FileGenerator {

    public static void main(String[] args) {
        // Ruta del primer archivo plano a generar
        String txtFilePath = "MT940Statement.txt";
        // Ruta del segundo archivo MT940 a generar
        String mt940FilePath = "MT940Statement.mt940";

        // Generar el primer archivo plano
        generateMT940File(txtFilePath);

        // Generar el segundo archivo MT940
        generateSecondMT940File(mt940FilePath);

        // Convertir el primer archivo a Base64 y guardarlo en una cadena
        String base64String = convertFileToBase64(txtFilePath);

        if (base64String != null) {
            System.out.println("Contenido del archivo en Base64:");
            System.out.println(base64String);
        }

        // Convertir la cadena Base64 a InputStream
        InputStream inputStream = convertBase64ToInputStream(base64String);

        // Leer y mostrar el contenido del InputStream
        if (inputStream != null) {
            System.out.println("Contenido del InputStream:");
            readFromInputStream(inputStream);
        }
    }

    public static void generateMT940File(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Escribir contenido en el archivo
            writer.write(":20:TRANSACTION_REF\n");
            writer.write("1234567890\n\n");

            writer.write(":25:ACCOUNT_NUMBER\n");
            writer.write("ES7620770024003102575766\n\n");

            writer.write(":28C:00001/001\n\n");

            writer.write(":60F:C100320EUR1234,56\n\n");

            writer.write(":61:1003210321C005000,NTRFNONREF//123456789\n");
            writer.write("Sender Name\n");
            writer.write("/123-456-789\n");
            writer.write("Description of the transaction\n\n");

            writer.write(":62F:C100321EUR1734,56\n\n");

            System.out.println("Archivo plano generado exitosamente en " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateSecondMT940File(String filePath) {
        String mt940Content = "{1:F01GEROCOBBXXXX0000000000}{2:I940APTGFR22XXXXN}{4:\n" +
                ":20:01471\n" +
                ":25:123456789\n" +
                ":28C:01471/00001\n" +
                ":60F:C240308COP160726989,20\n" +
                ":61:240308D128,00NGMFNONREF//0056\n" +
                "IMPUESTO DECRETO\n" +
                ":86:IMPUESTO DECRETO,CORPORATIVA CALI\n" +
                ":61:240308D32000,00FCOMNONREF//0955\n" +
                "COMISION POR DOMICILIACIO\n" +
                ":86:COMISION POR DOMICILIACIO,CORPORATIVA CALI\n" +
                ":61:240308D24,00NGMFNONREF//0056\n" +
                "IMPUESTO DECRETO\n" +
                ":86:IMPUESTO DECRETO,CORPORATIVA CALI\n" +
                ":61:240308D6080,00NTAXNONREF//0956\n" +
                "IVA POR COMISION POR DOMI\n" +
                ":86:IVA POR COMISION POR DOMI,CORPORATIVA CALI\n" +
                ":61:240308D31428,00NGMFNONREF//0056\n" +
                "IMPUESTO DECRETO\n" +
                ":86:IMPUESTO DECRETO,CORPORATIVA CALI\n" +
                ":61:240308D7857027,00NTRFNONREF//0838\n" +
                "CARGO DOMICILIACION\n" +
                ":86:/IDBENE/NONREF\n" +
                "/NOMBENE/Isabel Gomez\n" +
                "/CONCEPTO1/380000100017901000153485\n" +
                ":61:240308D29652,00NGMFNONREF//0056\n" +
                "IMPUESTO DECRETO\n" +
                ":86:IMPUESTO DECRETO,CORPORATIVA CALI\n" +
                ":61:240308D7412880,00NTRFNONREF//0838\n" +
                "CARGO DOMICILIACION\n" +
                ":86:/IDBENE/NONREF\n" +
                "/NOMBENE/Diego Alvarez\n" +
                "/CONCEPTO1/380000240012601000153486\n" +
                ":61:240308D13785,00NGMFNONREF//0056\n" +
                "IMPUESTO DECRETO\n" +
                ":86:IMPUESTO DECRETO,CORPORATIVA CALI\n" +
                ":62M:C240308COP145343985,20\n" +
                "-}";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(mt940Content);
            System.out.println("Archivo MT940 generado exitosamente en " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String convertFileToBase64(String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] fileBytes = Files.readAllBytes(path);
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static InputStream convertBase64ToInputStream(String base64String) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);
            return new ByteArrayInputStream(decodedBytes);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void readFromInputStream(InputStream inputStream) {
        try {
            int data;
            while ((data = inputStream.read()) != -1) {
                System.out.print((char) data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
