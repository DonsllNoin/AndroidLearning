package com.example.androidlearning.ContactsTest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyProvider extends ContentProvider {

    public static final int TABLE1_DIR = 0;
    public static final int TABLE1_ITEM = 1;
    public static final int TABLE2_DIR = 2;
    public static final int TABLE2_ITEM = 3;

    private static UriMatcher uriMatcher;

    static {

        uriMatcher = new UriMatcher(uriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.app.provider", "table1", TABLE1_DIR);
        uriMatcher.addURI("com.example.app.provider", "table1/#", TABLE1_ITEM);
        uriMatcher.addURI("com.example.app.provider", "table2", TABLE2_DIR);
        uriMatcher.addURI("com.example.app.provider", "table2/#", TABLE2_ITEM);

    }


    @Override
    // 通常会在这里对数据库的创建和升级等操作
    // 返回 true 表示初始化成功，false 表示初始化失败
    // 只有别人用 ContentResolver 来尝试访问我们的内容提供器时才会触发这个 onCreate
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    // uri: 确定查询哪张 表
    // projection：确定查询哪些 列
    // selection/selectionArgs：约束查询哪些 行
    // sortOrder：对 结果 进行排序
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:
                // 查询 table1 表中的 所有数据
                break;

            case TABLE1_ITEM:
                // 查询 table1 表中的 单条数据
                break;

            case TABLE2_DIR:
                // 查询 table2 表中的所有数据
                break;

            case TABLE2_ITEM:
                // 查询 table2 表中的单条数据
                break;

            default:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    // 根据 uri 来返回一个 MIME 类型
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri)){

            case TABLE1_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.app.provider.table1";

            case TABLE1_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.app.provider.table1";

            case TABLE2_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.app.provider.table2";

            case TABLE2_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.app.provider.table2";

            default:
                break;
        }

        return null;

    }

}
