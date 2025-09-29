package ch.heigvd.image;


import ch.heigvd.byteConverter.ByteConverter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public class BMP {
    //---------------------
    // Header attributs
    //---------------------
    private char bhType;                // = 19778;
    private int bhSize;
    private int bhReserved;             // = 0;
    private int bhOffBits;              // = 54;
    private int biSize;                 // = 40;
    private int biWidth;                //= 1080;
    private int biHeight;               // = 720;
    private char biPlanes;              // = 1;
    private char biBitCount;            // = 24;
    private int biCompression;          // = 0;
    private int biSizeImage;
    private int biXPelsPerMeter;        // = 0;
    private int biYPelsPerMeter;        // = 0;
    private int biClrUsed;              // = 0;
    private int biClrImportant;         // = 0;
    private PixelGrid pixelGrid;

    //---------------------
    // Derived values
    //---------------------
    private int metaByteSizeRow;
    private int metaPadding;

    public BMP(BufferedInputStream bis) {
        try {
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

            this.metaByteSizeRow = (this.biWidth * 3 + 3) / 4 * 4;
            this.metaPadding = this.metaByteSizeRow - this.biWidth * 3;

            // Recuperation de l'image
            pixelGrid = new PixelGrid(biHeight, biWidth);

            for(int _h = 0; _h < this.biHeight; ++_h) {
                for(int _w = 0; _w < this.biWidth; ++_w) {
                    this.pixelGrid.setPixel(_h, _w, ByteConverter.getPixel(bis));
                }
                ByteConverter.pass(bis, metaPadding);
            }

            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeBMP(BufferedOutputStream bos) {
        try {
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

            for(int _h = 0; _h < this.biHeight; ++_h) {
                for(int _w = 0; _w < this.biWidth; ++_w) {
                    ByteConverter.writePixel(bos, pixelGrid.getPixel(_h, _w));
                }
                ByteConverter.writePadding(bos, metaPadding);
            }

            bos.flush();
            bos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void appliesBlackWhiteFilter() {
        this.pixelGrid = pixelGrid.appliesBlackWhiteFilter();
    }

    public void appliesGaussianFilter() {
        for (int i = 0; i < 50; i++) {
            this.pixelGrid = pixelGrid.appliesGaussianFilter();
        }
    }

    public void applies90DegreeRotation() {
        this.pixelGrid = this.pixelGrid.applies90DegreeRotation();
        int tempo = biHeight;
        biHeight = biWidth;
        biWidth = tempo;
        metaByteSizeRow = (this.biWidth * 3 + 3) / 4 * 4;
        biSizeImage = this.metaByteSizeRow * this.biHeight;
        bhSize = this.bhOffBits + this.biSizeImage;
        metaPadding = this.metaByteSizeRow - this.biWidth * 3;
    }
}
