package com.example.book_trading;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private TextView RegText;
    private EditText UserName, Userpassword;
    private Button LoginBn;
    OnLoginFormActivityListener loginFormActivityListener;

    private String outputString;


    public interface OnLoginFormActivityListener{

        public void performRegister();
        public void performLogin(String name);
    }


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        RegText = view.findViewById(R.id.register_txt);
        UserName = view.findViewById(R.id.user_name);
        Userpassword = view.findViewById(R.id.user_password);
        LoginBn = view.findViewById(R.id.login_bn);



        LoginBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputString = HashHelper.encrypt(Userpassword.getText().toString()); //verschlüsseln des Textes
                    Userpassword.setText(outputString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                performLogin();
            }
        });

        RegText.setOnClickListener(new View.OnClickListener() { //User klickt TextView
            @Override
            public void onClick(View v) {
                loginFormActivityListener.performRegister();

            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        loginFormActivityListener = (OnLoginFormActivityListener) activity;
    }


    private void performLogin(){
        String username = UserName.getText().toString();
        String password = Userpassword.getText().toString();

        Call<User> call = MainActivity.apiInterface.performUserLogin(username,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body().getResponse().equals("ok")){

                    MainActivity.prefConfig.writeLoginStatus(true);
                    loginFormActivityListener.performLogin(response.body().getName());
                }
                else if(response.body().getResponse().equals("failed")){    //Login failed
                    MainActivity.prefConfig.displayToast("Login Failed. Please try again....");

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        UserName.setText("");   //Felder leeren
        Userpassword.setText("");

    }

}
