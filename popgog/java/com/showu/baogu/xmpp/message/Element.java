package com.showu.baogu.xmpp.message;

import java.util.ArrayList;
import java.util.List;

public class Element {
	private String name;
	private Object value = "";
	private List<Element> elements = new ArrayList<Element>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public List<Element> getElements() {
		return elements;
	}



	public Element(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public Element() {
		// TODO Auto-generated constructor stub
	}

	public String toXml() {
		StringBuffer sb = new StringBuffer();
			sb.append("<" + name);
			sb.append(">");
			sb.append(value);
			for (Element e : elements) {
				sb.append(e.toXml());
			}
			sb.append("</" + name + ">");
		return sb.toString();
	}
}
