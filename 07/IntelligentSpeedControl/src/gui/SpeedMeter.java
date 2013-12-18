package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import main.ParameterProvider;
import main.Simulation;
import sim.display.Console;
import sim.display.GUIState;
import sim.engine.SimState;

public class SpeedMeter extends GUIState {
	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		SpeedMeter speedmeter = new SpeedMeter();

		Console c = new Console(speedmeter);
		c.setVisible(true);

		speedmeter.initGUI();
	}

	private GUICreator gauges;
	
	private static SpeedMeter instance;

	public SpeedMeter() {
		super(new Simulation(System.currentTimeMillis()));
		instance = this;
	}

	public void initSimulation() {
		Simulation sim = (Simulation) state;
		if (ParameterProvider.USE_DEFAULT_OBJECTS)
			sim.loadDefaultSimulation();
		else {
			sim.loadCar();
			sim.loadConnector();
			sim.loadRoute();
		}

		sim.getCurrentCar().currentSpeedProperty()
				.addListener(new ChangeListener<Object>() {
					@Override
					public  void changed(ObservableValue<?> o, Object oldVal,
							Object newVal) {
						gauges.getRadial().setValue(Math.floor((Float) newVal));
					}
				});

		sim.getCurrentCar().currentFuelProperty()
				.addListener(new ChangeListener<Object>() {
					@Override
					public void changed(ObservableValue<?> o, Object oldVal,
							Object newVal) {
						// charging level is from 0 to 1, so we need to divide
						// fuel by 100
						gauges.getFuelMeter().setChargingLevel(
								(Float) newVal / 100);

						// stop simulation when no fuel is left
						if ((Float) newVal <= 0) {
							finish();
						}
					}
				});

		sim.getCurrentCar().distanceDrivenProperty()
				.addListener(new ChangeListener<Object>() {
					@Override
					public void changed(ObservableValue<?> o, Object oldVal,
							Object newVal) {
						gauges.getLCD().setValue((Float) newVal);
					}
				});

		sim.getCurrentCar().realFuelPerKilometerProperty()
				.addListener(new ChangeListener<Object>() {
					@Override
					public void changed(ObservableValue<?> o, Object oldVal,
							Object newVal) {
						gauges.setConsumptionValue(newVal + " l/s");
					}
				});

		sim.getCurrentCar().co2EmissionProperty()
				.addListener(new ChangeListener<Object>() {
					@Override
					public void changed(ObservableValue<?> o, Object oldVal,
							Object newVal) {
						gauges.setEmissionValue(newVal + " g/km");
					}
				});
	}

	@Override
	public void load(SimState state) {
		super.load(state);
		initSimulation();
	}

	@Override
	public void start() {
		super.start();
		initSimulation();
	}
	


	private void initGUI() {
		Application.launch(GUICreator.class);
		//gauges = new GUICreator((Simulation) state);
    }
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					JFrame frame = new JFrame("Intelligent SpeedMeter");
//					frame.setResizable(false);
//					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//					frame.setBounds(390, 10, 830, 450);
//					frame.setVisible(true);
//
//					frame.add(initFX());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});

	public static SpeedMeter getInstance(){
		return instance;
	}

	public void setGauges(GUICreator guiCreator) {
		gauges = guiCreator;		
	}
	
}

