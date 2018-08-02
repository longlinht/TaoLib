package com.happytime.faceparty.la.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Tao He on 18-6-28.
 * hetaoof@gmail.com
 */
public class BitmapUtil {

    /**
     * 通过一种比较省内存的方式获得bitmap
     */
    public static Bitmap getBitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = context.getResources().openRawResource(resId);
            bitmap = BitmapFactory.decodeStream(is, null, opt);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static BitmapDrawable getBitmapDrawableResized(Context context, int resId, int width, int height) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = context.getResources().openRawResource(resId);
            bitmap = BitmapFactory.decodeStream(is, null, opt);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new BitmapDrawable(context.getResources(), getResizedBitmap(bitmap, width, height));
    }

    /**
     * 根据resourceId获取缩略好的bitmap
     */
    public static Bitmap getBitmapResized(Context context, int resId, int width, int height) {
        return getBitmapDrawableResized(context, resId, width, height).getBitmap();
    }

    public static Bitmap getResizedBitmap(Bitmap bitmap, int width, int height) {
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / (float) bitmap.getWidth(), (float) height / (float) bitmap.getHeight()); //长和宽放大缩小的比例

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 获取缩放后的bitmap
     *
     * @param scale 缩放比例
     */
    public static Bitmap getScaledBitmap(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 从本地获取bitmap
     */
    public static Bitmap getBitmapFromDisk(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        return getBitmapFromDisk(new File(fileName));
    }

    /**
     * 从本地获取bitmap
     */
    public static Bitmap getBitmapFromDisk(File file) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            if (file.exists()) {
                fis = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(fis);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError error) {//这里是有可能内存溢出的
            error.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 将bitmap缓存到本地
     */
    public static void saveBitmapToDisk(String fileName, Bitmap bitmap) {
        if (!isBitmapAvailable(bitmap)) {
            return;
        }
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        saveBitmapToDisk(new File(fileName), bitmap);
    }

    /**
     * 将bitmap缓存到本地 JPEG格式
     */
    public static void saveBitmapToDiskJPEG(String fileName, Bitmap bitmap) {
        if (!isBitmapAvailable(bitmap)) {
            return;
        }
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        saveBitmapToDiskJPEG(new File(fileName), bitmap);
    }

    /**
     * 将bitmap缓存到本地
     */
    public static synchronized void saveBitmapToDisk(File file, Bitmap bitmap) {
        if (!isBitmapAvailable(bitmap)) {
            return;
        }
        if (file.exists()) {
            deleteFile(file);
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            deleteFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            deleteFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            deleteFile(file);
        } catch (Throwable e) {
            e.printStackTrace();
            deleteFile(file);
        }
    }

    /**
     * 将bitmap缓存到本地
     */
    private static void saveBitmapToDiskJPEG(File file, Bitmap bitmap) {
        if (!isBitmapAvailable(bitmap)) {
            return;
        }
        if (file.exists()) {
            deleteFile(file);
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            deleteFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            deleteFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            deleteFile(file);
        } catch (Throwable e) {
            e.printStackTrace();
            deleteFile(file);
        }
    }

    private static void deleteFile(File file) {
        file.delete();
    }

    /**
     * 判断bitmap是否可用 不能为空 不能是已经被回收的 isRecycled返回false
     */
    public static boolean isBitmapAvailable(Bitmap bitmap) {
        if (null == bitmap || bitmap.isRecycled()) {// 如果为null或者是已经回收了的就证明是不可用的
            return false;
        }
        return true;
    }



    public static int getPicRotate(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 通过需要的宽高计算图片收缩的比例
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding bitmaps using the
     * decode* methods from {@link BitmapFactory}. This implementation calculates the closest inSampleSize that will
     * result in the final decoded bitmap having a width and height equal to or larger than the requested width and
     * height. This implementation does not ensure a power of 2 is returned for inSampleSize which can be faster when
     * decoding but results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode* method with
     *                  inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (reqHeight <= 0 || reqWidth <= 0) {
            return inSampleSize;
        }

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            //             This offers some additional logic in case the image has a strange
            //             aspect ratio. For example, a panorama may have a much larger
            //             width than height. In these cases the total pixels might still
            //             end up being too large to fit comfortably in memory, so we should
            //             be more aggressive with sample downloadPlayVideo the image (=largerinSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample downloadPlayVideo
            // further.
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
        // final int height = options.outHeight;
        // final int width = options.outWidth;
        // int inSampleSize = 1;
        //
        // if (height > reqHeight || width > reqWidth) {
        //
        // // Calculate ratios of height and width to requested height and width
        // final int heightRatio = Math.round((float) height / (float) reqHeight);
        // final int widthRatio = Math.round((float) width / (float) reqWidth);
        //
        // // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
        // // with both dimensions larger than or equal to the requested height and width.
        // inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        //
        // // This offers some additional logic in case the image has a strange
        // // aspect ratio. For example, a panorama may have a much larger
        // // width than height. In these cases the total pixels might still
        // // end up being too large to fit comfortably in memory, so we should
        // // be more aggressive with sample downloadPlayVideo the image (=larger inSampleSize).
        //
        // final float totalPixels = width * height;
        //
        // // Anything more than 2x the requested pixels we'll sample downloadPlayVideo further
        // final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        //
        // while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
        // inSampleSize++;
        // }
        // }
        // return inSampleSize;

    }

    /**
     * Decode and sample downloadPlayVideo a bitmap from a file to the requested width and height.
     *
     * @param filename  The full path of the file to decode
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled downloadPlayVideo from the original with the same aspect ratio and dimensions that are equal to or
     * greater than the requested width and height
     * @throws Throwable
     * @deprecated 不要再使用decodefile和decodeResources而是使用decodeStream
     */
    public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) throws Throwable {
        // synchronized (lock1)
        {
            int sample = 0;
            try {
                // First decode with inJustDecodeBounds=true to check dimensions
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filename, options);

                sample = calculateInSampleSize(options, reqWidth, reqHeight);
                // Calculate inSampleSize
                options.inSampleSize = sample;
                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                // InKeLog.e("Image", "warn,path="+filename);
                return BitmapFactory.decodeFile(filename, options);
            } catch (Throwable e) {
                e.printStackTrace();
                if (e instanceof OutOfMemoryError) {
                    throw e;
                }
            }
        }
        return null;
    }

    /**
     * 旋转Bitmap
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        Bitmap rotaBitmap = null;
        try {
            rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return rotaBitmap;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, output);
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @author WangChunliang
     * @Date 2015-7-14
     * @Description 图片加载回调
     */
    public interface BitmapLoadListener {
        void onBitmapLoaded(Bitmap bitmap);
    }

    /**
     * 水平镜像转换
     */
    public static Bitmap horizontalMirrorConvert(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas canvas = new Canvas(newBitmap);
        Matrix matrix = new Matrix();
        //        m.postScale(1, -1); //镜像垂直翻转
        matrix.postScale(-1, 1); //镜像水平翻转
        //        m.postRotate(-90); //旋转-90度
        Bitmap tmpBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        canvas.drawBitmap(tmpBitmap, new Rect(0, 0, tmpBitmap.getWidth(), tmpBitmap.getHeight()), new Rect(0, 0, w, h),
                null);
        return newBitmap;
    }


    public static Bitmap convertViewToBitmap(View view) {
        if (null == view) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.buildDrawingCache();
            bitmap = view.getDrawingCache(true);
        } catch (Exception e) {

        }
        if (null == bitmap) {
            return null;
        }
        return bitmap;
    }

    public static Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secendBitmap) {
        if (firstBitmap == null || secendBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(), firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(secendBitmap, 0, 0, null);
        return bitmap;
    }

}
