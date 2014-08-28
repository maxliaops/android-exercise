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

public class RegisterUser extends Activity {

	EditText editUser, editPassword, editName, editEmail;
	Button buttonSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_user);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
		
		editUser = (EditText) findViewById(R.id.editUser);
		editPassword = (EditText) findViewById(R.id.editPassword);
		editName = (EditText) findViewById(R.id.editName);
		editEmail = (EditText) findViewById(R.id.editEmail);
		buttonSave = (Button) findViewById(R.id.buttonSave);

		buttonSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (editUser.getText().toString().isEmpty()
						|| editPassword.getText().toString().isEmpty()
						|| editName.getText().toString().isEmpty()
						|| editEmail.getText().toString().isEmpty())
					showMessage("Error", "Please fill all fields.");
				else {
					String urlPost = "http://121.199.47.152/wt_server/registerUser.php";
					
					ArrayList<NameValuePair> postParameter = new ArrayList<NameValuePair>();
					postParameter.add(new BasicNameValuePair("user",
							editUser.getText().toString()));
					postParameter.add(new BasicNameValuePair("passwd",
							editPassword.getText().toString()));
					postParameter.add(new BasicNameValuePair("name", editName
							.getText().toString()));
					postParameter.add(new BasicNameValuePair("email", editEmail
							.getText().toString()));
					String returnResponse = null;
					try {
						Log.i("LOG",""+postParameter);
						returnResponse = ConnectionHttpClient.executeHttpPost(
								urlPost, postParameter);
						String response = returnResponse.toString();

						if (response.charAt(0) == '1') {
							showMessage("Registered", "User registered");
						} else {
							showMessage("Error", "User not registered");
						}
					} catch (Exception error) {
						showMessage("Error", "Could not register" + error);
					}
				}
			}
		});
	}

	public void showMessage(String Title, String Text) {
		AlertDialog.Builder message = new AlertDialog.Builder(RegisterUser.this);
		message.setTitle(Title);
		message.setMessage(Text);
		message.setNeutralButton("Ok", null);
		message.show();
	}
}

