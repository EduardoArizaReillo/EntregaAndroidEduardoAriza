package com.example.entregaandroideduardoariza;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity {
    SQLiteDatabase db;
    ConexionSQLiteHelper helper;
    MediaPlayer mediaPlayer;
    int pos_reproduccion;
    String nameTag;
    String dificultades;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper= new ConexionSQLiteHelper(this);
        db=helper.getWritableDatabase();
        //Inicializamos el array con cada casilla del tablero
        CASILLAS= new int[9];
        CASILLAS[0]=R.id.a1;
        CASILLAS[1]=R.id.a2;
        CASILLAS[2]=R.id.a3;
        CASILLAS[3]=R.id.b1;
        CASILLAS[4]=R.id.b2;
        CASILLAS[5]=R.id.b3;
        CASILLAS[6]=R.id.c1;
        CASILLAS[7]=R.id.c2;
        CASILLAS[8]=R.id.c3;

        //Nombre de usuario
        Intent intent = getIntent();
        nameTag = intent.getStringExtra("nombreUser");
        Toast.makeText(this,nameTag,Toast.LENGTH_LONG);

    }
    private void insertarDatosPartidas(String player1, String player2, String dificultad, String resultado) {

        ContentValues values = new ContentValues();
        values.put("jugador1", player1);
        values.put("jugador2", player2);
        values.put("dificultad", dificultad);
        values.put("resultado", resultado);
        db.insert("datosPartidas", null, values);

    }
    public void Jugar(View v){

        //reseteamos el tablero
        ImageView imagen;

        for (int casilla:CASILLAS){
            imagen=(ImageView)findViewById(casilla);

            imagen.setImageResource(R.drawable.casilla);
        }

        //establecemos los jugadores que van a jugar (1 o 2 jugadores)
        jugadores=1;
        //el metodo Jugar será llamado tanto en el botón de un jugador como en el de dos
        //por eso comprobamos la vista que entra como parámetro
        if(v.getId()==R.id.dosjugadores){
            jugadores=2;
        }

        //evaluamos la dificultad
        RadioGroup configDificultad=(RadioGroup)findViewById(R.id.grupoDificultad);

        int id=configDificultad.getCheckedRadioButtonId();

        int dificultad=0;
        dificultades="Facil";
        if(id==R.id.normal){
            dificultad=1;
            dificultades="Dificil";
        }else if(id==R.id.imposible){
            dificultad=2;
            dificultades="Muy Dificil";
        }

        partida=new Partida(dificultad);

        //deshabilitamos los botones del tablero
        ((Button)findViewById(R.id.unjugador)).setEnabled(false);
        ((Button)findViewById(R.id.dosjugadores)).setEnabled(false);
        ((RadioGroup)findViewById(R.id.grupoDificultad)).setAlpha(0);

    }
    public void sonar(View view) {

        if (mediaPlayer == null) {

            mediaPlayer = MediaPlayer.create(this, R.raw.sonidito);
        }

        if (!mediaPlayer.isPlaying()) {

            mediaPlayer.start();
            mediaPlayer.seekTo(pos_reproduccion);

        }
    }

    //creamos el método que se lanza al pulsar cada casilla
    public void toqueCasilla(View v){
        sonar(v);
        //hacemos que sólo se ejecute cuando la variable partida no sea null
        if(partida==null){
            return;
        }else{
            int casilla=0;
            //recorremos el array donde tenemos almacenada cada casilla
            for(int i=0;i<9;i++){
                if(CASILLAS[i]==v.getId()){
                    casilla=i;
                    break;
                }
            }

            //creamos un mensaje toast
           /* Toast mensaje= Toast.makeText(this,"has pulsado la casilla "+ casilla,Toast.LENGTH_LONG);
            mensaje.setGravity(Gravity.CENTER,0,0);
            mensaje.show();*/

            //si la casilla pulsada ya está ocupada salimos del método
            if(partida.casilla_libre(casilla)==false){
                return;
            }
            //llamamos al método para marcar la casilla que se ha tocado
            marcaCasilla(casilla);

            int resultado=partida.turno();

            if(resultado>0){
                terminar_partida(resultado);
                return;
            }

            //realizamos el marcado de la casilla que elige el programa
            casilla=partida.ia();

            while (partida.casilla_libre(casilla)!=true){
                casilla=partida.ia();
            }

            marcaCasilla(casilla);

            resultado=partida.turno();

            if(resultado>0){
                terminar_partida(resultado);
            }

        }
    }



    private void terminar_partida(int res){

        String mensaje;

        if(res==1) {
            mensaje="Han ganado los círculos";
            insertarDatosPartidas(nameTag,"Bot",dificultades,nameTag);
        }


        else if(res==2) {
            insertarDatosPartidas(nameTag,"Bot",dificultades,"Bot");
            mensaje="Han ganado las aspas";
        }

        else{
            insertarDatosPartidas(nameTag,"Bot",dificultades,"Empate");
            mensaje="Empate";
        }

        Toast toast= Toast.makeText(this,mensaje,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

        //terminamos el juego
        partida=null;

        //habilitamos los botones del tablero
        ((Button)findViewById(R.id.unjugador)).setEnabled(true);
        ((Button)findViewById(R.id.dosjugadores)).setEnabled(true);
        ((RadioGroup)findViewById(R.id.grupoDificultad)).setAlpha(1);

    }

    //metodo para marcar las casillas
    private void marcaCasilla(int casilla){
        ImageView imagen;
        imagen=(ImageView)findViewById(CASILLAS[casilla]);

        if(partida.jugador==1){
            imagen.setImageResource(R.drawable.circulo);
        }else{
            imagen.setImageResource(R.drawable.aspa);
        }

    }

    //creamos un campo de clase para almacenar cuantos jugadores hay
    private int jugadores;
    //para guardar la casilla pulsada
    private int[] CASILLAS;

    private Partida partida;

    public void onPartida(View view) {
        Intent i= new Intent(this,ListarPartidas.class);
        startActivity(i);
    }
}