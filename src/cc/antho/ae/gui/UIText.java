package cc.antho.ae.gui;

import static org.lwjgl.nanovg.NanoVG.*;

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryStack;

import cc.antho.ae.gl.color.Color;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class UIText extends UIComponent {

	@Getter @Setter private Color color = new Color(1F, 1F, 1F);
	private NVGColor internalColor = NVGColor.create();
	@Getter @Setter private String text = "UIText";

	@Getter @Setter HAlignment hAlign = HAlignment.LEFT;
	@Getter @Setter VAlignment vAlign = VAlignment.TOP;

	public enum HAlignment {

		LEFT(NVG_ALIGN_LEFT), CENTER(NVG_ALIGN_CENTER), TOP(NVG_ALIGN_TOP);

		int mask;

		private HAlignment(int mask) {

			this.mask = mask;

		}

	}

	public enum VAlignment {

		TOP(NVG_ALIGN_TOP), MIDDLE(NVG_ALIGN_MIDDLE), BOTTOM(NVG_ALIGN_BOTTOM);

		int mask;

		private VAlignment(int mask) {

			this.mask = mask;

		}

	}

	public UIText(String text) {

		setText(text);

	}

	public void render(UIMaster owner) {

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
