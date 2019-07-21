package cc.antho.ae.camera;

import cc.antho.ae.math.Maths;
import lombok.Getter;

public class FreemoveCamera {

	@Getter private final Camera camera = new Camera();

	public void rotateHorz(float amount) {

		camera.getRotation().y += amount;

	}

	public void rotateVert(float amount) {

		camera.getRotation().x += amount;

		if (camera.getRotation().x < -90F) camera.getRotation().x = -90F;
		if (camera.getRotation().x > 90F) camera.getRotation().x = 90F;

	}

	public void move(float x, float y, float z) {

		camera.getPosition().y += y;
		camera.getPosition().x += Maths.sin(Maths.toRadians(camera.getRotation().y + 90F)) * x;
		camera.getPosition().z -= Maths.cos(Maths.toRadians(camera.getRotation().y + 90F)) * x;
		camera.getPosition().x += Maths.sin(Maths.toRadians(camera.getRotation().y)) * z;
		camera.getPosition().z -= Maths.cos(Maths.toRadians(camera.getRotation().y)) * z;

	}

}
