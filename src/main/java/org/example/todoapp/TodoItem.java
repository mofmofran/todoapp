package org.example.todoapp;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class TodoItem {
    private final String title;
    private final BooleanProperty done = new SimpleBooleanProperty();

    public TodoItem(String title,boolean done){
        this.title=title;
        this.done.set(done);
    }

    public String getTitle(){
        return title;
    }

    public boolean isDone(){
        return done.get();
    }
    //チェックボックスと「つなぐ」ためのプロパティ
    public BooleanProperty doneProperty(){
        return  done;
    }
    //一覧に表示される文字
    @Override
    public String toString(){
        return title;
    }
}
