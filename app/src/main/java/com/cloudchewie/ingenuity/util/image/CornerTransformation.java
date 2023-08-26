package com.cloudchewie.ingenuity.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.cloudchewie.util.ui.SizeUtil;

import java.security.MessageDigest;

public class CornerTransformation implements Transformation<Bitmap> {

    private BitmapPool mBitmapPool;

    private float radius;

    private boolean isLeftTop, isRightTop, isLeftBottom, isRightBotoom;

    /**
     * @param context 上下文
     * @param radius  圆角幅度
     */
    public CornerTransformation(Context context, float radius) {
        this.mBitmapPool = Glide.get(context).getBitmapPool();
        this.radius = radius;
    }

    @NonNull
    public static CornerTransformation getTransform(Context context, int radius) {
        return getTransform(context, radius, true, true, true, true);
    }

    @NonNull
    public static CornerTransformation getTransform(Context context, int radius, boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        CornerTransformation cornerTransformation = new CornerTransformation(context, SizeUtil.dp2px(context, radius));
        cornerTransformation.setNeedCorner(leftTop, rightTop, leftBottom, rightBottom);
        return cornerTransformation;
    }

    @NonNull
    public static CornerTransformation getTransform(Context context, boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        CornerTransformation cornerTransformation = new CornerTransformation(context, SizeUtil.dp2px(context, 10));
        cornerTransformation.setNeedCorner(leftTop, rightTop, leftBottom, rightBottom);
        return cornerTransformation;
    }

    /**
     * 需要设置圆角的部分
     *
     * @param leftTop     左上角
     * @param rightTop    右上角
     * @param leftBottom  左下角
     * @param rightBottom 右下角
     */
    public void setNeedCorner(boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        isLeftTop = leftTop;
        isRightTop = rightTop;
        isLeftBottom = leftBottom;
        isRightBotoom = rightBottom;
    }

    @NonNull
    @Override
    public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {

        Bitmap source = resource.get();
        int finalWidth, finalHeight;
        float scale;
        if (outWidth > outHeight) {
            scale = (float) outHeight / (float) outWidth;
            finalWidth = source.getWidth();
            finalHeight = (int) ((float) source.getWidth() * scale);
            if (finalHeight > source.getHeight()) {
                scale = (float) outWidth / (float) outHeight;
                finalHeight = source.getHeight();
                finalWidth = (int) ((float) source.getHeight() * scale);
            }
        } else if (outWidth < outHeight) {
            scale = (float) outWidth / (float) outHeight;
            finalHeight = source.getHeight();
            finalWidth = (int) ((float) source.getHeight() * scale);
            if (finalWidth > source.getWidth()) {
                scale = (float) outHeight / (float) outWidth;
                finalWidth = source.getWidth();
                finalHeight = (int) ((float) source.getWidth() * scale);
            }
        } else {
            finalHeight = source.getHeight();
            finalWidth = finalHeight;
        }
        this.radius *= (float) finalHeight / (float) outHeight;
        Bitmap outBitmap = this.mBitmapPool.get(finalWidth, finalHeight, Bitmap.Config.ARGB_8888);
        if (outBitmap == null) {
            outBitmap = Bitmap.createBitmap(finalWidth, finalHeight, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        int width = (source.getWidth() - finalWidth) / 2;
        int height = (source.getHeight() - finalHeight) / 2;
        if (width != 0 || height != 0) {
            Matrix matrix = new Matrix();
            matrix.setTranslate((float) (-width), (float) (-height));
            shader.setLocalMatrix(matrix);
        }
        paint.setShader(shader);
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0.0F, 0.0F, (float) canvas.getWidth(), (float) canvas.getHeight());
        canvas.drawRoundRect(rectF, this.radius, this.radius, paint);
        if (!isLeftTop) {
            canvas.drawRect(0, 0, radius, radius, paint);
        }
        if (!isRightTop) {
            canvas.drawRect(canvas.getWidth() - radius, 0, canvas.getWidth(), radius, paint);
        }
        if (!isLeftBottom) {
            canvas.drawRect(0, canvas.getHeight() - radius, radius, canvas.getHeight(), paint);
        }
        if (!isRightBotoom) {
            canvas.drawRect(canvas.getWidth() - radius, canvas.getHeight() - radius, canvas.getWidth(), canvas.getHeight(), paint);
        }
        return BitmapResource.obtain(outBitmap, this.mBitmapPool);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    }
}