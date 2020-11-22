package aa.droid.norbo.projects.edzesnaplo3.widgets;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import aa.droid.norbo.projects.edzesnaplo3.mvvm.data.model.NaploUI;

public class NaploGyakOsszsuly implements Serializable {
    private Long naplodatum;
    private Integer gyakorlatOsszsulys;
    private NaploUI naploUI;

    public NaploGyakOsszsuly() {
    }

    public NaploGyakOsszsuly(Long naplodatum, Integer gyakorlatOsszsulys, NaploUI naploUI) {
        this.naplodatum = naplodatum;
        this.gyakorlatOsszsulys = gyakorlatOsszsulys;
        this.naploUI = naploUI;
    }

    public Long getNaplodatum() {
        return naplodatum;
    }

    public Integer getGyakorlatOsszsulys() {
        return gyakorlatOsszsulys;
    }

    public void setNaplodatum(Long naplodatum) {
        this.naplodatum = naplodatum;
    }

    public void setGyakorlatOsszsulys(Integer gyakorlatOsszsulys) {
        this.gyakorlatOsszsulys = gyakorlatOsszsulys;
    }

    public NaploUI getNaploUI() {
        return naploUI;
    }

    public void setNaploUI(NaploUI naploUI) {
        this.naploUI = naploUI;
    }

    @NonNull
    @Override
    public String toString() {
        Date date = new Date(naplodatum);
        return date.getYear()+"."+date.getMonth()+"."+date.getDay()+" ";
    }
}
