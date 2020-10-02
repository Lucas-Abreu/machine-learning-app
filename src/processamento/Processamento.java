package processamento;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import model.Pixel;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Processamento {

    public static Image ruido(Image image) {
        try {
            int w = (int)image.getWidth();
            int h = (int)image.getHeight();

            PixelReader pr = image.getPixelReader();
            WritableImage wi = new WritableImage(w,h);
            PixelWriter pw = wi.getPixelWriter();

            Pixel[] vector = null;

            for(int i = 1; i < w - 1; i++) {
                for(int j = 1; j < h - 1; j++) {

                    Color oldColor = pr.getColor(i,j);
                    Pixel p = new Pixel(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), i, j);

                    findNeighbors(image, p);

                    vector = p.neighbor3x3;

                    double r = med(vector, 'r');
                    double g = med(vector, 'g');
                    double b = med(vector, 'b');

                    Color newColor = new Color(r,g,b, oldColor.getOpacity());

                    pw.setColor(i, j, newColor);
                }
            }

            return wi;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Pixel[] find3x3(Image image, Pixel p) {
        Pixel[] neighbor3x3 = new Pixel[8];
        PixelReader pr = image.getPixelReader();

        Color color = pr.getColor(p.x-1, p.y-1);
        neighbor3x3[0] = new Pixel(color.getRed(), color.getGreen(), color.getBlue(), p.x-1, p.y-1);

        color = pr.getColor(p.x-1, p.y);
        neighbor3x3[1] = new Pixel(color.getRed(), color.getGreen(), color.getBlue(), p.x-1, p.y);

        color = pr.getColor(p.x-1, p.y+1);
        neighbor3x3[2] = new Pixel(color.getRed(), color.getGreen(), color.getBlue(), p.x-1, p.y+1);

        color = pr.getColor(p.x, p.y-1);
        neighbor3x3[3] = new Pixel(color.getRed(), color.getGreen(), color.getBlue(), p.x, p.y-1);

        color = pr.getColor(p.x, p.y+1);
        neighbor3x3[4] = new Pixel(color.getRed(), color.getGreen(), color.getBlue(), p.x, p.y+1);

        color = pr.getColor(p.x+1, p.y-1);
        neighbor3x3[5] = new Pixel(color.getRed(), color.getGreen(), color.getBlue(), p.x+1, p.y-1);

        color = pr.getColor(p.x+1, p.y);
        neighbor3x3[6] = new Pixel(color.getRed(), color.getGreen(), color.getBlue(), p.x+1, p.y);

        color = pr.getColor(p.x+1, p.y+1);
        neighbor3x3[7] = new Pixel(color.getRed(), color.getGreen(), color.getBlue(), p.x+1, p.y+1);

        return neighbor3x3;
    }

    public static double med(Pixel[] vector, char type) {
        double[] v = new double[vector.length];

        for (int i = 0; i < vector.length; i ++) {
            if (type == 'r') {
                v[i] = vector[i].r;
            } else if (type == 'g') {
                v[i] = vector[i].g;
            } else if (type == 'b') {
                v[i] = vector[i].b;
            }
        }

        Arrays.sort(v);

        return v[v.length/2];

    }

    public static Mat imageToMat( Image image) {

        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);

        Mat returnMat = bufferedImage2Mat( bImage);
        Imgproc.cvtColor(returnMat, returnMat, Imgproc.COLOR_RGBA2RGB);

        return returnMat;
    }

    public static Mat bufferedImage2Mat(BufferedImage in) {

        Mat out;
        byte[] data;
        int r, g, b;
        int height = in.getHeight();
        int width = in.getWidth();

        out = new Mat(height, width, CvType.CV_8UC3);
        data = new byte[height * width * (int)out.elemSize()];
        int[] dataBuff = in.getRGB(0, 0, width, height, null, 0, width);
        for(int i = 0; i < dataBuff.length; i++)
        {
            data[i*3 + 2] = (byte) ((dataBuff[i] >> 16) & 0xFF);
            data[i*3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
            data[i*3] = (byte) ((dataBuff[i] >> 0) & 0xFF);
        }

        out.put(0, 0, data);
        return out;
    }

    public static void findNeighbors(Image image, Pixel p) {
        p.neighbor3x3 = find3x3(image, p);
    }
}
