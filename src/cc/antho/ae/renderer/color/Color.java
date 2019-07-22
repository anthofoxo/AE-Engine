package cc.antho.ae.renderer.color;

import org.lwjgl.nanovg.NVGColor;

public class Color {

	public float r, g, b, a = 1F;

	public Color(Color color) {

		set(color);

	}

	public Color() {

		set(0F, 0F, 0F, 1F);

	}

	public Color(float r, float g, float b, float a) {

		set(r, g, b, a);

	}

	public Color(float r, float g, float b) {

		set(r, g, b);

	}

	public void set(Color color) {

		set(color.r, color.g, color.b, color.a);

	}

	public void set(float r, float g, float b, float a) {

		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;

	}

	public void set(float r, float g, float b) {

		this.r = r;
		this.g = g;
		this.b = b;

	}

	public void store(NVGColor color) {

		color.r(r);
		color.g(g);
		color.b(b);
		color.a(a);

	}

}
