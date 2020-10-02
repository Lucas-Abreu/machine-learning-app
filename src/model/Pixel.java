package model;

public class Pixel {
    public double r;
    public double g;
    public double b;

    public int x;
    public int y;

    public Pixel[] neighbor3x3 = new Pixel[8];

    public Pixel(double r, double g, double b, int x, int y) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.x = x;
        this.y = y;
    }
}
