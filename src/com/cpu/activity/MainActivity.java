package com.cpu.activity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.cpu.adapter.DrawerListAdapter;
import com.cpu.constant.GetUserInformation;
import com.cpu.constant.InternetUrl;
import com.cpu.fragment.HomePageFragment;
import com.cpu.fragment.PersonFragment;
import com.cpu.tools.DealHandler;
import com.cpu.tools.OperateFile;
import com.example.schoolpartner.R;
import com.jauker.widget.BadgeView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.support.v4.widget.DrawerLayout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private HomePageFragment homefrag;
	private PersonFragment personfrag;
	private View homeLayout;
	private View personLayout;
	private View add;

	private ImageView homeImage;
	private ImageView personImage;
	private ImageView tipImageView;

	private TextView hometv;
	private TextView persontv;
	private View main;
	private TextView maintv;
	private ListView mDrawerList;

	private ImageView personInfo;
	private DrawerLayout mDrawerLayout;
	BadgeView badgeView;
	private FragmentManager fragmentmanager;
	private FragmentTransaction transaction;
	private ImageView badge;
	private Dialog dialog;//Լ�����dialog
	AlertDialog dia;
	private String data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentmanager = getFragmentManager();
		init();
		setContentView(main);
		
		// 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 自定义颜色
        tintManager.setTintColor(Color.parseColor("#0099FF"));
        
		badgeView = new BadgeView(this);  
		badgeView.setTargetView(badge);  
		badgeView.setBadgeCount(3);  
		setTab(0);
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
	private void init() {
		main = this.getLayoutInflater().inflate(R.layout.menu, null);
		homeLayout = main.findViewById(R.id.include1).findViewById(
				R.id.homepage_layout);
		personLayout = main.findViewById(R.id.include1).findViewById(
				R.id.person_layout);
		badge = (ImageView) main.findViewById(R.id.include1).findViewById(
				R.id.person_tips);
		
		
		personImage = (ImageView) main.findViewById(R.id.include1)
				.findViewById(R.id.person_image);
		persontv = (TextView) main.findViewById(R.id.include1).findViewById(
				R.id.person_text);
		homeImage = (ImageView) main.findViewById(R.id.include1).findViewById(
				R.id.homepage_image);
		hometv = (TextView) main.findViewById(R.id.include1).findViewById(
				R.id.homepage_text);
		maintv = (TextView) main.findViewById(R.id.include1).findViewById(
				R.id.main_title);
		tipImageView = (ImageView)main.findViewById(R.id.include1).findViewById(R.id.tixing);
		add = main.findViewById(R.id.include1).findViewById(R.id.add_layout);
		add.setId(10);
		mDrawerLayout = (DrawerLayout) main.findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) main.findViewById(R.id.left_drawer);
		mDrawerList.setAdapter(new DrawerListAdapter(getApplicationContext()) {
			
			@Override
			public void convert(ViewHolder helper, int position) {
				helper.getPhoto().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						try {
							if(GetUserInformation.ReadId("Token")==null){
								Intent it = new Intent();
								it.setClass(MainActivity.this, LoginActivity.class);
								startActivity(it);
							}else{
								Intent it = new Intent();
								it.setClass(MainActivity.this, MyInfoActivity.class);
								startActivity(it);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				helper.getClearText().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent it = new Intent();
						it.setClass(MainActivity.this, Clear.class);
						startActivity(it);
					}
				});
				helper.getLogoutText().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						try {
							if(GetUserInformation.ReadId("Token")!=null){
								OperateFile.deleteFile("Token");
							}
							finish();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				helper.getRefreshText().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Toast.makeText(getApplicationContext(), 
								"已是最新版本", Toast.LENGTH_LONG).show();
					}
				});
				helper.getAboutText().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent it = new Intent();
						it.setClass(MainActivity.this, About.class);
						startActivity(it);
					}
				});
				helper.getBestText().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent=new Intent(Intent.ACTION_SEND);    
		                intent.setType("image/*");    
		                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");    
		                intent.putExtra(Intent.EXTRA_TEXT,
		                		"啊");    
		                 
		                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
		                startActivity(Intent.createChooser(intent, getTitle()));    
		                
					}
				});
				helper.getAdviceText().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						LayoutInflater flater = LayoutInflater.from(MainActivity.this);
						View dialog = flater.inflate(R.layout.dialog, null);
						
						AlertDialog.Builder builder = new AlertDialog.Builder(
								MainActivity.this);  
						
				        builder.setView(dialog);  
				        builder.create();  
				       
				        final EditText edit = (EditText) 
				        		dialog.findViewById(R.id.editText1); 
				        TextView bt_test = (TextView) 
				        		dialog.findViewById(R.id.textView2);  
				        bt_test.setOnClickListener(
				        		new android.view.View.OnClickListener(){  
				              
				            @Override  
				            public void onClick(View v) {
				            	//链接网络
				            	toIntern(edit.getText().toString());
				            	dia.dismiss();
				            }
  
				        });  
				        TextView bt_test1 = (TextView)
				        		dialog.findViewById(R.id.textView3);  
				        bt_test1.setOnClickListener(
				        		new android.view.View.OnClickListener(){  
				              
				            @Override  
				            public void onClick(View v) { 
				            	dia.dismiss();
				            }  
				        });   
				        dia = builder.show(); 
					}
				});
			}
		});
		personInfo = (ImageView) main.findViewById(R.id.include1).findViewById(
				R.id.main_imageView1);
		personInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		});
		add.setOnClickListener(this);
		homeLayout.setOnClickListener(this);
		personLayout.setOnClickListener(this);
		tipImageView.setOnClickListener(this);
	}
	private void toIntern(String str) {
		
		List<BasicNameValuePair> params;
		params = new ArrayList<BasicNameValuePair>();
		try {
			params.add(new BasicNameValuePair("UserId",
					GetUserInformation.ReadId("Token")+""));
			params.add(new BasicNameValuePair("Myadvice",
					str));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		new DealHandler(params,
				InternetUrl.HeaderPath 
				+ "/API/AppLogin/Login"){

			@Override
			public void success(String result) {
				JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(result);
						int code = jsonObject.getInt("Code");
						if(code == 200){
							
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
			}
			
		};
	}
	protected void onSaveInstanceState(Bundle outState) {
		Log.e("11111111111111111111111111", "MainActivity.onSaveInstanceState");
		// super.onSaveInstanceState(outState);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.homepage_layout:
			setTab(0);
			maintv.setText("约伴");
			break;
		case 10: {
			showAddDialog();//����Լ�����dialog
			break;
		}
		case R.id.person_layout:
			badgeView.setVisibility(View.INVISIBLE);
			setTab(1);
			maintv.setText("我");
			break;
		case R.id.tixing:
			Intent it = new Intent();
			it.setClass(getApplicationContext(), NoticeActivity.class);
			startActivity(it);
			break;
		case R.id.llStudy:
			//Toast.makeText(this, "ѧϰ", 0).show();
			Intent intentS = new Intent(MainActivity.this, Send.class);
			intentS.putExtra("Category", "study");
			startActivity(intentS);
			dialog.dismiss();//�ر�Dialog
			break;
		case R.id.llMovie:
			Intent intentM = new Intent(MainActivity.this, Send.class);
			intentM.putExtra("Category", "movie");
			startActivity(intentM);
			dialog.dismiss();
			break;
			
		case R.id.llTravel:
			Intent intentT = new Intent(MainActivity.this, Send.class);
			intentT.putExtra("Category", "travel");
			startActivity(intentT);
			dialog.dismiss();
			break;
		case R.id.llFood:
			Intent intentF = new Intent(MainActivity.this, Send.class);
			intentF.putExtra("Category", "food");
			startActivity(intentF);
			dialog.dismiss();
			break;
			
		case R.id.llExercise:
			Intent intentE = new Intent(MainActivity.this, Send.class);
			intentE.putExtra("Category", "exercise");
			startActivity(intentE);
			dialog.dismiss();
			break;
			
		case R.id.llOther:
			Intent intentO = new Intent(MainActivity.this, Send.class);
			intentO.putExtra("Category", "other");
			startActivity(intentO);
			dialog.dismiss();
			break;
			
		case R.id.img_close_dialog:/*�ر�Dialog*/
			dialog.dismiss();
			break;
		}
	}

	private void setTab(int i) {
		clear();
		transaction = fragmentmanager.beginTransaction();
		hide_frag(transaction);
		switch (i) {
		case 0:
			hometv.setTextColor(Color.parseColor("#ffcccccc"));
			if (homefrag == null) {
				homefrag = new HomePageFragment();
				transaction.add(R.id.content, homefrag);
			} else if (!homefrag.isAdded()) {
				transaction.show(homefrag);
			} else {
				transaction.show(homefrag);
			}
			break;
		case 1:
			persontv.setTextColor(Color.parseColor("#ffcccccc"));
			if (personfrag == null) {
				personfrag = new PersonFragment();
				transaction.add(R.id.content, personfrag);
			} else if (!personfrag.isAdded()) {
				transaction.add(R.id.content, personfrag);
			} else {
				transaction.show(personfrag);
			}
			break;
		}
		transaction.commit();
	}

	private void hide_frag(FragmentTransaction transaction) {
		if (personfrag != null) {
			transaction.hide(personfrag);
		}
		if (homefrag != null) {
			transaction.hide(homefrag);
		}
	}

	private void clear() {
		personImage.setImageResource(R.drawable.mine);
		homeImage.setImageResource(R.drawable.sb);
		persontv.setTextColor(Color.parseColor("#FFFFFF"));
		hometv.setTextColor(Color.parseColor("#FFFFFF"));
	}
	private void showAddDialog(){
		dialog = new Dialog(MainActivity.this, R.style.MyDialogStyle);
		dialog.setContentView(R.layout.view_dialog_add);//����dialog����
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM);//����dialog��ʾ��λ��
		window.setWindowAnimations(R.style.MyWindowStyle);//��Ӷ���
		dialog.show();
		window.getDecorView().setPadding(0, 0, 0, 0);//����dialog���ڱ߾�Ϊ0
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;//dialog�Ŀ����丸����
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		
		LinearLayout llStudy = (LinearLayout) window.findViewById(R.id.llStudy);
		LinearLayout llMovie = (LinearLayout) window.findViewById(R.id.llMovie);
		LinearLayout llTravel = (LinearLayout) window.findViewById(R.id.llTravel);
		LinearLayout llFood = (LinearLayout) window.findViewById(R.id.llFood);
		LinearLayout llExercise = (LinearLayout) window.findViewById(R.id.llExercise);
		LinearLayout llOther = (LinearLayout) window.findViewById(R.id.llOther);
		ImageView imgClose = (ImageView) window.findViewById(R.id.img_close_dialog);
		
		llStudy.setOnClickListener(this);
		llMovie.setOnClickListener(this);
		llTravel.setOnClickListener(this);
		llFood.setOnClickListener(this);
		llExercise.setOnClickListener(this);
		llOther.setOnClickListener(this);
		imgClose.setOnClickListener(this);
	}
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        {  
        	LayoutInflater flater = LayoutInflater.from(MainActivity.this);
			View dialog = flater.inflate(R.layout.dialog_exit, null);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);  
			
	        builder.setView(dialog);  
	        builder.create();  
	       
	        TextView bt_test = (TextView) 
	        		dialog.findViewById(R.id.textView2);  
	        bt_test.setOnClickListener(
	        		new android.view.View.OnClickListener(){  
	              
	            @Override  
	            public void onClick(View v) {
	            	 finish(); 
	            }

	        });  
	        TextView bt_test1 = (TextView)
	        		dialog.findViewById(R.id.textView3);  
	        bt_test1.setOnClickListener(
	        		new android.view.View.OnClickListener(){  
	              
	            @Override  
	            public void onClick(View v) { 
	            	dia.dismiss();
	            }  
	        });   
	        dia = builder.show();
        }  
          
        return false;  
          
    }  
	   
}  


