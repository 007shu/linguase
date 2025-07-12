package com.example.myapplicationapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import java.util.HashSet;

public class GermanCrosswordGridAdapter extends BaseAdapter {
    private Context context;
    private String[][] grid;
    private EditText[][] inputCells;
    private SharedPreferences prefs;

    public GermanCrosswordGridAdapter(Context context, String[][] grid, SharedPreferences prefs) {
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

        // Prevent index out of bounds error
        if (row >= grid.length || col >= grid[row].length) {
            return new View(context);
        }

        EditText cell;
        if (convertView == null) {
            cell = new EditText(context);
        } else {
            cell = (EditText) convertView;
        }

        if (!grid[row][col].equals("")) {
            cell.setText(grid[row][col]);
            cell.setEnabled(false);
        } else {
            String savedValue = prefs.getString(row + "_" + col, "");
            cell.setText(savedValue);
            cell.setHint("_");
        }

        return cell;
    }

    public boolean isCrosswordCorrect(String[] correctWords) {
        HashSet<String> enteredWords = new HashSet<>();
        StringBuilder wordBuilder = new StringBuilder();

        for (int i = 0; i < grid.length; i++) {
            wordBuilder.setLength(0);
            for (int j = 0; j < grid[i].length; j++) {
                String letter = inputCells[i][j].getText().toString().toUpperCase();
                if (!letter.isEmpty()) {
                    wordBuilder.append(letter);
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
                editor.putString(i + "_" + j, inputCells[i][j].getText().toString());
            }
        }
        editor.apply();
    }
}