package cc.antho.ae.gui;

import static org.lwjgl.nanovg.NanoVG.*;

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryStack;

public class GuiText extends GuiComponent {

	public String text;
	public GuiAlignment horzAlign = GuiAlignment.CENTER;
	public GuiAlignment vertAlign = GuiAlignment.MIDDLE;
	public GuiFont font = new GuiFont();

	public GuiText(String text) {

		this.text = text;

	}

	public void renderComponent(GuiContext ctx) {

		float x;
		float y;

		switch (horzAlign) {

			case LEFT:
				x = this.x;
				break;
			case CENTER:
				x = this.x + this.w / 2f;
				break;
			case RIGHT:
				x = this.x + this.w;
				break;
			default:
				x = 0;
				break;

		}

		switch (vertAlign) {

			case TOP:
				y = this.y;
				break;
			case MIDDLE:
				y = this.y + this.h / 2f;
				break;
			case BOTTOM:
				y = this.y + this.h;
				break;
			default:
				y = 0;
				break;

		}

		try (MemoryStack stack = MemoryStack.stackPush()) {

			String t = text;
			if (t == null || t.isEmpty()) t = " ";

			ByteBuffer buffer = stack.ASCII(t, false);

			NVGColor nvgColor = NVGColor.mallocStack(stack);
			nvgColor.r(color.r);
			nvgColor.g(color.g);
			nvgColor.b(color.b);
			nvgColor.a(color.a);

			nvgFillColor(ctx.handle, nvgColor);
			nvgTextAlign(ctx.handle, horzAlign.mask | vertAlign.mask);
			nvgFontSize(ctx.handle, font.size);
			nvgFontFace(ctx.handle, font.face);
			nvgText(ctx.handle, x, y, buffer);

		}

	}

}