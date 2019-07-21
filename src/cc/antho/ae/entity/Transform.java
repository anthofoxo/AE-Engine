package cc.antho.ae.entity;

import org.joml.Vector3f;

import lombok.Getter;
import lombok.Setter;

public class Transform extends Component {

	@Getter @Setter private Vector3f position = new Vector3f();
	@Getter @Setter private Vector3f rotation = new Vector3f();
	@Getter @Setter private Vector3f scale = new Vector3f();

	public void tick() {

	}

	public void render() {

	}

}
