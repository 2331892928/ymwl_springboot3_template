package cn.ymypay.team.utils;

public class WindDirectionConverter {

    public static String convertDegreesToDirection(int degrees) {
        if (degrees < 0 || degrees > 360) {
            return "Invalid Degree";
        }

        if (degrees >= 350 || degrees < 10) {
            return "北";
        } else if (degrees >= 10 && degrees < 35) {
            return "北偏东";
        } else if (degrees >= 35 && degrees < 55) {
            return "东北";
        } else if (degrees >= 55 && degrees < 80) {
            return "东偏北";
        } else if (degrees >= 80 && degrees < 100) {
            return "东";
        } else if (degrees >= 100 && degrees < 125) {
            return "东偏南";
        } else if (degrees >= 125 && degrees < 145) {
            return "东南";
        } else if (degrees >= 145 && degrees < 170) {
            return "南偏东";
        } else if (degrees >= 170 && degrees < 190) {
            return "南";
        } else if (degrees >= 190 && degrees < 215) {
            return "南偏西";
        } else if (degrees >= 215 && degrees < 235) {
            return "西南";
        } else if (degrees >= 235 && degrees < 260) {
            return "西偏南";
        } else if (degrees >= 260 && degrees < 280) {
            return "西";
        } else if (degrees >= 280 && degrees < 305) {
            return "西偏北";
        } else if (degrees >= 305 && degrees < 325) {
            return "西北";
        } else if (degrees >= 325 && degrees < 350) {
            return "北偏西";
        }

        return "Unknown";
    }
}

