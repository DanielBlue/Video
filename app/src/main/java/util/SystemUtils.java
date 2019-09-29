package util;

import android.content.Context;
import android.content.pm.PackageInfo;

import java.util.List;

public class SystemUtils {

    public static boolean isAvilible(Context context, String packageName) {
        List<PackageInfo> packages = context.getApplicationContext().getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packages.size(); ++i) {
            PackageInfo packageInfo = (PackageInfo) packages.get(i);
            if (packageInfo.packageName.equals(packageName)) {
                if (packageInfo.versionCode < 205) {
                    return false;
                }

                return true;
            }
        }
        return false;
    }
}
