package com.example.myapplicationapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import java.util.HashSet;

public class SpanishCrosswordGridAdapter extends BaseAdapter {
    private Context context;
    private String[][] grid;
    private EditText[][] inputCells;
    private SharedPreferences prefs;

    public SpanishCrosswordGridAdapter(Context context, String[][] grid, SharedPreferences prefs) {
        this.context = context;
        this.grid = grid;
        this.prefs = prefs;
        this.inputCells = new EditText[grid.length][grid[0].length];
    }

    @Override
    public int getCount() {
        return grid.length * grid[0].length;
    }

    @Override
    public Object getItem(int position) {
        int row = position / grid[0].length;
        int col = position % grid[0].length;
        return grid[row][col];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int row = position / grid[0].length;
        int col = position % grid[0].length;

        EditText cell = new EditText(context);
        cell.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)}); // Restrict to 1 letter

        if (!grid[row][col].equals("")) {
            cell.setText(grid[row][col]);
            cell.setEnabled(false);
        } else {
            String savedValue = prefs.getString(row + "_" + col, "");
            cell.setText(savedValue);
            cell.setHint("_");
        }

        inputCells[row][col] = cell;
        return cell;
    }

    public boolean isCrosswordCorrect(String[] correctWords) {
        HashSet<String> enteredWords = new HashSet<>();

        // Check words in rows
        for (int i = 0; i < grid.length; i++) {
            StringBuilder wordBuilder = new StringBuilder();
            for (int j = 0; j < grid[i].length; j++) {
                if (inputCells[i][j] != null) {
                    String letter = inputCells[i][j].getText().toString().toUpperCase();
                    if (!letter.isEmpty()) {
                        wordBuilder.append(letter);
                    }
                }
            }
            if (wordBuilder.length() > 1) {
                enteredWords.add(wordBuilder.toString());
            }
        }

        // Check words in columns
        for (int j = 0; j < grid[0].length; j++) {
            StringBuilder wordBuilder = new StringBuilder();
            for (int i = 0; i < grid.length; i++) {
                if (inputCells[i][j] != null) {
                    String letter = inputCells[i][j].getText().toString().toUpperCase();
                    if (!letter.isEmpty()) {
                        wordBuilder.append(letter);
                    }
                }
            }
            if (wordBuilder.length() > 1) {
                enteredWords.add(wordBuilder.toString());
            }
        }

        for (String word : correctWords) {
            if (!enteredWords.contains(word)) {
                return false;
            }
        }
        return true;
    }

    public void saveUserProgress() {
        SharedPreferences.Editor editor = prefs.edit();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (inputCells[i][j] != null) {
                    editor.putString(i + "_" + j, inputCells[i][j].getText().toString());
                }
            }
        }
        editor.apply();
    }
}
