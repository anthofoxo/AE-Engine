package cc.antho.ae.math;

import org.joml.Vector2f;
import org.joml.Vector3f;

import cc.antho.ae.renderer.color.Color;

public final class Maths {

	public static final float E = 2.71828182F;
	public static final float PI = 3.14159265F;
	public static final float TAU = 2F * PI;

	private Maths() {

	}

	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	public static float fract(float value) {

		return value - floor(value);

	}

	public static float clampMin(float value, float min) {

		if (value <= min) return min;
		return value;

	}

	public static float clampMax(float value, float max) {

		if (value >= max) return max;
		return value;

	}

	public static float clamp(float value, float min, float max) {

		return clampMin(clampMax(value, max), min);

	}

	public static Color mix(Color a, Color b, float percent) {

		return new Color(mix(a.r, b.r, percent), mix(a.g, b.g, percent), mix(a.b, b.b, percent), mix(a.a, b.a, percent));

	}

	public static float mix(float a, float b, float percent) {

		return a * (1f - percent) + b * percent;

	}

	public static int ceil(float a) {

		return (int) Math.ceil(a);

	}

	public static int floor(float a) {

		return (int) Math.floor(a);

	}

	public static int round(float a) {

		return Math.round(a);

	}

	public static float abs(float value) {

		return value < 0 ? -value : value;

	}

	public static float min(float a, float b) {

		return a < b ? a : b;

	}

	public static float max(float a, float b) {

		return a > b ? a : b;

	}

	public static float sin(float a) {

		return (float) Math.sin(a);

	}

	public static float cos(float a) {

		return (float) Math.cos(a);

	}

	public static float tan(float a) {

		return (float) Math.tan(a);

	}

	public static float sinNormalized(float a) {

		return sin(TAU * a);

	}

	public static float cosNormalized(float a) {

		return cos(TAU * a);

	}

	public static float tanNormalized(float a) {

		return tan(TAU * a);

	}

	public static float toRadians(float a) {

		return a / 180F * PI;

	}

	public static float toDegrees(float a) {

		return a * 180F / PI;

	}

	public static float sqrt(float a) {

		return (float) Math.sqrt(a);

	}

	public static float cbrt(float a) {

		return (float) Math.cbrt(a);

	}

	public static float pow(float a, float b) {

		return (float) Math.pow(a, b);

	}

	public static float approach(float current, float goal, float delta) {

		float difference = goal - current;

		if (difference > delta) return current + delta;
		if (difference < -delta) return current - delta;

		return goal;

	}

	public static float approachSmooth(float current, float goal, float delta) {

		return approach(current, goal, abs(current - goal) * delta);

	}

	public static Color approachSmooth(Color current, Color goal, float delta) {

		Color c = new Color();
		c.r = approachSmooth(current.r, goal.r, delta);
		c.g = approachSmooth(current.g, goal.g, delta);
		c.b = approachSmooth(current.b, goal.b, delta);
		c.a = approachSmooth(current.a, goal.a, delta);

		return c;

	}

	public static float map(float value, float start1, float end1, float start2, float end2) {

		return (value - start1) / (end1 - start1) * (end2 - start2) + start2;

	}

	public static float mapClamped(float value, float start1, float end1, float start2, float end2) {

		return clamp(map(value, start1, end1, start2, end2), start2, end2);

	}

	public static float log(float a, float base) {

		return (float) (Math.log(a) / Math.log(base));

	}

	// Used for smoothstep calculations
	private static float t(float x, float edge0, float edge1) {

		return (x - edge0) / (edge1 - edge0);

	}

	public static float smoothStep(float x, float edge0, float edge1) {

		float t = t(x, edge0, edge1);

		return 2F * pow(t, 2F) * (-t + 1.5F);

	}

	public static float smootherStep(float x, float edge0, float edge1) {

		float t = t(x, edge0, edge1);

		return 6F * pow(t, 3F) * (pow(t, 2F) - 2.5F * t + 5F / 3F);

	}

	public static float smoothestStep(float x, float edge0, float edge1) {

		float t = t(x, edge0, edge1);

		return 20F * pow(t, 4F) * (-pow(t, 3F) + 3.5F * pow(t, 2F) - 4.2F * t + 1.75F);

	}

}
