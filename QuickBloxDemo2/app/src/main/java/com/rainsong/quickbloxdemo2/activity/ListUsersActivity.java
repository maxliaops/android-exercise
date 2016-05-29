package com.rainsong.quickbloxdemo2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.rainsong.quickbloxdemo2.R;
import com.rainsong.quickbloxdemo2.adapter.UsersAdapter;
import com.rainsong.quickbloxdemo2.holder.DataHolder;
import com.rainsong.quickbloxdemo2.util.Consts;
import com.rainsong.quickbloxdemo2.util.ErrorUtils;
import com.rainsong.quickbloxdemo2.util.Toaster;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * QuickBlox team
 */
public class ListUsersActivity extends Activity {
    private static final String TAG = ListUsersActivity.class.getSimpleName();

    private static final long ON_ITEM_CLICK_DELAY = TimeUnit.SECONDS.toMillis(10);

    private UsersAdapter usersListAdapter;
    private ListView usersList;
    private ProgressBar progressBar;
    private Context context;
    private static QBChatService chatService;
    private static ArrayList<QBUser> users = new ArrayList<>();
    private volatile boolean resultReceived = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

        if (getActionBar() != null) {
            getActionBar().setTitle(R.string.opponentsListActionBarTitle);
        }

        QBChatService.setDebugEnabled(true);
        QBChatService.setDefaultAutoSendPresenceInterval(60); //seconds
        chatService = QBChatService.getInstance();

        loadUsers();
    }

    private void initUI() {
        usersList = (ListView) findViewById(R.id.usersListView);
        progressBar = (ProgressBar) findViewById(R.id.loginPB);
        progressBar.setVisibility(View.INVISIBLE);

    }

    public static int resourceSelector(int number) {
        int resStr = -1;
        switch (number) {
            case 0:
                resStr = R.drawable.shape_oval_spring_bud;
                break;
            case 1:
                resStr = R.drawable.shape_oval_orange;
                break;
            case 2:
                resStr = R.drawable.shape_oval_water_bondi_beach;
                break;
            case 3:
                resStr = R.drawable.shape_oval_blue_green;
                break;
            case 4:
                resStr = R.drawable.shape_oval_lime;
                break;
            case 5:
                resStr = R.drawable.shape_oval_mauveine;
                break;
            case 6:
                resStr = R.drawable.shape_oval_gentianaceae_blue;
                break;
            case 7:
                resStr = R.drawable.shape_oval_blue;
                break;
            case 8:
                resStr = R.drawable.shape_oval_blue_krayola;
                break;
            case 9:
                resStr = R.drawable.shape_oval_coral;
                break;
            default:
                resStr = resourceSelector(number % 10);
        }
        return resStr;
    }

    public static int selectBackgrounForOpponent(int number) {
        int resStr = -1;
        switch (number) {
            case 0:
                resStr = R.drawable.rectangle_rounded_spring_bud;
                break;
            case 1:
                resStr = R.drawable.rectangle_rounded_orange;
                break;
            case 2:
                resStr = R.drawable.rectangle_rounded_water_bondi_beach;
                break;
            case 3:
                resStr = R.drawable.rectangle_rounded_blue_green;
                break;
            case 4:
                resStr = R.drawable.rectangle_rounded_lime;
                break;
            case 5:
                resStr = R.drawable.rectangle_rounded_mauveine;
                break;
            case 6:
                resStr = R.drawable.rectangle_rounded_gentianaceae_blue;
                break;
            case 7:
                resStr = R.drawable.rectangle_rounded_blue;
                break;
            case 8:
                resStr = R.drawable.rectangle_rounded_blue_krayola;
                break;
            case 9:
                resStr = R.drawable.rectangle_rounded_coral;
                break;
            default:
                resStr = selectBackgrounForOpponent(number % 10);
        }
        return resStr;
    }

    public static int getUserIndex(int id) {
        int index = 0;

        for (QBUser usr : users) {
            if (usr.getId().equals(id)) {
                index = (users.indexOf(usr)) + 1;
                break;
            }
        }
        return index;
    }

    private void initUsersList() {
        usersListAdapter = new UsersAdapter(this, users);
        usersList.setAdapter(usersListAdapter);
        usersList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        usersList.setOnItemClickListener(clicklistener);
    }

    private void loadUsers() {
        showProgress(false);
        users.clear();
        users.addAll(DataHolder.getUsersList());
        initUsersList();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private long upTime = 0l;

    private QBUser currentUser;
    AdapterView.OnItemClickListener clicklistener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!resultReceived || (SystemClock.uptimeMillis() - upTime) < ON_ITEM_CLICK_DELAY) {
                return;
            }
            resultReceived = false;
            upTime = SystemClock.uptimeMillis();
            currentUser = usersListAdapter.getItem(position);

            login(currentUser);
        }
    };

    private void login(final QBUser user) {
        if (chatService.isLoggedIn()) {
            resultReceived = true;
            startCallActivity(user.getLogin());
        } else {
            Log.d(TAG, "JID:" + JIDHelper.INSTANCE.getJid(user) + " password:" + user.getPassword());
            chatService.login(user, new QBEntityCallback<Void>() {

                @Override
                public void onSuccess(Void result, Bundle bundle) {
                    Log.d(TAG, "onSuccess login to chat");
                    resultReceived = true;
                    DataHolder.setLoggedUser(currentUser);

                    ListUsersActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress(false);
                        }
                    });

                    startCallActivity(user.getLogin());
                }

                @Override
                public void onError(QBResponseException exc) {
                    resultReceived = true;

                    showProgress(false);
                    ErrorUtils.showErrorToast(exc);
                }
            });
        }
    }

    private void startCallActivity(String login) {
        Intent intent = new Intent(ListUsersActivity.this, CallActivity.class);
        intent.putExtra("login", login);
        startActivityForResult(intent, Consts.CALL_ACTIVITY_CLOSE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Consts.CALL_ACTIVITY_CLOSE) {
            if (resultCode == Consts.CALL_ACTIVITY_CLOSE_WIFI_DISABLED) {
                Toaster.longToast(R.string.WIFI_DISABLED);
            }
        }
    }
}
