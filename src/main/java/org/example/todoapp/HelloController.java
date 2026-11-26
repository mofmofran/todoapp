package org.example.todoapp;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HelloController {
    @FXML private TextField inputField;
    @FXML private ListView<TodoItem> todoList;
    @FXML private Label countLabel;

    //一覧のデータ。チェックのOn/Offでも「変更あり」と分かるようにしておく
    private final ObservableList<TodoItem> items=
            FXCollections.observableArrayList(item ->new Observable[]{item.doneProperty()});

    //保存先:C:\Users\(ユーザー名)\todo.txt
    private static final Path SAVE_FILE = Path.of(System.getProperty("user.home"),"todo.txt");

    //画面ができた直後に1回だけ呼ばれる(C#のForm_Load)
    @FXML
    public  void initialize(){
        todoList.setItems(items);
        //各行をチェックボックス付きで表示しTodoItemのdoneとつなぐ
        todoList.setCellFactory(CheckBoxListCell.forListView(TodoItem::doneProperty));

        load(); //段階４:起動時に読み込む
        updateCount();

        //一覧が変わるたびに、件数の表示と保存をする
        items.addListener((ListChangeListener<TodoItem>)change->{
            updateCount();
            save(); //段階４
        } );
    }
    //追加ボタン/入力欄でEnter
    @FXML
    private void onAdd(){
        String text =inputField.getText().trim();
        if(text.isEmpty()){
            return;
        }
        items.add(new TodoItem(text,false));
        inputField.clear();
        inputField.requestFocus();
    }

    //選択を削除
    @FXML
    private void onDelete(){
        TodoItem selected = todoList.getSelectionModel().getSelectedItem();
        if(selected !=null){
            items.remove(selected);
        }
    }

    //完了を片付ける
    @FXML
    private void onClearDone(){
        items.removeIf(TodoItem::isDone);
    }

    //「残り○件/全〇件」///stream API (C#のLINQ)
    private void updateCount(){
        long remaining = items.stream()
                .filter(item-> !item.isDone())
                .count();
        countLabel.setText("残り"+remaining+"件/全"+items.size()+"件");
    }

    //=====段階４:ファイルに保存・読み込み=====

    //１行=「完了なら1、未完了なら0」 ＋タブ＋タイトル
    private void save(){
        List<String>lines =items.stream()
                .map(item -> (item.isDone() ? "1":"0")+"\t" + item.getTitle())
                .toList();
        try {
            Files.write(SAVE_FILE,lines,StandardCharsets.UTF_8);
        }catch (IOException e){
            showError("保存に失敗しました:"+e.getMessage());
        }
    }

    private  void load(){
        if(!Files.exists(SAVE_FILE)){
            return;
        }
        try {
            List<TodoItem>loaded = Files.readAllLines(SAVE_FILE,StandardCharsets.UTF_8).stream()
                    .filter(line->line.contains("\t"))
                    .map(line->{
                        String[] parts = line.split("\t",2);
                        return  new TodoItem(parts[1],parts[0].equals("1"));
                    })
                    .toList();
            items.setAll(loaded);
        }catch (IOException e){
            showError("読み込みに失敗しました:"+e.getMessage());
        }
    }

    private  void showError(String message){
        new Alert(Alert.AlertType.ERROR,message).show();
    }
}
