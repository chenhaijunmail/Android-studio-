package com.cjs.notebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements OnScrollListener,
        OnItemClickListener, OnItemLongClickListener {

    private Context mContext;
    private ListView listview;//定义列表
    private SimpleAdapter simp_adapter;
    private List<Map<String, Object>> dataList;//定义数组
    private Button addNote;//保存按钮
    private TextView tv_content;
    private NotesDB DB;
    private SQLiteDatabase dbread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //启动APP就会弹出对话框
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.star_on)
                .setTitle("关于")
                .setMessage("APP名称：备忘录\n" +
                            "制作人：Chenhaijun\n" +
                            "App版本：V1.0.1\n" +
                            "注意！此APP仅用于学习，禁止用于商业")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create().show();

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        tv_content = (TextView) findViewById(R.id.tv_content);
        listview = (ListView) findViewById(R.id.listview);
        dataList = new ArrayList<Map<String, Object>>();

        addNote = (Button) findViewById(R.id.btn_editnote);
        mContext = this;
        addNote.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                NoteEdit.ENTER_STATE = 0;
                Intent intent = new Intent(mContext, NoteEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", "");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        DB = new NotesDB(this);
        dbread = DB.getReadableDatabase();

        // 清空数据库表中内容
        //dbread.execSQL("delete from note");
        refreshNotesList();

        listview.setOnItemClickListener(this);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        listview.setOnScrollListener(this);
    }

    //清除数据
    public void refreshNotesList() {
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simp_adapter.notifyDataSetChanged();
            listview.setAdapter(simp_adapter);
        }
        dataList=getData();
        simp_adapter = new SimpleAdapter(this, dataList, R.layout.item,
                new String[] { "tv_content", "tv_date" }, new int[] {
                R.id.tv_content, R.id.tv_date });
        listview.setAdapter(simp_adapter);
    }

    //从数据库查询数据
    private List<Map<String, Object>> getData() {
        Cursor cursor = dbread.query("note", null, "content!=\"\"", null, null,
                null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tv_content", name);
            map.put("tv_date", date);
            dataList.add(map);
        }
        cursor.close();
        return dataList;

    }

    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    // 滑动listview监听事件
    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
        // TODO Auto-generated method stub
        switch (arg1) {
            case SCROLL_STATE_FLING:
                Log.i("main", "");
            case SCROLL_STATE_IDLE:
                Log.i("main", "");
            case SCROLL_STATE_TOUCH_SCROLL:
                Log.i("main", "");
        }
    }

    // 点击某一个item
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        NoteEdit.ENTER_STATE = 1;
        // Log.d("arg2", arg2 + "");
        // TextView
        // content=(TextView)listview.getChildAt(arg2).findViewById(R.id.tv_content);
        // String content1=content.toString();
        String content = listview.getItemAtPosition(arg2) + "";//{tv_date=2019-06-26, tv_content=爸爸家里}
        String content1 = content.substring(content.lastIndexOf("=") + 1,content.length()-1);
        Log.d("CONTENT", content);

//        Cursor cursor = dbread.query("note", null, "content!=\"\"", null, null,
//                null, null);{tv_date=2019-06-26, tv_content=爸爸家里}


        Cursor c = dbread.query("note", null,"content=" + "'" + content1 + "'", null, null, null, null);
        Log.d("CONTENT", c.getCount()+"");
        if (c.moveToNext()) {
            String No = c.getString(c.getColumnIndex("_id"));
            Log.d("TEXT", No);
            // Intent intent = new Intent(mContext, NoteEdit.class);
            // intent.putExtra("data", text);
            // setResult(4, intent);
            // intent.putExtra("data",text);
            // startActivityForResult(intent, 3);
            Intent myIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("info", content1);
            NoteEdit.id = Integer.parseInt(No);
            myIntent.putExtras(bundle);
            myIntent.setClass(MainActivity.this, NoteEdit.class);
            startActivityForResult(myIntent, 1);
        }
    }

    @Override
    // 接受上一个页面返回的数据
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            refreshNotesList();
        }
    }

    // 长按item的点击事件
    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
        final int n=arg2;
        Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除该条备忘录吗？");
        builder.setIcon(android.R.drawable.btn_dialog);
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = listview.getItemAtPosition(n) + "";
                String content1 = content.substring(content.lastIndexOf("=") + 1,content.length()-1);
                Log.d("CONTENT", content1);
                Cursor c = dbread.query("note", null, "content=" + "'"
                        + content1 + "'", null, null, null, null);
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex("_id"));
                    String sql_del = "update note set content='' where _id="
                            + id;
                    dbread.execSQL(sql_del);
                    refreshNotesList();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
        return true;
    }
}