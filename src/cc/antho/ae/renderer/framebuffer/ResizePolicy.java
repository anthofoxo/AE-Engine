package cc.antho.ae.renderer.framebuffer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class ResizePolicy {

	public static final ResizePolicy POLICY_IGNORE = new ResizePolicy(0f);
	public static final ResizePolicy POLICY_100 = new ResizePolicy(1f);
	public static final ResizePolicy POLICY_50 = new ResizePolicy(.5f);
	public static final ResizePolicy POLICY_25 = new ResizePolicy(.25f);

	public final float value;

	public boolean isIgnored() {

		return value == 0f;

	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof ResizePolicy)) return false;
		return this.value == ((ResizePolicy) obj).value;

	}

}
