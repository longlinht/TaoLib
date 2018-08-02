package io.github.longlinht.library.android;

import android.content.Context;
import android.util.DisplayMetrics;

import io.github.longlinht.library.utils.Utils;

/**
 * {@code AndroidUnit} is a utility to convert between the different display units used when laying
 * out android views. {@code AndroidUnit} doesn't care about android the {@link Context}.
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public enum AndroidUnit {

    PX {
        @Override
        public float convert(float sourceCount, AndroidUnit sourceUnit) {
            return sourceUnit.toPx(sourceCount);
        }

        @Override
        public float toPx(float count) {
            return count;
        }

        @Override
        public float toDP(float count) {
            return count / getDisplayMetrics().density;
        }

        @Override
        public float toSP(float count) {
            return count / getDisplayMetrics().scaledDensity;
        }
    },

    DP {
        @Override
        public float convert(float sourceCount, AndroidUnit sourceUnit) {
            return sourceUnit.toDP(sourceCount);
        }

        @Override
        public float toPx(float count) {
            return count * getDisplayMetrics().density;
        }

        @Override
        public float toDP(float count) {
            return count;
        }

        @Override
        public float toSP(float count) {
            return count * (getDisplayMetrics().scaledDensity / getDisplayMetrics().density);
        }
    },

    SP {
        @Override
        public float convert(float sourceCount, AndroidUnit sourceUnit) {
            return sourceUnit.toSP(sourceCount);
        }

        @Override
        public float toPx(float count) {
            return count * getDisplayMetrics().scaledDensity;
        }

        @Override
        public float toDP(float count) {
            return count * (getDisplayMetrics().density / getDisplayMetrics().scaledDensity);
        }

        @Override
        public float toSP(float count) {
            return count;
        }
    };

    static DisplayMetrics getDisplayMetrics() {
        return Utils.getApp().getResources().getDisplayMetrics();
    }

    /**
     * Converts the given size in the given unit to this unit.
     *
     * @param sourceCount the size in the given {@code sourceUnit}.
     * @param sourceUnit  the unit of the {@code sourceCount} argument.
     * @return the converted size of the unit
     */
    public float convert(float sourceCount, AndroidUnit sourceUnit) {
        throw new AbstractMethodError();
    }

    /**
     * Convert from a source unit count to a pixel count
     *
     * @param count the source unit count
     * @return the converted source to a pixel count
     */
    public float toPx(float count) {
        throw new AbstractMethodError();
    }

    /**
     * Convert from a source unit count to a density independent pixel count
     *
     * @param count the source unit count
     * @return the converted density independent pixel count
     */
    public float toDP(float count) {
        throw new AbstractMethodError();
    }

    /**
     * Convert from a source unit count to a scaled independent pixel count
     *
     * @param count the source unit count
     * @return the converted scaled independent pixel count
     */
    public float toSP(float count) {
        throw new AbstractMethodError();
    }
}
