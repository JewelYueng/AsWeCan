/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package org.deckfour.xes.model.impl;

import java.io.Serializable;
import java.util.Date;
import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeTimestamp;

// Referenced classes of package org.deckfour.xes.model.impl:
//            XAttributeImpl, XsDateTimeFormat

public class XAttributeTimestampImpl extends XAttributeImpl
    implements XAttributeTimestamp,Serializable
{

    public XAttributeTimestampImpl(String key, Date value)
    {
        this(key, value, null);
    }

    public XAttributeTimestampImpl(String key, Date value, XExtension extension)
    {
        super(key, extension);
        setValue(value);
    }

    public XAttributeTimestampImpl(String key, long millis)
    {
        this(key, millis, null);
    }

    public XAttributeTimestampImpl(String key, long millis, XExtension extension)
    {
        this(key, new Date(millis), extension);
    }

    public Date getValue()
    {
        return value;
    }

    public long getValueMillis()
    {
        return value.getTime();
    }

    public void setValue(Date value)
    {
        if(value == null)
        {
            throw new NullPointerException("No null value allowed in timestamp attribute!");
        } else
        {
            this.value = value;
            return;
        }
    }

    public void setValueMillis(long value)
    {
        this.value.setTime(value);
    }

//    public String toString()
//    {
//        XsDateTimeFormat xsdatetimeformat = FORMATTER;
//        JVM INSTR monitorenter ;
//        return FORMATTER.format(value);
//        Exception exception;
//        throw exception;
//    }

    public Object clone()
    {
        XAttributeTimestampImpl clone = (XAttributeTimestampImpl)super.clone();
        clone.value = new Date(clone.value.getTime());
        return clone;
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof XAttributeTimestamp)
        {
            XAttributeTimestamp other = (XAttributeTimestamp)obj;
            return super.equals(other) && value.equals(other.getValue());
        } else
        {
            return false;
        }
    }

    public int compareTo(XAttribute other)
    {
        if(!(other instanceof XAttributeTimestamp))
            throw new ClassCastException();
        int result = super.compareTo(other);
        if(result != 0)
            return result;
        else
            return value.compareTo(((XAttributeTimestamp)other).getValue());
    }

//    public volatile int compareTo(Object x0)
//    {
//        return compareTo((XAttribute)x0);
//    }

    private Date value;
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\leenkey\DependencyFile\OpenXES.jar
	Total time: 44 ms
	Jad reported messages/errors:
Overlapped try statements detected. Not all exception handlers will be resolved in the method toString
Couldn't fully decompile method toString
Couldn't resolve all exception handlers in method toString
	Exit status: 0
	Caught exceptions:
*/