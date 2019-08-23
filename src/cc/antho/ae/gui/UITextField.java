package cc.antho.ae.gui;

import static org.lwjgl.nanovg.NanoVG.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryStack;

import cc.antho.ae.gui.UIText.HAlignment;
import cc.antho.ae.gui.UIText.VAlignment;
import cc.antho.ae.renderer.color.Color;
import cc.antho.ae.renderer.color.Colors;

@Deprecated
public class UITextField extends UIComponent {

	private Color background = new Color(1F, 1F, 1F);
	private Color foreground = new Color(0F, 0F, 0F);

	private UIPanel panel = new UIPanel();
	private UIText text = new UIText();

	private NVGColor cursorColor = NVGColor.create();

	public UITextField() {

		panel.setPosition(position);
		panel.setSize(size);
		panel.setColor(background);

		panel.getBorder().setSize(1);

		text.setColor(foreground);
		text.setHAlign(HAlignment.LEFT);
		text.setVAlign(VAlignment.MIDDLE);

	}

	public void onChar(char key) {

		if (!focused) return;

		setText(getText() + key);

	}

	public void onKeyRepeat(int key) {

		onKeyPress(key);

	}

	public void onKeyPress(int key) {

		if (!focused) return;

		if (key == GLFW.GLFW_KEY_BACKSPACE) {

			if (getText().length() > 0) {

				setText(getText().substring(0, getText().length() - 1));

			}

		}

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

		}

	}

	public void render(UIMaster owner) {

		panel.render(owner);

		text.getPosition().set(position);
		text.getPosition().add(0F, size.y / 2F);

		text.render(owner);

		if (focused && System.currentTimeMillis() % 1000 > 500) {

			try (MemoryStack stack = MemoryStack.stackPush()) {

				ByteBuffer buffer = stack.ASCII(text.getText());

				FloatBuffer bounds = stack.mallocFloat(4);

				float width = nvgTextBounds(owner.getHandle(), text.getPosition().x, text.getPosition().y, buffer, bounds);
				float height = bounds.get(3) - bounds.get(1);

				Colors.BLACK.store(cursorColor);
				nvgStrokeColor(owner.getHandle(), cursorColor);

				nvgSave(owner.getHandle());

				nvgTranslate(owner.getHandle(), position.x + width, position.y - height / 2F + size.y / 2F);

				nvgBeginPath(owner.getHandle());
				nvgMoveTo(owner.getHandle(), 0, 0);
				nvgLineTo(owner.getHandle(), 0, height);
				nvgStrokeWidth(owner.getHandle(), 1);
				nvgStroke(owner.getHandle());

				nvgRestore(owner.getHandle());

			}

		}

	}

}
