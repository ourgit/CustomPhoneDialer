package customphonedialer.abror96.customphonedialer;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.collections.ArraysKt;

import static android.Manifest.permission.CALL_PHONE;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;
import static android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER;
import static android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DialerActivity extends AppCompatActivity {

    @BindView(R.id.phoneNumberInput)
    EditText phoneNumberInput;

    public static int REQUEST_PERMISSION = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().getData() != null)
            phoneNumberInput.setText(getIntent().getData().getSchemeSpecificPart());

    }

    @Override
    protected void onStart() {
        super.onStart();
        offerReplacingDefaultDialer();

        phoneNumberInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                makeCall();
                return true;
            }
        });
    }

    private void makeCall() {
        if (androidx.core.content.PermissionChecker.checkSelfPermission(this, CALL_PHONE) == PERMISSION_GRANTED) {
            Uri uri = Uri.parse("tel:"+phoneNumberInput.getText().toString().trim());
            startActivity(new Intent(Intent.ACTION_CALL, uri));
        }
    }

    private void offerReplacingDefaultDialer() {
        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);

        if (!getPackageName().equals(telecomManager.getDefaultDialerPackage())) {

//            mDeviceAdmin = new ComponentName(this, AdminReceiver.class);
//            mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//
//            mDpm.setApplicationHidden(mDeviceAdmin, "com.google.android.dialer", true);
//
//
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(Intent.ACTION_DIAL);
//            ComponentName activity = new ComponentName("com.dialer_app", "com.dialer_app.MainActivity");
//            mDpm.addPersistentPreferredActivity(mDeviceAdmin, filter, activity);


            Intent intent = new Intent(ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION && ArraysKt.contains(grantResults, PERMISSION_GRANTED)) {
            makeCall();
        }
    }
}
