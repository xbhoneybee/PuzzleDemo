package com.example.bjtu.puzzle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static int Difficulty=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void gotoPuzzle(View view) {
        Intent intent=new Intent(MainActivity.this,Puzzle.class);
        intent.putExtra("Difficulty",Difficulty);
        intent.putExtra("Picture",R.drawable.bjtu);
        startActivity(intent);
    }
    public static int getDifficulty(){
        return Difficulty;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.id_easy:
                Difficulty=3;
                //Toast.makeText(this,"You clicked"+Difficulty,Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_medium:
                Difficulty=4;
                //Toast.makeText(this,"You clicked"+Difficulty,Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_hard:
                Difficulty=5;
                //Toast.makeText(this,"You clicked"+Difficulty,Toast.LENGTH_SHORT).show();
            break;
            default:
            break;
        }
        return true;
    }

}
