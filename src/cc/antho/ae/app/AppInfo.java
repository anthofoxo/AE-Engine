package cc.antho.ae.app;

import cc.antho.ae.common.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class AppInfo {

	private final String name, author;
	private final Version version;

}
