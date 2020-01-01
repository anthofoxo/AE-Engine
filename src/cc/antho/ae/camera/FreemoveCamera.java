package cc.antho.ae.camera;

import cc.antho.common.Camera;
import cc.antho.common.math.Maths;

public class FreemoveCamera extends Camera {

	public void rotateHorz(float amount) {

		rotation.y += amount;

	}

	public void rotateVert(float amount) {

		rotation.x += amount;

		if (rotation.x < -90F) rotation.x = -90F;
		if (rotation.x > 90F) rotation.x = 90F;

	}

	public void move(float x, float y, float z) {

		position.y += y;
		position.x += Maths.sin(Maths.toRadians(rotation.y + 90F)) * x;
		position.z -= Maths.cos(Maths.toRadians(rotation.y + 90F)) * x;
		position.x += Maths.sin(Maths.toRadians(rotation.y)) * z;
		position.z -= Maths.cos(Maths.toRadians(rotation.y)) * z;

	}

}
