package gui;

import helper.XMLHelper;

import java.io.File;

import javafx.stage.FileChooser;

import javax.xml.bind.JAXBException;

import main.Car;
import routes.Route;

public class FileChooserHelper {
	private FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
			"XML Files", "*.xml");
	private FileChooser fc = new FileChooser();
	private XMLHelper xmlHelper = new XMLHelper();
	private static FileChooserHelper instance;

	private FileChooserHelper() {
	}

	public static FileChooserHelper getInstance() {
		if (instance == null)
			instance = new FileChooserHelper();
		return instance;
	}

	public Car loadCarFromXML() {
		fc.getExtensionFilters().add(extFilter);
		fc.setTitle("Load Car");

		File file = fc.showOpenDialog(null);
		try {
			return xmlHelper.loadCar(file);
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}

		return null;
	}

	public void saveToXML(Car car) {
		fc.getExtensionFilters().add(extFilter);

		File file = fc.showSaveDialog(null);
		try {
			xmlHelper.write(car, file);
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
	}

	public Route loadRouteFromXML() {
		fc.getExtensionFilters().add(extFilter);
		fc.setTitle("Load Route");

		File file = fc.showOpenDialog(null);
		try {
			return xmlHelper.loadRoute(file);
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}

		return null;
	}

	public void saveToXML(Route route) {
		fc.getExtensionFilters().add(extFilter);

		File file = fc.showSaveDialog(null);
		try {
			xmlHelper.write(route, file);
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
	}
}
