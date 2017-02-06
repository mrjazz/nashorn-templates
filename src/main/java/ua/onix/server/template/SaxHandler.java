package ua.onix.server.template;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * It's good for performance use SAX parser but unfortunately implementation could take some time
 * Created by denis on 1/20/2017.
 */
public class SaxHandler extends DefaultHandler {

    private Hashtable tags;
    private OutputStream out;

    public SaxHandler(OutputStream out) {
        this.out = out;
    }

    public void startDocument() throws SAXException {
        tags = new Hashtable();
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        try {
            out.write(("<" + localName + ">").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endElement (String uri, String localName, String qName) throws SAXException {
        try {
            out.write(("<" + localName + "/>").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
