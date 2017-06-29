/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   XAttributeImpl.java

package org.deckfour.xes.model.impl;

import java.io.Serializable;
import java.util.*;
import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.model.*;
import org.deckfour.xes.util.XAttributeUtils;
import org.deckfour.xes.model.impl.XAttributeMapImpl;

// Referenced classes of package org.deckfour.xes.model.impl:
//            XAttributeMapLazyImpl, XAttributeMapImpl

public abstract class XAttributeImpl
    implements XAttribute,Serializable
{
	private static final Class X = XAttributeMapImpl.class;

    protected XAttributeImpl(String key)
    {
        this(key, null);
    }

    protected XAttributeImpl(String key, XExtension extension)
    {
        this.key = key;
        this.extension = extension;
    }

    public String getKey()
    {
        return key;
    }

    public XExtension getExtension()
    {
        return extension;
    }

    public XAttributeMap getAttributes()
    {
        if(attributes == null)
            attributes = new XAttributeMapLazyImpl(X);
        return attributes;
    }

    public void setAttributes(XAttributeMap attributes)
    {
        this.attributes = attributes;
    }

    public boolean hasAttributes()
    {
        return attributes != null && !attributes.isEmpty();
    }

    public Set getExtensions()
    {
        if(attributes != null)
            return XAttributeUtils.extractExtensions(getAttributes());
        else
            return Collections.emptySet();
    }

    public Object clone()
    {
        XAttributeImpl clone = null;
        try
        {
            clone = (XAttributeImpl)super.clone();
        }
        catch(CloneNotSupportedException e)
        {
            e.printStackTrace();
            return null;
        }
        if(attributes != null)
            clone.attributes = (XAttributeMap)getAttributes().clone();
        return clone;
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof XAttribute)
        {
            XAttribute other = (XAttribute)obj;
            return other.getKey().equals(key);
        } else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return key.hashCode();
    }

    public int compareTo(XAttribute o)
    {
        return key.compareTo(o.getKey());
    }

    public void accept(XVisitor visitor, XAttributable parent)
    {
        visitor.visitAttributePre(this, parent);
        if(this instanceof XAttributeCollection)
        {
            XAttribute attribute;
            for(Iterator i$ = ((XAttributeCollection)this).getCollection().iterator(); i$.hasNext(); attribute.accept(visitor, this))
                attribute = (XAttribute)i$.next();

        } else
        if(attributes != null)
        {
            XAttribute attribute;
            for(Iterator i$ = getAttributes().values().iterator(); i$.hasNext(); attribute.accept(visitor, this))
                attribute = (XAttribute)i$.next();

        }
        visitor.visitAttributePost(this, parent);
    }

//    public volatile int compareTo(Object x0)
//    {
//        return compareTo((XAttribute)x0);
//    }

    private final String key;
    private final XExtension extension;
    private XAttributeMap attributes;
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\leenkey\DependencyFile\OpenXES.jar
	Total time: 52 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/