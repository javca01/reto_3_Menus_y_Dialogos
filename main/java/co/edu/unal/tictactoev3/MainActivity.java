package co.edu.unal.tictactoev3;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button[] mBoardButtons;
    private Button playAgainButton;
    private TextView mInfoTextView;
    private TextView mHumanScoreTextView;
    private TextView mComputerScoreTextView;
    private TextView mTieScoreTextView;

    private TicTacToeGame mGame;

    private int mHumanWins = 0;
    private int mComputerWins = 0;
    private int mTies = 0;
    private boolean mGameOver = false;
    private boolean mHumanFirst = true;

    private TicTacToeGame.DifficultyLevel mDifficultyLevel = TicTacToeGame.DifficultyLevel.Expert;

    TextView textView;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link those objects with their respective id's that we have given in .XML file
        textView = (TextView) findViewById(R.id.textView);
        linearLayout = (LinearLayout) findViewById(R.id.linLayout);

        // here you have to register a view for context menu you can register any view
        // like listview, image view, textview, button etc
        registerForContextMenu(textView);


        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = findViewById(R.id.one);
        mBoardButtons[1] = findViewById(R.id.two);
        mBoardButtons[2] = findViewById(R.id.three);
        mBoardButtons[3] = findViewById(R.id.four);
        mBoardButtons[4] = findViewById(R.id.five);
        mBoardButtons[5] = findViewById(R.id.six);
        mBoardButtons[6] = findViewById(R.id.seven);
        mBoardButtons[7] = findViewById(R.id.eight);
        mBoardButtons[8] = findViewById(R.id.nine);

        playAgainButton = findViewById(R.id.play_again);
        mInfoTextView = findViewById(R.id.information);
        mHumanScoreTextView = findViewById(R.id.human_score);
        mComputerScoreTextView = findViewById(R.id.computer_score);
        mTieScoreTextView = findViewById(R.id.tie_score);

        mGame = new TicTacToeGame();

        playAgainButton.setOnClickListener(v -> startNewGame());

        startNewGame();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // you can set menu header with title icon etc
        menu.setHeaderTitle("Selecciona una opción");
        // add menu items
        menu.add(0, v.getId(), 0, "Nuevo Juego");
        //menu.add(0, v.getId(), 0, "Dificultad");
        // Create a submenu for "Dificultad"
        SubMenu dificultadMenu = menu.addSubMenu(0, v.getId(), 0, "Dificultad");
        dificultadMenu.add(0, v.getId(), 0, "Facil");
        dificultadMenu.add(0, v.getId(), 0, "Medio");
        dificultadMenu.add(0, v.getId(), 0, "Dificil");

        menu.add(0, v.getId(), 0, "Salir");
    }

    // menu item select listener
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Nuevo Juego") {
            //linearLayout.setBackgroundColor(Color.YELLOW);
            startNewGame();
        } else if (item.getTitle() == "Dificultad") {
            //linearLayout.setBackgroundColor(Color.GRAY);
        } else if (item.getTitle() == "Salir") {
            //linearLayout.setBackgroundColor(Color.CYAN);
            showQuitConfirmationDialog();
        }else if(item.getTitle() == "Facil"){
            Toast.makeText(this, "Nivel Fácil elegido", Toast.LENGTH_SHORT).show();
            mDifficultyLevel = TicTacToeGame.DifficultyLevel.Easy;
        }
        else if(item.getTitle() == "Medio"){
            Toast.makeText(this, "Nivel Medio elegido", Toast.LENGTH_SHORT).show();
            mDifficultyLevel = TicTacToeGame.DifficultyLevel.Harder;
        }
        else if(item.getTitle() == "Dificil"){
            Toast.makeText(this, "Nivel Difícil elegido", Toast.LENGTH_SHORT).show();
            mDifficultyLevel = TicTacToeGame.DifficultyLevel.Expert;
        }
        return true;
    }

    private void startNewGame() {
        mGame.clearBoard();
        mGameOver = false;

        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }

        if (mHumanFirst) {
            mInfoTextView.setText(R.string.first_human);
        } else {
            mInfoTextView.setText(R.string.turn_computer);
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
        }

        mHumanFirst = !mHumanFirst; // Alternate first move
    }

    private void setMove(char player, int location) {
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        mBoardButtons[location].setTextColor(player == TicTacToeGame.HUMAN_PLAYER ?
                Color.rgb(0, 200, 0) : Color.rgb(200, 0, 0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("New Game");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startNewGame();
        return true;
    }

    private class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location) {
            this.location = location;
        }

        @Override
        public void onClick(View view) {
            if (mGameOver) return;

            if (mBoardButtons[location].isEnabled()) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);
                int winner = mGame.checkForWinner();

                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }

                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_human);
                } else {
                    mGameOver = true;
                    updateScoresAndDisplayResult(winner);
                }
            }
        }
    }

    private void updateScoresAndDisplayResult(int winner) {
        if (winner == 1) { // Tie
            mInfoTextView.setText(R.string.result_tie);
            mTies++;
            mTieScoreTextView.setText(String.valueOf(mTies));
        } else if (winner == 2) { // Human wins
            mInfoTextView.setText(R.string.result_human_wins);
            mHumanWins++;
            mHumanScoreTextView.setText(String.valueOf(mHumanWins));
        } else if (winner == 3) { // Computer wins
            mInfoTextView.setText(R.string.result_computer_wins);
            mComputerWins++;
            mComputerScoreTextView.setText(String.valueOf(mComputerWins));
        }
    }

    private void showQuitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.quit_confirm)
                .setPositiveButton(R.string.yes, (dialog, id) -> finish())
                .setNegativeButton(R.string.no, null)
                .create()
                .show();
    }

}