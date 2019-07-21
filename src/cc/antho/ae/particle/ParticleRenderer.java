package cc.antho.ae.particle;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import cc.antho.ae.common.Camera;
import cc.antho.ae.gl.model.Dataset;
import cc.antho.ae.gl.model.RawModel;
import cc.antho.ae.gl.shader.ShaderProgram;
import cc.antho.ae.gl.shader.ShaderUtil;
import cc.antho.ae.math.Maths;

public class ParticleRenderer {

	private static final float[] VERTS = { -.5F, .5F, -.5F, -.5F, .5F, .5F, .5F, -.5F };

	private RawModel model;
	private ShaderProgram shader;

	public ParticleRenderer() throws IOException {

		model = new RawModel(GL_TRIANGLE_STRIP);
		model.uploadData(null, new Dataset(VERTS, 2));

		shader = ShaderUtil.createProgram("/shader/particle.vs", "/shader/particle.fs");

	}

	public void render(List<Particle> particles, Camera camera, float aspect) {

		Matrix4f view = camera.getViewMatrix();
		Matrix4f projection = camera.getProjectionMatrix(aspect);

		shader.bind();
		model.bind();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDepthMask(false);

		shader.uniformMat4f("u_projection", projection);

		for (Particle p : particles) {

			updateModelViewMatrix(p.getPosition(), p.getRotation(), p.getScale(), view);
			model.render();

		}

		glDisable(GL_BLEND);
		glDepthMask(true);

	}

	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f view) {

		Matrix4f modelMatrix = new Matrix4f();
		modelMatrix.translate(position);

		modelMatrix.m00(view.m00());
		modelMatrix.m01(view.m10());
		modelMatrix.m02(view.m20());
		modelMatrix.m10(view.m01());
		modelMatrix.m11(view.m11());
		modelMatrix.m12(view.m21());
		modelMatrix.m20(view.m02());
		modelMatrix.m21(view.m12());
		modelMatrix.m22(view.m22());

		modelMatrix.rotate(Maths.toRadians(rotation), new Vector3f(0F, 0F, 1F));
		modelMatrix.scale(scale);

		Matrix4f modelViewMatrix = new Matrix4f(view).mul(modelMatrix);
		shader.uniformMat4f("u_modelview", modelViewMatrix);

	}

	public void destroy() {

		model.destroy();
		shader.destory();

	}

}
