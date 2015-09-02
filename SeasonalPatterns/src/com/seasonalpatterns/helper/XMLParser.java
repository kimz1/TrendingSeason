package com.seasonalpatterns.helper;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.seasonalpatterns.comm.Commodity;

public class XMLParser {

	private static CommContainer container = null;
	private final Object lock = new Object();

	public XMLParser(CommContainer cont) {
		XMLParser.container = cont;
	}

	public void worker() {

		try {
			String lang = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			SchemaFactory sFactory = SchemaFactory.newInstance(lang);
			Schema schema = sFactory.newSchema(new File(
					"C:/workspace/SeasonalPatterns/resourceSchema.xsd"));

			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setSchema(schema);
			SAXParser parser = factory.newSAXParser();

			synchronized (lock) {
				File file = new File(
						"C:/workspace/SeasonalPatterns/resource.xml");
				SAXHandler handler = new SAXHandler();
				parser.parse(file, handler);
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	class SAXHandler extends DefaultHandler {

		private Stack<String> elementStack = new Stack<>();
		String commName = null;
		String year = null;
		String month = null;
		String exchange = null;

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			this.elementStack.push(qName);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if (qName.equals("sp:commodity")) {

				String fullCommName = "com.seasonalpatterns.comm." + commName;

				try {
					
					// add a commodity to the set of commodities (appended only if it's not in the set already);
					Commodity comm = CommFactory.createCommodity(fullCommName,
							year, month, exchange);
					container.add(comm);
					System.out.println(comm);
				} catch (CommodityException e) {
					e.printStackTrace();
				}
			}
			this.elementStack.pop();
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			String value = new String(ch, start, length).trim();
			String element = (String) this.elementStack.peek();

			if (element.equals("sp:type")) {
				commName = value;
			} else if (element.equals("sp:month")) {
				month = value;
			} else if (element.equals("sp:year")) {
				year = value;
			} else if (element.equals("sp:exchange")) {
				exchange = value;
			}
		}

		@Override
		public void warning(SAXParseException e) throws SAXException {
			System.err.println("Something unexpected happened!");
			e.printStackTrace();
		}

		@Override
		public void error(SAXParseException e) throws SAXException {
			System.err.println("Something went wrong!");
			e.printStackTrace();
		}

		@Override
		public void fatalError(SAXParseException e) throws SAXException {
			System.err.println("Something went terribly wrong!");
			e.printStackTrace();
		}
	}
}
