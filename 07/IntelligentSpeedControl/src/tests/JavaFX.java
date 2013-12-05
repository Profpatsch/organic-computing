package tests;

import helper.XMLHelper;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;

import jfxtras.labs.scene.control.gauge.Battery;
import jfxtras.labs.scene.control.gauge.Gauge;
import jfxtras.labs.scene.control.gauge.Lcd;
import jfxtras.labs.scene.control.gauge.LcdDesign;
import jfxtras.labs.scene.control.gauge.Radial;
import jfxtras.labs.scene.control.gauge.StyleModel;
import jfxtras.labs.scene.control.gauge.StyleModelBuilder;

public class JavaFX extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	private Battery fuelMeter;
	private Lcd lcd;

	private Radial radial;

	public Battery getFuelMeter() {
		return fuelMeter;
	}

	public Lcd getLCD() {
		return lcd;
	}

	public Radial getRadial() {
		return radial;
	}

	public Scene init(Stage stage) {
		Group root = new Group();
		Scene scene = new Scene(root, 820, 350);

		final GridPane pane = new GridPane();
		pane.setPadding(new Insets(0, 5, 0, 5));
		pane.setHgap(30);
		pane.autosize();
		// pane.setGridLinesVisible(true);

		initSpeedGauge(pane);
		initLCD(pane);
		initFuel(pane);
		initFileChooser(stage, pane);

		root.getChildren().add(pane);

		return (scene);
	}

	private void initFileChooser(final Stage stage, GridPane pane) {
		final FileChooser fc = new FileChooser();

		Button openButton = new Button("Load Car");
		Button saveButton = new Button("Save Car");

		openButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				File file = fc.showOpenDialog(stage);
				XMLHelper xmlHelper = new XMLHelper();
				try {
					xmlHelper.loadCar(file);
				} catch (JAXBException e1) {
					e1.printStackTrace();
				}
			}
		});

		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				File file = fc.showSaveDialog(stage);
				XMLHelper xmlHelper = new XMLHelper();
				// xmlHelper.write(car, file);
			}
		});

		pane.add(openButton, 1, 2);
		pane.add(saveButton, 1, 3);
	}

	private void initFuel(GridPane pane) {
		fuelMeter = new Battery();
		fuelMeter.setPrefSize(200, 200);

		Text label = new Text("Fuel Meter");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 35));

		GridPane.setValignment(fuelMeter, VPos.TOP);
		GridPane.setHalignment(label, HPos.CENTER);

		pane.add(label, 3, 0);
		pane.add(fuelMeter, 3, 1);
	}

	private void initLCD(GridPane pane) {
		StyleModel STYLE_MODEL = StyleModelBuilder.create()
				.lcdDesign(LcdDesign.DARKAMBER).lcdDecimals(3)
				.lcdValueFont(Gauge.LcdFont.PIXEL).build();

		lcd = new Lcd(STYLE_MODEL);
		lcd.setThreshold(50);
		lcd.setValueAnimationEnabled(true);
		lcd.setPrefSize(250, 70);
		lcd.setLcdUnitVisible(true);
		lcd.setLcdUnit("KM");

		Text label = new Text("Distance Meter");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 35));

		GridPane.setValignment(lcd, VPos.TOP);
		GridPane.setHalignment(label, HPos.CENTER);

		pane.add(label, 2, 0);
		pane.add(lcd, 2, 1);
	}

	private void initSpeedGauge(GridPane pane) {
		StyleModel STYLE_MODEL_1 = StyleModelBuilder.create()
				.frameDesign(Gauge.FrameDesign.STEEL)
				.tickLabelOrientation(Gauge.TicklabelOrientation.HORIZONTAL)
				.pointerType(Gauge.PointerType.TYPE14).thresholdVisible(true)
				.lcdDesign(LcdDesign.STANDARD_GREEN).build();

		radial = new Radial(STYLE_MODEL_1);
		radial.setThreshold(30);
		radial.setPrefSize(250, 250);
		radial.setMaxValue(160);

		Text label = new Text("Speed Meter");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 35));

		GridPane.setValignment(radial, VPos.TOP);
		GridPane.setHalignment(label, HPos.CENTER);

		pane.add(label, 1, 0);
		pane.add(radial, 1, 1);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = init(stage);
		stage.setScene(scene);
		stage.show();
	}
}
