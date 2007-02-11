package com.quantum.editors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorManager {

	protected Map colorTable = new HashMap(10);

	public void dispose() {
		Iterator e= this.colorTable.values().iterator();
		while (e.hasNext()) {
			((Color) e.next()).dispose();
		}
	}
	public Color getColor(RGB rgb) {
		Color color= (Color) colorTable.get(rgb);
		if (color == null) {
			color= new Color(Display.getCurrent(), rgb);
			this.colorTable.put(rgb, color);
		}
		return color;
	}
}
