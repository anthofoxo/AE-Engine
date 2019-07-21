package cc.antho.ae.common;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import cc.antho.ae.math.Maths;
import lombok.Getter;
import lombok.Setter;

public final class Camera {

	@Getter @Setter private Vector3f position = new Vector3f();
	@Getter @Setter private Vector3f rotation = new Vector3f();

	@Getter @Setter private float fov = 70F;
	@Getter @Setter private float near = .1F;
	@Getter @Setter private float far = 1000F;

	private final Matrix4f projection = new Matrix4f();
	private final Matrix4f view = new Matrix4f();

	public Matrix4f getProjectionMatrix(float aspect) {

		return projection.setPerspective(Maths.toRadians(fov), aspect, near, far);

	}

	public Matrix4f getViewMatrix() {

		position.mul(-1F);

		view.rotation(Maths.toRadians(rotation.z), 0F, 0F, 1F);
		view.rotate(Maths.toRadians(rotation.x), 1F, 0F, 0F);
		view.rotate(Maths.toRadians(rotation.y), 0F, 1F, 0F);
		view.translate(position);

		position.mul(-1F);

		return view;

	}

	public final Vector3f forward() {

		final Vector4f clipCoords = new Vector4f(0F, 0F, -1F, 1F);
		final Vector4f eyeCoords = toEyeCoords(clipCoords);
		final Vector3f worldRay = toWorldCoords(eyeCoords);

		return worldRay;

	}

	private final Vector4f toEyeCoords(final Vector4f clipCoords) {

		Matrix4f invertedProjection = new Matrix4f();
		Vector4f eyeCoords = new Vector4f();

		projection.invert(invertedProjection);
		clipCoords.mul(invertedProjection, eyeCoords);

		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);

	}

	private final Vector3f toWorldCoords(final Vector4f eyeCoords) {

		Matrix4f invertedView = new Matrix4f();
		Vector4f rayWorld = new Vector4f();

		view.invert(invertedView);
		eyeCoords.mul(invertedView, rayWorld);

		final Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalize();

		return mouseRay;

	}

}
