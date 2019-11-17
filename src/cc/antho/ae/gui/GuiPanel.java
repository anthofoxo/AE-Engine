package cc.antho.ae.gui;

import static org.lwjgl.nanovg.NanoVG.*;

import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryStack.*;

import org.lwjgl.nanovg.NVGColor;

public class GuiPanel extends GuiComponent {

	@Override
	public void renderComponent(GuiContext ctx) {

		try (MemoryStack stack = stackPush()) {

			NVGColor nvgColor = NVGColor.mallocStack(stack);
			nvgColor.r(color.r);
			nvgColor.g(color.g);
			nvgColor.b(color.b);
			nvgColor.a(color.a);

			nvgBeginPath(ctx.handle);
			nvgFillColor(ctx.handle, nvgColor);
			nvgRect(ctx.handle, x, y, w, h);
			nvgFill(ctx.handle);

		}

	}

}
