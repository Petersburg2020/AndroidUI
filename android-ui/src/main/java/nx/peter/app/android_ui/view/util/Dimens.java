package nx.peter.app.android_ui.view.util;

import android.content.res.Resources;
import android.util.TypedValue;

public class Dimens {
    Dimens() {}

    public static float SP_10 = toSp(10);
    public static float SP_11 = toSp(11);
    public static float SP_12 = toSp(12);
    public static float SP_13 = toSp(13);
    public static float SP_24 = toSp(24);
    public static float SP_28 = toSp(28);
    public static float SP_16 = toSp(16);
    public static float SP_32 = toSp(32);
    public static float SP_18 = toSp(18);
    public static float SP_30 = toSp(30);
    public static float SP_20 = toSp(20);
    public static float SP_22 = toSp(22);
    public static float SP_8 = toSp(8);
    public static float SP_9 = toSp(9);
    public static float SP_6 = toSp(6);
    public static float SP_40 = toSp(40);
    public static float SP_36 = toSp(36);
    public static float SP_42 = toSp(42);
    public static float SP_48 = toSp(48);
    public static float SP_45 = toSp(45);



    public static float toSp(float size) {
        return getUnit(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public static float toPx(float size) {
        return getUnit(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public static float toDp(float size) {
        return getUnit(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    public static float getUnit(int unit, float size) {
        return TypedValue.applyDimension(unit, size, Resources.getSystem().getDisplayMetrics());
    }
}
