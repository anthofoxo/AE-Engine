package cc.antho.ae.log;

public class LoggerImpl extends Logger {

	public void _warn(final String s) {

		System.out.println("[" + Thread.currentThread().getName() + "][warn]: " + s);

	}

	public void _error(final String s) {

		System.err.println("[" + Thread.currentThread().getName() + "][error]: " + s);

		throw new RuntimeException();

	}

	public void _debug(final String s) {

		System.out.println("[" + Thread.currentThread().getName() + "][debug]: " + s);

	}

	public void _info(final String s) {

		System.out.println("[" + Thread.currentThread().getName() + "][info]: " + s);

	}

	public void _trace(final String s) {

		System.out.println("[" + Thread.currentThread().getName() + "][trace]: " + s);

	}

}
