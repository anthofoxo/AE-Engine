package cc.antho.ae.gui;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.system.MemoryStack.*;

import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.system.MemoryStack;

public class UITexture extends UIComponent {

	private int texture;

	public UITexture(int texture) {

		this.texture = texture;

	}

	public void render() {

		try (MemoryStack stack = stackPush()) {

			NVGPaint paint = NVGPaint.mallocStack(stack);
			nvgImagePattern(context.handle, position.x, position.y, size.x, size.y, 0, texture, 1F, paint);

			nvgBeginPath(context.handle);
			nvgFillPaint(context.handle, paint);
			nvgRect(context.handle, position.x, position.y, size.x, size.y);
			nvgFill(context.handle);

		}

	}

}
