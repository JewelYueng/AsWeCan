package org.k2.processmining.support.event.XESEntity;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.model.*;
import org.deckfour.xes.model.impl.XLogImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XLog4EventLog extends ArrayList<XTrace> implements XLog {
	private static final long serialVersionUID = -9192919845877466525L;
	/*     */   private XAttributeMap attributes;
	/*     */   private Set<XExtension> extensions;
	/*     */   private List<XEventClassifier> classifiers;
	/*     */   private List<XAttribute> globalTraceAttributes;
	/*     */   private List<XAttribute> globalEventAttributes;
	/*     */   private XEventClassifier cachedClassifier;
	/*     */   private XLogInfo cachedInfo;
	/*     */   
	/*     */   public XLog4EventLog(XAttributeMap attributeMap, Set<XExtension> extensionsSet, List<XEventClassifier> classifiersList, List<XAttribute> globalTraceAttributesList, List<XAttribute> globalEventAttributesList)
	/*     */   {
	/* 103 */     attributes = attributeMap;
	/* 104 */     extensions = extensionsSet;
	/* 105 */     classifiers = classifiersList;
	/* 106 */     globalTraceAttributes = globalTraceAttributesList;
	/* 107 */     globalEventAttributes = globalEventAttributesList;
	/* 108 */     cachedClassifier = null;
	/* 109 */     cachedInfo = null;
	/*     */   }
	/*     */   
	/*     */ 
	/*     */ 
	/*     */   public XAttributeMap getAttributes()
	/*     */   {
	/* 116 */     return attributes;
	/*     */   }
	/*     */   
	/*     */ 
	/*     */ 
	/*     */   public void setAttributes(XAttributeMap attributes)
	/*     */   {
	/* 123 */     this.attributes = attributes;
	/*     */   }
	/*     */   
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */   public boolean hasAttributes()
	/*     */   {
	/* 131 */     return !attributes.isEmpty();
	/*     */   }
	/*     */   
	/*     */ 
	/*     */ 
	/*     */   public Set<XExtension> getExtensions()
	/*     */   {
	/* 138 */     return extensions;
	/*     */   }
	/*     */   
	/*     */ 
	/*     */ 
	/*     */   public Object clone()
	/*     */   {
	/* 145 */     XLogImpl clone = (XLogImpl)super.clone();
	/* 146 */     attributes = ((XAttributeMap)attributes.clone());
	/* 147 */     extensions = new HashSet(extensions);
	/* 148 */     classifiers = new ArrayList(classifiers);
	/* 149 */     globalTraceAttributes = new ArrayList(globalTraceAttributes);
	/* 150 */     globalEventAttributes = new ArrayList(globalEventAttributes);
	/* 151 */     cachedClassifier = null;
	/* 152 */     cachedInfo = null;
	/* 153 */     clone.clear();
	/* 154 */     for (XTrace trace : this) {
	/* 155 */       clone.add((XTrace)trace.clone());
	/*     */     }
	/* 157 */     return clone;
	/*     */   }
	/*     */   
	/*     */ 
	/*     */ 
	/*     */   public List<XEventClassifier> getClassifiers()
	/*     */   {
	/* 164 */     return classifiers;
	/*     */   }
	/*     */   
	/*     */ 
	/*     */ 
	/*     */   public List<XAttribute> getGlobalEventAttributes()
	/*     */   {
	/* 171 */     return globalEventAttributes;
	/*     */   }
	/*     */   
	/*     */ 
	/*     */ 
	/*     */   public List<XAttribute> getGlobalTraceAttributes()
	/*     */   {
	/* 178 */     return globalTraceAttributes;
	/*     */   }
	/*     */   
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */   public boolean accept(XVisitor visitor)
	/*     */   {
	/* 191 */     if (visitor.precondition())
	/*     */     {
	/*     */ 
	/*     */ 
	/* 195 */       visitor.init(this);
	/*     */       
	/*     */ 
	/*     */ 
	/* 199 */       visitor.visitLogPre(this);
	/*     */       
	/*     */ 
	/*     */ 
	/* 203 */       for (XExtension extension : extensions) {
	/* 204 */         extension.accept(visitor, this);
	/*     */       }
	/*     */       
	/*     */ 
	/*     */ 
	/* 209 */       for (XEventClassifier classifier : classifiers) {
	/* 210 */         classifier.accept(visitor, this);
	/*     */       }
	/*     */       
	/*     */ 
	/*     */ 
	/* 215 */       for (XAttribute attribute : attributes.values()) {
	/* 216 */         attribute.accept(visitor, this);
	/*     */       }
	/*     */       
	/*     */ 
	/*     */ 
	/* 221 */       for (XTrace trace : this) {
	/* 222 */         trace.accept(visitor, this);
	/*     */       }
	/*     */       
	/*     */ 
	/*     */ 
	/* 227 */       visitor.visitLogPost(this);
	/* 228 */       return true;
	/*     */     }
	/* 230 */     return false;
	/*     */   }
	/*     */   
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */   public XLogInfo getInfo(XEventClassifier classifier)
	/*     */   {
	/* 238 */     return classifier.equals(cachedClassifier) ? cachedInfo : null;
	/*     */   }
	/*     */   
	/*     */ 
	/*     */ 
	/*     */   public void setInfo(XEventClassifier classifier, XLogInfo info)
	/*     */   {
	/* 245 */     cachedClassifier = classifier;
	/* 246 */     cachedInfo = info;
	            }
}
