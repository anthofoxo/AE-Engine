package cc.antho.ae.gui;

public class GuiFillLayout implements GuiLayoutManager {

	public float margin;

	public GuiFillLayout() {

		this(0f);

	}

	public GuiFillLayout(float border) {

		this.margin = border;

	}

	@Override
	public void layout(GuiComponent parent) {

		for (int i = 0; i < parent.children.size(); i++) {

			GuiComponent child = parent.children.get(i);
			child.x = parent.x + margin;
			child.y = parent.y + margin;
			child.w = parent.w - margin * 2f;
			child.h = parent.h - margin * 2f;

		}

	}

}
