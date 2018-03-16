package liteshell.utils;

import java.util.Optional;

import com.sun.javafx.PlatformUtil;

/**
 * @author xvraniak@stuba.sk
 */

public class OsUtils {

    private static Optional<OperatingSystem> operatingSystem = Optional.empty();

    private OsUtils() {
    }

    public static Optional<OperatingSystem> getOperatingSystem() {
        if (!operatingSystem.isPresent()) {
            operatingSystem = findSystemType();
        }
        return operatingSystem;
    }

    private static Optional<OperatingSystem> findSystemType() {
        if (PlatformUtil.isUnix()) {
            return Optional.of(OperatingSystem.UNIX);
        }
        if (PlatformUtil.isLinux()) {
            return Optional.of(OperatingSystem.LINUX);
        }
        if (PlatformUtil.isWindows()) {
            return Optional.of(OperatingSystem.WINDOWS);
        }
        if (PlatformUtil.isMac()) {
            return Optional.of(OperatingSystem.MAC);
        }
        if (PlatformUtil.isIOS()) {
            return Optional.of(OperatingSystem.IOS);
        }
        if (PlatformUtil.isAndroid()) {
            return Optional.of(OperatingSystem.ANDROID);
        }
        return Optional.empty();
    }
}
