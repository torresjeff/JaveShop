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
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.javeshop.javeshop.R;
import com.javeshop.javeshop.dialogs.ChangePasswordDialog;
import com.javeshop.javeshop.infrastructure.User;
import com.javeshop.javeshop.services.Account;
import com.javeshop.javeshop.views.MainNavDrawer;
import com.soundcloud.android.crop.Crop;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Muestra la informacion del usuario que tiene su sesion iniciada.
 */
public class ProfileActivity extends BaseAuthenticatedActivity implements View.OnClickListener
{
    private static final int REQUEST_SELECT_IMAGE = 100;

    private static final int STATE_VIEWING = 1;
    private static final int STATE_EDITING = 2;

    private static final String BUNDLE_STATE = "BUNDLE_STATE";
    private static final String BUNDLE_FIRST_NAME = "BUNDLE_FIRST_NAME";
    private static final String BUNDLE_LAST_NAME = "BUNDLE_LAST_NAME";
    private static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";


    private int currentState;
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private EditText emailText;
    private View changeAvatarButton;
    //TODO: agregar EditText para saldo, poner inputType de numbers

    private ActionMode editProfileActionMode;


    private ImageView avatarView;
    private View avatarProgressFrame;
    private File tempOutputFile;


    private Dialog progressDialog;
    private boolean progressBarVisible;

    /**
     * Infla la interfaz de la Actividad
     * @param savedInstanceState
     */
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

            changeState(STATE_VIEWING); //default state
        }
        else
        {
            changeState(savedInstanceState.getInt(BUNDLE_STATE));
            firstName.setText(savedInstanceState.getString(BUNDLE_FIRST_NAME));
            lastName.setText(savedInstanceState.getString(BUNDLE_LAST_NAME));
            phoneNumber.setText(savedInstanceState.getString(BUNDLE_PHONE_NUMBER));
        }

        emailText.setText(user.getEmail());

        if (progressBarVisible)
        {
            setProgressBarVisible(true);
        }
    }

    /**
     * Cambia de un estado a otro (editando o viendo) para poner un menu contextual dependiendo del estado.
     * @param state nuevo estado.
     */
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

    /**
     * Responde a eventos de clicks/touch.
     * @param view el View que fue tocado.
     */
    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == R.id.activity_profile_changeAvatar || id == R.id.activity_profile_avatar)
        {
            changeAvatar();
        }
    }

    /**
     * Le da al usuario la opcion de tomar una foto con la camara o escoger una ya existente en la galeria, y ponerla como foto de perfil (avatar).
     */
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

    /**
     * Llamada cuando se escoge alguna imagen.
     * @param requestCode la solicitud que se hizo (seleccionar una imagen o crop).
     * @param resultCode si fue exitoso o no.
     * @param data datos adicionales (por ejemplo la imagen misma).
     */
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

    /**
     * Callback. Se llama automaticamente cuando el servidor responde si el cambio fallo o fue exitoso.
     * @param response respuesta del servidor con el nuevo link del avatar.
     */
    @Subscribe
    public void onAvatarUpdated(Account.ChangeAvatarResponse response)
    {
        avatarProgressFrame.setVisibility(View.GONE);
        User user = application.getAuth().getUser();
        if (!response.succeeded())
        {
            Log.e("ProfileActivity", "Didn't succeed, avatar url = " + user.getAvatarUrl());
            /*Picasso.with(this).setLoggingEnabled(true);*/
            avatarView.invalidate();
            avatarView.setImageResource(0);
            Picasso.with(this).setLoggingEnabled(true);
            //Picasso.with(this).cache.clear();
            //Picasso.with(this).invalidate(user.getAvatarUrl());
            Picasso.with(this).load(user.getAvatarUrl()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(avatarView);
            response.showErrorToast(this);
        }

        //TODO: cargar la imagen con el url de la imagen en el servidor
        Picasso.with(this).load(response.avatarUrl).into(avatarView);
        user.setAvatarUrl(response.avatarUrl);
        bus.post(new Account.UserDetailsUpdatedEvent(user));
    }

    /**
     * Callback. Se llama cuando el servidor responde con los nuevos datos personales que el usuario ingreso.
     * @param response respuesta del servidor.
     */
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

    /**
     * Callback. Se llama automaticamente cuando el usuario actualiza sus datos personales para reflejar el cambio en el ActionBar.
     * @param event
     */
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

    /**
     * Esta clase se encarga de instanciar un menu contextual cuando el usuario esta editando su perfil.
     */
    private class EditProfileActionCallback implements ActionMode.Callback
    {
        /**
         * Infla el menu contextual.
         * @param mode modo.
         * @param menu menu que se quiere inflar.
         * @return true si fue creado satisfactoriamente.
         */
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

        /**
         * Responde a eventos de click/touch en el menu contextual.
         * @param mode modo.
         * @param item item del menu que fue seleccionado.
         * @return true si se manejo el evento correctamente.
         */
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

        /**
         * Se encarga de volver al estado normal de la Actividad cuando se cierra el menu contextual.
         * @param mode modo.
         */
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

    /**
     * Crea el menu de la actividad.
     * @param menu el menu que se va a inflar.
     * @return true si fue creado exitosamente.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_profile, menu);
        return true;
    }

    /**
     * Responde a eventos de click/touch en el menu.
     * @param item elemento del menu que fue seleccionado.
     * @return true si se manejo el evento correctamente.
     */
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

    /**
     * Guarda la informacion que tiene la Actividad cuando se va a recrear.
     * @param savedInstanceState objeto donde se guarda la infomracion necesaria.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putInt(BUNDLE_STATE, currentState);
        savedInstanceState.putString(BUNDLE_FIRST_NAME, firstName.getText().toString());
        savedInstanceState.putString(BUNDLE_LAST_NAME, lastName.getText().toString());
        savedInstanceState.putString(BUNDLE_PHONE_NUMBER, phoneNumber.getText().toString());
    }
}
