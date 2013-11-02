package com.qinnuan.common.util;

import android.content.Context;

import com.qinnuan.common.widget.Face;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FaceParser {

	// private HashMap<String, ArrayList<String>> emoMap = new HashMap<String,
	// ArrayList<String>>();
    private static List<Face> faces  ;
	public static List<Face> readMap(Context mContext) {
        if(faces!=null){
            return faces ;
        }else{
			faces = new ArrayList<Face>();
			XmlPullParser xmlpull = null;
			try {
				XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
				xmlpull = xppf.newPullParser();
                String[] files = mContext.getAssets().list(".") ;
				InputStream stream = mContext.getAssets().open("face.xml");
				xmlpull.setInput(stream, "UTF-8");
				int eventCode = xmlpull.getEventType();
				Face face = null;
				while (eventCode != XmlPullParser.END_DOCUMENT) {
					switch (eventCode) {
					case XmlPullParser.START_DOCUMENT: {
						break;
					}
					case XmlPullParser.START_TAG: {
						if (xmlpull.getName().equals("key")) {
							face.key = xmlpull.nextText();
                        }else if(xmlpull.getName().equals("face")){
                            face=new Face();
                        }else if (xmlpull.getName().equals("value")) {
							face.value = xmlpull.nextText();
						}
						break;
					}
					
					case XmlPullParser.END_TAG: {
						if (xmlpull.getName().equals("face")) {
							faces.add(face);	
						}
						break;
					}
					case XmlPullParser.END_DOCUMENT: {
						System.out.println("parse emoji complete");
						break;
					}
					}
					eventCode = xmlpull.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return faces;
	}
}
