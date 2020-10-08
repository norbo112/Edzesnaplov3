package aa.droid.norbo.projects.edzesnaplo3.mvvm.db.entities.edzesterv;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.edzesterv.Edzesnap;

@Entity
public class EdzesTervEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String megnevezes;

    public EdzesTervEntity() {
    }

    @Ignore
    public EdzesTervEntity(String megnevezes) {
        this.megnevezes = megnevezes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMegnevezes() {
        return megnevezes;
    }

    public void setMegnevezes(String megnevezes) {
        this.megnevezes = megnevezes;
    }
}
