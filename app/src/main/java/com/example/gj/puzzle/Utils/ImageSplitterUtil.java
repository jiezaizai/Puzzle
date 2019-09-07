package com.example.gj.puzzle.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.gj.puzzle.entity.ImagePiece;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gj
 * Created on 8/28/19
 * Description
 */

public class ImageSplitterUtil {

    /**
     * 传入bitmap
     * @param bitmap
     * @param piece
     * 			切成piece * piece 块
     * @return List<ImagePiece>
     */
    public static List<ImagePiece> splitImage(Bitmap bitmap, int piece) {
        List<ImagePiece> imagePieces = new ArrayList<ImagePiece>();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        /**
         * 根据这个切割方法，放入的图片最好选择正方形。
         */
        int pieceWidth = Math.min(width, height)/piece;

        for(int i = 0; i < piece ; i++) {
            for(int j = 0; j < piece; j++) {

                ImagePiece imagePiece = new ImagePiece();
                imagePiece.setIndex(j + i * piece);

                int x = j * pieceWidth;
                int y = i * pieceWidth;

                //  按照左边和资源图片进行切图
                imagePiece.setBitmap(Bitmap.createBitmap(bitmap, x, y, pieceWidth, pieceWidth));
                imagePieces.add(imagePiece);
            }
        }
        return imagePieces;
    }
    /**
     * 读取图片，按照缩放比保持长宽比例返回bitmap对象
     * <p>
     *
     * @param scale 缩放比例(1到10, 为2时，长和宽均缩放至原来的2分之1，为3时缩放至3分之1，以此类推)
     * @return Bitmap
     */
    public synchronized static Bitmap readBitmap(Context context, int res, int scale) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeResource(context.getResources(), res, options);
        } catch (Exception e) {
            return null;
        }
    }
}

