package ch.heigvd;

import ch.heigvd.byteConverter.ByteConverter;
import ch.heigvd.image.BMP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        try {
            FileInputStream fis =  new FileInputStream("rondoudou.bmp");
            BufferedInputStream bis = new BufferedInputStream(fis);

            BMP bmp = new BMP(bis);

            FileOutputStream fos = new FileOutputStream("rondoudou-COPY.bmp");
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            bmp.writeBMP(bos);

            fis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}