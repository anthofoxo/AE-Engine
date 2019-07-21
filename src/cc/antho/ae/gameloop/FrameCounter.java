package cc.antho.ae.gameloop;

import cc.antho.ae.events.EventUpsFpsUpdate;
import cc.antho.eventsystem.EventDispatcher;

public final class FrameCounter {

	private long mark;
	private int ticks;
	private int frames;

	public void mark() {

		mark = System.currentTimeMillis();

	}

	public void addTick() {

		ticks++;

	}

	public void addFrame() {

		frames++;

	}

	public void check() {

		if (System.currentTimeMillis() - mark >= 1000) {

			EventDispatcher.dispatch(new EventUpsFpsUpdate(ticks, frames));

			ticks = 0;
			frames = 0;
			mark();

		}

	}

}
