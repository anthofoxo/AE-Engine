package cc.antho.ae.guii;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class UIGridLayout implements UILayoutManager {

	public int w, h;
	public float gap;

	public void layout(UIContainer container) {

		float ew = (container.getSize().x - (w - 1) * gap) / (float) w;
		float eh = (container.getSize().y - (h - 1) * gap) / (float) h;

		for (int i = 0; i < container.getComponentKeys().size(); i++) {

			UIComponent key = container.getComponentKeys().get(i);
			Object value = container.getComponentValues().get(i);

			int x = 0;
			int y = 0;
			int w = 1;
			int h = 1;

			if (value instanceof UIGridConstraints) {

				UIGridConstraints c = (UIGridConstraints) value;
				x = c.x;
				y = c.y;
				w = c.w;
				h = c.h;

			}

			float tw = (w * ew) + (w - 1) * gap;
			float th = (h * eh) + (h - 1) * gap;
			float fx = x * (ew + gap);
			float fy = y * (eh + gap);

			key.getSize().set(tw, th);
			key.getPosition().set(container.getPosition()).add(fx, fy);

		}

	}

}
