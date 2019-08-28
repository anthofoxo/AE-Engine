package cc.antho.ae.gui;

import org.joml.Vector2f;

import cc.antho.ae.events.window.EventWindowMouseMoved;
import cc.antho.ae.events.window.EventWindowMousePress;
import cc.antho.ae.gui.util.RectangleF;
import cc.antho.ae.renderer.color.Color;
import cc.antho.ae.renderer.color.Colors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public abstract class UIComponent {

	@Getter @Setter protected Vector2f position = new Vector2f();
	@Getter @Setter protected Vector2f size = new Vector2f(100F);
	@Getter @Setter protected Color color = new Color(Colors.WHITE);
	@Getter @Setter private boolean visible = true;

	private boolean containsMouse = false;

	protected UIContext context;

	void context(UIContext context) {

		this.context = context;

	}

	final void _onEventWindowMouseMoved(EventWindowMouseMoved event) {

		RectangleF rect = new RectangleF(position, size);
		containsMouse = rect.contains(event.getX(), event.getY());

		if (containsMouse) onEventWindowMouseMoved(event);

	}

	protected void onEventWindowMouseMoved(EventWindowMouseMoved event) {

	}

	final void _onEventWindowMousePress(EventWindowMousePress event) {

		if (containsMouse) onEventWindowMousePress(event);

	}

	protected void onEventWindowMousePress(EventWindowMousePress event) {

	}

	final void _render() {

		if (!visible) return;
		render();

	}

	public abstract void render();

}
