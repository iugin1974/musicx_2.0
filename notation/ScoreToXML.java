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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Measure.Bar;
import musicEvent.Alteration;
import musicEvent.Note;
import musicEvent.Rest;
import musicInterface.MusicObject;

public class ScoreToXML {

	private Score score;

	public ScoreToXML(Score score) {
		this.score = score;
	}

	public void load() throws ParserConfigurationException, SAXException, IOException {
		File xmlFile = new File("/tmp/musicWriter.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        NodeList staffList = doc.getElementsByTagName("staff");
        for (int staffIndex = 0; staffIndex < staffList.getLength(); staffIndex++) {
        	Element staffEl = (Element) staffList.item(staffIndex);
        	score.addStaff();
        	
        	// legge voice
        	NodeList voiceList = staffEl.getElementsByTagName("voice");
        	for (int voiceIndex = 0; voiceIndex < voiceList.getLength(); voiceIndex++) {
        		Element voiceEl = (Element) voiceList.item(voiceIndex);
        		
        		// legge objects
        		NodeList objectList = voiceEl.getElementsByTagName("object");
        		for (int o = 0; o < objectList.getLength(); o++) {
        			Element objectEl = (Element) objectList.item(o);
        			parseObject(score, objectEl, staffIndex, voiceIndex);
        		}
        		
        	}
        }
	}
	
	private void parseObject(Score score, Element objectEl, int staffIndex, int voiceIndex) {
	    int tick = Integer.parseInt(objectEl.getAttribute("tick"));

	    // Prendi tutti gli elementi figli
	    NodeList children = objectEl.getChildNodes();
	    MusicObject mo = null;

	    for (int i = 0; i < children.getLength(); i++) {
	        Node child = children.item(i);
	        if (child.getNodeType() != Node.ELEMENT_NODE) continue;

	        Element childEl = (Element) child;
	        String nodeName = childEl.getNodeName();

	        switch (nodeName) {
	            case "note":
	                mo = parseNote(childEl, tick);
	                break;
	            case "rest":
	                mo = parseRest(childEl, tick);
	                break;
	            case "bar":
	                mo = parseBar(childEl, tick);
	                break;
	            case "clef":
	            	mo = parseClef(childEl, tick);
	            	break;
	            default:
	                // tipo non riconosciuto → ignora o logga
	                System.out.println("Unknown object child: " + nodeName);
	        }

	        // Aggiungi l'oggetto se è stato creato
	        if (mo != null) {
	            score.addObject(mo, staffIndex, voiceIndex);
	            mo = null; // pronto per eventuali altri figli
	        }
	    }

	}


	private MusicObject parseRest(Element restEl, int tick) {
		// TODO Auto-generated method stub
		return null;
	}

	private Note parseNote(Element noteEl, int tick) {
		int midi = stringAsInt(noteEl.getAttribute("midi"));
		Alteration alteration = new Alteration(stringAsInt(noteEl.getAttribute("alteration")));
		int duration = stringAsInt(noteEl.getAttribute("duration"));
		int dots = stringAsInt(noteEl.getAttribute("dots"));
		Note n = new Note( midi,  alteration,  duration,  dots);
		n.setTick(tick);
		return n;
	}

	private Clef parseClef(Element clefEl, int tick) {
	    String type = clefEl.getAttribute("type");
	    Clef c;

	    switch (type) {
	        case "treble":
	            c = Clef.treble();
	            break;
	        case "treble_8":
	            c = Clef.treble8();
	            break;
	        case "bass":
	            c = Clef.bass();
	            break;
	        default:
	            throw new IllegalArgumentException("Clef non supportata: " + type);
	    }

	    c.setTick(tick);
	    return c;
	}
	
	private Bar parseBar(Element barEl, int tick) {
	    String type = barEl.getAttribute("type");
	    Bar bar = new Bar();

	    switch (type) {
	        case "single":
	            // default già è singolo, non serve fare nulla
	            break;
	        case "double":
	            bar.setDoubleBar();
	            break;
	        case "end":
	            bar.setEndBar();
	            break;
	        case "beginRepeat":
	            bar.setBeginRepeatBar();
	            break;
	        case "endRepeat":
	            bar.setEndRepeatBar();
	            break;
	        default:
	            throw new IllegalArgumentException("Tipo di barra non supportato: " + type);
	    }

	    bar.setTick(tick);
	    return bar;
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
		} else if (mo instanceof Clef) {
			Element clefElement = parseClef((Clef) mo, doc);
			e.appendChild(clefElement);
		}

		return e;

	}

	private Element parseNote(Note mo, Document doc) {
		Element e = doc.createElement("note");
		e.setAttribute("midi", intAsString(mo.getMidiNumber()));
		e.setAttribute("alteration", intAsString(mo.getAlteration().getValue())); // può essere null
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

	private Element parseClef(Clef clef, Document doc) {
		Element e = doc.createElement("clef");
		if (clef.getType() == Clef.Type.TREBLE)
			e.setAttribute("type", "treble");
		else if (clef.getType() == Clef.Type.TREBLE_8)
		e.setAttribute("type", "treble_8");
		else if (clef.getType() == Clef.Type.BASS)
			e.setAttribute("type", "bass");
		// TODO continua
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
	
	private int stringAsInt(String s) {
		return Integer.parseInt(s);
	}
}
