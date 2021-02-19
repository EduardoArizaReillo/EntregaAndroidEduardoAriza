package com.example.entregaandroideduardoariza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    SQLiteDatabase db;
    ConexionSQLiteHelper helper;
    String n1;
    private EditText usuario;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario= findViewById(R.id.usuario);
        MainActivity guardarDato = new MainActivity();
        n1 = usuario.getText().toString();
        guardarDato.nameTag = n1;
        login= findViewById(R.id.login);
        usuario = (EditText) findViewById(R.id.usuario);
        usuario.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();
        helper= new ConexionSQLiteHelper(this);
        db=helper.getWritableDatabase();
    }
    //controlamos el login para que no pueda poner un nombre vacio
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
        @Override
        public void afterTextChanged(Editable editable) {
            checkFieldsForEmptyValues(); }
    };
    void checkFieldsForEmptyValues() {
        Button b = (Button) findViewById(R.id.login);
        n1 = usuario.getText().toString();
        if (n1.equals("")) { b.setEnabled(false); }
        else { b.setEnabled(true); }
    }
    public void irMain(View view) {

            Intent myIntent = new Intent(view.getContext(), MainActivity.class);
            myIntent.putExtra("nombreUser", n1);
            startActivityForResult(myIntent, 0);

    }
}