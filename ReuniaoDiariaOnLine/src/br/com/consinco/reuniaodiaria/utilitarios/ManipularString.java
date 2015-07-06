package br.com.consinco.reuniaodiaria.utilitarios;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

public class ManipularString {
	
	public static String convertTextRTF2HTML(String text) {
		if (text == null) {
			return "";
		}
		StringReader reader = new StringReader(text);
		StringWriter writer = new StringWriter();
		RTFEditorKit rtfEditorKit = new RTFEditorKit();
		HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
		Document doc = rtfEditorKit.createDefaultDocument();
		try {
			rtfEditorKit.read(reader, doc, 0);
			htmlEditorKit.write(writer, doc, 0, doc.getLength());
		} catch (IOException ex) {
			return "";
		} catch (BadLocationException ex) {
			return "";
		}
		return writer.toString();
	}
}
