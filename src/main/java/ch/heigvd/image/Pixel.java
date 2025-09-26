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

    public byte[] getRGB() {
        return new byte[]{this.b, this.g, this.r};
    }

    public byte[] getBW() {
        byte gray = (byte)((int)Math.floor((double)(0.299F * (float)this.r + 0.587F * (float)this.g + 0.114F * (float)this.b)));
        return new byte[]{gray, gray, gray};
    }
}