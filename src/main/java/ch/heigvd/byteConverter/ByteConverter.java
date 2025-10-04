/**
 * @file : ByteConverter.java
 * @author : Quentin Michon, Gianni BEE
 * @date : 2025-09-26
 * @since : 1.0
 *
 * Description:
 *   Utilitaire pour la lecture et l’écriture de données binaires
 *   en encodage Little-endian.
 */

package ch.heigvd.byteConverter;

import ch.heigvd.image.Pixel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Classe abstraite fournissant des méthodes utilitaires pour manipuler des flux binaires (bitstreams).
 * Elle permet :
 *  - de lire plusieurs octets à la suite et de les convertir automatiquement dans le type numérique approprié ;
 *  - de prendre une valeur typée (int, char, Pixel, etc.) et de l’écrire octet par octet dans un fichier.
 *
 * Toutes les opérations sont effectuées en encodage Little-endian.
 */
public abstract class ByteConverter {
    /**
     * Lit 4 octets depuis le flux et les convertit en entier signé (little-endian).
     *
     * @param bis flux binaire en entrée
     * @return l’entier signé représenté par les 4 octets lus
     * @throws IOException si les 4 octets n’ont pas pu être lus
     */
    public static int getInt(BufferedInputStream bis) throws IOException {
        byte[] bytes = new byte[4];

        int bytesRead = bis.read(bytes);
        if (bytesRead < 4) {
            throw new IOException("Impossible de lire 4 octets");
        }
        return ((bytes[3] & 0xFF) << 24) |
                ((bytes[2] & 0xFF) << 16) |
                ((bytes[1] & 0xFF) << 8)  |
                (bytes[0] & 0xFF);
    }

    /**
     * Lit 2 octets depuis le flux et les convertit en caractère (little-endian).
     *
     * @param bis flux binaire en entrée
     * @return le caractère représenté par les 2 octets lus
     * @throws IOException si les 2 octets n’ont pas pu être lus
     */
    public static char getChar(BufferedInputStream bis) throws IOException {
        byte[] bytes = new byte[2];

        int bytesRead = bis.read(bytes);
        if (bytesRead < 2) {
            throw new IOException("Impossible de lire 2 octets");
        }
        return (char) (((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF));
    }

    /**
     * Lit 3 octets depuis le flux et les convertit en un pixel (RGB).
     *
     * @param bis flux binaire en entrée
     * @return un objet Pixel construit à partir des 3 octets lus
     * @throws IOException si les 3 octets n’ont pas pu être lus
     */
    public static Pixel getPixel(BufferedInputStream bis) throws IOException {
        byte[] bytes = new byte[3];

        int bytesRead = bis.read(bytes);
        if (bytesRead < 3) {
            throw new IOException("Impossible de lire 3 octets");
        }
        return new Pixel(bytes[2], bytes[1], bytes[0]);
    }

    /**
     * Ignore un certain nombre d’octets dans le flux.
     *
     * @param bis flux binaire en entrée
     * @param nb  nombre d’octets à passer
     * @throws IOException si les octets n’ont pas pu être ignorés
     */
    public static void pass(BufferedInputStream bis, int nb) throws IOException {
        for (int i = 0; i < nb; i++) {
            bis.read();
        }
    }

    /**
     * Écrit un entier signé sur 4 octets dans le flux (little-endian).
     *
     * @param bos   flux binaire en sortie
     * @param value entier à écrire
     * @throws IOException si l’écriture échoue
     */
    public static void writeInt(BufferedOutputStream bos, int value) throws IOException {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        bytes[2] = (byte) ((value >> 16) & 0xFF);
        bytes[3] = (byte) ((value >> 24) & 0xFF);

        bos.write(bytes);
    }

    /**
     * Écrit un caractère sur 2 octets dans le flux (little-endian).
     *
     * @param bos   flux binaire en sortie
     * @param value caractère à écrire
     * @throws IOException si l’écriture échoue
     */
    public static void writeChar(BufferedOutputStream bos, char value) throws IOException {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value >> 8) & 0xFF);

        bos.write(bytes);
    }

    /**
     * Écrit un pixel (3 octets RGB) dans le flux.
     *
     * @param bos   flux binaire en sortie
     * @param pixel pixel à écrire
     * @throws IOException si l’écriture échoue
     */
    public static void writePixel(BufferedOutputStream bos, Pixel pixel) throws IOException {
        bos.write(pixel.getRGB());
    }

    /**
     * Écrit une séquence d’octets de remplissage (padding).
     *
     * @param bos     flux binaire en sortie
     * @param padding nombre d’octets à écrire (tous à 0x00)
     * @throws IOException si l’écriture échoue
     */
    public static void writePadding(BufferedOutputStream bos, int padding) throws IOException {
        for (int _p = 0; _p < padding; _p++) {
            bos.write(0x00);
        }
    }
}