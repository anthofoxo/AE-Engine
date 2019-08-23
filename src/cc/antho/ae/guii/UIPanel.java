package cc.antho.ae.guii;

import static org.lwjgl.nanovg.NanoVG.*;

import cc.antho.ae.renderer.color.Color;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UIPanel extends UIComponent {

	public UIPanel(Color color) {

		getColor().set(color);

	}

	public void render() {

		nvgBeginPath(context.handle);
		nvgFillColor(context.handle, context.color(color));
		nvgRect(context.handle, position.x, position.y, size.x, size.y);
		nvgFill(context.handle);

	}

}
