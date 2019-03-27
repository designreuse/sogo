package com.yihexinda.core.utils;

/**
 * 方向角计算公式
 */
public class DirctionAngle {
    /**
     * 车辆方向角计算
     * 方向角：车辆行驶方向和正北方向的夹角
     * 上一时刻车辆位置(flng,flati) 车辆实时位置（tlng,tlati）
     * @param flng:经度
     * @param flati：纬度
     * @param tlng：经度
     * @param tlati：纬度
     * @return
     */
    public static int angleCalculate(double flng, double flati, double tlng, double tlati) {
        double point = tlati-flati;
        double a = Math.sqrt(Math.pow((tlng - flng), 2) + Math.pow((tlati - flati), 2));
        double b = Math.sqrt(1);
        double cos = (point / (a * b));
        double angle = Math.toDegrees(Math.acos(cos));
        if ((tlng-flng) < 0) {
            angle = 360 - angle;
        }
        int inTangle =(int) angle;
        return inTangle;
    }
}
