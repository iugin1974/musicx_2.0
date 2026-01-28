package notation;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import Measure.Bar;
import musicEvent.Note;
import musicEvent.Rest;
import musicInterface.MusicObject;

public class ScoreToXML {

	private Score score;

	public ScoreToXML(Score score) {
		this.score = score;
	}

	public void parse() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		File xmlFile = new File("/tmp/musicWriter.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = factory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();

		Element root = doc.createElement("score");
		doc.appendChild(root);

		for (int staffIndex = 0; staffIndex < score.getStaffCount(); staffIndex++) {
		    Element staff = doc.createElement("staff");
		    staff.setAttribute("index", intAsString(staffIndex));
		    root.appendChild(staff);  // <- append staff al root qui

		    for (int voiceIndex = 0; voiceIndex < score.getNumberOfVoices(staffIndex); voiceIndex++) {
		        Element voice = doc.createElement("voice");
		        voice.setAttribute("index", intAsString(voiceIndex));
		        staff.appendChild(voice);  // <- append voice allo staff qui

		        List<MusicObject> listObjects = score.getObjects(staffIndex, voiceIndex);
		        for (MusicObject mo : listObjects) {
		            Element object = parseObject(mo, doc);
		            voice.appendChild(object);
		        }
		    }
		}

		saveFile(xmlFile, doc);

	}

	private Element parseObject(MusicObject mo, Document doc) {
		Element e = doc.createElement("object");
		e.setAttribute("tick", intAsString(mo.getTick()));
		if (mo instanceof Note) {
			Element eElement = parseNote((Note) mo, doc);
			e.appendChild(eElement);
		} else if (mo instanceof Rest) {
			Element restElement = parseRest((Rest) mo, doc);
			e.appendChild(restElement);
		} else if (mo instanceof Bar) {
			Element barElement = parseBar((Bar) mo, doc);
			e.appendChild(barElement);
		}

		return e;

	}

	private Element parseNote(Note mo, Document doc) {
		Element e = doc.createElement("note");
		e.setAttribute("midi", intAsString(mo.getMidiNumber()));
		e.setAttribute("alteration", intAsString(mo.getAlteration()));
		e.setAttribute("duration", intAsString(mo.getDuration()));
		e.setAttribute("dots", intAsString(mo.getDots()));
		return e;
	}

	private Element parseRest(Rest mo, Document doc) {
		Element e = doc.createElement("rest");
		e.setAttribute("duration", intAsString(mo.getDuration()));
		e.setAttribute("dots", intAsString(mo.getDots()));
		return e;
	}

	private Element parseBar(Bar mo, Document doc) {
		Element e = doc.createElement("bar");
		if (mo.getType() == Bar.Type.DOUBLE)
			e.setAttribute("type", "double");
		else if (mo.getType() == Bar.Type.END)
			e.setAttribute("type", "end");
		else if (mo.getType() == Bar.Type.BEGIN_REPEAT)
			e.setAttribute("type", "beginRepeat");
		else if (mo.getType() == Bar.Type.END_REPEAT)
			e.setAttribute("type", "endRepeat");
		else
			e.setAttribute("type", "single"); // fallback
		return e;
	}

	private void saveFile(File xmlFile, Document doc) throws TransformerException {
		// crea un transformer
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		// imposta indentazione per leggibilità (opzionale)
		transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		// crea DOMSource dal documento
		DOMSource source = new DOMSource(doc);

		// crea StreamResult verso il file
		StreamResult result = new StreamResult(xmlFile);

		// scrivi il file
		transformer.transform(source, result);
	}

	private String intAsString(int i) {
		return String.valueOf(i);
	}
}
