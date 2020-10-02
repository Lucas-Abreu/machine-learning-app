package extrator;

import java.io.File;
import java.io.FileOutputStream;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import processamento.Processamento;

public class ExtractCaracteristicas {

	public static double[] extraiCaracteristicas(File f, Image img, boolean showHigh) {
		
		double[] caracteristicas = new double[7];

		double nedHair = 0;
		double nedShirt = 0;
		double nedCollar = 0;
		double willieHair = 0;
		double willieOverall = 0;
		double willieShirt = 0;

		PixelReader pr = img.getPixelReader();
		
		Mat imagemOriginal = Processamento.imageToMat(img);
        Mat imagemProcessada = Processamento.imageToMat(Processamento.ruido(img));
		
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		for(int i=0; i<h; i++) {
			for(int j=0; j<w; j++) {
				
				Color cor = pr.getColor(j,i);
				
				double r = cor.getRed()*255; 
				double g = cor.getGreen()*255;
				double b = cor.getBlue()*255;
				
				if (i < (h/2) && isNedHair(r, g, b)) {
					nedHair++;
					imagemProcessada.put(i, j, 0, 255, 128);
				}
				if (i > (h/2) && isNedShirt(r, g, b)) {
					nedShirt++;
					imagemProcessada.put(i, j, 0, 255, 128);
				}
				if (i > (h/2) && isNedCollar(r, g, b)) {
					nedCollar++;
					imagemProcessada.put(i, j, 0, 255, 128);
				}
				if(i < (h/2) && isWillieHair(r, g, b)) {
					willieHair++;
					imagemProcessada.put(i, j, 0, 255, 255);
				}
				if(i > (h/2) && isWillieOverall(r, g, b)) {
					willieOverall++;
					imagemProcessada.put(i, j, 0, 255, 255);
				}
				if (i > (h/2) && isWillieShirt(r, g, b)) {
					willieHair++;
					imagemProcessada.put(i, j, 0, 255, 255);
				}
			}
		}
		
		// Normaliza as características pelo número de pixels totais da imagem para %
        nedHair 	= (nedHair / (w * h)) * 100;
        nedShirt 	= (nedShirt / (w * h)) * 100;
        nedCollar   = (nedCollar / (w * h)) * 100;
        willieHair  = (willieHair / (w * h)) * 100;
        willieOverall = (willieOverall / (w * h)) * 100;
        willieShirt = (willieShirt / (w * h)) * 100;
        
        caracteristicas[0] = nedHair;
        caracteristicas[1] = nedShirt;
        caracteristicas[2] = nedCollar;
        caracteristicas[3] = willieHair;
        caracteristicas[4] = willieOverall;
        caracteristicas[5] = willieShirt;

        caracteristicas[6] = f.getName().charAt(0) == 'n' ? 0 : 1;

		if (showHigh) {
			HighGui.imshow("Imagem original", imagemOriginal);
			HighGui.imshow("Imagem processada", imagemProcessada);

			HighGui.waitKey(0);
		}

		return caracteristicas;
	}
	
	public static boolean isNedHair(double r, double g, double b) {
		return r >= 95 && r <= 150 && g >= 46 && g <= 90 && b >= 0 && b <= 60;
	}
	public static boolean isNedShirt(double r, double g, double b) {
		return r >= 20 && r <= 75 && g >= 55 && g <= 122 && b >= 6 && b <= 40;
	}
	public static boolean isNedCollar(double r, double g, double b) {
		return r >= 190 && r <= 220 && g >= 120 && g <= 193 && b >= 128 && b <= 205;
	}
	public static boolean isWillieHair(double r, double g, double b) {
		return r >= 127 && r <= 200 && g >= 0 && g <= 50 && b >= 0 && b <= 45;
	}
	public static boolean isWillieOverall(double r, double g, double b) {
		return r >= 16 && r <= 37 && g >= 73 && g <= 100 && b >= 60 && b <= 80;
	}
	public static boolean isWillieShirt(double r, double g, double b) {
		return r >= 150 && r <= 200 && g >= 140 && g <= 160 && b >= 130 && b <= 150;
	}

	public static void extrair() {
				
	    // Cabeçalho do arquivo Weka
		String exportacao = "@relation caracteristicas\n\n";
		exportacao += "@attribute ned_hair real\n";
		exportacao += "@attribute ned_shirt real\n";
		exportacao += "@attribute ned_collar real\n";
		exportacao += "@attribute willie_hair real\n";
		exportacao += "@attribute willie_apron real\n";
		exportacao += "@attribute willie_shirt real\n";
		exportacao += "@attribute classe {Ned, Willie}\n\n";
		exportacao += "@data\n";
	        
	    // Diretório onde estão armazenadas as imagens
	    File diretorio = new File("src\\imagens\\imagens_treinamento");
	    File[] arquivos = diretorio.listFiles();
	    
        // Definição do vetor de características
        double[][] caracteristicas = new double[305][7];
        
        // Percorre todas as imagens do diretório
        int cont = -1;
		for (File arquivo : arquivos != null ? arquivos : new File[0]) {

        	Image imagem = new Image(arquivo.toURI().toString());
        	cont++;
        	caracteristicas[cont] = extraiCaracteristicas(arquivo, imagem, false);

        	String classe = caracteristicas[cont][6] == 0 ? "Ned" : "Willie";

        	/*System.out.println("Laranja camisa Bart: " + caracteristicas[cont][0]
            		+ " - Azul calção Bart: " + caracteristicas[cont][1]
            		+ " - Azul sapato Bart: " + caracteristicas[cont][2]
            		+ " - Azul calça Homer: " + caracteristicas[cont][3]
            		+ " - Marrom boca Homer: " + caracteristicas[cont][4]
            		+ " - Preto sapato Homer: " + caracteristicas[cont][5]
            		+ " - Classe: " + classe); */

        	exportacao += caracteristicas[cont][0] + ","
                    + caracteristicas[cont][1] + ","
        		    + caracteristicas[cont][2] + ","
                    + caracteristicas[cont][3] + ","
        		    + caracteristicas[cont][4] + ","
                    + caracteristicas[cont][5] + ","
                    + classe + "\n";
        }
        
     // Grava o arquivo ARFF no disco
        try {
        	File arquivo = new File("caracteristicas_trabalho.arff");
        	FileOutputStream f = new FileOutputStream(arquivo);
        	f.write(exportacao.getBytes());
        	f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

}
