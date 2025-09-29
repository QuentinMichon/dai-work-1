package ch.heigvd.image;

public class Pixel {
    private byte r;
    private byte g;
    private byte b;

    public Pixel(byte r, byte g, byte b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Pixel copy() {
        return new Pixel(r, g, b);
    }

    public byte[] getRGB() {
        return new byte[]{this.b, this.g, this.r};
    }


    public Pixel getBWPixel() {
        int intR = this.r & 0xFF;
        int intG = this.g & 0xFF;
        int intB = this.b & 0xFF;

        byte gray = (byte)(Math.floor((double)(0.299F*intR + 0.587F*intG + 0.114F*intB)));
        return new Pixel(gray, gray, gray);
    }
}