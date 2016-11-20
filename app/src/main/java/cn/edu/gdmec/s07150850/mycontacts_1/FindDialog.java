package cn.edu.gdmec.s07150850.mycontacts_1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by zzs on 2016/11/20.
 */
public class FindDialog extends Dialog{
    private Context l_context;
    public FindDialog(Context context) {
        super(context);
        l_context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("查询联系人");
        final Button find= (Button) findViewById(R.id.find);
        Button cansel= (Button) findViewById(R.id.cancel);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText value= (EditText) findViewById(R.id.value);
                ContactsTable ct= new ContactsTable(l_context);
                User[] users=ct.findUserByKey(value.getText().toString());
                for (int i=0;i<users.length;i++){
                    System.out.println("姓名是："+users[i].getName()+"电话是："+users[i].getMobile());
                }
                ((MyContactsActivity)l_context).setUsers(users);
                ((MyContactsActivity)l_context).getListViewAdapter().notifyDataSetChanged();
                ((MyContactsActivity)l_context).setSelectItem(0);
                dismiss();
            }
        });
        cansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
