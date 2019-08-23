package cc.antho.ae.guii;

import static org.lwjgl.nanovg.NanoVG.*;

import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryStack;

public  class UIText extends UIComponent {

	public String text;
	public UIAlignment horzAlign = UIAlignment.CENTER;
	public UIAlignment vertAlign = UIAlignment.MIDDLE;
	public UIFont font = new UIFont();

	public UIText(String text) {

		this.text = text;

	}

	public void render() {

		float x;
		float y;

		switch (horzAlign) {

			case LEFT:
				x = position.x;
				break;
			case CENTER:
				x = position.x + size.x / 2F;
				break;
			case RIGHT:
				x = position.x + size.x;
				break;
			default:
				x = 0;
				break;

		}

		switch (vertAlign) {

			case TOP:
				y = position.y;
				break;
			case MIDDLE:
				y = position.y + size.y / 2F;
				break;
			case BOTTOM:
				y = position.y + size.y;
				break;
			default:
				y = 0;
				break;

		}

		try (MemoryStack stack = MemoryStack.stackPush()) {

			String t = text;
			if (t == null || t.isEmpty()) t = " ";

			ByteBuffer buffer = stack.ASCII(t, false);

			nvgFillColor(context.handle, context.color(color));
			nvgTextAlign(context.handle, horzAlign.mask | vertAlign.mask);
			nvgFontSize(context.handle, font.size);
			nvgFontFace(context.handle, font.face);
			nvgText(context.handle, x, y, buffer);

		}

	}

}
