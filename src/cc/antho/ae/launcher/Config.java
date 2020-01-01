package cc.antho.ae.launcher;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONObject;

import cc.antho.common.Util;

public class Config {

	public final String title;
	public final int width, height;
	public final int sources;

	public Config(String path) throws FileNotFoundException, IOException {

		byte[] bytes = Util.loadByteArray(Util.getStream(path));
		JSONObject root = new JSONObject(new String(bytes));

		title = root.getString("title");
		width = root.getInt("width");
		height = root.getInt("height");
		sources = root.getInt("sources");

	}

}
