package cc.antho.ae.gui;

import static org.lwjgl.nanovg.NanoVG.*;

import java.nio.ByteBuffer;

import org.joml.Vector2f;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryStack;

import cc.antho.ae.gui.UIText.HAlignment;
import cc.antho.ae.gui.UIText.VAlignment;
import cc.antho.ae.renderer.color.Color;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Deprecated
public class UITextBox extends UIComponent {

	@Getter @Setter private Color color = new Color(1F, 1F, 1F);
	private NVGColor internalColor = NVGColor.create();
	@Getter @Setter private String text = "UIText";

	@Getter @Setter HAlignment hAlign = HAlignment.CENTER;
	@Getter @Setter VAlignment vAlign = VAlignment.MIDDLE;

	public UITextBox(String text) {

		setText(text);

	}

	public void render(UIMaster owner) {

		Vector2f position = new Vector2f(this.position);
		position.add(size.x / 2F, size.y / 2F);

		color.store(internalColor);

		try (MemoryStack stack = MemoryStack.stackPush()) {

			String t = text;
			if (t == null || t.isEmpty()) t = " ";

			ByteBuffer buffer = stack.ASCII(t);

			nvgFillColor(owner.getHandle(), internalColor);
			nvgTextAlign(owner.getHandle(), hAlign.mask | vAlign.mask);
			nvgFontSize(owner.getHandle(), font.getSize());
			nvgFontFace(owner.getHandle(), font.getFace());
			nvgText(owner.getHandle(), position.x, position.y, buffer);

		}

	}

}
