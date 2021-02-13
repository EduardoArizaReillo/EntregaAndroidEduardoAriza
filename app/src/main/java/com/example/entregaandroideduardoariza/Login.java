package com.example.entregaandroideduardoariza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    private TextView usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario= findViewById(R.id.usuario);
    }

    public void irMain(View view) {
        if (usuario!=null){
            Intent i= new Intent(this,MainActivity.class);
            startActivity(i);
        }

    }
}