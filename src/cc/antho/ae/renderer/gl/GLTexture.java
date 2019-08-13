package cc.antho.ae.renderer.gl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class GLTexture {

	@Getter protected final int target;
	@Getter protected final int handle;

}
	