package cc.antho.ae.guii;

import static org.lwjgl.nanovg.NanoVG.*;

import cc.antho.ae.renderer.color.Color;

public  class UIRoundedPanel extends UIComponent {

	public float radius;

	public UIRoundedPanel(float radius) {

		this.radius = radius;

	}

	public UIRoundedPanel(float radius, Color color) {

		this.radius = radius;
		this.color = color;

	}

	public void render() {

		nvgBeginPath(context.handle);
		nvgFillColor(context.handle, context.color(color));
		nvgRoundedRect(context.handle, position.x, position.y, size.x, size.y, radius);
		nvgFill(context.handle);

	}

}
