package com.example.asterismos;

import android.content.Context;
import android.content.SharedPreferences;

public class ConstellationRepository {
    //Сохраняем данные на устройстве
    private static final String PREFS_NAME = "AsterismosPrefs";
    private static final String KEY_CONSTELLATION_PREFIX = "constellation_";
    private static final String KEY_GROUP_PREFIX = "group_";
    private static final String KEY_FINAL_TEST = "final_test_passed";


    private final SharedPreferences prefs;
    private final Context context;

    // Массивы с данными созвездий
    private static final int[] CONSTELLATION_IMAGES = {
            R.drawable.imgand, R.drawable.imggem, R.drawable.imguma, R.drawable.imgcma, R.drawable.imglib, R.drawable.imgaqr,
            R.drawable.imgaur, R.drawable.imglup, R.drawable.imgboo, R.drawable.imgcom, R.drawable.imgcrv, R.drawable.imgher,
            R.drawable.imghya, R.drawable.imgcol, R.drawable.imgcvn, R.drawable.imgvir, R.drawable.imgdel, R.drawable.imgdra,
            R.drawable.imgmon, R.drawable.imgara, R.drawable.imgpic, R.drawable.imgcam, R.drawable.imggru, R.drawable.imglep,
            R.drawable.imgoph, R.drawable.imgser, R.drawable.imgdor, R.drawable.imgind, R.drawable.imgcas, R.drawable.imgcar,
            R.drawable.imgcet, R.drawable.imgcap, R.drawable.imgpyx, R.drawable.imgpup, R.drawable.imgcyg, R.drawable.imgleo,
            R.drawable.imgvol, R.drawable.imglyr, R.drawable.imgvul, R.drawable.imgumi, R.drawable.imgequ, R.drawable.imglmi,
            R.drawable.imgcmi, R.drawable.imgmic, R.drawable.imgmus, R.drawable.imgant, R.drawable.imgnor, R.drawable.imgari,
            R.drawable.imgoct, R.drawable.imgaql, R.drawable.imgori, R.drawable.imgpav, R.drawable.imgvel, R.drawable.imgpeg,
            R.drawable.imgper, R.drawable.imgfor, R.drawable.imgaps, R.drawable.imgcnc, R.drawable.imgcae, R.drawable.imgpsc,
            R.drawable.imglyn, R.drawable.imgcrb, R.drawable.imgsex, R.drawable.imgret, R.drawable.imgsco, R.drawable.imgscl,
            R.drawable.imgmen, R.drawable.imgsge, R.drawable.imgsgr, R.drawable.imgtel, R.drawable.imgtau, R.drawable.imgtri,
            R.drawable.imgtuc, R.drawable.imgphe, R.drawable.imgcha, R.drawable.imgcen, R.drawable.imgcep, R.drawable.imgcir,
            R.drawable.imghor, R.drawable.imgcrt, R.drawable.imgsct, R.drawable.imgeri, R.drawable.imghyi, R.drawable.imgcra,
            R.drawable.imgpsa, R.drawable.imgcru, R.drawable.imgtra, R.drawable.imglac
    };

    private static final int[] REAL_CONSTELLATION_IMAGES = {
            R.drawable.rimgand, R.drawable.rimggem, R.drawable.rimguma, R.drawable.rimgcma, R.drawable.rimglib, R.drawable.rimgaqr,
            R.drawable.rimgaur, R.drawable.rimglup, R.drawable.rimgboo, R.drawable.rimgcom, R.drawable.rimgcrv, R.drawable.rimgher,
            R.drawable.rimghya, R.drawable.rimgcol, R.drawable.rimgcvn, R.drawable.rimgvir, R.drawable.rimgdel, R.drawable.rimgdra,
            R.drawable.rimgmon, R.drawable.rimgara, R.drawable.rimgpic, R.drawable.rimgcam, R.drawable.rimggru, R.drawable.rimglep,
            R.drawable.rimgoph, R.drawable.rimgser, R.drawable.rimgdor, R.drawable.rimgind, R.drawable.rimgcas, R.drawable.rimgcar,
            R.drawable.rimgcet, R.drawable.rimgcap, R.drawable.rimgpyx, R.drawable.rimgpup, R.drawable.rimgcyg, R.drawable.rimgleo,
            R.drawable.rimgvol, R.drawable.rimglyr, R.drawable.rimgvul, R.drawable.rimgumi, R.drawable.rimgequ, R.drawable.rimglmi,
            R.drawable.rimgcmi, R.drawable.rimgmic, R.drawable.rimgmus, R.drawable.rimgant, R.drawable.rimgnor, R.drawable.rimgari,
            R.drawable.rimgoct, R.drawable.rimgaql, R.drawable.rimgori, R.drawable.rimgpav, R.drawable.rimgvel, R.drawable.rimgpeg,
            R.drawable.rimgper, R.drawable.rimgfor, R.drawable.rimgaps, R.drawable.rimgcnc, R.drawable.rimgcae, R.drawable.rimgpsc,
            R.drawable.rimglyn, R.drawable.rimgcrb, R.drawable.rimgsex, R.drawable.rimgret, R.drawable.rimgsco, R.drawable.rimgscl,
            R.drawable.rimgmen, R.drawable.rimgsge, R.drawable.rimgsgr, R.drawable.rimgtel, R.drawable.rimgtau, R.drawable.rimgtri,
            R.drawable.rimgtuc, R.drawable.rimgphe, R.drawable.rimgcha, R.drawable.rimgcen, R.drawable.rimgcep, R.drawable.rimgcir,
            R.drawable.rimghor, R.drawable.rimgcrt, R.drawable.rimgsct, R.drawable.rimgeri, R.drawable.rimghyi, R.drawable.rimgcra,
            R.drawable.rimgpsa, R.drawable.rimgcru, R.drawable.rimgtra, R.drawable.rimglac
    };

    private static final int[] SYMBOL_IMAGES = {
            R.drawable.iconand, R.drawable.icongem, R.drawable.iconuma, R.drawable.iconcma, R.drawable.iconlib, R.drawable.iconaqr,
            R.drawable.iconaur, R.drawable.iconlup, R.drawable.iconboo, R.drawable.iconcom, R.drawable.iconcrv, R.drawable.iconher,
            R.drawable.iconhya, R.drawable.iconcol, R.drawable.iconcvn, R.drawable.iconvir, R.drawable.icondel, R.drawable.icondra,
            R.drawable.iconmon, R.drawable.iconara, R.drawable.iconpic, R.drawable.iconcam, R.drawable.icongru, R.drawable.iconlep,
            R.drawable.iconoph, R.drawable.iconser, R.drawable.icondor, R.drawable.iconind, R.drawable.iconcas, R.drawable.iconcar,
            R.drawable.iconcet, R.drawable.iconcap, R.drawable.iconpyx, R.drawable.iconpup, R.drawable.iconcyg, R.drawable.iconleo,
            R.drawable.iconvol, R.drawable.iconlyr, R.drawable.iconvul, R.drawable.iconumi, R.drawable.iconequ, R.drawable.iconlmi,
            R.drawable.iconcmi, R.drawable.iconmic, R.drawable.iconmus, R.drawable.iconant, R.drawable.iconnor, R.drawable.iconari,
            R.drawable.iconoct, R.drawable.iconaql, R.drawable.iconori, R.drawable.iconpav, R.drawable.iconvel, R.drawable.iconpeg,
            R.drawable.iconper, R.drawable.iconfor, R.drawable.iconaps, R.drawable.iconcnc, R.drawable.iconcae, R.drawable.iconpsc,
            R.drawable.iconlyn, R.drawable.iconcrb, R.drawable.iconsex, R.drawable.iconret, R.drawable.iconsco, R.drawable.iconscl,
            R.drawable.iconmen, R.drawable.iconsge, R.drawable.iconsgr, R.drawable.icontel, R.drawable.icontau, R.drawable.icontri,
            R.drawable.icontuc, R.drawable.iconphe, R.drawable.iconcha, R.drawable.iconcen, R.drawable.iconcep, R.drawable.iconcir,
            R.drawable.iconhor, R.drawable.iconcrt, R.drawable.iconsct, R.drawable.iconeri, R.drawable.iconhyi, R.drawable.iconcra,
            R.drawable.iconpsa, R.drawable.iconcru, R.drawable.icontra, R.drawable.iconlac
    };

    public ConstellationRepository(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Сохранение изученного созвездия
    public void setConstellationLearned(int constellationId) {
        prefs.edit().putBoolean(KEY_CONSTELLATION_PREFIX + constellationId, true).apply();
    }

    // Проверка, изучено ли созвездие
    public boolean isConstellationLearned(int constellationId) {
        return prefs.getBoolean(KEY_CONSTELLATION_PREFIX + constellationId, false);
    }

    // Сохранение пройденного теста группы
    public void setGroupTestPassed(int groupId) {
        prefs.edit().putBoolean(KEY_GROUP_PREFIX + groupId, true).apply();
    }

    // Проверка, пройден ли тест группы
    public boolean isGroupTestPassed(int groupId) {
        return prefs.getBoolean(KEY_GROUP_PREFIX + groupId, false);
    }

    // Сохранение итогового теста
    public void setFinalTestPassed(boolean passed) {
        prefs.edit().putBoolean(KEY_FINAL_TEST, passed).apply();
    }

    public boolean isFinalTestPassed() {
        return prefs.getBoolean(KEY_FINAL_TEST, false);
    }

    public String[] getNames() {
        return context.getResources().getStringArray(R.array.constellation_names);
    }

    public String[] getCommonInfo() {
        return context.getResources().getStringArray(R.array.common_information);
    }

    public String[] getStory() {
        return context.getResources().getStringArray(R.array.story_constellation);
    }

    public int[] getConstellationImages() {
        return CONSTELLATION_IMAGES;
    }

    public int[] getSymbolImages() {
        return SYMBOL_IMAGES;
    }

    public int[] getRealConstellationImages() { return REAL_CONSTELLATION_IMAGES; }

}
