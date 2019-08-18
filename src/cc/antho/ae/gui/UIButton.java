package cc.antho.ae.gui;

import cc.antho.ae.gui.UIText.HAlignment;
import cc.antho.ae.gui.UIText.VAlignment;
import cc.antho.ae.math.Maths;
import cc.antho.ae.renderer.color.Color;

public class UIButton extends UIComponent {

	private Color background = new Color(.9F, .9F, .9F);
	private Color foreground = new Color(0F, 0F, 0F);

	private Color background_unfocus = new Color(.9F, .9F, .9F);
	private Color background_focus = new Color(.9F, .95F, 1F);

	private Color border_unfocus = new Color(.7F, .7F, .7F);
	private Color border_focus = new Color(0F, .5F, .85F);

	private UIPanel panel = new UIPanel();
	private UIText text = new UIText();

	public UIButton(String text) {

		this();
		setText(text);

	}

	public UIButton() {

		panel.setPosition(position);
		panel.setSize(size);
		panel.setColor(background);

		text.setColor(foreground);
		text.setHAlign(HAlignment.CENTER);
		text.setVAlign(VAlignment.MIDDLE);

		getBorder().setSize(1);

	}

	public void setText(String text) {

		this.text.setText(text);

	}

	public String getText() {

		return this.text.getText();

	}

	public void tick(UIMaster owner) {

		text.getFont().setSize(getSize().y - 8);

		if (getBounds().contains(owner.getInput().getRawCursorPosition().x, owner.getInput().getRawCursorPosition().y) || focused) {

			if (owner.getInput().getMouseLeft().isDown()) {

				owner.clearFocus();
				focused = true;

			}

			background.set(Maths.approachSmooth(background, background_focus, (float) (owner.getDelta() * 5F)));
			border.getColor().set(Maths.approachSmooth(border.getColor(), border_focus, (float) (owner.getDelta() * 5F)));

		} else {

			background.set(Maths.approachSmooth(background, background_unfocus, (float) (owner.getDelta() * 5F)));
			border.getColor().set(Maths.approachSmooth(border.getColor(), border_unfocus, (float) (owner.getDelta() * 5F)));

		}

	}

	public void render(UIMaster owner) {

		panel.render(owner);

		text.getPosition().set(position);
		text.getPosition().add(size.x / 2F, size.y / 2F);

		text.render(owner);

		super.render(owner);

	}

}
