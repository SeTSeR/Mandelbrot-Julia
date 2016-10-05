package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Controller {

    @FXML
    private Canvas canvas;

    @FXML
    private Slider sliderReZ0, sliderImZ0, sliderK;

    @FXML
    private Button buttonDraw;

    @FXML
    private Button buttonSave;

    @FXML
    private TextField iterationsField;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private CheckBox colorCheckBox;

    private static Main main;

    private final int width = 700, height = 500;
    private final double defaultx0 = -2, defaulty0 = -2, defaultx1 = 2, defaulty1 = 2;

    private double x0 = defaultx0, y0 = defaulty0, x1 = defaultx1, y1 = defaulty1;
    private double xcoef = (width - 1) / (x1 - x0), ycoef = (height - 1) / (y0 - y1);
    private double xshift = -xcoef * x0, yshift = -ycoef * y1;
    private double begX = 0, begY = 0, endX = 0, endY = 0;
    private double k = 1;
    private Complex z0;
    private Integer iterations;
    private Image img;
    private BackgroundImage bgimg;
    private int mode;

    public static void setMain(Main newMain) {
        main = newMain;
    }

    @FXML
    private void onButtonClicked(ActionEvent event) {
        if(event.getSource().equals(buttonDraw)) {
            z0 = new Complex(sliderReZ0.getValue(), sliderImZ0.getValue());
            k = sliderK.getValue();
            iterations = iterationsField.getText().equals("") ? 256 : Integer.parseInt(iterationsField.getText());
            mode = colorCheckBox.isSelected() ? Utils.MODE_COLOR : Utils.MODE_GRAY;
            x0 = defaultx0;
            y0 = defaulty0;
            x1 = defaultx1;
            y1 = defaulty1;
            img = Utils.calc(z0, (int)k, iterations, x0, y0, x1, y1, mode);
            bgimg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                             BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
            anchorPane.setBackground(new Background(bgimg));
        }
        else if(event.getSource().equals(buttonSave)) {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select directory to save the image");
            File selected = chooser.showDialog(main.getStage());
            if((selected == null) || (!selected.isDirectory())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Selected file is not directory", ButtonType.CLOSE);
                alert.show();
            }
            else {
                File imagefile = new File(selected, "image-" + Complex.toString(z0) + "-" + Double.toString(k) +
                                                        "-(" + Double.toString(x0) + ";" + Double.toString(y0) + ")-(" +
                                                               Double.toString(x1) + ";" + Double.toString(y1) + ").png");
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(img, null), "PNG", imagefile);
                }
                catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void onMousePressed(MouseEvent event) {
        begX = event.getX();
        begY = event.getY();
        endX = begX;
        endY = begY;
    }

    @FXML
    private void onMouseDragged(MouseEvent event) {
        final GraphicsContext context = canvas.getGraphicsContext2D();
        context.clearRect(Math.min(begX, endX), Math.min(begY, endY),
                          Math.max(begX, endX) - Math.min(begX, endX),
                          Math.max(begY, endY) - Math.min(begY, endY));
        endX = event.getX();
        endY = event.getY();
        context.strokeRect(Math.min(begX, endX), Math.min(begY, endY),
                           Math.max(begX, endX) - Math.min(begX, endX),
                           Math.max(begY, endY) - Math.min(begY, endY));
    }

    @FXML
    private void onMouseReleased(MouseEvent event) {
        final GraphicsContext context = canvas.getGraphicsContext2D();
        context.clearRect(Math.min(begX, endX), Math.min(begY, endY),
                Math.max(begX, endX) - Math.min(begX, endX),
                Math.max(begY, endY) - Math.min(begY, endY));
        endX = event.getX();
        endY = event.getY();
        endX = event.getX();
        endY = event.getY();
        x0 = (Math.min(begX, endX) - xshift) / xcoef;
        y0 = (Math.max(begY, endY) - yshift) / ycoef;
        x1 = (Math.max(begX, endX) - xshift) / xcoef;
        y1 = (Math.min(begY, endY) - yshift) / ycoef;
        xcoef = (width - 1) / (x1 - x0);
        ycoef = (height - 1) / (y0 - y1);
        xshift = -xcoef * x0;
        yshift = -ycoef * y1;
        mode = colorCheckBox.isSelected() ? Utils.MODE_COLOR : Utils.MODE_GRAY;
        img = Utils.calc(z0, (int)k, iterations, x0, y0, x1, y1, mode);
        bgimg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        anchorPane.setBackground(new Background(bgimg));
    }
}
