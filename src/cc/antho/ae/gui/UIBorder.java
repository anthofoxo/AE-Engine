package cc.antho.ae.gui;

import static org.lwjgl.nanovg.NanoVG.*;

import org.lwjgl.nanovg.NVGColor;

import cc.antho.ae.renderer.color.Color;
import lombok.Getter;
import lombok.Setter;

@Deprecated
public class UIBorder {

	@Getter @Setter private Color color = new Color(.7F, .7F, .7F);
	@Getter @Setter private int size = 0;

	private NVGColor internalColor = NVGColor.create();

	public void render(UIMaster owner) {

		color.store(internalColor);
		nvgStrokeColor(owner.getHandle(), internalColor);
		nvgStrokeWidth(owner.getHandle(), size);
		nvgStroke(owner.getHandle());

	}

}
