package cc.antho.ae.renderer.gl.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.antho.ae.renderer.gl.GLRenderer;
import cc.antho.ae.renderer.gl.GLShaderProgram;
import cc.antho.common.Util;

public class ShaderParserInst {

	private static final int MODE_HEADER = 0;
	private static final int MODE_VERTEX = 1;
	private static final int MODE_FRAGMENT = 2;

	private ShaderParser parser;

	private String content;

	private StringBuilder header = new StringBuilder();
	private StringBuilder vertex_main = new StringBuilder();
	private StringBuilder fragment_main = new StringBuilder();

	private StringBuilder vertex_final = new StringBuilder();
	private StringBuilder fragment_final = new StringBuilder();

	private Map<LinkInstance, LinkInstance> autoLinks = new HashMap<>();

	private List<LinkInstance> inputs = new ArrayList<>();
	private List<LinkInstance> links = new ArrayList<>();
	private List<LinkInstance> outputs = new ArrayList<>();
	private List<LinkInstance> uniforms = new ArrayList<>();
	private List<LinkInstance> consts = new ArrayList<>();

	public ShaderParserInst(ShaderParser parser, String file) throws IOException {

		this.parser = parser;
		content = Util.loadString(Util.getStream(file));

		parseVars();
		parseAutoLink();
		splitSections();
		parseHeader();
		convertAutoLinks();
		createVertex();
		createFragment();

	}

	private void parseVars() {

		for (String key : parser.vars.keySet())
			content = content.replace("@var{" + key + "}", parser.vars.get(key));

	}

	private void parseAutoLink() {

		Pattern pattern = Pattern.compile("@link\\{.+\\}");

		// Keep searching in the source code for @link until there are none found
		while (true) {

			// We have to recreate this as we replace content
			Matcher matcher = pattern.matcher(content);

			// Parse the code if a match is found, finish searching otherwise
			if (matcher.find()) {

				// ex: @link{in_position}
				String match_whole = content.substring(matcher.start(), matcher.end());

				// ex: in_position
				String match_part = match_whole.substring(6, match_whole.length() - 1);

				// Replace @link{in_position} with in_position in source code
				content = content.substring(0, matcher.start()) + match_part + content.substring(matcher.end());

				String[] tokens = match_part.split("\\s+");

				// Add to content list
				autoLinks.put(new LinkInstance(tokens[0], tokens[1], null), new LinkInstance(tokens[0], tokens[1], null));

			} else break;

		}

	}

	private void splitSections() {

		String[] lines = content.split("\n");

		int mode = MODE_HEADER;

		for (String line : lines) {

			// For each line check if we exit the header or an existing section, is we
			// change section, adjust the mode and skip this line
			if (line.strip().equals("@vertex")) {

				mode = MODE_VERTEX;
				continue;

			} else if (line.strip().equals("@fragment")) {

				mode = MODE_FRAGMENT;
				continue;

			}

			// Depending on the mode we will pick a different source to add to
			StringBuilder target = null;

			switch (mode) {

				case MODE_HEADER:
					target = header;
					break;
				case MODE_VERTEX:
					target = vertex_main;
					break;
				case MODE_FRAGMENT:
					target = fragment_main;
					break;

			}

			// Add this line into whatever source it should goto
			target.append(line + "\n");

		}

	}

	private void parseHeader() {

		// Split the header by each statement
		String[] statements = header.toString().split(";");

		// Loop through each statement and add them to their lists
		for (String statement : statements) {

			// Strip whitespace off the statement and find the first part
			String stripped = statement.strip();
			if (stripped.isBlank()) continue;

			String start = stripped.split("\\s+")[0];

			String[] tokens = stripped.split("\\s+");
			String type = tokens[1];
			String name = tokens[2];
			String value = null;

			// Pick the correct list depending on what the token starts with
			List<LinkInstance> target = null;

			switch (start) {

				case "in":
					target = inputs;
					break;
				case "link":
					target = links;
					break;
				case "out":
					target = outputs;
					break;
				case "uniform":
					target = uniforms;
					break;
				case "const":
					target = consts;
					break;

			}

			if (stripped.contains("=")) value = tokens[4];

			// Add the line to the list, adding the ;
			target.add(new LinkInstance(type, name, value));

		}

	}

	private void convertAutoLinks() {

		for (LinkInstance inst : autoLinks.keySet()) {

			String name = inst.name;
			String newname;

			if (!name.contains("_")) newname = "v_" + name;
			else newname = "v" + name.substring(name.indexOf('_'));

			LinkInstance val = autoLinks.get(inst);
			val.name = newname;

			links.add(val);

		}

	}

	private void createVertex() {

		vertex_final.append(parser.getHeader());
		vertex_final.append("\n\n");

		for (LinkInstance link : inputs) {

			if (link.value != null) {

				vertex_final.append("layout(location = ");
				vertex_final.append(link.value);
				vertex_final.append(") ");

			}

			vertex_final.append("in ");
			vertex_final.append(link.type);
			vertex_final.append(" ");
			vertex_final.append(link.name);
			vertex_final.append(";\n");

		}

		vertex_final.append("\n");

		for (LinkInstance link : links) {

			vertex_final.append("out ");
			vertex_final.append(link.type);
			vertex_final.append(" ");
			vertex_final.append(link.name);
			vertex_final.append(";\n");

		}

		vertex_final.append("\n");

		for (LinkInstance link : uniforms) {

			vertex_final.append("uniform ");
			vertex_final.append(link.type);
			vertex_final.append(" ");
			vertex_final.append(link.name);
			vertex_final.append(";\n");

		}

		vertex_final.append("\n");

		for (LinkInstance link : consts) {

			vertex_final.append("const ");
			vertex_final.append(link.type);
			vertex_final.append(" ");
			vertex_final.append(link.name);
			vertex_final.append(" = ");
			vertex_final.append(link.value);
			vertex_final.append(";\n");

		}

		vertex_final.append("\n");

		for (LinkInstance link : autoLinks.keySet()) {

			vertex_main.append("\t");
			vertex_main.append(autoLinks.get(link).name);
			vertex_main.append(" = ");
			vertex_main.append(link.name);
			vertex_main.append(";\n");

		}

		vertex_main.append("}");

		vertex_main.insert(0, "void main() {\n");

		vertex_final.append(vertex_main.toString());

	}

	private void createFragment() {

		fragment_final.append(parser.getHeader());
		fragment_final.append("\n\n");

		for (LinkInstance link : links) {

			fragment_final.append("in ");
			fragment_final.append(link.type);
			fragment_final.append(" ");
			fragment_final.append(link.name);
			fragment_final.append(";\n");

		}

		fragment_final.append("\n");

		for (LinkInstance link : outputs) {

			if (link.value != null) {

				fragment_final.append("layout(location = ");
				fragment_final.append(link.value);
				fragment_final.append(") ");

			}

			fragment_final.append("out ");
			fragment_final.append(link.type);
			fragment_final.append(" ");
			fragment_final.append(link.name);
			fragment_final.append(";\n");

		}

		fragment_final.append("\n");

		for (LinkInstance link : uniforms) {

			fragment_final.append("uniform ");
			fragment_final.append(link.type);
			fragment_final.append(" ");
			fragment_final.append(link.name);
			fragment_final.append(";\n");

		}

		fragment_final.append("\n");

		for (LinkInstance link : consts) {

			fragment_final.append("const ");
			fragment_final.append(link.type);
			fragment_final.append(" ");
			fragment_final.append(link.name);
			fragment_final.append(" = ");
			fragment_final.append(link.value);
			fragment_final.append(";\n");

		}

		fragment_main.append("}");

		fragment_main.insert(0, "void main() {\n");

		fragment_final.append(fragment_main.toString());

	}

	public GLShaderProgram getProgram(GLRenderer renderer) {

		GLShaderProgram program = renderer.genProgramDirect(vertex_final.toString(), fragment_final.toString());

		program.bind();

		for (LinkInstance link : uniforms) {

			if (link.value == null) continue;

			if (link.type.equals("sampler2D")) {

				try {

					program.uniform1i(link.name, Integer.parseInt(link.value));

				} catch (NumberFormatException e) {

					System.err.println("Unsupported auto uniform loader value: " + link.value + " for type: " + link.type);

				}

			} else System.err.println("Unsupported auto uniform loader: " + link.type);

		}

		return program;

	}

}
