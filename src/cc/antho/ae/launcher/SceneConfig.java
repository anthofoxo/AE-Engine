package cc.antho.ae.launcher;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;

import cc.antho.ae.common.Util;

public class SceneConfig {

	public final String music;
	public final String model;
	public final Vector3f position;

	public SceneConfig(String path) throws FileNotFoundException, IOException {

		byte[] bytes = Util.loadByteArray(Util.getStream(path));
		JSONObject root = new JSONObject(new String(bytes));

		music = root.optString("music", null);

		model = root.getString("model");
		JSONArray array = root.getJSONArray("position");
		position = new Vector3f();
		position.x = array.getFloat(0);
		position.y = array.getFloat(1);
		position.z = array.getFloat(2);

	}

}
