/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package org.deckfour.xes.in;

import java.io.*;
import java.util.List;

public abstract class XParser
{

    public XParser()
    {
    }

    public abstract String name();

    public abstract String description();

    public abstract String author();

    public abstract boolean canParse(File file);

    public abstract List parse(InputStream inputstream)
        throws Exception;

    public List parse(File file)
        throws Exception
    {
        if(canParse(file))
        {
            InputStream is = new FileInputStream(file);
            return parse(is);
        } else
        {
            throw new IllegalArgumentException("Parser cannot handle this file!");
        }
    }

    public String toString()
    {
        return name();
    }

    protected boolean endsWithIgnoreCase(String name, String suffix)
    {
        if(name == null || suffix == null)
            return false;
        int i = name.length() - suffix.length();
        if(i < 0)
            return false;
        else
            return name.substring(i).equalsIgnoreCase(suffix);
    }
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\leenkey\DependencyFile\lib\OpenXES.jar
	Total time: 33 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/
