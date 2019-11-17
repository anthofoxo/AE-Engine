package cc.antho.ae.gui;

public class GuiFlexLayout implements GuiLayoutManager {

	public GuiDirection direction = GuiDirection.HORIZONTAL;

	@Override
	public void layout(GuiComponent parent) {

		float availableSpace = direction == GuiDirection.HORIZONTAL ? parent.w : parent.h;
		float totalUsage = 0f;

		for (int i = 0; i < parent.children.size(); i++) {

			GuiComponent child = parent.children.get(i);

			float grow = child.style.getOrDefault("grow", 0f);

			if (grow <= 0f) availableSpace -= direction == GuiDirection.HORIZONTAL ? child.w : child.h;
			else totalUsage += grow;

		}

		float current = direction == GuiDirection.HORIZONTAL ? parent.x : parent.y;

		for (int i = 0; i < parent.children.size(); i++) {

			GuiComponent child = parent.children.get(i);

			float grow = child.style.getOrDefault("grow", 0f);

			if (direction == GuiDirection.HORIZONTAL) child.x = current;
			else child.y = current;

			if (grow > 0f) {

				if (direction == GuiDirection.HORIZONTAL) child.w = grow / totalUsage * availableSpace;
				else child.h = grow / totalUsage * availableSpace;

			}

			if (direction == GuiDirection.HORIZONTAL) {

				child.y = parent.y;
				child.h = parent.h;
				current += child.w;

			} else {

				child.x = parent.x;
				child.w = parent.w;
				current += child.h;

			}

		}

	}

}
