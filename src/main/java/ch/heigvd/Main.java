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

        String source = "rondoudou.bmp";
        String destination = "rondudou-copy.bmp";

        try {
            // ouverture du input stream pour lire l'image
            FileInputStream fis =  new FileInputStream("IOFile/" + source);
            BufferedInputStream bis = new BufferedInputStream(fis);

            // copie l'image dans l'objet BMP
            BMP bmp = new BMP(bis);
            //application des filtres
            bmp.appliesBlackWhiteFilter();
            bmp.applies90DegreeRotation();
            bmp.applies90DegreeRotation();
            bmp.applies90DegreeRotation();
            bmp.appliesGaussianFilter();

            // ouverture du output stream pour écrire l'image
            FileOutputStream fos = new FileOutputStream("IOFile/" + destination);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            // écriture de l'image
            bmp.writeBMP(bos);

            bos.flush();
            bos.close();
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}