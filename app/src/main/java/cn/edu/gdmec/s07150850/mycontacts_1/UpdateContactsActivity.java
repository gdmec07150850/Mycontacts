package cn.edu.gdmec.s07150850.mycontacts_1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by zzs on 2016/11/20.
 */
public class UpdateContactsActivity extends Activity{
    private EditText nameEditText;
    private EditText mobileEditText;
    private EditText qqEditText;
    private EditText danweiEditText;
    private EditText addressEditText;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        setTitle("修改联系人");
        nameEditText= (EditText) findViewById(R.id.name);
        mobileEditText= (EditText) findViewById(R.id.mobile);
        danweiEditText= (EditText) findViewById(R.id.danwei);
        qqEditText= (EditText) findViewById(R.id.qq);
        addressEditText= (EditText) findViewById(R.id.address);

        Bundle localBundle=getIntent().getExtras();
        int id=localBundle.getInt("user_ID");
        ContactsTable ct=new ContactsTable(this);
        user=ct.getUserByID(id);
        nameEditText.setText(user.getMobile());
        mobileEditText.setText(user.getMobile());
        qqEditText.setText(user.getQq());
        danweiEditText.setText(user.getDanwei());
        addressEditText.setText(user.getAddress());

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,1,Menu.NONE,"保存");
        menu.add(Menu.NONE,2,Menu.NONE,"返回");
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                if (!nameEditText.getText().toString().equals("")){
                    User user=new User();
                    user.setName(nameEditText.getText().toString());
                    user.setMobile(mobileEditText.getText().toString());
                    user.setDanwei(danweiEditText.getText().toString());
                    user.setQq(qqEditText.getText().toString());
                    user.setAddress(addressEditText.getText().toString());
                    ContactsTable ct=new ContactsTable(UpdateContactsActivity.this);
                    if (ct.updateUser(user)){
                        Toast.makeText(UpdateContactsActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(UpdateContactsActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UpdateContactsActivity.this,"请先输入数据",Toast.LENGTH_SHORT).show();
                }break;
            case 2:
                finish();break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
}
