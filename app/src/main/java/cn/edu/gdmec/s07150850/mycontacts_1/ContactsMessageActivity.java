package cn.edu.gdmec.s07150850.mycontacts_1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;

/**
 * Created by zzs on 2016/11/20.
 */
public class ContactsMessageActivity extends Activity {
    private EditText nameEditText;
    private EditText mobileEditText;
    private EditText qqEditText;
    private EditText danweiEditText;
    private EditText addressEditText;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        setTitle("联系人信息");

        nameEditText= (EditText) findViewById(R.id.name);
        mobileEditText= (EditText) findViewById(R.id.mobile);
        danweiEditText= (EditText) findViewById(R.id.danwei);
        qqEditText= (EditText) findViewById(R.id.qq);
        addressEditText= (EditText) findViewById(R.id.address);

        Bundle localBundle=getIntent().getExtras();
        int id=localBundle.getInt("user_ID");
        ContactsTable ct=new ContactsTable(this);
        user=ct.getUserByID(id);
        nameEditText.setText("姓名："+user.getMobile());
        mobileEditText.setText("电话："+user.getMobile());
        qqEditText.setText("QQ："+user.getQq());
        danweiEditText.setText("单位："+user.getDanwei());
        addressEditText.setText("地址："+user.getAddress());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,1,Menu.NONE,"返回");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case 1:finish();break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
}
