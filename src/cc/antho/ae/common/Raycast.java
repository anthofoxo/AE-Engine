package cc.antho.ae.common;

import org.joml.Vector3f;

public abstract class Raycast {

	private Vector3f velocity = new Vector3f();

	public Vector3f castRay(Vector3f position, Vector3f direction, float maxDistance, float inc) {

		velocity.set(direction);
		velocity.normalize(inc);

		Vector3f currentPosition = new Vector3f(position);
		float distance = 0F;

		do {

			if (isColliding(currentPosition.x, currentPosition.y, currentPosition.z)) return currentPosition;

			currentPosition.add(velocity);
			distance += inc;

		} while (distance <= maxDistance);

		return null;

	}

	public Vector3f castRayAfter(Vector3f position, Vector3f direction, float maxDistance, float inc) {

		velocity.set(direction);
		velocity.normalize(inc);

		Vector3f lastPosition = new Vector3f(position);
		Vector3f currentPosition = new Vector3f(position);
		float distance = 0F;

		do {

			if (isColliding(currentPosition.x, currentPosition.y, currentPosition.z)) return lastPosition;

			lastPosition.set(currentPosition);
			currentPosition.add(velocity);
			distance += inc;

		} while (distance <= maxDistance);

		return null;

	}

	public abstract boolean isColliding(float x, float y, float z);

}
