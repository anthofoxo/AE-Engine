package cc.antho.ae.audio;

import static org.lwjgl.openal.AL10.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import cc.antho.ae.camera.Camera;

public class AudioListener {

	private AudioListener() {

	}
	
	static {
		
		alDistanceModel(AL_INVERSE_DISTANCE_CLAMPED);
		
	}

	public static final void setPosition(final Vector3f position) {

		alListener3f(AL_POSITION, position.x, position.y, position.z);

	}

	public static final void setVelocity(final Vector3f position) {

		alListener3f(AL_VELOCITY, position.x, position.y, position.z);

	}

	public static final void setOrientation(final Vector3f look, final Vector3f up) {

		alListenerfv(AL_ORIENTATION, new float[] { look.x, look.y, look.z, up.x, up.y, up.z });

	}

	public static final void setPositionAndOrientation(final Camera camera) {

		setPosition(camera.getPosition());

		final Matrix4f view = camera.getViewMatrix();

		final Vector3f look = new Vector3f(view.m01(), view.m02(), view.m03());
		final Vector3f up = new Vector3f(view.m11(), view.m12(), view.m13());

		setOrientation(look, up);

	}

}
