package com.cpu.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.schoolpartner.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class RegisterMessage extends Activity implements OnClickListener{
	private EditText register_txt_phone;//用于输入电话号码的文本框
	//private EditText register_txt_valicode;//用于输入验证码的文本框
	
	//private Button register_btn_valicode;//“获取验证码”的Button
	private Button register_btn_next;//“下一步”Button
	
	private ImageView register_back = null;//返回按钮
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_view);
		initView();
		//InitColor();
	}
	private void initView() {
		register_txt_phone = (EditText) findViewById(R.id.register_txt_phone);
		
		//register_txt_valicode = (EditText) findViewById(R.id.register_txt_valicode);
		
		//register_btn_valicode = (Button) findViewById(R.id.register_btn_valicode);
		//register_btn_valicode.setOnClickListener(this);
		
		register_btn_next = (Button) findViewById(R.id.register_btn_next);
		register_btn_next.setOnClickListener(this);
		
		register_back = (ImageView) findViewById(R.id.register_back);
		register_back.setOnClickListener(this);
	}
	void InitColor(){
		// 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 自定义颜色
        tintManager.setTintColor(Color.parseColor("#0099FF"));
	}
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_back://返回
			this.finish();
			break;
		case R.id.register_btn_valicode://"获取验证码"
			break;
		case R.id.register_btn_next:
			if(this.register_txt_phone.getText().length() == 11) {
				Intent intent = new Intent(RegisterMessage.this,RegisterPassword.class);
				intent.putExtra("phone", this.register_txt_phone.getText().toString().trim());
				startActivity(intent);
			}else{
				Toast.makeText(getApplicationContext(),"手机号格式不正确", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
}
