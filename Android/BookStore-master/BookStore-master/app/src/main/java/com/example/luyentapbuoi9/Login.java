package com.example.luyentapbuoi9;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luyentapbuoi9.Database.Account;
import com.example.luyentapbuoi9.Database.SQLHelper;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {
    TextView tvforget;
    Button btnLogin, btnRegister;
    TextInputEditText edtUsername, edtPassword;
    CheckBox SaveAcc;
    String ten, mk;
    static  final  String SHARE_PRE_NAME="account";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AnhXa();
        String tvf="<u>forget your account</u>";
        tvforget.setText(Html.fromHtml(tvf));
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        SQLHelper sqlHelper=new SQLHelper(Login.this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(Login.this);
                dialog.setTitle("ĐIỀN THÔNG TIN");
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_dialog_login);

               final EditText edtUsernameRe=dialog.findViewById(R.id.edtUsernameRe),
                       edtPasswordRe=dialog.findViewById(R.id.edtPasswordRe),
                       edtPasswordReAgain=dialog.findViewById(R.id.edtPasswordReAgain);
                Button btnHuy=(Button) dialog.findViewById(R.id.btnhuy);
                Button btnOk=(Button) dialog.findViewById(R.id.btnOk);

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtPasswordRe.getText().toString().compareTo(edtPasswordReAgain.getText().toString())!=0){
                            Toast.makeText(Login.this, "MK không trùng khớp", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            ten=edtUsernameRe.getText().toString();
                            mk=edtPasswordRe.getText().toString();
                            edtUsername.setText(ten);
                            edtPassword.setText(mk);
                            boolean cc= sqlHelper.insertAccount(new Account(ten, mk));
                            if(cc){
                                Toast.makeText(Login.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                            else
                                Toast.makeText(Login.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten=edtUsername.getText().toString();
                String mk=edtPassword.getText().toString();
                Account account=new Account(ten, mk);
               boolean cc= sqlHelper.checkLogin(account);
               if (cc){
                   Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                   Intent intent1=new Intent(Login.this, MainActivity.class);
                   intent1.putExtra("account", (Parcelable) account);
                   startActivity(intent1);
               }
               else
                   Toast.makeText(Login.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
               if(SaveAcc.isChecked()){
                   SaveAccount();
               }
            }
        });

    }

    private void SaveAccount(){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARE_PRE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username", edtUsername.getText().toString());
        editor.putString("password", edtPassword.getText().toString());
        editor.commit();

    }
    private void AnhXa() {
        tvforget=findViewById(R.id.tvforget);
        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnRegister);
        edtPassword=findViewById(R.id.edtPassword);
        edtUsername=findViewById(R.id.edtUsername);
        SaveAcc=findViewById(R.id.SaveAcc);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences=getSharedPreferences(SHARE_PRE_NAME, MODE_PRIVATE);
        String username=sharedPreferences.getString("username", "null");
        String password=sharedPreferences.getString("password", "null");
        edtUsername.setText(username);
        edtPassword.setText(password);
        SaveAcc.isChecked();
    }
}