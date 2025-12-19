package Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

	public String[] readException(String lang) {

		Properties prop = new Properties();
		InputStream input = null;
		String[] exc = null;
		try {
			input = this.getClass().getResourceAsStream("Exceptions");
			prop.load(input);
			String n = prop.getProperty(lang);
		if (n == null) return null;
			exc = n.split(",");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return exc;
	}

	public String[] readNoteName(String lang) {

		String[] notes = null;
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = this.getClass().getResourceAsStream("Language");
			prop.load(input);
			String n = prop.getProperty(lang);
			notes = n.split(",");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Language \""+lang+"\" loaded");
		return notes;
	}

	public String[] readAlterations(String lang) {

		String[] alterations = null;
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = this.getClass().getResourceAsStream("Alterations");
			prop.load(input);
			String n = prop.getProperty(lang);
			alterations = n.split(",");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return alterations;
	}
}
