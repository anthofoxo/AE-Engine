package cc.antho.ae.particle;

import org.joml.Vector3f;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Particle {

	private Vector3f position;
	private Vector3f velocity;
	private float gravity;
	private float lifeLength;
	private float rotation;
	private float scale;

	private float elapsedTime = 0F;

	public Particle(Vector3f position, Vector3f velocity, float gravity, float lifeLength, float rotation, float scale) {

		this.position = position;
		this.velocity = velocity;
		this.gravity = gravity;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;

		ParticleMaster.addParticle(this);

	}

	protected boolean tick(float delta) {

		velocity.y -= 10 * gravity * delta;
		Vector3f change = new Vector3f(velocity);
		change.mul(delta);
		position.add(change);

		elapsedTime += delta;

		return elapsedTime < lifeLength;

	}

}
