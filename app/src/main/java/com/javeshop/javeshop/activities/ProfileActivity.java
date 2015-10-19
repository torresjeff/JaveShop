package com.javeshop.javeshop.activities;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.dialogs.ChangePasswordDialog;
import com.javeshop.javeshop.infrastructure.User;
import com.javeshop.javeshop.services.Account;
import com.javeshop.javeshop.views.MainNavDrawer;
import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.CropImageActivity;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeffrey Torres on 12/10/2015.
 */
public class ProfileActivity extends BaseAuthenticatedActivity implements View.OnClickListener
{
    private static final int REQUEST_SELECT_IMAGE = 100;

    private static final int STATE_VIEWING = 1;
    private static final int STATE_EDITING = 2;

    private static final String BUNDLE_STATE = "BUNDLE_STATE";

    private boolean isProgressBarVisible;

    private int currentState;
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private EditText emailText;
    private View changeAvatarButton;
    //TODO: agregar EditText para saldo, poner inputType de numbers
    //TODO: agregar EditText para phoneNumber

    private ActionMode editProfileActionMode;


    private ImageView avatarView;
    private View avatarProgressFrame;
    private File tempOutputFile;


    private Dialog progressDialog;
    private boolean progressBarVisible;

    @Override
    protected void onJaveShopCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_profile);
        setNavDrawer(new MainNavDrawer(this));

        View textFieldsContainer = findViewById(R.id.activity_profile_textFieldsContainer);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)textFieldsContainer.getLayoutParams();
        params.setMargins(0, params.getMarginStart(), 0, 0);
        params.removeRule(RelativeLayout.END_OF);
        params.addRule(RelativeLayout.BELOW, R.id.activity_profile_changeAvatar);

        textFieldsContainer.setLayoutParams(params);

        avatarView = (ImageView) findViewById(R.id.activity_profile_avatar);
        avatarProgressFrame = findViewById(R.id.activity_profile_avatarProgressFrame);
        changeAvatarButton = findViewById(R.id.activity_profile_changeAvatar);
        tempOutputFile = new File(getExternalCacheDir(), "javeshop_profile_picture.jpg");

        firstName = (EditText) findViewById(R.id.activity_profile_firstName);
        lastName = (EditText) findViewById(R.id.activity_profile_lastName);
        phoneNumber = (EditText) findViewById(R.id.activity_profile_phoneNumber);
        emailText = (EditText) findViewById(R.id.activity_profile_email);
        emailText.setEnabled(false);

        avatarView.setOnClickListener(this);
        changeAvatarButton.setOnClickListener(this);

        avatarProgressFrame.setVisibility(View.GONE);

        User user = application.getAuth().getUser();
        getSupportActionBar().setTitle(user.getFirstName() + " " + user.getLastName());

        //TODO: load el avatar
        Picasso.with(this).load(user.getAvatarUrl()).into(avatarView);

        if (savedInstanceState == null)
        {
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            phoneNumber.setText(user.getPhoneNumber());
            emailText.setText(user.getEmail());

            changeState(STATE_VIEWING); //default state
        }
        else
        {
            changeState(savedInstanceState.getInt(BUNDLE_STATE));
        }

        if (progressBarVisible)
        {
            setProgressBarVisible(true);
        }
    }

    private void changeState(int state)
    {
        if (state == currentState)
        {
            return;
        }

        currentState = state;

        if (state == STATE_VIEWING)
        {
            firstName.setEnabled(false);
            lastName.setEnabled(false);
            phoneNumber.setEnabled(false);
            changeAvatarButton.setVisibility(View.VISIBLE);

            if (editProfileActionMode != null)
            {
                editProfileActionMode.finish();
                editProfileActionMode = null;
            }
        }

        else if (state == STATE_EDITING)
        {
            firstName.setEnabled(true);
            lastName.setEnabled(true);
            phoneNumber.setEnabled(true);
            changeAvatarButton.setVisibility(View.GONE);

            editProfileActionMode = toolbar.startActionMode(new EditProfileActionCallback());
        }
        else
        {
            throw new IllegalArgumentException("Estado invalido " + state);
        }
    }


    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == R.id.activity_profile_changeAvatar || id == R.id.activity_profile_avatar)
        {
            changeAvatar();
        }
    }

    private void changeAvatar()
    {
        List<Intent> otherImageCaptureIntents = new ArrayList<>();
        List<ResolveInfo> otherImageCaptureActivites = getPackageManager().queryIntentActivities(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);

        for (ResolveInfo info : otherImageCaptureActivites)
        {
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempOutputFile));
            otherImageCaptureIntents.add(captureIntent);
        }

        Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
        selectImageIntent.setType("image/*");

        Intent chooser = Intent.createChooser(selectImageIntent, "Escoge tu imagen");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, otherImageCaptureIntents.toArray(new Parcelable[otherImageCaptureIntents.size()]));

        startActivityForResult(chooser, REQUEST_SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
        {
            tempOutputFile.delete();
            return;
        }

        if (requestCode == REQUEST_SELECT_IMAGE)
        {
            Uri outputFile;
            Uri tempFileUri = Uri.fromFile(tempOutputFile);

            if (data != null && (data.getAction() == null || !data.getAction().equals(MediaStore.ACTION_IMAGE_CAPTURE)))
                outputFile = data.getData();

            else
                outputFile = tempFileUri;

            Crop.of(outputFile, tempFileUri).asSquare().start(this);
            /*new Crop(outputFile)
                    .asSquare()
                    .output(tempFileUri)
                    .start(this);*/
        }

        else if (requestCode == Crop.REQUEST_CROP)
        {
            //TODO: mandar tempFileUri al servidor como el nuevo avatar
            avatarProgressFrame.setVisibility(View.VISIBLE);

            bus.post(new Account.ChangeAvatarRequest(Uri.fromFile(tempOutputFile)));
        }
    }

    @Subscribe
    public void onAvatarUpdated(Account.ChangeAvatarResponse response)
    {
        avatarProgressFrame.setVisibility(View.GONE);
        if (!response.succeeded())
        {
            response.showErrorToast(this);
        }

        //TODO: eliminar esta parte cuando estemos conectados al servidor
        /*avatarView.setImageResource(0);
        avatarView.setImageURI(Uri.parse(response.avatarUrl));*/
        //TODO: cargar la imagen con el url de la imagen en el servidor
        Picasso.with(this).load(response.avatarUrl).into(avatarView);
    }

    @Subscribe
    public void onProfileUpdated(Account.UpdateProfileResponse response)
    {
        if (!response.succeeded())
        {
            response.showErrorToast(this);

            changeState(STATE_EDITING);
        }

        //TODO: que pasa si el response no es successful? se deben reset los campos a lo valores iniciales del usuario

        firstName.setError(response.getPropertyError("firstName"));
        lastName.setError(response.getPropertyError("lastName"));
        emailText.setError(response.getPropertyError("email"));
        setProgressBarVisible(false);
    }

    @Subscribe
    public void onUserDetailsUpdated(Account.UserDetailsUpdatedEvent event)
    {
        //Change our username in the action bar when we update our details
        getSupportActionBar().setTitle(event.user.getFirstName() + " " + event.user.getLastName());
        //TODO: cargar la nueva imagen con el url en el servidor
        //Picasso.with(this).load(event.user.getAvatarUrl()).into(avatarView);
    }

    public void setProgressBarVisible(boolean newVisible)
    {
        if (newVisible)
        {
            progressDialog = new ProgressDialog.Builder(this)
                    .setTitle("Actualizando perfil")
                    .setCancelable(false)
                    .show();
        }
        else if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }


        this.progressBarVisible = newVisible;
    }

    private class EditProfileActionCallback implements ActionMode.Callback
    {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            getMenuInflater().inflate(R.menu.activity_profile_edit, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            int itemId = item.getItemId();

            switch (itemId)
            {
                case R.id.activity_profile_edit_menuDone:
                    setProgressBarVisible(true);
                    changeState(STATE_VIEWING);
                    bus.post(new Account.UpdateProfileRequest(firstName.getText().toString(),
                            lastName.getText().toString(), phoneNumber.getText().toString()));
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            //Cuando estamos editando pero presionamos cancelar
            if (currentState != STATE_VIEWING)
            {
                changeState(STATE_VIEWING);
                //Mantenemos la informacion original del usuario
                User user = application.getAuth().getUser();
                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                phoneNumber.setText(user.getPhoneNumber());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.activity_profile_menuEdit:
                changeState(STATE_EDITING);
                return true;
            case R.id.activity_profile_menuChangePassword:
                FragmentTransaction transaction = getFragmentManager().beginTransaction().addToBackStack(null);
                ChangePasswordDialog dialog = new ChangePasswordDialog();
                dialog.show(transaction, null);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putInt(BUNDLE_STATE, currentState);
    }
}
