package aa.droid.norbo.projects.edzesnaplo3.mvvm.ui.utils;

/**
 * Video mentésekor bekövetkezett események kezelése, mint pl az activity bezárása
 */
public interface VideoUtilsInterface {
    /**
     * Video Utils osztályhoz tartozó callback, főleg amikor finishelni kell az aktivityt
     * így oldottam meg, hogy evvel a megfelelő activity meghivja magának a finish-t
     */
    void finishActivity();
}

