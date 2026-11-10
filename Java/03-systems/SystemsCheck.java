import java.util.Arrays;
import java.util.Objects;

/**
 * Minimal test helper, so tests run with plain javac and java and need no JUnit download.
 *
 * Each check records a pass or prints a failure; finish() prints a summary and
 * exits with status 1 if anything failed, so scripts and CI can detect it.
 *
 * An identical copy lives in every chapter folder, so each folder still compiles
 * on its own with "javac *.java". In a real project you would use JUnit.
 */
final class Check {

    private static int passed = 0;
    private static int failed = 0;

    private Check() {
    }

    static void equal(Object expected, Object actual, String name) {
        if (Objects.equals(expected, actual)) {
            passed++;
        } else {
            fail(name, "expected <" + expected + "> but was <" + actual + ">");
        }
    }

    static void equal(int[] expected, int[] actual, String name) {
        equal(Arrays.toString(expected), Arrays.toString(actual), name);
    }

    static void equal(double expected, double actual, double tolerance, String name) {
        if (Math.abs(expected - actual) <= tolerance) {
            passed++;
        } else {
            fail(name, "expected <" + expected + "> but was <" + actual + ">");
        }
    }

    static void isTrue(boolean condition, String name) {
        equal(true, condition, name);
    }

    static void isFalse(boolean condition, String name) {
        equal(false, condition, name);
    }

    static void isNull(Object value, String name) {
        equal(null, value, name);
    }

    /** Passes only if action throws IllegalArgumentException. */
    static void throwsIllegalArgument(Runnable action, String name) {
        try {
            action.run();
            fail(name, "expected IllegalArgumentException, but nothing was thrown");
        } catch (IllegalArgumentException expected) {
            passed++;
        }
    }

    static void finish() {
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void fail(String name, String detail) {
        failed++;
        System.out.println("FAIL " + name + ": " + detail);
    }
}
