package cc.antho.ae.gui.layout;

import cc.antho.ae.gui.UIComponent;
import cc.antho.ae.gui.UIContainer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UIGridLayout implements UILayoutManager {

	@Getter private final int w, h, gap;

	public void layout(UIContainer container) {

		float ew = (container.getSize().x - (w - 1) * gap) / (float) w;
		float eh = (container.getSize().y - (h - 1) * gap) / (float) h;

		for (UIComponent component : container.getComponents().keySet()) {

			if (component == null) continue;

			Object constraints = container.getComponents().get(component);

			int x = 0;
			int y = 0;
			int w = 1;
			int h = 1;

			if (constraints instanceof UIGridConstraints) {

				UIGridConstraints c = (UIGridConstraints) constraints;
				x = c.x;
				y = c.y;
				w = c.w;
				h = c.h;

			}

			float tw = (w * ew) + (w - 1) * gap;
			float th = (h * eh) + (h - 1) * gap;

			component.getSize().set(tw, th);

			// (x * ew) + (x * gap)
			float fx = x * (ew + gap);

			// (y * eh) + (y * gap)
			float fy = y * (eh + gap);

			component.getPosition().set(fx + container.getPosition().x, fy + container.getPosition().y);

		}

	}

}
