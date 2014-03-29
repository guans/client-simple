package com.ubirtls.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.ubirtls.R;
import com.ubirtls.config.IConfigureManager;
import com.ubirtls.config.Setting;
import com.ubirtls.config.XMLConfigurationManager;
import com.ubirtls.config.XMLElement;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

public class ConfigureTest extends Activity {
	private String path = "/sdcard/config.xml";
	private TextView tv = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		tv = (TextView) this.findViewById(R.id.tv);

		IConfigureManager config = new XMLConfigurationManager(path);
		config.loadConfigurationFile();
		// Setting.DEFAULT_X = 100.23;
		// config.saveConfigurationFile();
		String text = new Setting().toString();
		tv.setText(text);
	}

	private void createXmlFile() {
		File linceseFile = new File(path);
		try {
			linceseFile.createNewFile();
		} catch (IOException e) {
			Log.e("IOException", "exception in createNewFile() method");
		}
		FileOutputStream fileos = null;
		try {
			fileos = new FileOutputStream(linceseFile);
		} catch (FileNotFoundException e) {
			Log.e("FileNotFoundException", "can't create FileOutputStream");
		}
		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(fileos, "UTF-8");
			serializer.startDocument(null, true);
			serializer.startTag(null, "setting");
			serializer.startTag(null, XMLElement.LANGUAGE);
			serializer.text(new Integer(1).toString());
			serializer.endTag(null, XMLElement.LANGUAGE);
			serializer.startTag(null, XMLElement.FONTSIZE);
			serializer.text(new Integer(1).toString());
			serializer.endTag(null, XMLElement.FONTSIZE);
			serializer.startTag(null, XMLElement.SHOWMODE);
			serializer.text(new Integer(1).toString());
			serializer.endTag(null, XMLElement.SHOWMODE);

			serializer.startTag(null, XMLElement.VIEWMODE);
			serializer.text(new Integer(1).toString());
			serializer.endTag(null, XMLElement.VIEWMODE);

			serializer.startTag(null, XMLElement.SIMULATIONSPEED);
			serializer.text(new Double(20.45).toString());
			serializer.endTag(null, XMLElement.SIMULATIONSPEED);

			serializer.startTag(null, XMLElement.SCANINTERVAL);
			serializer.text(new Integer(1000).toString());
			serializer.endTag(null, XMLElement.SCANINTERVAL);

			serializer.startTag(null, XMLElement.MONITORMODE);
			serializer.text(new Boolean(true).toString());
			serializer.endTag(null, XMLElement.MONITORMODE);

			serializer.startTag(null, XMLElement.SAVEPASSWORD);
			serializer.text(new Boolean(false).toString());
			serializer.endTag(null, XMLElement.SAVEPASSWORD);
			serializer.startTag(null, XMLElement.USERNAME);
			serializer.text("");
			serializer.endTag(null, XMLElement.USERNAME);
			serializer.startTag(null, XMLElement.PASSWORD);
			serializer.text("");
			serializer.endTag(null, XMLElement.PASSWORD);

			serializer.startTag(null, XMLElement.DEFAULTPOSITION);
			serializer.startTag(null, XMLElement.X);
			serializer.text("32.4");
			serializer.endTag(null, XMLElement.X);
			serializer.startTag(null, XMLElement.Y);
			serializer.text("3.2");
			serializer.endTag(null, XMLElement.Y);
			serializer.startTag(null, XMLElement.Z);
			serializer.text("3.5");
			serializer.endTag(null, XMLElement.Z);
			serializer.endTag(null, XMLElement.DEFAULTPOSITION);

			serializer.startTag(null, XMLElement.SOCKET);
			serializer.startTag(null, XMLElement.IP);
			serializer.text("192.168.1.100");
			serializer.endTag(null, XMLElement.IP);
			serializer.startTag(null, XMLElement.PORT);
			serializer.text(new Integer(4545).toString());
			serializer.endTag(null, XMLElement.PORT);
			serializer.endTag(null, XMLElement.SOCKET);

			serializer.endTag(null, "setting");
			serializer.endDocument();
			serializer.flush();
			fileos.close();
		} catch (Exception e) {
			Log.e("Exception", "error occurred while creating xml file");
		}
	}
}
