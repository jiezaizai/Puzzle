package com.example.gj.puzzle.ui;

/**
 * Created by gj
 * Created on 8/28/19
 * Description
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gj.puzzle.Activity.GameActivity;
import com.example.gj.puzzle.Activity.MainActivity;
import com.example.gj.puzzle.R;
import com.example.gj.puzzle.Utils.ImageSplitterUtil;
import com.example.gj.puzzle.Utils.MyApplication;
import com.example.gj.puzzle.Utils.SpUtil;
import com.example.gj.puzzle.entity.ImagePiece;
import com.example.gj.puzzle.entity.ImageSoures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressLint("HandlerLeak")
public class GamePintuLayout extends RelativeLayout implements View.OnClickListener {

    /**
     * 拼图图片资源来源于ImageSoures类。
     */
    //表示拼图的行数
    public static int mColumn = 3;    // 最开始默认为是3x3
    /**
     * 容器的内边距
     */
    private int mPadding;
    /**
     * 每张小图之间的距离 dp
     */
    private int mMargin = 3;
    public static int PICTURENUM = 0;

    private ImageView[] mGamePintuItems;

    /**
     * 游戏面板的宽度
     */
    private int mWidth;

    private int mItemWidth;

    /**
     * 游戏的图片
     */
    private Bitmap mBitmap;

    private List<ImagePiece> mItemsBitmaps;
    private String[] tags;
    private boolean once;
    private boolean isGameSuccess;
    private boolean isPause;

    // 可以继续点击
    private boolean canContinuePoint = true;
    private Context context;

    /**
     * 回调的接口
     */
    public interface GamePintuListener {
        void gamesuccess();

        void timeChanged(int currentTime);

    }

    public GamePintuListener mListener;

    /**
     * 设置接口回调
     */
    public void setOnGamePintuListener(GamePintuListener mListener) {
        this.mListener = mListener;
    }

    private static final int TIME_CHANGED = 01;
    private static final int NEXT_LEVEL = 11;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_CHANGED:
                    // 如果游戏过关或者是时间到还未过关或者是游戏暂停 计时器时间不发生改变
                    if (isGameSuccess || isPause)
                        return;
                    if (mListener != null) {
                        mListener.timeChanged(mTime);
                    }
                    SpUtil.put(context, "time", mTime);
                    mTime++;
                 //   Log.e("gj", "++++++++++++++" + mTime);
                    mHandler.sendEmptyMessageDelayed(TIME_CHANGED, 1000); // 继续使用handler发送广播达到时间递减的效果
                    break;
                case NEXT_LEVEL:
                    if (mListener != null) {
                        mListener.gamesuccess();
                    } else {
                        nextLevel();
                        canContinuePoint = false;
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    private boolean isTimeEabled; // 是否开启时间
    private int mTime; // 计时器记录时间

    /**
     * 设置是否开启时间
     *
     * @param isTimeEabled
     */
    public void setTimeEabled(boolean isTimeEabled) {
        this.isTimeEabled = isTimeEabled;
    }

    /**
     * 设置是否可以移动拼图
     * @param canContinuePoint
     */
    public void setcanContinuePoint(boolean canContinuePoint) {
        this.canContinuePoint = canContinuePoint;
    }

    public GamePintuLayout(Context context) {
        this(context, null);
    }

    public GamePintuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GamePintuLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 初始化的方法
        this.context = context;
        init();
    }

    /**
     * TypedValue.applyDimension()方法的功能就是把非标准尺寸转换成标准尺寸, 如:
     * dp->px:  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
     * 中间的数字参数就是所转换的值
     */
    private void init() {
        // 将dp 装换成px
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                3, getResources().getDisplayMetrics());
        // 获取游戏界面的各个边界中的最小边界为图片的内边界
        mPadding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(),
                getPaddingBottom());
    }

    /**
     * 重写onMeasure方法 确定当前布局的大小 此处要设置成为一个正方形
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 取宽和高的最小值作为容器的宽高度
        mWidth = context.getResources().getDisplayMetrics().widthPixels;
        //绘制图形只能执行一次
        if (!once) {
            // 进行切图, 以及排序
            initBitmap();
            // 设置ImageView(Item)的宽高等属性
            initItem();
            // 判断是否开启时间
           // checkTimeEnable();

            once = true;
        }
        setMeasuredDimension(mWidth, mWidth);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    private void checkTimeEnable() {
        if (isTimeEabled) {
            if ((Integer) (SpUtil.get(context, "time", 0)) == 0) {
                mTime = 1;
            } else {
                mTime = (Integer) SpUtil.get(context, "time", 0);
            }
            // 通过handler发送通知
            mHandler.sendEmptyMessage(TIME_CHANGED);
        }
    }

    /**
     * 进行切图, 以及排序
     */
    private void initBitmap() {
        // 这里设置图片
        mBitmap = BitmapFactory.decodeResource(getResources(), ImageSoures.imageSours[PICTURENUM]);
        // 按照指定图片和列数进行切图
        mItemsBitmaps = ImageSplitterUtil.splitImage(mBitmap, mColumn);
        tags = SpUtil.getStringArray(context, "tag");
        // 使用 sort 使图片乱序的功能
        if (tags.length <= 0) {
            tags = new String[mColumn * mColumn];
            Collections.sort(mItemsBitmaps, new Comparator<ImagePiece>() {

                @Override
                public int compare(ImagePiece a, ImagePiece b) {
                    return Math.random() > 0.5 ? 1 : -1;
                }
            });
        } else {
            List<ImagePiece> list = new ArrayList<>();
            for (int i = 0; i < mColumn * mColumn; i++) {
                int l = getImageIndex(tags[i]);
                list.add(mItemsBitmaps.get(l));
            }
            mItemsBitmaps = list;
        }
    }

    /**
     * 设置ImageView(Item)的宽高等属性 以及每个Item的排列
     */
    private void initItem() {
        mItemWidth = (mWidth - mPadding * 2 - mMargin * (mColumn - 1))
                / mColumn;

        mGamePintuItems = new ImageView[mColumn * mColumn];


        // 生成我们的Item， 设置Rule
        for (int i = 0; i < mGamePintuItems.length; i++) {
            ImageView item = new ImageView(getContext());
            // 为每个Item 添加监听
            item.setOnClickListener(this);
            item.setImageBitmap(mItemsBitmaps.get(i).getBitmap());

            mGamePintuItems[i] = item;

            // 为每一个Item设置向对象的ID方便后面对每个Item使用Id进行排版
            item.setId(i + 1);
            // 在Item的tag中存储了index index里面的数字是过关时图片应有的index
            if (tags[i] == null || tags[i].equals("")) {
                item.setTag(i + "_" + mItemsBitmaps.get(i).getIndex());
                tags[i] = i + "_" + mItemsBitmaps.get(i).getIndex();
            } else {
                item.setTag(tags[i]);
            }
            SpUtil.putStringArray(context, "tag", tags);
            Log.d("gj", "initItem: " + tags[i]);
            //LayoutParams继承于Android.View.ViewGroup.LayoutParams，相当于一个Layout的信息包，它封装了Layout的位置、高、宽等信息。
            LayoutParams lp1 = new LayoutParams(
                    mItemWidth, mItemWidth);
            // 设置Item间横向间隙，通过rightMargin

            // 不是最后一列
            if ((i + 1) % mColumn != 0) {
                lp1.rightMargin = mMargin;
            }

            // 不是第一列
            if (i % mColumn != 0) {
                lp1.addRule(RelativeLayout.RIGHT_OF,
                        mGamePintuItems[i - 1].getId());
            }

            // 如果不是第一行
            if ((i + 1) > mColumn) {
                lp1.topMargin = mMargin;
                lp1.addRule(RelativeLayout.BELOW,
                        mGamePintuItems[i - mColumn].getId());
            }

            addView(item, lp1);
        }
    }

    public ImageView[] getImgList() {
        return mGamePintuItems;
    }

    /**
     * 获取多个参数的最小值
     *
     * @return
     */
    private int min(int... params) {
        int min = params[0];
        for (int param : params) {
            if (param < min) {
                min = param;
            }
        }

        return min;
    }

    /**
     * 记录点击的两个图片
     */
    private ImageView mFirst;
    private ImageView mSecond;

    @Override
    public void onClick(View v) {
        //动画中或者设置不能被点击时点击无事件.
        if (isAniming || !canContinuePoint) {
            return;
        }

        // 两次点击同一个Item 取消高亮
        if (mFirst == v) {
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }

        if (mFirst == null) { // 这是第一次点击
            mFirst = (ImageView) v;
            // 设置选中状态 前面的两个数字表示透明度：FF:完全不透明 00：完全透明
            mFirst.setColorFilter(Color.parseColor("#55FF5500"));
        } else { // 这是第二次点击
            mSecond = (ImageView) v;
            // 交换我们的Item
            exchangeView();
        }
    }

    /**
     * 动画层
     */
    private RelativeLayout mAnimLayout;
    private boolean isAniming;

    /**
     * 交换我们的Item
     */
    private void exchangeView() {

        mFirst.setColorFilter(null); // 取消高亮

        // 构造我们的动画层
        setUpAnimLayout();

        String firstTag = (String) mFirst.getTag();
        ImageView firstImage = new ImageView(getContext());
        final Bitmap firstBitmap = mItemsBitmaps.get(getImageIdByTag(firstTag))
                .getBitmap();
        firstImage.setImageBitmap(firstBitmap);
        LayoutParams lp1 = new LayoutParams(mItemWidth, mItemWidth);
        lp1.leftMargin = mFirst.getLeft() - mPadding;
        lp1.topMargin = mFirst.getTop() - mPadding;
        firstImage.setLayoutParams(lp1);
        mAnimLayout.addView(firstImage);
        String secondTag = (String) mSecond.getTag();
        ImageView secondImage = new ImageView(getContext());
        final Bitmap secondBitmap = mItemsBitmaps.get(
                getImageIdByTag(secondTag)).getBitmap();
        secondImage.setImageBitmap(secondBitmap);
        LayoutParams lp2 = new LayoutParams(mItemWidth, mItemWidth);
        lp2.leftMargin = mSecond.getLeft() - mPadding;
        lp2.topMargin = mSecond.getTop() - mPadding;
        secondImage.setLayoutParams(lp2);
        mAnimLayout.addView(secondImage);

        // 设置动画
        TranslateAnimation animFirst = new TranslateAnimation(0,
                mSecond.getLeft() - mFirst.getLeft(), 0, mSecond.getTop()
                - mFirst.getTop());
        animFirst.setDuration(300);
        animFirst.setFillAfter(true);
        firstImage.startAnimation(animFirst); // 为第一个点击的图片设置动画

        TranslateAnimation animSecond = new TranslateAnimation(0,
                -mSecond.getLeft() + mFirst.getLeft(), 0, -mSecond.getTop()
                + mFirst.getTop());
        animSecond.setDuration(300);
        animSecond.setFillAfter(true);
        secondImage.startAnimation(animSecond); // 为第二个点击的图片设置动画

        // 监听动画
        animFirst.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                mFirst.setVisibility(View.INVISIBLE);
                mSecond.setVisibility(View.INVISIBLE);
                isAniming = true;
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                String firstTag = (String) mFirst.getTag();
                String secondTag = (String) mSecond.getTag();

                mFirst.setImageBitmap(secondBitmap);
                mSecond.setImageBitmap(firstBitmap);

                /**
                 * 上面的交换图片的代码只是将指点点击区域的图片进行了改变 但是该区域的相应的信息 (例如：tag和index并没有交换
                 * 就是说该区域显示的图片的信息始终没有改变只是将图片换了 这样会为后面的判断是否过关
                 * 以及将两个图片在此还原时获取的index是和原来一样的所以会交换不了 操作造成困惑和难点 所以在此要将所有的信息进行交换)
                 */
                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);

                mFirst.setVisibility(View.VISIBLE);
                mSecond.setVisibility(View.VISIBLE);

                // 将他们赋值为空 方便下次点击的时候是
                mFirst = mSecond = null;

                // 动画层中删除view 方便下次交换重新定义要交换的动画图片
                mAnimLayout.removeAllViews();

                // 判断用户游戏是否成功
                checkSuccess();

                isAniming = false;
            }
        });
    }

    /**
     * 判断用户游戏是否成功
     */
    private void checkSuccess() {
        boolean isSuccess = true;
        for (int i = 0; i < mGamePintuItems.length; i++) {
            ImageView imageView = mGamePintuItems[i];

            tags[i] = i+"_"+getImageIndex((String) imageView.getTag());
            Log.d("gj", "success" + i + tags[i]);
            SpUtil.putStringArray(context, "tag", tags);
        }
        for (int j = 0; j < mGamePintuItems.length; j++) {
            ImageView imageViewc = mGamePintuItems[j];
            if (getImageIndex((String) imageViewc.getTag()) != j) {
                isSuccess = false;
                break;
            }
        }

        if (isSuccess) {
            isGameSuccess = true;
            // 删掉上一关卡的handler消息 防止进入下一关的时候接收到消息的频率变快 时间变快
            mHandler.removeMessages(NEXT_LEVEL);
            // handler发送消息 然后回调 使得页面重置
            mHandler.sendEmptyMessage(NEXT_LEVEL);
        }
    }

    /**
     * 构造我们的动画层
     */
    private void setUpAnimLayout() {
        if (mAnimLayout == null) {
            mAnimLayout = new RelativeLayout(getContext());
            addView(mAnimLayout);
        }
    }

    /**
     * 返回tag_前面的值
     */
    public int getImageIdByTag(String tag) {
        String[] parms = tag.split("_");
        return Integer.parseInt(parms[0]);
    }

    /**
     * 返回tag_后面的值
     * @param tag
     * @return
     */
    public int getImageIndex(String tag) {
        String[] parms = tag.split("_");
        return Integer.parseInt(parms[1]);
    }

    /**
     * 游戏暂停 计时器暂停
     */
    public void pause() {
        isPause = true;
        mHandler.removeMessages(TIME_CHANGED);
    }

    /**
     * 游戏恢复 计时器继续开启
     */
    public void reSume() {
        if (isPause) {
            isPause = false;
            setcanContinuePoint(true);
            if ((Integer) (SpUtil.get(context, "time", 0)) > 0) {
                int i = (Integer) SpUtil.get(context, "time", 0);
                mTime = i;
            } else {
                mTime = 0;
            }
            mHandler.sendEmptyMessage(TIME_CHANGED);
        }
    }

    /**
     * 下一关卡调用 重置页面
     */
    public int nextLevel() {
        System.out.println("进入GamePintuLayout的nextLevel方法");
        if (mColumn < 7) {
            this.removeAllViews();
            mAnimLayout = null;
            mColumn++;
            SpUtil.put(context, "column", GamePintuLayout.mColumn);
            SpUtil.put(context, "save", true);
            isGameSuccess = false;
            canContinuePoint = false;
            mHandler.removeMessages(TIME_CHANGED);
            setTimeEabled(false);
            // 重新更新时间
            checkTimeEnable();
            // 重置页面
            initBitmap();
            initItem();
        } else {
            Toast.makeText(context, getResources().getString(R.string.already_the_most_level), Toast.LENGTH_SHORT).show();
        }
        return mColumn;
    }

    public int lastLevel() {
        System.out.println("进入GamePintuLayout的lastLevel方法");

        if (mColumn > 3) {
            this.removeAllViews();
            mAnimLayout = null;
            mColumn--;
            SpUtil.put(context, "column", GamePintuLayout.mColumn);
            SpUtil.put(context, "save", true);
            isGameSuccess = false;
            canContinuePoint = false;
            mHandler.removeMessages(TIME_CHANGED);
            setTimeEabled(false);
            // 重新更新时间
            checkTimeEnable();
            // 重置页面
            initBitmap();
            initItem();
        } else {
            Toast.makeText(context, getResources().getString(R.string.already_the_less_level), Toast.LENGTH_SHORT).show();
        }
        return mColumn;
    }
}

