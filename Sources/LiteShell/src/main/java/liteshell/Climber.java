package liteshell;

/**
 * @author xvraniak@stuba.sk
 */

interface Climb {

    boolean isTooHigh(int height, int limit);
}

public class Climber {

    public static void main(String[] args) {
        check((h, l) -> h < l, 5);
        howMany(true, new int[]{true, true});
    }

    final void method() {
    }

    private static void check(Climb climb, int height) {

        if (climb.isTooHigh(height, 10)) {
            System.out.println("too high");
        } else {
            System.out.println("ok");
        }
    }

    public static int howMany(boolean b, boolean... b2) {
        return b2.length;
    }
}


