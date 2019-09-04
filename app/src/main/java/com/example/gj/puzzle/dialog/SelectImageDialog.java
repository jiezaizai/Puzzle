package com.example.gj.puzzle.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import org.jetbrains.annotations.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.example.gj.puzzle.R;
import com.example.gj.puzzle.Utils.ImageSplitterUtil;
import com.example.gj.puzzle.entity.ImageSoures;

/**
 * Created by gj
 * Created on 8/28/19
 * Description
 */

public class SelectImageDialog extends DialogFragment {
    private View view;
    private RecyclerView imageList;
    private OnItemClickListener itemClickListener;
    private Activity activity;
    private int selectRes;
    private ImageListAdapter imageListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_select_image, container);
        imageList = view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        imageList.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 2));
        imageListAdapter = new ImageListAdapter();
        imageList.setAdapter(imageListAdapter);
    }

    public class ImageListAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImageViewHolder(LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageViewHolder viewHolder = (ImageViewHolder) holder;
            Bitmap bitmap = ImageSplitterUtil.readBitmap(activity.getApplicationContext(), ImageSoures.imageSours[position], 4);
            viewHolder.imageView.setImageBitmap(bitmap);
        }

        @Override
        public int getItemCount() {
            return ImageSoures.imageSours.length;
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImg);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != -1 && itemClickListener != null) {
                        itemClickListener.itemClick(getAdapterPosition(), ImageSoures.imageSours[getAdapterPosition()]);
                        dismiss();
                    }
                }
            });
        }
    }


    public void showDialog(FragmentManager fragmentManager, String tag, int res) {
        show(fragmentManager, "tag");
        selectRes = res;
    }

    public void addItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void itemClick(int postion, int res);
    }
}
