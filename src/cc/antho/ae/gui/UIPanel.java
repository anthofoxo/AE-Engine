package cc.antho.ae.gui;

import static org.lwjgl.nanovg.NanoVG.*;

import org.lwjgl.nanovg.NVGColor;

import cc.antho.ae.renderer.color.Color;
import lombok.Getter;
import lombok.Setter;

public class UIPanel extends UIComponent {

	@Getter @Setter private Color color = new Color(0F, 0F, 0F, .7F);
	private NVGColor internalColor = NVGColor.create();

	public void render(UIMaster owner) {

		color.store(internalColor);

		nvgBeginPath(owner.getHandle());
		nvgFillColor(owner.getHandle(), internalColor);
		nvgRect(owner.getHandle(), position.x, position.y, size.x, size.y);
		nvgFill(owner.getHandle());

		super.render(owner);

	}

}
