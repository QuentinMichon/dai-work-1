package ch.heigvd;

import ch.heigvd.byteConverter.ByteConverter;
import ch.heigvd.commands.Root;
import ch.heigvd.image.BMP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        new CommandLine(new Root()).execute(args);
    }
}