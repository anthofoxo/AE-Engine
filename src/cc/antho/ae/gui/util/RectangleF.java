package cc.antho.ae.gui.util;

import org.joml.Vector2f;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RectangleF {

	private Vector2f position, size;

	public RectangleF(Vector2f position, Vector2f size) {

		this.position = new Vector2f(position);
		this.size = new Vector2f(size);

	}

	public boolean intersects(RectangleF r) {

		float tw = this.size.x;
		float th = this.size.y;
		float rw = r.getSize().x;
		float rh = r.getSize().y;

		if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) return false;

		float tx = this.position.x;
		float ty = this.position.y;
		float rx = r.getPosition().x;
		float ry = r.getPosition().y;

		rw += rx;
		rh += ry;
		tw += tx;
		th += ty;

		return ((rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx) && (th < ty || th > ry));
	}

	public boolean contains(float X, float Y) {

		float w = this.size.x;
		float h = this.size.y;
		float x = this.position.x;
		float y = this.position.y;

		if (X < x || Y < y) return false;

		w += x;
		h += y;

		return ((w < x || w > X) && (h < y || h > Y));
	}

}
