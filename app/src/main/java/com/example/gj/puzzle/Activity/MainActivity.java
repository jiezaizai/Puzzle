package com.example.gj.puzzle.Activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gj.puzzle.R;
import com.example.gj.puzzle.Utils.BaseActivity;
import com.example.gj.puzzle.Utils.Language_util;
import com.example.gj.puzzle.Utils.SpUtil;
import com.example.gj.puzzle.ui.GamePintuLayout;

import java.lang.reflect.Method;


public class MainActivity extends BaseActivity {
    private String[] language;
    private Button button1, button2, button3, button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        language = new String[]{getResources().getString(R.string.zh), getResources().getString(R.string.en), getResources().getString(R.string.ja)};
        Toolbar toolbar = findViewById(R.id.mytoolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(Color.BLACK);
        this.setSupportActionBar(toolbar);
        button1 = findViewById(R.id.start);
        button2 = findViewById(R.id.go_on);
        button3 = findViewById(R.id.look);
        button4 = findViewById(R.id.quit);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getResources().getString(R.string.remind))
                        .setMessage(getResources().getString(R.string.start_info))
                        .setIcon(R.drawable.info)
                        .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               GamePintuLayout.mColumn=3;
                               GamePintuLayout.PICTURENUM=0;
                                /**
                                 * 开始新游戏前把所以缓存都清理掉
                                 */
                                SpUtil.clear(MainActivity.this);
                                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                                startActivityForResult(intent, 0);

                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
//                SpUtil.clear(MainActivity.this);
//                Intent intent=new Intent(MainActivity.this,GameActivity.class);
//                startActivityForResult(intent,0);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           //按照缓存来读取其中的图片和切割行数。
                                           GamePintuLayout.mColumn =(Integer) SpUtil.get(MainActivity.this, "column",3);
                                           GamePintuLayout.PICTURENUM=(Integer) SpUtil.get(MainActivity.this, "image",0);
                                               Intent intent = new Intent(MainActivity.this, GameActivity.class);
                                               startActivityForResult(intent, 0);
                                           }
                                       });
        button3.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick (View v){
                                           Intent intent = new Intent(MainActivity.this, LookActivity.class);
                                           startActivity(intent);
//                MainActivity.this.finish();

                                       }
                                       });
        button4.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick (View v){
                                           //MainActivity.this.finish();

                                           new AlertDialog.Builder(MainActivity.this)
                                                   .setTitle(getResources().getString(R.string.remind))
                                                   .setMessage(getResources().getString(R.string.leave_info))
                                                   .setIcon(R.drawable.info)
                                                   .setPositiveButton(getResources().getString(R.string.quit), new DialogInterface.OnClickListener() {
                                                       @Override
                                                       public void onClick(DialogInterface dialogInterface, int i) {
                                                           System.exit(0);

                                                       }
                                                   })
                                                   .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                       @Override
                                                       public void onClick(DialogInterface dialogInterface, int i) {
                                                           dialogInterface.dismiss();
                                                       }
                                                   })
                                                   .setCancelable(false)
                                                   .create()
                                                   .show();
                                       }
                                       });

        if(!(Boolean)SpUtil.get(this,"save",false))

                                       {
                                           button2.setEnabled(false);
                                       }else

                                       {
                                           button2.setEnabled(true);
                                       }
                                   }
    //这个方法改变了menu的setOptionalIconsVisible的返回值，这样可以让菜单显示图标和文字
        @Override
        public boolean onMenuOpened ( int featureId, Menu menu){
            if (menu != null) {
                if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                    try {
                        Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                        method.setAccessible(true);
                        method.invoke(menu, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return super.onMenuOpened(featureId, menu);
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            this.getMenuInflater().inflate(R.menu.mymenu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        //菜单的监听事件
        @Override
        public boolean onOptionsItemSelected (MenuItem Item){
            switch (Item.getItemId()) {
                case R.id.lOption:
                    new AlertDialog.Builder(this)
                            .setTitle(getResources().getString(R.string.language_option))
                            .setIcon(R.mipmap.loption)
                            .setItems(language, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getBaseContext(), language[which].toString(), Toast.LENGTH_SHORT).show();
                                    switch (which) {
                                        case 0:
                                            Language_util.setLanAtr("zh", MainActivity.this);
                                            MainActivity.this.recreate();
                                            break;
                                        case 1:
                                            Language_util.setLanAtr("en", MainActivity.this);
                                            MainActivity.this.recreate();
                                            break;
                                        case 2:
                                            Language_util.setLanAtr("ja", MainActivity.this);
                                            MainActivity.this.recreate();
                                            break;

                                    }
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .create()
                            .show();

                    break;
                case R.id.about:
                    LinearLayout alinearLayout = (LinearLayout) this.getLayoutInflater()
                            .inflate(R.layout.about_us, null);
                    new AlertDialog.Builder(this)
                            .setTitle(getResources().getString(R.string.about))
                            .setIcon(R.mipmap.about)
                            .setView(alinearLayout)
                            .setNegativeButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .create().show();
                    break;
            }
            return super.onOptionsItemSelected(Item);
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            //boolean i=(Boolean) SpUtil.get(this,"save",false);
            if (!(Boolean) SpUtil.get(this, "save", false)||SpUtil.get(this,"save",false)==null) {
                button2.setEnabled(false);
            } else {
                button2.setEnabled(true);
            }
        }
        //重写返回键监听事件
        @Override
        public void onBackPressed () {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(getResources().getString(R.string.remind))
                    .setMessage(getResources().getString(R.string.leave_info))
                    .setIcon(R.drawable.info)
                    .setPositiveButton(getResources().getString(R.string.quit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.exit(0);

                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();

        }
    }
