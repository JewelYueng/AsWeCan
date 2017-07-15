/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package org.deckfour.xes.model.impl;

import java.io.Serializable;
import java.util.*;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;

public class XAttributeMapLazyImpl
    implements XAttributeMap,Serializable
{

    public XAttributeMapLazyImpl(Class implementingClass)
    {
        backingStore = null;
        backingStoreClass = implementingClass;
        backingStore = null;
    }

    public Class getBackingStoreClass()
    {
        return backingStoreClass;
    }

    public synchronized void clear()
    {
        backingStore = null;
    }

    public synchronized boolean containsKey(Object key)
    {
        if(backingStore != null)
            return backingStore.containsKey(key);
        else
            return false;
    }

    public synchronized boolean containsValue(Object value)
    {
        if(backingStore != null)
            return backingStore.containsValue(value);
        else
            return false;
    }

    public synchronized Set entrySet()
    {
        if(backingStore != null)
            return backingStore.entrySet();
        else
            return EMPTY_ENTRYSET;
    }

    public synchronized XAttribute get(Object key)
    {
        if(backingStore != null)
            return (XAttribute)backingStore.get(key);
        else
            return null;
    }

    public synchronized boolean isEmpty()
    {
        if(backingStore != null)
            return backingStore.isEmpty();
        else
            return true;
    }

    public synchronized Set keySet()
    {
        if(backingStore != null)
            return backingStore.keySet();
        else
            return EMPTY_KEYSET;
    }

    public synchronized XAttribute put(String key, XAttribute value)
    {
        if(backingStore == null)
            try
            {
                backingStore = (XAttributeMap)backingStoreClass.newInstance();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        return (XAttribute)backingStore.put(key, value);
    }

    public synchronized void putAll(Map t)
    {
        if(t.size() > 0)
        {
            if(backingStore == null)
                try
                {
                    backingStore = (XAttributeMap)backingStoreClass.newInstance();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            backingStore.putAll(t);
        }
    }

    public synchronized XAttribute remove(Object key)
    {
        if(backingStore != null)
            return (XAttribute)backingStore.remove(key);
        else
            return null;
    }

    public synchronized int size()
    {
        if(backingStore != null)
            return backingStore.size();
        else
            return 0;
    }

    public synchronized Collection values()
    {
        if(backingStore != null)
            return backingStore.values();
        else
            return EMPTY_ENTRIES;
    }

    public Object clone()
    {
        try
        {
            XAttributeMapLazyImpl clone = (XAttributeMapLazyImpl)super.clone();
            if(backingStore != null)
                clone.backingStore = (XAttributeMap)backingStore.clone();
            return clone;
        }
        catch(CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }

//    public volatile Object remove(Object x0)
//    {
//        return remove(x0);
//    }
//
//    public volatile Object put(Object x0, Object x1)
//    {
//        return put((String)x0, (XAttribute)x1);
//    }
//
//    public volatile Object get(Object x0)
//    {
//        return get(x0);
//    }

    private static final Set EMPTY_ENTRYSET = Collections.unmodifiableSet(new HashSet(0));
    private static final Set EMPTY_KEYSET = Collections.unmodifiableSet(new HashSet(0));
    private static final Collection EMPTY_ENTRIES = Collections.unmodifiableCollection(new ArrayList(0));
    private Class backingStoreClass;
    private XAttributeMap backingStore;

}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\leenkey\DependencyFile\OpenXES.jar
	Total time: 48 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/