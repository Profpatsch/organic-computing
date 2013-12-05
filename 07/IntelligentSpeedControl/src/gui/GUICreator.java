package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.gauge.Battery;
import jfxtras.labs.scene.control.gauge.Gauge;
import jfxtras.labs.scene.control.gauge.Lcd;
import jfxtras.labs.scene.control.gauge.LcdDesign;
import jfxtras.labs.scene.control.gauge.Radial;
import jfxtras.labs.scene.control.gauge.StyleModel;
import jfxtras.labs.scene.control.gauge.StyleModelBuilder;
import main.ParameterProvider;
import main.Simulation;

public class GUICreator extends Application{
	private Battery fuelMeter;
	private Lcd lcd;
	private Radial radial;
	private Text consumptionValue = new Text();
	private Text emissionValue = new Text();
	private Simulation sim;
	
	
	public GUICreator(){}
	
	public GUICreator(Simulation state) {
		this.sim = state;
	}

	public synchronized Scene initGauges() {
		Group root = new Group();
		Scene scene = new Scene(root);

		final GridPane pane = new GridPane();
		pane.setPadding(new Insets(0, 5, 0, 5));
		pane.setHgap(30);
		pane.autosize();
		// pane.setGridLinesVisible(true);

		initSpeedGauge(pane);
		initDistanceMeter(pane);
		initFuelMeter(pane);
		initButtons(pane, sim);

		root.getChildren().add(pane);

		return (scene);
	}

	private synchronized void initDistanceMeter(GridPane pane) {
		StyleModel STYLE_MODEL = StyleModelBuilder.create()
				.lcdDesign(LcdDesign.DARKAMBER).lcdDecimals(3)
				.lcdValueFont(Gauge.LcdFont.PIXEL).build();

		lcd = new Lcd(STYLE_MODEL);
		lcd.setThreshold(50);
		lcd.setValueAnimationEnabled(true);
		lcd.setPrefSize(250, 70);
		lcd.setLcdUnitVisible(true);
		lcd.setLcdUnit("KM");
		GridPane.setValignment(lcd, VPos.TOP);

		Text label = new Text("Distance Meter");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 35));
		GridPane.setHalignment(label, HPos.CENTER);
		
		Text emission = new Text("CO2 emission");
		emission.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		GridPane.setValignment(emission, VPos.BOTTOM);

		emissionValue.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
		emissionValue.setText("-.- g/km");

		pane.add(label, 2, 0);
		pane.add(lcd, 2, 1);
		pane.add(emission, 2, 1);
		pane.add(emissionValue, 2, 2);
	}

	private synchronized void initButtons(GridPane pane, final Simulation sim) {
		final ToggleGroup group = new ToggleGroup();
		final RadioButton rb = new RadioButton("Use Default Situation");
		final RadioButton rb2 = new RadioButton("Load Car and Route from XML");
		rb.setToggleGroup(group);
		rb2.setToggleGroup(group);
		rb.setSelected(true);

		final Button writeCarBtn = new Button("Write Car to XML");
		final Button saveRouteBtn = new Button("Write Route to XML");

		writeCarBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				sim.writeCarToXML();
			}
		});

		saveRouteBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				sim.writeRouteToXML();
			}
		});

		group.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov,
							Toggle old_toggle, Toggle new_toggle) {
						if (rb.isSelected()) {
							ParameterProvider.USE_DEFAULT_OBJECTS = true;
						} else {
							ParameterProvider.USE_DEFAULT_OBJECTS = false;
						}
					}
				});

		final Separator sepHor = new Separator();
		GridPane.setMargin(sepHor, new Insets(15));
		sepHor.setScaleY(2);
        GridPane.setConstraints(sepHor, 0, 3);
        GridPane.setColumnSpan(sepHor, 5);
        pane.getChildren().add(sepHor);
		
		pane.add(writeCarBtn, 1, 4);
		pane.add(saveRouteBtn, 1, 6);
		pane.add(rb, 2, 4);
		pane.add(rb2, 2, 5);
	}

	private synchronized void initFuelMeter(GridPane pane) {
		fuelMeter = new Battery();
		fuelMeter.setPrefSize(200, 200);
		GridPane.setValignment(fuelMeter, VPos.TOP);

		Text label = new Text("Fuel Meter");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 35));
		GridPane.setHalignment(label, HPos.CENTER);
		
		Text consumption = new Text("Current consumption");
		consumption.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		GridPane.setValignment(consumption, VPos.BOTTOM);
		
		consumptionValue.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
		
		pane.add(label, 3, 0);
		pane.add(fuelMeter, 3, 1);
		pane.add(consumption, 3, 1);
		pane.add(consumptionValue, 3, 2);
	}

	private synchronized void initSpeedGauge(GridPane pane) {
		StyleModel STYLE_MODEL_1 = StyleModelBuilder.create()
				.frameDesign(Gauge.FrameDesign.STEEL)
				.tickLabelOrientation(Gauge.TicklabelOrientation.HORIZONTAL)
				.pointerType(Gauge.PointerType.TYPE14).thresholdVisible(true)
				.lcdDesign(LcdDesign.STANDARD_GREEN).build();

		radial = new Radial(STYLE_MODEL_1);
		radial.setThreshold(30);
		radial.setPrefSize(250, 250);
		radial.setMaxValue(180);

		Text label = new Text("Speed Meter");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 35));

		GridPane.setValignment(radial, VPos.TOP);
		GridPane.setHalignment(label, HPos.CENTER);

		pane.add(label, 1, 0);
		pane.add(radial, 1, 1);
	}

	public synchronized Battery getFuelMeter() {
		return fuelMeter;
	}

	public synchronized Lcd getLCD() {
		return lcd;
	}

	public synchronized Radial getRadial() {
		return radial;
	}

	public synchronized void setConsumptionValue(String value) {
		this.consumptionValue.setText(value);
	}
	
	public synchronized void setEmissionValue(String value) {
		this.emissionValue.setText(value);
	}

	@Override
	public void start(Stage stage){
		//Scene scene = new Scene(new Group(new Text(25, 25, "Hello World!"))); 
		
		sim = (Simulation) SpeedMeter.getInstance().state;
		
		SpeedMeter.getInstance().setGauges(this);

        stage.setTitle("SpeedMeter");  
        
        Scene scene = initGauges();
        stage.sizeToScene();
		stage.setScene(scene);
		stage.show();
	}

}
