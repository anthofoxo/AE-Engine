package cc.antho.ae.renderer.gl.shader.parse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cc.antho.ae.renderer.gl.shader.ShaderProgram;
import lombok.Getter;
import lombok.Setter;

public class ShaderParser {

	final Map<String, String> vars = new HashMap<>();
	@Getter @Setter private String header;

	public void setVar(String key, String value) {

		vars.put(key, value);

	}

	public String getVar(String key) {

		return vars.containsKey(key) ? vars.get(key) : null;

	}

	public void clearVar(String key) {

		vars.remove(key);

	}

	public ShaderProgram parse(String file) throws IOException {

		return new ShaderParserInst(this, file).getProgram();

	}

}
