package com.example.todoapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.*;
import android.view.*;
import android.app.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.database.TaskContract;
import com.example.todoapp.database.TaskDBHelper;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TaskDBHelper taskDBHelper;
    private ListView TaskList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskDBHelper = new TaskDBHelper(this);
        TaskList = findViewById(R.id.list_todo);
        updateUI();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about_app:
               startActivity(new Intent(MainActivity.this, AboutAppActivity.class));
                return true;
            case R.id.nav_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            case R.id.add_task:
                final EditText taskEdit = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.add_task).setMessage(R.string.want).setView(taskEdit)
                        .setPositiveButton(R.string.add, (dialogInterface, i) -> {
                            String task = String.valueOf(taskEdit.getText());
                            SQLiteDatabase db = taskDBHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                            db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                            db.close();
                            updateUI();
                        }).setNegativeButton(R.string.cancel, null).create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteTask(View view){
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.title_task);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = taskDBHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE, TaskContract.TaskEntry.COL_TASK_TITLE + " = ?", new String[]{task});
        db.close();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void updateUI(){
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = taskDBHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()){
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if(arrayAdapter == null){
            arrayAdapter = new ArrayAdapter<>(this, R.layout.list_row_main, R.id.title_task, taskList);
            TaskList.setAdapter(arrayAdapter);
        } else{
            arrayAdapter.clear();
            arrayAdapter.addAll(taskList);
            arrayAdapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }

}