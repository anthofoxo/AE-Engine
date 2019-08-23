package cc.antho.ae.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import cc.antho.ae.gui.util.RectangleF;
import cc.antho.eventsystem.EventCallback;
import lombok.Getter;
import lombok.Setter;

@Deprecated
public class UIComponent {

	@Getter @Setter protected Vector2f position = new Vector2f();
	@Getter @Setter protected Vector2f size = new Vector2f(100F);
	@Getter @Setter protected boolean focused;
	@Getter @Setter protected UIBorder border = new UIBorder();
	@Getter @Setter protected boolean visible = true;
	@Getter @Setter protected UIFont font = new UIFont();

	@Getter private List<EventCallback> clickCallbacks = new ArrayList<>();

	public void tick(UIMaster owner) {

	}

	public void render(UIMaster owner) {

		border.render(owner);

	}

	public void onKeyPress(int key) {

	}

	public void onKeyRelease(int key) {

	}

	public void onKeyRepeat(int key) {

	}

	public void onChar(char key) {

	}

	public RectangleF getBounds() {

		return new RectangleF(position, size);

	}

}
