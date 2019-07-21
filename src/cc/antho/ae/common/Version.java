package cc.antho.ae.common;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Version implements Serializable {

	private static final long serialVersionUID = -4515745916280444897L;

	@Getter private int major, minor, patch;

	public boolean equals(Object obj) {

		Objects.requireNonNull(obj);

		if (obj instanceof String) return obj.equals(toString());
		else if (obj instanceof Version) {

			Version v = (Version) obj;

			if (v.major != major) return false;
			if (v.minor != minor) return false;
			if (v.patch != patch) return false;
			return true;

		} else return false;

	}

	public String toString() {

		return major + "." + minor + "." + patch;

	}

}
