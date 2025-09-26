package ch.heigvd.image;

public class PixelGrid {
    private int width;
    private int height;
    private Pixel[][] pixels;

    public PixelGrid(int height, int width) {
        this.width = width;
        this.height = height;
        pixels = new Pixel[height][width];
    }

    public void setPixel(int height, int width, Pixel pixel) {
        if (height < 0 || height >= this.height || width < 0 || width >= this.width) {
            throw new IndexOutOfBoundsException();
        }
        pixels[height][width] = pixel;
    }

    public Pixel getPixel(int height, int width) {
        if (height < 0 || height >= this.height || width < 0 || width >= this.width) {
            throw new IndexOutOfBoundsException();
        }
        return pixels[height][width];
    }
}
