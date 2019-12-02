package cc.antho.ae.audio.effect;

import static org.lwjgl.openal.EXTEfx.*;

import java.util.ArrayList;
import java.util.List;

import cc.antho.ae.audio.AudioSource;
import cc.antho.ae.common.Destroyable;
import cc.antho.ae.log.Logger;
import lombok.Getter;

public abstract class AudioFilter implements Destroyable {

	@Getter protected int handle;
	@Getter private final int type;

	@Getter private List<AudioSource> attached = new ArrayList<>();

	public AudioFilter(int handle, int type) {

		Logger.debug("Creating audio filter");

		this.handle = handle;
		this.type = type;
		alFilteri(handle, AL_FILTER_TYPE, type);

		load();

	}

	public AudioFilter(int type) {

		this(alGenFilters(), type);

	}

	public void load() {

		for (int i = 0; i < attached.size(); i++)
			attached.get(i).setDirectFilter(this);

	}

	public void destroy() {

		Logger.debug("Destroying audio filter");

		for (int i = 0; i < attached.size(); i++)
			attached.get(i).setDirectFilter(null);

		attached.clear();

		alDeleteFilters(handle);
		handle = 0;

	}

}
