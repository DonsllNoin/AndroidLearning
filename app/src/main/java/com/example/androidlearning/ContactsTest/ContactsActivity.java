package com.example.androidlearning.ContactsTest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.androidlearning.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;

    List<String> contactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // 这个 ListView 就绑定一下 Adapter 就可以了，里面啥都不用写
        ListView contactsView = findViewById(R.id.contacts_view);

        // 开始创建 Adapter
        // 第一个参数: 表示当前的 Activity
        // 第二个参数: 表示选择的 xml 格式，这里的 xml 是 Android 自带的
        // 第三个参数: 表示添加进 Adapter 中的 List 表
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsList);
        contactsView.setAdapter(adapter);

        // 如果用户没有授权，则发起一个 获取用户联系人 的授权
        if (ContextCompat.checkSelfPermission
                (this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_CONTACTS
            }, 1);

            // 如果已经授权了
        }else {
            readContact();
        }

    }

    // 查询用户的联系人
    private void readContact() {
        Cursor cursor = null;
        try {
            // 查询 手机联系人 的数据（根据他的 uri 地址）
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,null,null,null);

            // 如果查询到了数据，就开始读取
            if (cursor != null){

                while (cursor.moveToNext()){

                    // 获取联系人姓名
                    String displayName = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                    ));

                    // 获取联系人手机号
                    String number = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    ));

                    // 向 Adapter 中添加数据
                    contactsList.add(displayName + "\n" + number);

                }
                // 起到 动态刷新列表 的作用
                adapter.notifyDataSetChanged();

            }


        }catch (Exception e){
            e.printStackTrace();

        }finally {

            // 关闭资源，避免占用内存
            if (cursor != null){
                cursor.close();
            }
        }
    }

    @Override
    // 这一步是在 onCreate 中用户选择了选项之后，对选项进行一个判断，如果同意的话，那就开始查询并展示
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode){

            // 指定到上面那个 希望获取用户联系人列表 的权限申请
            case 1:
                // 如果用户 点击了同意 的话
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContact();

                }else {
                    Toast.makeText(this, "您拒绝了授权！", Toast.LENGTH_SHORT).show();

                }
                break;

            default:
        }

    }
}