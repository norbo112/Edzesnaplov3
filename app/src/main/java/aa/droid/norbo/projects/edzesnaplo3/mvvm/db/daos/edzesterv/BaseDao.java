package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.daos.edzesterv;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@Dao
public interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(T t);

    @Delete
    void delete(T t);

    @Update
    void update(T t);
}
