package com.example.entregaandroideduardoariza;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListarPartidas extends AppCompatActivity {
    SQLiteDatabase db;
    ConexionSQLiteHelper helper;
    ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_partidas);
        helper= new ConexionSQLiteHelper(this);
        db=helper.getWritableDatabase();
        lista=findViewById(R.id.lista);

        consultaDatosPartidas();
    }
    private void consultaDatosPartidas() {

        helper = new ConexionSQLiteHelper(this);

        db = helper.getReadableDatabase();

        Cursor cursor =
                db.query(EstructureDDBB.EstructuraDatosPartidas.TABLE_NAME_DATOS_PARTIDAS, null, null, null,
                        null, null, null);

        //adaptamos el cursor a nuestro ListView
        String[] from = {EstructureDDBB.EstructuraDatosPartidas.COLUMN_NAME_JUGADOR1,
                EstructureDDBB.EstructuraDatosPartidas.COLUMN_NAME_JUGADOR2,
                EstructureDDBB.EstructuraDatosPartidas.COLUMN_DIFICULTAD,
                EstructureDDBB.EstructuraDatosPartidas.COLUMN_RESULTADO};

        int[] to = { R.id.Nombre1, R.id.Nombre2, R.id.textViewDificultad, R.id.textViewResultado};
        // SON DEL LAYOUT LINEAR_LAYOUT

        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(this,
                R.layout.tabla, cursor, from, to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        lista.setAdapter(adaptador);

        db.close();
    }

    public void vueltaPrincipal(View view) {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }
}