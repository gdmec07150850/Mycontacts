package cn.edu.gdmec.s07150850.mycontacts_1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by zzs on 2016/11/20.
 */
public class MyContactsActivity extends Activity{
    private ListView listView;
    private BaseAdapter listViewAdapter;
    private User users[];
    private int selectItem=0;
    public  BaseAdapter getListViewAdapter(){
        return listViewAdapter;
    }
    public void setUsers(User[] users){
        this.users=users;
    }
    public void setSelectItem(int selectItem){
        this.selectItem=selectItem;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("通讯录");
        listView= (ListView) findViewById(R.id.listView);
        loadContacts();
    }
    private void loadContacts(){
        ContactsTable ct=new ContactsTable(this);
        users=ct.getAllUser();
        listViewAdapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return users.length;
            }

            @Override
            public Object getItem(int position) {
                return users[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null){
                    TextView textView=new TextView(MyContactsActivity.this);
                    textView.setTextSize(22);
                    convertView=textView;
                }
                String mobile=users[position].getMobile()==null?"":users[position].getMobile();
                ((TextView)convertView).setText(users[position].getName()+"---"+mobile);
                if (position==selectItem){
                    convertView.setBackgroundColor(Color.YELLOW);
                }else{
                    convertView.setBackgroundColor(0);
                }
                return convertView;
            }
        };
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem=position;
                listViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,1,Menu.NONE,"添加");
        menu.add(Menu.NONE,2,Menu.NONE,"编辑");
        menu.add(Menu.NONE,3,Menu.NONE,"查看信息");
        menu.add(Menu.NONE,4,Menu.NONE,"删除");
        menu.add(Menu.NONE,5,Menu.NONE,"查询");
        menu.add(Menu.NONE,6,Menu.NONE,"导入到手机电话薄");
        menu.add(Menu.NONE,7,Menu.NONE,"退出");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String sHint;
        switch (item.getItemId()){
            case 1:
                Intent intent=new Intent(MyContactsActivity.this,AddContactsActivity.class);
                startActivity(intent);
                break;
            case 2:if (users[selectItem].getId_DB()>0){
                intent=new Intent(MyContactsActivity.this,UpdateContactsActivity.class);
                intent.putExtra("user_ID",users[selectItem].getId_DB());
                startActivity(intent);
            }else {
                sHint="无结果记录，无法操作！";
                Toast.makeText(this,sHint,Toast.LENGTH_SHORT).show();
            }
                break;
            case 3:
                if (users[selectItem].getId_DB()>0){
                    intent=new Intent(MyContactsActivity.this,ContactsMessageActivity.class);
                    intent.putExtra("user_ID",users[selectItem].getId_DB());
                    startActivity(intent);
                }else {
                    sHint="无结果记录，无法操作！";
                    Toast.makeText(this,sHint,Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                if (users[selectItem].getId_DB()>0){
                    delete();
                }else{
                    sHint="无结果记录，无法操作！";
                    Toast.makeText(this,sHint,Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                new FindDialog(this).show();
                break;
            case 6:
                if (users[selectItem].getId_DB()>0){
                    importPhone(users[selectItem].getName(),users[selectItem].getMobile());
                    sHint="已经成功导入'"+users[selectItem].getName()+"'导入到手机电话薄！";
                    Toast.makeText(this,sHint,Toast.LENGTH_SHORT).show();
                }else{
                    sHint="无结果记录，无法操作！";
                    Toast.makeText(this,sHint,Toast.LENGTH_SHORT).show();
                }
                break;
            case 7:finish();break;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContactsTable ct=new ContactsTable(this);
        users=ct.getAllUser();
        listViewAdapter.notifyDataSetChanged();
    }

    public class FindDialog extends Dialog {
        public FindDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.find);
            setTitle("联系人查询");
            Button find= (Button) findViewById(R.id.find);
            Button cancel= (Button) findViewById(R.id.cancel);
            find.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText value= (EditText) findViewById(R.id.value);
                    ContactsTable ct=new ContactsTable(MyContactsActivity.this);
                    users=ct.findUserByKey(value.getText().toString());
                    for (int i=0;i<users.length;i++){
                        System.out.println("姓名是："+users[i].getName()+"电话是："+users[i].getMobile());
                    }
                    listViewAdapter.notifyDataSetChanged();
                    selectItem=0;
                    dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    public void delete() {
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("系统信息");
        alert.setMessage("是否删除联系人？");
        alert.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContactsTable ct=new ContactsTable(MyContactsActivity.this);
                if (ct.delectByUser(users[selectItem])){
                    users=ct.getAllUser();
                    listViewAdapter.notifyDataSetChanged();
                    selectItem=0;
                    Toast.makeText(MyContactsActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyContactsActivity.this,"删除失败！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }
    public void importPhone(String name,String phone){
        Uri phoneURL= ContactsContract.Data.CONTENT_URI;
        ContentValues values=new ContentValues();
        Uri rawContactUri=this.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,values);
        long rawContactId= ContentUris.parseId(rawContactUri);

        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,name);
        this.getContentResolver().insert(phoneURL,values);

        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,phone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        this.getContentResolver().insert(phoneURL,values);
    }

}
