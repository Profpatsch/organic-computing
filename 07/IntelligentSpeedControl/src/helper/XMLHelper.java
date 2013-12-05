package helper;

import java.io.File;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import main.Car;
import routes.Route;

public class XMLHelper {
	public Car loadCar(File file) throws JAXBException {
		return JAXB.unmarshal(file, Car.class);
	}

	public Route loadRoute(File file) throws JAXBException {
		return JAXB.unmarshal(file, Route.class);
	}
	
	public void write(Car car, File file) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(Car.class);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(car, file);
	}

	public void write(Route route, File file) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(Route.class);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(route, file);
	}
}
