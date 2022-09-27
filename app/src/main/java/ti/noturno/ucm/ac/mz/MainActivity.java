package ti.noturno.ucm.ac.mz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //Criacao das Variaveis
    TextView posicao, duracao;
    SeekBar seekBar;
    ImageView btn_recuar, btn_play, btn_pause, btn_adiantar;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializacao das Variaveis
        posicao = findViewById(R.id.posicao);
        duracao = findViewById(R.id.duracao);
        seekBar = findViewById(R.id.seekBar);
        btn_recuar = findViewById(R.id.btn_recuar);
        btn_play = findViewById(R.id.btn_play);
        btn_pause = findViewById(R.id.btn_pause);
        btn_adiantar = findViewById(R.id.btn_adiantar);

        //Inicializacao do Media Player
        mediaPlayer = MediaPlayer.create(this,R.raw.musica);

        //Inicializacao do Runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                //Definindo progresso no SeekBar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                //Atraso 0.5seg
                handler.postDelayed(this, 500);
            }
        };

        //Obter Duracao da Midia
        int duracao1 = mediaPlayer.getDuration();

        //Convertendo milisegundos para minutos e segundos
        String sDuracao = convertFormat(duracao1);

        //Definir duracao no TextView
        duracao.setText(sDuracao);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ocultar Botao de Play
                btn_play.setVisibility(view.GONE);

                //Mostrar Botao de Pause
                btn_pause.setVisibility(view.VISIBLE);

                //Iniciar a Midia
                mediaPlayer.start();

                //Definir Maximo no SeekBar
                seekBar.setMax(mediaPlayer.getDuration());

                //Iniciar o Handler
                handler.postDelayed(runnable, 0);

            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ocultar Botao de Pause
                btn_pause.setVisibility(view.GONE);

                //Mostrar Botao de Play
                btn_play.setVisibility(view.VISIBLE);

                //Pausar a Midia
                mediaPlayer.pause();

                //Parar Handler
                handler.removeCallbacks(runnable);

            }
        });

        btn_adiantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtendo a posicao actual
                int posicaoAtual = mediaPlayer.getCurrentPosition();

                //Obtendo duracao do Som
                int duracao = mediaPlayer.getDuration();

                //Verificando condicoes
                if (mediaPlayer.isPlaying() && duracao != posicaoAtual){

                    posicaoAtual += 5000;

                    posicao.setText(convertFormat(posicaoAtual));

                    mediaPlayer.seekTo(posicaoAtual);
                }
            }
        });

        btn_recuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posicaoAtual = mediaPlayer.getCurrentPosition();

                //Verificando condicoes
                if (mediaPlayer.isPlaying() && posicaoAtual > 5000){

                    posicaoAtual -= 5000;

                    posicao.setText(convertFormat(posicaoAtual));

                    mediaPlayer.seekTo(posicaoAtual);
                }

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                }
                posicao.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btn_pause.setVisibility(View.GONE);

                btn_play.setVisibility(View.VISIBLE);

                mediaPlayer.seekTo(0);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duracao) {

        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duracao), TimeUnit.MILLISECONDS.toSeconds(duracao),
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duracao)));
    }


}