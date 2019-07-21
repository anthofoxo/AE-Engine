package cc.antho.ae.particle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cc.antho.ae.camera.Camera;

public class ParticleMaster {

	private static List<Particle> particles = new ArrayList<>();
	private static ParticleRenderer renderer;

	public static void init() throws IOException {

		renderer = new ParticleRenderer();

	}

	public static void tick(float delta) {

		Iterator<Particle> iterator = particles.iterator();

		while (iterator.hasNext()) {

			Particle p = iterator.next();

			if (!p.tick(delta)) {

				iterator.remove();

			}

		}

	}

	public static void render(Camera camera, float aspect) {

		renderer.render(particles, camera, aspect);

	}

	public static void destroy() {

		renderer.destroy();

	}
	
	public static void addParticle(Particle p) {
		
		particles.add(p);
		
	}

}
