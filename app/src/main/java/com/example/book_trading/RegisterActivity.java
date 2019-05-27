package com.example.book_trading;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText  UserName, UserPassword;
    private Button BnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserName = (EditText) findViewById(R.id.txt_user_name);
        UserPassword = (EditText) findViewById(R.id.txt_password);
        final Button RegBtn = (Button) findViewById(R.id.bn_register);
        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //User Registrieren
                performRegistration();
            }
        });
    }

    public void performRegistration(){

        String username = UserName.getText().toString();
        String password = UserPassword.getText().toString();
        try {
            password = HashHelper.encrypt(password); //verschlüsseln des Textes
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (username.length()==0 || password.length()==0){
            LoginActivity.prefConfig.displayToast("Leereingaben sind ungültig!");
            return;
        }
        Call<User> call = LoginActivity.apiInterface.performRegistration(username,password);// alles ab hier nach unten
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body().getResponse().equals("ok")){
                    LoginActivity.prefConfig.displayToast("Registration success...");    //Registrierung hat funktioniert
                    //springe zurück
                }else if(response.body().getResponse().equals("exist")){
                    LoginActivity.prefConfig.displayToast("User already exist..."); //Es gibt bereits einen User mit gleichem Username
                }else if(response.body().getResponse().equals("error")){
                    LoginActivity.prefConfig.displayToast("Something went wrong...");   //Es ist ein fehler aufgetreten, vorgang wiederholen
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
          //Nach dem anmelden werden die Felder wieder geleert und es können neue Inhalte eingetragen werden
        UserPassword.setText("");
        UserName.setText("");
    }

}