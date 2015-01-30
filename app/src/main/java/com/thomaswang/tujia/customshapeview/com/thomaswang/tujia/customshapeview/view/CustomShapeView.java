package com.thomaswang.tujia.customshapeview.com.thomaswang.tujia.customshapeview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.thomaswang.tujia.customshapeview.R;

import java.lang.ref.WeakReference;

/**
 * Created by luwang on 2015/1/30.
 */
public class CustomShapeView extends ImageView {
    private Paint mPaint;
    private float mBorderRadius;

    public static final int DEFAULT_BORDER_RADIUS = 10;
    private int mType;
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;

    private WeakReference<Bitmap> mBitmapWeakRef;
    private Bitmap mMaskBitmap;
    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    public CustomShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomShapeView(Context context) {
        super(context);
    }

    public CustomShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomShapeView);

        mBorderRadius = a.getDimension(R.styleable.CustomShapeView_borderRadius,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_BORDER_RADIUS,
                getResources().getDisplayMetrics()));

        mType = a.getInt(R.styleable.CustomShapeView_type, TYPE_CIRCLE);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(mType == TYPE_CIRCLE) {
            int minWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(minWidth, minWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = (mBitmapWeakRef == null ) ? null : mBitmapWeakRef.get();

        if(bitmap == null || bitmap.isRecycled()) {
            Drawable drawable = getDrawable();
            if(drawable != null) {
                int dWidth = drawable.getIntrinsicWidth();
                int dHeight = drawable.getIntrinsicHeight();
                bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas drawCanvas = new Canvas(bitmap);
                float scale = 1.0f;
                if(mType == TYPE_ROUND) {
                    scale = Math.max(getWidth() * 1.0f / dWidth, getHeight() * 1.0f / dHeight);
                }
                if(mType == TYPE_ROUND) {
                    scale = getWidth() * 1.0f / Math.min(dWidth, dHeight);
                }
                drawable.setBounds(0, 0, (int)(scale * dWidth), (int)(scale * dHeight));
                drawable.draw(drawCanvas);

                if(mMaskBitmap == null || !mMaskBitmap.isRecycled()) {
                    mMaskBitmap = getBitmap();
                }

                mPaint.reset();
                mPaint.setAntiAlias(true);
                mPaint.setXfermode(mXfermode);

                drawCanvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
                mPaint.setXfermode(null);
                canvas.drawBitmap(bitmap, 0, 0, null);
                mBitmapWeakRef = new WeakReference<Bitmap>(bitmap);
            }
        }

        if(bitmap != null) {
            mPaint.setXfermode(null);
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
            return;
        }
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);

        if(mType == TYPE_ROUND) {
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), mBorderRadius, mBorderRadius, paint);
        }
        if(mType == TYPE_CIRCLE) {
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, paint);
        }

        return bitmap;
    }

    @Override
    public void invalidate() {
        mBitmapWeakRef = null;
        if(mMaskBitmap != null) {
            mMaskBitmap.recycle();
            mMaskBitmap = null;
        }
        super.invalidate();
    }

    public void setType(int type) {
        mType = type;
    }
}
