package ch.heigvd.image;

import ch.heigvd.AvailableRotation;
import ch.heigvd.byteConverter.ByteConverter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Représente une image au format BMP (24 bits, non compressée).
 * Cette classe lit et écrit l’en-tête BMP ainsi que la matrice de pixels associée,
 * et fournit quelques transformations (niveaux de gris, flou gaussien, rotations).
 *
 * <p>Convention d’octets : toutes les valeurs sont lues/écrites en Little-endian.</p>
 */
public class BMP {
    //---------------------
    // Champs d’en-tête BMP
    //---------------------
    private final char bhType;                // Signature du fichier ("BM" = 0x4D42)
    private int bhSize;                       // Taille totale du fichier en octets
    private final int bhReserved;             // Réservé (0)
    private final int bhOffBits;              // Offset des données d'image (54 pour BMP24)
    private final int biSize;                 // Taille de l'entête DIB (40 pour BITMAPINFOHEADER)
    private int biWidth;                      // Largeur en pixels
    private int biHeight;                     // Hauteur en pixels
    private final char biPlanes;              // Nombre de plans (1)
    private final char biBitCount;            // Bits par pixel (24)
    private final int biCompression;          // Type de compression (0 = BI_RGB, non compressé)
    private int biSizeImage;                  // Taille des données d'image (avec padding)
    private final int biXPelsPerMeter;        // Résolution horizontale (px/m)
    private final int biYPelsPerMeter;        // Résolution verticale (px/m)
    private final int biClrUsed;              // Couleurs dans la palette (0 si non applicable)
    private final int biClrImportant;         // Couleurs importantes (0 si toutes)
    private PixelGrid pixelGrid;

    //---------------------
    // Valeurs dérivées (alignement des lignes)
    //---------------------
    private int metaByteSizeRow;              // Taille d'une ligne en octets (multiples de 4)
    private int metaPadding;                  // Octets de remplissage ajoutés par ligne

    /**
     * Construit une image BMP en la lisant depuis un flux binaire.
     * Lit successivement l’en-tête BMP, l’en-tête DIB, puis tous les pixels
     * en respectant le padding de fin de ligne.
     *
     * Le flux d’entrée est fermé à la fin.
     *
     * @param bis flux binaire tamponné en entrée (positionné au début du fichier BMP)
     * @throws IOException si une lecture échoue ou si les octets attendus ne peuvent pas être lus
     */
    public BMP(BufferedInputStream bis) throws IOException {
        bhType = ByteConverter.getChar(bis);
        bhSize = ByteConverter.getInt(bis);
        bhReserved = ByteConverter.getInt(bis);
        bhOffBits = ByteConverter.getInt(bis);
        biSize = ByteConverter.getInt(bis);
        biWidth = ByteConverter.getInt(bis);
        biHeight = ByteConverter.getInt(bis);
        biPlanes = ByteConverter.getChar(bis);
        biBitCount = ByteConverter.getChar(bis);
        biCompression = ByteConverter.getInt(bis);
        biSizeImage = ByteConverter.getInt(bis);
        biXPelsPerMeter = ByteConverter.getInt(bis);
        biYPelsPerMeter = ByteConverter.getInt(bis);
        biClrUsed = ByteConverter.getInt(bis);
        biClrImportant = ByteConverter.getInt(bis);

        // Calcul du padding pour des lignes multiples de 4 octets
        this.metaByteSizeRow = (this.biWidth * 3 + 3) / 4 * 4;
        this.metaPadding = this.metaByteSizeRow - this.biWidth * 3;

        // Lecture des pixels (ligne par ligne)
        pixelGrid = new PixelGrid(biHeight, biWidth);
        for (int _h = 0; _h < this.biHeight; ++_h) {
            for (int _w = 0; _w < this.biWidth; ++_w) {
                this.pixelGrid.setPixel(_h, _w, ByteConverter.getPixel(bis));
            }
            ByteConverter.pass(bis, metaPadding);
        }

        bis.close();
    }

    /**
     * Écrit l’image BMP (en-têtes + pixels + padding) dans un flux de sortie.
     *
     * Le flux de sortie est flush puis fermé.</p>
     *
     * @param bos flux binaire tamponné en sortie (ouvert en écriture)
     * @throws IOException si l’écriture échoue
     */
    public void writeBMP(BufferedOutputStream bos) throws IOException {
        ByteConverter.writeChar(bos, bhType);
        ByteConverter.writeInt(bos, bhSize);
        ByteConverter.writeInt(bos, bhReserved);
        ByteConverter.writeInt(bos, bhOffBits);
        ByteConverter.writeInt(bos, biSize);
        ByteConverter.writeInt(bos, biWidth);
        ByteConverter.writeInt(bos, biHeight);
        ByteConverter.writeChar(bos, biPlanes);
        ByteConverter.writeChar(bos, biBitCount);
        ByteConverter.writeInt(bos, biCompression);
        ByteConverter.writeInt(bos, biSizeImage);
        ByteConverter.writeInt(bos, biXPelsPerMeter);
        ByteConverter.writeInt(bos, biYPelsPerMeter);
        ByteConverter.writeInt(bos, biClrUsed);
        ByteConverter.writeInt(bos, biClrImportant);

        for (int _h = 0; _h < this.biHeight; ++_h) {
            for (int _w = 0; _w < this.biWidth; ++_w) {
                ByteConverter.writePixel(bos, pixelGrid.getPixel(_h, _w));
            }
            ByteConverter.writePadding(bos, metaPadding);
        }

        bos.flush();
        bos.close();
    }

    /**
     * Applique un filtre niveaux de gris (noir et blanc) sur l’image.
     * Remplace la grille de pixels par sa version convertie.
     */
    public void appliesBlackWhiteFilter() {
        this.pixelGrid = pixelGrid.appliesBlackWhiteFilter();
    }

    /**
     * Applique un filtre gaussien (flou) à l’image.
     * Le filtre est appliqué 50 fois de suite pour renforcer l’effet.
     */
    public void appliesGaussianFilter() {
        for (int i = 0; i < 50; i++) {
            this.pixelGrid = pixelGrid.appliesGaussianFilter();
        }
    }

    /**
     * Applique une rotation de l’image selon la valeur demandée (90°, 180° ou 270°).
     * Met à jour la largeur/hauteur ainsi que les métadonnées dépendantes
     * (taille d’une ligne, padding, taille de l’image, taille du fichier).
     *
     * @param rotation type de rotation à appliquer
     */
    public void appliesRotation(AvailableRotation rotation) {
        int tempo;
        switch (rotation) {
            case ROTATE_90:
                this.pixelGrid = this.pixelGrid.applies90DegreeRotation();
                tempo = biHeight;
                biHeight = biWidth;
                biWidth = tempo;
                break;
            case ROTATE_180:
                this.pixelGrid = this.pixelGrid.applies180DegreeRotation();
                break;
            case ROTATE_270:
                this.pixelGrid = this.pixelGrid.applies270DegreeRotation();
                tempo = biHeight;
                biHeight = biWidth;
                biWidth = tempo;
        }

        // Recalcul des champs dérivés après rotation
        metaByteSizeRow = (this.biWidth * 3 + 3) / 4 * 4;
        biSizeImage = this.metaByteSizeRow * this.biHeight;
        bhSize = this.bhOffBits + this.biSizeImage;
        metaPadding = this.metaByteSizeRow - this.biWidth * 3;
    }
}
