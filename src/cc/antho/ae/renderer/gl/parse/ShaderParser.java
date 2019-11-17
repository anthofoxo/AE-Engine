package cc.antho.ae.renderer.gl.parse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cc.antho.ae.renderer.gl.GLRenderer;
import cc.antho.ae.renderer.gl.GLShaderProgram;
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

	public GLShaderProgram parse(String file, GLRenderer renderer) throws IOException {

		return new ShaderParserInst(this, file).getProgram(renderer);

	}

}
