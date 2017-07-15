/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package org.deckfour.xes.in;

import java.io.*;
import java.net.URI;
import java.util.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.deckfour.xes.classification.XEventAttributeClassifier;
import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.extension.XExtensionManager;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.id.XID;
import org.deckfour.xes.model.*;
import org.deckfour.xes.model.buffered.XTraceBufferedImpl;
import org.deckfour.xes.util.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// Referenced classes of package org.deckfour.xes.in:
//            XParser

public class XesXmlParser extends XParser
{
    protected class XesXmlHandler extends DefaultHandler
    {

        public XLog getLog()
        {
            return log;
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            String tagName = localName.trim();
            if(tagName.length() == 0)
                tagName = qName;
            if(tagName.equalsIgnoreCase("string") || tagName.equalsIgnoreCase("date") || tagName.equalsIgnoreCase("int") || tagName.equalsIgnoreCase("float") || tagName.equalsIgnoreCase("boolean") || tagName.equalsIgnoreCase("id") || tagName.equalsIgnoreCase("list") || tagName.equalsIgnoreCase("container"))
            {
                String key = attributes.getValue("key");
                if(key == null)
                    key = "";
                String value = attributes.getValue("value");
                if(value == null)
                    value = "";
                XExtension extension = null;
                if(key != null)
                {
                    int colonIndex = key.indexOf(':');
                    if(colonIndex > 0 && colonIndex < key.length() - 1)
                    {
                        String prefix = key.substring(0, colonIndex);
                        extension = XExtensionManager.instance().getByPrefix(prefix);
                    }
                }
                XAttribute attribute = null;
                if(tagName.equalsIgnoreCase("string"))
                    attribute = factory.createAttributeLiteral(key, value, extension);
                else
                if(tagName.equalsIgnoreCase("date"))
                {
                    Date date = xsDateTimeConversion.parseXsDateTime(value);
                    if(date != null)
                        attribute = factory.createAttributeTimestamp(key, date, extension);
                    else
                        return;
                } else
                if(tagName.equalsIgnoreCase("int"))
                    attribute = factory.createAttributeDiscrete(key, Long.parseLong(value), extension);
                else
                if(tagName.equalsIgnoreCase("float"))
                    attribute = factory.createAttributeContinuous(key, Double.parseDouble(value), extension);
                else
                if(tagName.equalsIgnoreCase("boolean"))
                    attribute = factory.createAttributeBoolean(key, Boolean.parseBoolean(value), extension);
                else
                if(tagName.equalsIgnoreCase("id"))
                    attribute = factory.createAttributeID(key, XID.parse(value), extension);
                else
                if(tagName.equalsIgnoreCase("list"))
                    attribute = factory.createAttributeList(key, extension);
                else
                if(tagName.equalsIgnoreCase("container"))
                    attribute = factory.createAttributeContainer(key, extension);
                if(attribute != null)
                {
                    attributeStack.push(attribute);
                    attributableStack.push(attribute);
                }
            } else
            if(tagName.equalsIgnoreCase("event"))
            {
                event = factory.createEvent();
                attributableStack.push(event);
            } else
            if(tagName.equalsIgnoreCase("trace"))
            {
                trace = factory.createTrace();
                attributableStack.push(trace);
            } else
            if(tagName.equalsIgnoreCase("log"))
            {
                log = factory.createLog();
                attributableStack.push(log);
            } else
            if(tagName.equalsIgnoreCase("extension"))
            {
                XExtension extension = null;
                String uriString = attributes.getValue("uri");
                if(uriString != null)
                {
                    extension = XExtensionManager.instance().getByUri(URI.create(uriString));
                } else
                {
                    String prefixString = attributes.getValue("prefix");
                    if(prefixString != null)
                        extension = XExtensionManager.instance().getByPrefix(prefixString);
                }
                if(extension != null)
                    log.getExtensions().add(extension);
            } else
            if(tagName.equalsIgnoreCase("global"))
            {
                String scope = attributes.getValue("scope");
                if(scope.equalsIgnoreCase("trace"))
                    globals = log.getGlobalTraceAttributes();
                else
                if(scope.equalsIgnoreCase("event"))
                    globals = log.getGlobalEventAttributes();
            } else
            if(tagName.equalsIgnoreCase("classifier"))
            {
                String name = attributes.getValue("name");
                String keys = attributes.getValue("keys");
                if(name != null && keys != null && name.length() > 0 && keys.length() > 0)
                {
                    List keysList = fixKeys(log, XTokenHelper.extractTokens(keys));
                    String keysArray[] = new String[keysList.size()];
                    int i = 0;
                    for(Iterator i$ = keysList.iterator(); i$.hasNext();)
                    {
                        String key = (String)i$.next();
                        keysArray[i++] = key;
                    }

                    org.deckfour.xes.classification.XEventClassifier classifier = new XEventAttributeClassifier(name, keysArray);
                    log.getClassifiers().add(classifier);
                }
            }
        }

        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            String tagName = localName.trim();
            if(tagName.length() == 0)
                tagName = qName;
            if(tagName.equalsIgnoreCase("global"))
                globals = null;
            else
            if(tagName.equalsIgnoreCase("string") || tagName.equalsIgnoreCase("date") || tagName.equalsIgnoreCase("int") || tagName.equalsIgnoreCase("float") || tagName.equalsIgnoreCase("boolean") || tagName.equalsIgnoreCase("id") || tagName.equalsIgnoreCase("list") || tagName.equalsIgnoreCase("container"))
            {
                XAttribute attribute = (XAttribute)attributeStack.pop();
                attributableStack.pop();
                if(globals != null)
                {
                    globals.add(attribute);
                } else
                {
                    ((XAttributable)attributableStack.peek()).getAttributes().put(attribute.getKey(), attribute);
                    if(!attributeStack.isEmpty() && (attributeStack.peek() instanceof XAttributeCollection))
                        ((XAttributeCollection)attributeStack.peek()).addToCollection(attribute);
                }
            } else
            if(tagName.equalsIgnoreCase("event"))
            {
                trace.add(event);
                event = null;
                attributableStack.pop();
            } else
            if(tagName.equalsIgnoreCase("trace"))
            {
                if(trace instanceof XTraceBufferedImpl)
                    ((XTraceBufferedImpl)trace).consolidate();
                log.add(trace);
                trace = null;
                attributableStack.pop();
            } else
            if(tagName.equalsIgnoreCase("log"))
            {
                XExtension ext;
                for(Iterator i$ = extensions.iterator(); i$.hasNext(); log.getExtensions().add(ext))
                    ext = (XExtension)i$.next();

                attributableStack.pop();
            }
        }

        protected XLog log;
        protected XTrace trace;
        protected XEvent event;
        protected Stack attributeStack;
        protected Stack attributableStack;
        protected HashSet extensions;
        protected List globals;
        final XesXmlParser this$0;

        public XesXmlHandler()
        {
        	super();
            this$0 = XesXmlParser.this;
            log = null;
            trace = null;
            event = null;
            attributeStack = new Stack();
            attributableStack = new Stack();
            extensions = new HashSet();
            globals = null;
        }
    }


    public XesXmlParser(XFactory factory)
    {
        xsDateTimeConversion = new XsDateTimeConversionJava7();
        this.factory = factory;
    }

    public XesXmlParser()
    {
        this((XFactory)XFactoryRegistry.instance().currentDefault());
    }

    public String author()
    {
        return "Christian W. G\303\274nther";
    }

    public boolean canParse(File file)
    {
        String filename = file.getName();
        return endsWithIgnoreCase(filename, ".xes");
    }

    public String description()
    {
        return "Reads XES models from plain XML serializations";
    }

    public String name()
    {
        return "XES XML";
    }

    public List parse(InputStream is)
        throws Exception
    {
        BufferedInputStream bis = new BufferedInputStream(is);
        XesXmlHandler handler = new XesXmlHandler();
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(false);
        SAXParser parser = parserFactory.newSAXParser();
        parser.parse(bis, handler);
        bis.close();
        ArrayList wrapper = new ArrayList();
        wrapper.add(handler.getLog());
        return wrapper;
    }

    private List fixKeys(XLog log, List keys)
    {
        List fixedKeys = fixKeys(log, keys, 0);
        return fixedKeys != null ? fixedKeys : keys;
    }

    private List fixKeys(XLog log, List keys, int index)
    {
        if(index >= keys.size())
            return keys;
        if(findGlobalEventAttribute(log, (String)keys.get(index)))
        {
            List fixedKeys = fixKeys(log, keys, index + 1);
            if(fixedKeys != null)
                return fixedKeys;
        }
        if(index + 1 == keys.size())
            return null;
        List newKeys = new ArrayList(keys.size() - 1);
        for(int i = 0; i < index; i++)
            newKeys.add(keys.get(i));

        newKeys.add((new StringBuilder()).append((String)keys.get(index)).append(" ").append((String)keys.get(index + 1)).toString());
        for(int i = index + 2; i < keys.size(); i++)
            newKeys.add(keys.get(i));

        return fixKeys(log, newKeys, index);
    }

    private boolean findGlobalEventAttribute(XLog log, String key)
    {
        for(Iterator i$ = log.getGlobalEventAttributes().iterator(); i$.hasNext();)
        {
            XAttribute attribute = (XAttribute)i$.next();
            if(attribute.getKey().equals(key))
                return true;
        }

        return false;
    }

    protected XsDateTimeConversion xsDateTimeConversion;
    protected static final URI XES_URI = URI.create("http://www.xes-standard.org/");
    protected XFactory factory;


}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\leenkey\DependencyFile\lib\OpenXES.jar
	Total time: 33 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
	
*/

