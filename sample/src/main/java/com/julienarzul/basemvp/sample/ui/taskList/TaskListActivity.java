package com.julienarzul.basemvp.sample.ui.taskList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.julienarzul.basemvp.MvpActivity;
import com.julienarzul.basemvp.sample.R;
import com.julienarzul.basemvp.sample.datasource.DatasourceFactory;
import com.julienarzul.basemvp.sample.model.Task;
import com.julienarzul.basemvp.sample.ui.addEditTask.AddEditTaskActivity;
import com.julienarzul.basemvp.sample.ui.taskDetails.TaskDetailsActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright @ Julien Arzul 2016
 */

public class TaskListActivity extends MvpActivity<TaskListContract.Presenter> implements TaskListContract.View {

    private TaskListAdapter adapter;

    @Override
    protected TaskListContract.Presenter createPresenter() {
        return new TaskListPresenter(DatasourceFactory.tasksDatasource());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_task_list);

        ButterKnife.bind(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        this.adapter = new TaskListAdapter(this.presenter);
        recyclerView.setAdapter(this.adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.presenter.loadData();
    }

    @Override
    public void setTaskList(List<Task> data) {
        this.adapter.setTaskList(data);
    }

    @Override
    public void displayErrorView() {
        // TODO: Add error view in the screen
    }

    @Override
    public void launchTaskDetailsScreen(Task task) {
        this.startActivity(TaskDetailsActivity.getStartingIntent(this.getApplicationContext(), task));
    }

    @Override
    public void launchAddTaskScreen() {
        this.startActivity(AddEditTaskActivity.getStartingIntent(this.getApplicationContext()));
    }

    @OnClick(R.id.task_list_add_button)
    void onAddTaskButtonClicked() {
        this.presenter.onAddTaskButtonClicked();
    }
}
