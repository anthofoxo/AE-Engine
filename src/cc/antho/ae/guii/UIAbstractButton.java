package cc.antho.ae.guii;

import java.util.ArrayList;
import java.util.List;

import cc.antho.ae.events.window.EventWindowMousePress;
import cc.antho.eventsystem.EventCallback;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UIAbstractButton extends UIComponent {

	private List<EventCallback> callbacks = new ArrayList<>();

	public UIAbstractButton(EventCallback... callbacks) {

		addClickCallbacks(callbacks);

	}

	public void addClickCallbacks(EventCallback... callbacks) {

		for (int i = 0; i < callbacks.length; i++)
			this.callbacks.add(callbacks[i]);

	}

	public void removeClickCallbacks(EventCallback... callbacks) {

		for (int i = 0; i < callbacks.length; i++)
			this.callbacks.remove(callbacks[i]);

	}

	protected void onEventWindowMousePress(EventWindowMousePress event) {

		for (int i = 0; i < callbacks.size(); i++)
			callbacks.get(i).callback();

	}

	public void render() {

	}

}
