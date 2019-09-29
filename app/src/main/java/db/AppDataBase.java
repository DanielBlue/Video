package db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by JC on 2019/9/9.
 */
@Database(name = AppDataBase.DATANAM,version = AppDataBase.VERSION)
public class AppDataBase {
    public static  final String DATANAM="shortvideo";
    public static  final int VERSION=1;
}
