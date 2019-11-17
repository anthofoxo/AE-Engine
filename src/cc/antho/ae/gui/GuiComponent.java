package cc.antho.ae.gui;

import static org.lwjgl.nanovg.NanoVG.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.antho.abstractwindow.event.window.input.mouse.EventWindowMouseMoved;
import cc.antho.ae.math.Maths;
import cc.antho.ae.renderer.color.Color;
import cc.antho.ae.renderer.color.Colors;

public abstract class GuiComponent {

	public float x, y, w = 100, h = 100;
	public Color color = new Color(Colors.WHITE);
	public boolean enabled = true, visible = true;

	public GuiLayoutManager layout = new GuiFillLayout();
	public List<GuiComponent> children = new ArrayList<>();

	private float lastW = w, lastH = h;
	private boolean layoutContainer = false;

	public float mouseX, mouseY;
	public boolean mouseInside;

	public Map<String, Float> style = new HashMap<>();

	public void add(GuiComponent component) {

		children.add(component);
		layoutContainer = true;

	}

	public void remove(GuiComponent component) {

		children.remove(component);
		layoutContainer = true;

	}

	public void clear() {

		children.clear();
		layoutContainer = true;

	}

	public void render(GuiContext ctx) {

		if (!visible) return;

		if (lastW != w || lastH != h) {

			lastW = w;
			lastH = h;

			layoutContainer = true;

		}

		if (layoutContainer && layout != null) {

			layout.layout(this);
			layoutContainer = false;

		}

		nvgSave(ctx.handle);
		nvgIntersectScissor(ctx.handle, x, y, w, h);

		renderComponent(ctx);

		for (int i = 0; i < children.size(); i++)
			children.get(i).render(ctx);

		nvgRestore(ctx.handle);

	}

	public abstract void renderComponent(GuiContext ctx);

	public void onMouseMove(EventWindowMouseMoved event) {

		mouseX = Maths.map(event.x, x, x + w, 0, 1);
		mouseY = Maths.map(event.y, y, y + h, 1, 0);
		mouseInside = mouseX > 0f && mouseX < 1f && mouseY > 0f && mouseY < 1f;

		for (int i = 0; i < children.size(); i++)
			children.get(i).onMouseMove(event);

	}

}
