package cc.antho.ae.audio;

import static org.lwjgl.openal.AL10.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import cc.antho.ae.camera.Camera;

public final class AudioListener {

	private AudioListener() {

	}

	static {

		alDistanceModel(AL_INVERSE_DISTANCE_CLAMPED);

	}

	public static void setPosition(Vector3f position) {

		alListener3f(AL_POSITION, position.x, position.y, position.z);

	}

	public static void setVelocity(Vector3f position) {

		alListener3f(AL_VELOCITY, position.x, position.y, position.z);

	}

	public static void setOrientation(Vector3f look, Vector3f up) {

		alListenerfv(AL_ORIENTATION, new float[] { look.x, look.y, look.z, up.x, up.y, up.z });

	}

	public static void setPositionAndOrientation(Camera camera) {

		Matrix4f view = camera.getViewMatrix();
		Vector3f look = new Vector3f(view.m01(), view.m02(), view.m03());
		Vector3f up = new Vector3f(view.m11(), view.m12(), view.m13());

		setPosition(camera.getPosition());
		setOrientation(look, up);

	}

}
