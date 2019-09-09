package com.example.gj.puzzle.Activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gj.puzzle.R;
import com.example.gj.puzzle.Utils.BaseActivity;
import com.example.gj.puzzle.Utils.ImageSplitterUtil;
import com.example.gj.puzzle.Utils.SpUtil;
import com.example.gj.puzzle.dialog.SelectImageDialog;
import com.example.gj.puzzle.entity.ImageSoures;
import com.example.gj.puzzle.ui.GamePintuLayout;

import java.lang.reflect.GenericArrayType;

/**
 * Created by gj
 * Created on 8/29/19
 * Description
 */

public class GameActivity extends BaseActivity {
    private ContentResolver contentResolver;
    //标示着activity的暂停
    private boolean status = true;
    //拼图的布局
    private GamePintuLayout mGamePintuLayout;
    private TextView mLevel;
    private TextView mTime;
    private ImageView imageView;
    private Button btnstart, btnpause, btnAddLevel, btnReduceLevel;
    private SelectImageDialog selectImageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        SpUtil.put(GameActivity.this, "save", true);
        contentResolver = this.getContentResolver();
        Toolbar toolbar = findViewById(R.id.myToolbarg);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);
        (toolbar.findViewById(R.id.back_tvg)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status = false;
                mGamePintuLayout.pause();
                new AlertDialog.Builder(GameActivity.this)
                        .setTitle(getResources().getString(R.string.remind))
                        .setMessage(getResources().getString(R.string.leave_info))
                        .setIcon(R.drawable.info)
                        .setPositiveButton(getResources().getString(R.string.quit_directly), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SpUtil.clear(GameActivity.this);
                                GameActivity.this.finish();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.quit_save), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                //添加一个缓存存在的标示
                                SpUtil.put(GameActivity.this, "save", true);
                                GameActivity.this.finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();

            }
        });
        mTime = findViewById(R.id.id_time);
        if ((Integer) (SpUtil.get(GameActivity.this, "time", 0)) > 0) {
            int i = (Integer) SpUtil.get(GameActivity.this, "time", 0);
            mTime.setText("" + i);
        }
        mLevel = findViewById(R.id.id_level);
        mLevel.setText("" + (GamePintuLayout.mColumn - 2));
        imageView = findViewById(R.id.ivSrcImg);
        imageView.setImageBitmap(ImageSplitterUtil.readBitmap(getApplicationContext(), ImageSoures.imageSours[GamePintuLayout.PICTURENUM], 4));
        btnstart = findViewById(R.id.btnstart);
        btnpause = findViewById(R.id.btnpause);
        btnAddLevel = findViewById(R.id.btnAddLevel);
        btnReduceLevel = findViewById(R.id.btnReduceLevel);
        btnpause.setEnabled(false);
        mGamePintuLayout = findViewById(R.id.id_game_pintu);
        mGamePintuLayout.setTimeEabled(false);
        mGamePintuLayout.setcanContinuePoint(false);
        //实现自定义view里的接口。
        mGamePintuLayout.setOnGamePintuListener(new GamePintuLayout.GamePintuListener() {
            @Override
            public void timeChanged(int currentTime) {
                mTime.setText("" + currentTime);
            }

            @Override
            public void gamesuccess() {
                LinearLayout playername = (LinearLayout) GameActivity.this.getLayoutInflater()
                        .inflate(R.layout.add_player, null);
                final EditText pname = playername.findViewById(R.id.r_pname);
                pname.setText("");
                //assert pname != null;
                /**
                 * 把数据通过provider插入到另一个APP的数据库里
                 */
                AlertDialog.Builder builder=new AlertDialog.Builder(GameActivity.this);
                builder .setView(playername)
                        .setTitle(getResources().getString(R.string.remind)).setMessage(getResources().getString(R.string.success))
                        .setIcon(R.drawable.info);
                builder.setPositiveButton(getResources().getString(R.string.quit_save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.restart), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
                //重写“确定”，截取监听
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pname.getText().toString() == null || pname.getText().toString().trim().equals("")) {
                                    Toast.makeText(GameActivity.this, getResources().getString(R.string.no_name), Toast.LENGTH_SHORT).show();

                                } else {
                                    int score = 0;
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("name", pname.getText().toString());
                                    contentValues.put("score", mTime.getText().toString());
                                    contentValues.put("difficulty",mLevel.getText().toString());
                                    Cursor cursor = contentResolver.query(Uri.parse("content://org.gj.providers.playerProvider/players"), new String[]{"id", "name", "score","difficulty"}, "name = ? and difficulty = ?", new String[]{pname.getText().toString(),mLevel.getText().toString()}, null);
                                    if (cursor.getCount() > 0) {
                                        while (cursor.moveToNext()) {
                                            score = cursor.getInt(2);
                                        }
                                        if (Integer.parseInt(mTime.getText().toString().trim()) <= score) {
                                            contentResolver.update(Uri.parse("content://org.gj.providers.playerProvider/players"), contentValues, "name = ? and difficulty = ?", new String[]{pname.getText().toString(),mLevel.getText().toString()});
                                        }
                                    } else {
                                        contentResolver.insert(
                                                Uri.parse("content://org.gj.providers.playerProvider/players"), contentValues
                                        );
                                    }
                                    Toast.makeText(GameActivity.this, getResources().getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                                    status = false;
                                    dialog.dismiss();
                                    SpUtil.clear(GameActivity.this);
                                    GameActivity.this.finish();
                                }
                    }
                });
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SpUtil.clear(GameActivity.this);
                                //mGamePintuLayout.reStart();
                                GameActivity.this.recreate();
                                dialog.dismiss();
                    }
                });
            }

        });
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnpause.setEnabled(true);
                mGamePintuLayout.pause();
                mGamePintuLayout.reSume();
                btnstart.setEnabled(false);
                btnAddLevel.setEnabled(false);
                btnReduceLevel.setEnabled(false);
                imageView.setClickable(false);
            }
        });
        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.this.onPause();

            }
        });
        btnAddLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTime.setText("0");
                SpUtil.clear(GameActivity.this);
                int i = mGamePintuLayout.nextLevel();
                mLevel.setText("" + (i - 2));
            }
        });
        btnReduceLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTime.setText("0");
                SpUtil.clear(GameActivity.this);
                int i = mGamePintuLayout.lastLevel();
                mLevel.setText("" + (i - 2));
            }
        });
        if (selectImageDialog == null) {
            selectImageDialog = new SelectImageDialog();
            selectImageDialog.addItemClickListener(new SelectImageDialog.OnItemClickListener() {
                @Override
                public void itemClick(int postion, int res) {
                    //更新布局
                    status = false;
                    GamePintuLayout.PICTURENUM = postion;
                    SpUtil.put(GameActivity.this, "time", 0);
                    //mGamePintuLayout.reStart();

                    GameActivity.this.recreate();
                    // imageView.setImageBitmap(ImageSplitterUtil.readBitmap(getApplicationContext(), ImageSoures.imageSours[GamePintuLayout.PICTURENUM], 4));
                }
            });
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageDialog.showDialog(getFragmentManager(), "dialog", 0);
            }
        });


    }

    /**
     * 这个方法是游戏在暂停时创建一个弹出框。
     */
    public void play_onPause() {
        mGamePintuLayout.pause();
        status = false;
        new AlertDialog.Builder(GameActivity.this)
                .setTitle(getResources().getString(R.string.remind))
                .setMessage(getResources().getString(R.string.paused))
                .setIcon(R.drawable.info)
                .setPositiveButton(getResources().getString(R.string.go_on), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        status = true;
                        GameActivity.this.onResume();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.save_game), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        SpUtil.put(GameActivity.this, "save", true);
                        GameActivity.this.finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mGamePintuLayout.pause();
        if (status) {
            play_onPause();
        }
        SpUtil.put(GameActivity.this, "image", GamePintuLayout.PICTURENUM);
//        SpUtil.put(GameActivity.this,"column",GamePintuLayout.mColumn);
    }

    @Override
    protected void onResume() {
        if (status) {
            super.onResume();
            mGamePintuLayout.reSume();
        } else {
            GameActivity.this.onPause();
        }
    }

    @Override
    public void onBackPressed() {
        status = false;
        mGamePintuLayout.pause();
        new AlertDialog.Builder(GameActivity.this)
                .setTitle(getResources().getString(R.string.remind))
                .setMessage(getResources().getString(R.string.leave_info))
                .setIcon(R.drawable.info)
                .setPositiveButton(getResources().getString(R.string.quit_directly), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SpUtil.clear(GameActivity.this);
                        GameActivity.this.finish();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.quit_save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        SpUtil.put(GameActivity.this, "save", true);
                        GameActivity.this.finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();

    }
}
