package com.ctd.bank.common.util;

import org.dom4j.Document;
import org.dom4j.Element;

import java.util.*;

@SuppressWarnings({"unchecked","rawtypes"})
public class XmlUtil {
	
	public static Map xml2map(Element root) {
		Map ret = new HashMap();
		try {
			if (root == null) {
				return null;
			}
			
			List<Element> child1 = root.elements();
			for (int i = 0; i < child1.size(); i++) {
				Element child1et = (Element) child1.get(i);
				List<Element> child2 = child1et.elements();
				if (child2.size() == 0) {
					ret.put(child1et.getName(), child1et.getStringValue());
				} else {
					for (int k = 0; k < child2.size(); k++) {
						Element child2et = (Element) child2.get(k);
						List<Element> child3 = child2et.elements();
						if (child3.size() == 0) {
							ret.put(child2et.getName(), child2et.getStringValue());
						} else {
							List list = new ArrayList();
							for (int j = 0; j < child3.size(); j++) {
								Element child3et = (Element) child3.get(j);
								List<Element> child4 = child3et.elements();
								if (child4.size() == 0) {
									ret.put(child3et.getName(), child3et.getStringValue());
								} else {
									Map<String, String> map = new HashMap();
									for (int m = 0; m < child4.size(); m++) {
										Element child4et = (Element) child4.get(m);
										map.put(child4et.getName().toString(), child4et.getStringValue());
									}
									list.add(map);
								}
							}
							ret.put(child1et.getName(), list);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	   public static Map<String, Object> Dom2Map(Document doc) {
		         Map<String, Object> map = new HashMap<String, Object>();
		          if (doc == null)
		                return map;
		           Element root = doc.getRootElement();
		           for (Iterator iterator = root.elementIterator(); iterator.hasNext(); ) {
		                Element e = (Element) iterator.next();
		               List list = e.elements();
		               if (list.size() > 0) {
		                   map.put(e.getName(), xml2map(e));
		             } else
		                  map.put(e.getName(), e.getText());
		           }
		           return map;
		        }
}
