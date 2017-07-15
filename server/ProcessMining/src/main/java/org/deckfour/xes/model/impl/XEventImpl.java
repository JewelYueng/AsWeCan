/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package org.deckfour.xes.model.impl;

import java.io.Serializable;
import java.util.*;
import org.deckfour.xes.id.XID;
import org.deckfour.xes.id.XIDFactory;
import org.deckfour.xes.model.*;
import org.deckfour.xes.util.XAttributeUtils;

// Referenced classes of package org.deckfour.xes.model.impl:
//            XAttributeMapImpl

public class XEventImpl
    implements XEvent,Serializable
{

    public XEventImpl()
    {
        this(XIDFactory.instance().createId(), ((XAttributeMap) (new XAttributeMapImpl())));
    }

    public XEventImpl(XID id)
    {
        this(id, ((XAttributeMap) (new XAttributeMapImpl())));
    }

    public XEventImpl(XAttributeMap attributes)
    {
        this(XIDFactory.instance().createId(), attributes);
    }

    public XEventImpl(XID id, XAttributeMap attributes)
    {
        this.id = id;
        this.attributes = attributes;
    }

    public XAttributeMap getAttributes()
    {
        return attributes;
    }

    public void setAttributes(XAttributeMap attributes)
    {
        this.attributes = attributes;
    }

    public boolean hasAttributes()
    {
        return !attributes.isEmpty();
    }

    public Set getExtensions()
    {
        return XAttributeUtils.extractExtensions(attributes);
    }

    public Object clone()
    {
        XEventImpl clone;
        try
        {
            clone = (XEventImpl)super.clone();
        }
        catch(CloneNotSupportedException e)
        {
            e.printStackTrace();
            return null;
        }
        clone.id = XIDFactory.instance().createId();
        clone.attributes = (XAttributeMap)attributes.clone();
        return clone;
    }

    public boolean equals(Object o)
    {
        if(o instanceof XEventImpl)
            return ((XEventImpl)o).id.equals(id);
        else
            return false;
    }

    public int hashCode()
    {
        return id.hashCode();
    }

    public XID getID()
    {
        return id;
    }

    public void setID(XID id)
    {
        this.id = id;
    }

    public void accept(XVisitor visitor, XTrace trace)
    {
        visitor.visitEventPre(this, trace);
        XAttribute attribute;
        for(Iterator i$ = attributes.values().iterator(); i$.hasNext(); attribute.accept(visitor, this))
            attribute = (XAttribute)i$.next();

        visitor.visitEventPost(this, trace);
    }

    private XID id;
    private XAttributeMap attributes;
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\leenkey\DependencyFile\OpenXES.jar
	Total time: 44 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/