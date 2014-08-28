package org.liaops.tracking;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends Activity {

	EditText editTextUser, editTextPassword;
	Button btAcess, btRegistrar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
		
		editTextUser = (EditText) findViewById(R.id.editUser);
		editTextPassword = (EditText) findViewById(R.id.editPassword);
		btAcess = (Button) findViewById(R.id.btAcess);
		btRegistrar = (Button) findViewById(R.id.btRegister);
		editTextUser.setFocusable(true);

		btAcess.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(editTextPassword.getText().toString().isEmpty() || editTextUser.getText().toString().isEmpty()){
					showMessage("Login", "Fill in all fields.");
				}
				
				else {
				String urlPost = "http://121.199.47.152/wt_server/login.php";

				ArrayList<NameValuePair> postParameter = new ArrayList<NameValuePair>();
				postParameter.add(new BasicNameValuePair("user",
						editTextUser.getText().toString()));
				postParameter.add(new BasicNameValuePair("passwd",
						editTextPassword.getText().toString()));

/*
				Intent intent = new Intent();
				intent.setClass(Login.this, Main.class);
				//i.putExtra("id_user", id);
				startActivity(intent);
				*/
				
				String returnResponse = null;
				try {
					Log.i("LOG",""+postParameter);
					returnResponse = ConnectionHttpClient.executeHttpPost(
							urlPost, postParameter);

					String response = returnResponse.toString();

					int id = Integer.parseInt(response);
					if (id!=0 && id!=-1) {
						Intent i = new Intent();
						i.setClass(Login.this, Main.class);
						i.putExtra("id_user", id);
						startActivity(i);
					} else if (id == -1) {
						showMessage("Login", "Invalid password.");
					} else {
						showMessage("Login", "User not found.");
					}
				} catch (Exception error) {
					Toast.makeText(Login.this, "Erro:" + error,
							Toast.LENGTH_LONG).show();
				}
				
			}
			}
		});

		btRegistrar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				//intent.setAction("RegisterUser");
				intent.setClass(Login.this, RegisterUser.class);
				startActivity(intent);
			}

		});

	}
	
	public void onRestart(){
		super.onRestart();
		editTextUser.setText("");
		editTextPassword.setText("");
		
	}

	public void showMessage(String Title, String Text) {
		AlertDialog.Builder message = new AlertDialog.Builder(Login.this);
		message.setTitle(Title);
		message.setMessage(Text);
		message.setNeutralButton("Ok", null);
		message.show();
	}
}
