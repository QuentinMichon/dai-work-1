package ch.heigvd.byteConverter;

import ch.heigvd.image.Pixel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public abstract class ByteConverter {
    public static int getInt(BufferedInputStream bis) throws IOException {
        byte[] bytes = new byte[4];

        int bytesRead = bis.read(bytes);
        if (bytesRead < 4) {
            throw new IOException("Impossible de lire 4 octets");
        }

        // Assembler les octets en little endian
        int value = ((bytes[3] & 0xFF) << 24) |
                ((bytes[2] & 0xFF) << 16) |
                ((bytes[1] & 0xFF) << 8)  |
                (bytes[0] & 0xFF);

        //System.out.println("Valeur lue (little endian) : " + value);
        return value;
    }

    public static char getChar(BufferedInputStream bis) throws IOException {
        byte[] bytes = new byte[2];

        int bytesRead = bis.read(bytes);
        if (bytesRead < 2) {
            throw new IOException("Impossible de lire 2 octets");
        }

        // Assembler les octets en little endian
        char value = (char) (((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF));

        //System.out.println("Valeur lue (little endian) : " + (int) value);
        return value;
    }

    public static Pixel getPixel(BufferedInputStream bis) throws IOException {
        byte[] bytes = new byte[3];

        int bytesRead = bis.read(bytes);
        if (bytesRead < 3) {
            throw new IOException("Impossible de lire 3 octets");
        }

        // Assembler les octets en little endian
        Pixel pixel = new Pixel(bytes[2], bytes[1], bytes[0]);
        //System.out.println("R:" + (bytes[2] & 0xFF) + " G:" + (bytes[1] & 0xFF) + " B:" + (bytes[0] & 0xFF));
        return pixel;
    }

    // use to pass [nb] of bite
    public static void pass(BufferedInputStream bis, int nb) throws IOException {
        for (int i = 0; i < nb; i++) {
            bis.read();
        }
    }

    public static void writeInt(BufferedOutputStream bos, int value) throws IOException {
        // Extraire les 4 octets du int
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value & 0xFF);        // L'octet le moins significatif
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        bytes[2] = (byte) ((value >> 16) & 0xFF);
        bytes[3] = (byte) ((value >> 24) & 0xFF);  // L'octet le plus significatif

        // Écrire les octets dans l'ordre little-endian
        bos.write(bytes);
    }

    public static void writeChar(BufferedOutputStream bos, char value) throws IOException {
        // Extraire les 2 octets du char
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (value & 0xFF);        // L'octet le moins significatif
        bytes[1] = (byte) ((value >> 8) & 0xFF); // L'octet le plus significatif

        // Écrire les octets dans l'ordre little-endian
        bos.write(bytes);
    }

    public static void writePixel(BufferedOutputStream bos, Pixel pixel) throws IOException {
        bos.write(pixel.getRGB());
    }

    public static void writePadding(BufferedOutputStream bos, int padding) throws IOException {
        for (int _p = 0; _p < padding; _p++) {
            bos.write(0x00);
        }
    }
}
