package principal;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;

import algoritmos.Bayes;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.stage.Stage;

import java.util.ResourceBundle;

import extrator.ExtractCaracteristicas;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.application.HostServices;

public class PrincipalController implements Initializable {
	
	@FXML
	private ImageView imageView;

	@FXML
	private Label nedHair;
	@FXML
	private Label nedShirt;
	@FXML
	private Label nedCollar;
	@FXML
	private Label willieHair;
	@FXML
	private Label willieOverall;
	@FXML
	private Label willieShirt;

	@FXML
	private CheckBox showHighGui;

	@FXML
	private Label probNed;
	@FXML
	private Label probWillie;

	@FXML
	public void extrairCaracteristicas() {
		ExtractCaracteristicas.extrair();
	}

	@FXML
	private MediaView mv;
	private MediaPlayer mp;
	private static final String MEDIA_URL = "assets/logo.mp4";

	double caracteristicas[];
	double probabilidades[];

	@Override
	public void initialize(URL Location, ResourceBundle resources) {
		mp = new MediaPlayer(new Media (this.getClass().getResource(MEDIA_URL).toExternalForm()));
		mp.setAutoPlay(true);

		mv.setMediaPlayer(mp);
	}

	@FXML
	public void tencent(){
		HostServices hostServices = (HostServices) ((Stage) this.imageView.getScene().getWindow()).getProperties().get("hostServices");
		hostServices.showDocument("https://www.youtube.com/watch?v=AcDvkjug9RY");
	}

	@FXML
	public void ccpCorona(){
		HostServices hostServices = (HostServices) ((Stage) this.imageView.getScene().getWindow()).getProperties().get("hostServices");
		hostServices.showDocument("https://www.youtube.com/watch?v=RxQUrHbuYf4");
	}

	@FXML
	public void classificar() {
		probabilidades = Bayes.naiveBayes(caracteristicas);
		probNed.setText("Probabilidade Ned: " + BigDecimal.valueOf(probabilidades[0]).setScale(4, RoundingMode.HALF_DOWN).doubleValue());
		probWillie.setText("Probabilidade Ned: " + BigDecimal.valueOf(probabilidades[1]).setScale(4, RoundingMode.HALF_DOWN).doubleValue());
	}

	@FXML
	public void selecionaImagem() {
		imageView.setImage(null);
		File f = buscaImg();
		if(f != null) {
			Image img = new Image(f.toURI().toString());
			imageView.setImage(img);
			imageView.setFitWidth(img.getWidth());
			imageView.setFitHeight(img.getHeight());
			caracteristicas = ExtractCaracteristicas.extraiCaracteristicas(f, img, showHighGui.isSelected());

			nedHair.setText("Cabelo Ned: " + BigDecimal.valueOf(caracteristicas[0]).setScale(4, RoundingMode.HALF_DOWN).doubleValue());
			nedShirt.setText("Suéter Ned: " + BigDecimal.valueOf(caracteristicas[1]).setScale(4, RoundingMode.HALF_DOWN).doubleValue());
			nedCollar.setText("Gola Ned: " + BigDecimal.valueOf(caracteristicas[2]).setScale(4, RoundingMode.HALF_DOWN).doubleValue());

			willieHair.setText("Cabelo Willie: " + BigDecimal.valueOf(caracteristicas[3]).setScale(4, RoundingMode.HALF_DOWN).doubleValue());
			willieOverall.setText("Macacão Willie: " + BigDecimal.valueOf(caracteristicas[4]).setScale(4, RoundingMode.HALF_DOWN).doubleValue());
			willieShirt.setText("Blusa Willie: " + BigDecimal.valueOf(caracteristicas[5]).setScale(4, RoundingMode.HALF_DOWN).doubleValue());
		}
	}

	@FXML
	public void close(){
		((Stage) this.imageView.getScene().getWindow()).close();
	}

	@FXML
	public void minimize(){
		((Stage) this.imageView.getScene().getWindow()).setIconified(true);
	}
	
	private File buscaImg() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new 
				   FileChooser.ExtensionFilter(
						   "Imagens", "*.jpg", "*.JPG", 
						   "*.png", "*.PNG", "*.gif", "*.GIF", 
						   "*.bmp", "*.BMP"));
		 fileChooser.setInitialDirectory(new File("src/imagens"));
		 File imgSelec = fileChooser.showOpenDialog(null);
		 try {
			 if (imgSelec != null) {
			    return imgSelec;
			 }
		 } catch (Exception e) {
			e.printStackTrace();
		 }
		 return null;
	}

}
