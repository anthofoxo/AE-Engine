package cc.antho.ae.window;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class GLContext {

	private final int major, minor;
	private final boolean core, forward;

}
