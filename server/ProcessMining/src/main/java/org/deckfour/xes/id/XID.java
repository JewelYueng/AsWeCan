/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package org.deckfour.xes.id;

import java.io.*;
import java.util.UUID;

public class XID
    implements Cloneable, Comparable,Serializable
{

    public static XID parse(String idString)
    {
        UUID uuid = UUID.fromString(idString);
        return new XID(uuid);
    }

    public static XID read(DataInputStream dis)
        throws IOException
    {
        long msb = dis.readLong();
        long lsb = dis.readLong();
        return new XID(msb, lsb);
    }

    public static XID read(DataInput in)
        throws IOException
    {
        long msb = in.readLong();
        long lsb = in.readLong();
        return new XID(msb, lsb);
    }

    public static void write(XID id, DataOutputStream dos)
        throws IOException
    {
        dos.writeLong(id.uuid.getMostSignificantBits());
        dos.writeLong(id.uuid.getLeastSignificantBits());
    }

    public static void write(XID id, DataOutput out)
        throws IOException
    {
        out.writeLong(id.uuid.getMostSignificantBits());
        out.writeLong(id.uuid.getLeastSignificantBits());
    }

    public XID()
    {
        uuid = UUID.randomUUID();
    }

    public XID(long msb, long lsb)
    {
        uuid = new UUID(msb, lsb);
    }

    public XID(UUID uuid)
    {
        this.uuid = uuid;
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof XID)
        {
            XID other = (XID)obj;
            return uuid.equals(other.uuid);
        } else
        {
            return false;
        }
    }

    public String toString()
    {
        return uuid.toString().toUpperCase();
    }

    public Object clone()
    {
        XID clone;
        try
        {
            clone = (XID)super.clone();
        }
        catch(CloneNotSupportedException e)
        {
            e.printStackTrace();
            clone = null;
        }
        return clone;
    }

    public int hashCode()
    {
        return uuid.hashCode();
    }

    public int compareTo(XID o)
    {
        return uuid.compareTo(o.uuid);
    }

    public int compareTo(Object x0)
    {
        return compareTo((XID)x0);
    }

    private final UUID uuid;
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\leenkey\DependencyFile\OpenXES.jar
	Total time: 56 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/
