package cc.antho.ae.entity;

import lombok.Getter;

public abstract class Component {

	@Getter Entity entity;

	public abstract void tick();

	public abstract void render();

}
