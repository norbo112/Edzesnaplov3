package aa.droid.norbo.projects.edzesnaplo3.widgets;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class NaploGyakOsszsuly implements Serializable {
    private Long naplodatum;
    private Integer gyakorlatOsszsulys;

    public NaploGyakOsszsuly(Long naplodatum, Integer gyakorlatOsszsulys) {
        this.naplodatum = naplodatum;
        this.gyakorlatOsszsulys = gyakorlatOsszsulys;
    }

    public Long getNaplodatum() {
        return naplodatum;
    }

    public Integer getGyakorlatOsszsulys() {
        return gyakorlatOsszsulys;
    }

    @NonNull
    @Override
    public String toString() {
        Date date = new Date(naplodatum);
        return date.getYear()+"."+date.getMonth()+"."+date.getDay()+" ";
    }
}
