package cc.antho.ae.guii;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public final class UIFillLayout implements UILayoutManager {

	public float border = 0F;

	public void layout(UIContainer container) {

		for (UIComponent component : container.getComponentKeys()) {

			component.getSize().set(container.getSize()).sub(border * 2F, border * 2F);
			component.getPosition().set(container.getPosition()).add(border, border);

		}

	}

}
