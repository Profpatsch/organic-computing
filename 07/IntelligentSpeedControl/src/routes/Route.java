package routes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "route")
public class Route {
	@XmlElementWrapper(name = "sections")
	List<Section> sections = new ArrayList<Section>();

	public List<Section> getSections() {
		return sections;
	}

	public void addSection(Section section) {
		this.sections.add(section);
	}

	public Section getSection(int pos) {
		return this.sections.get(pos);
	}

	public Section getNextSection(Section section) {
		int position = getPosition(section);
		if (position + 1 != this.getSize()) {
			return this.sections.get(position + 1);
		}
		return null;
	}

	public int getPosition(Section section) {
		return sections.indexOf(section);
	}

	public int getSize() {
		return this.sections.size();
	}
}
