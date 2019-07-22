package cc.antho.ae.renderer.gl.shader.parse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LinkInstance {

	public String type, name, value;

	public String toString() {

		return type + " " + name + " = " + value;

	}

}
