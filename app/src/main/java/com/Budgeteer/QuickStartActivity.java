/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package com.Budgeteer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

public class QuickStartActivity extends AppCompatActivity {
	private AccountAuthService mAuthService;

	private AccountAuthParams mAuthParam;

	private static final int REQUEST_CODE_SIGN_IN = 1000;

	private static final String TAG = "Account";
	private TextView logTextView, txtProfileName;
	ImageView imgProfile;
	ActivityResultLauncher<Intent> resultLauncher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huawei_quickstart);

		txtProfileName = findViewById(R.id.txtProfileName);
		imgProfile = findViewById(R.id.imgProfile);

		findViewById(R.id.HuaweiIdAuthButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				silentSignInByHwId();
			}
		});
		findViewById(R.id.HuaweiIdSignOutButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				signOut();
			}
		});

		findViewById(R.id.HuaweiIdCancelAuthButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelAuthorization();
			}
		});

		logTextView = (TextView) findViewById(R.id.LogText);

		resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
			@Override
			public void onActivityResult(ActivityResult result) {
				if(result.getResultCode() == Activity.RESULT_OK){
					Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(result.getData());
					if (authAccountTask.isSuccessful()) {
						showLog("sign in success");
						AuthAccount authAccount = authAccountTask.getResult();
						dealWithResultOfSignIn(authAccount);
					} else {
						showLog("sign in failed : " + ((ApiException) authAccountTask.getException()).getStatusCode());
					}
				}
			}
		});
	}

	private void silentSignInByHwId() {
		mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
				.setEmail()
				.createParams();

		mAuthService = AccountAuthManager.getService(this, mAuthParam);

		Task<AuthAccount> task = mAuthService.silentSignIn();
		task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
			@Override
			public void onSuccess(AuthAccount authAccount) {
				showLog("silent sign in success");
				dealWithResultOfSignIn(authAccount);
			}
		});
		task.addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(Exception e) {
				if (e instanceof ApiException) {
					Intent signInIntent = mAuthService.getSignInIntent();
					resultLauncher.launch(signInIntent);
				}
			}
		});
	}

	private void dealWithResultOfSignIn(AuthAccount authAccount) {
		txtProfileName.setText(authAccount.getDisplayName());
		imgProfile.setImageURI(authAccount.getAvatarUri());
	}

	private void signOut() {
		if (mAuthService == null) {
			return;
		}
		Task<Void> signOutTask = mAuthService.signOut();
		signOutTask.addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				txtProfileName.setText("Not logged in");
				imgProfile.setImageResource(R.drawable.ic_account);
				showLog("sign out Success");
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(Exception e) {
				showLog("sign out fail");
			}
		});
	}

	private void cancelAuthorization() {
		if (mAuthService == null) {
			return;
		}
		Task<Void> task = mAuthService.cancelAuthorization();
		task.addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				showLog("cancel authorization success");
			}
		});
		task.addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(Exception e) {
				showLog("cancel authorization failure：" + e.getClass().getSimpleName());
			}
		});
	}

	private void showLog(String log) {
		logTextView.setText(log);
	}


}
