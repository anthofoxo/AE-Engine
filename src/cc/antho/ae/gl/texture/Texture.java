package cc.antho.ae.gl.texture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Texture {

	@Getter protected final int target;
	@Getter protected final int handle;

}
