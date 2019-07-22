package cc.antho.ae.renderer.gl.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Dataset {

	@Getter private final float[] data;
	@Getter private final int size;
	@Getter int position = 0;

}