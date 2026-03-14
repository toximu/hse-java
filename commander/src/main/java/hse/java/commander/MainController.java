package hse.java.commander;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MainController {

    private File cur_file_left_;
    private File cur_file_right_;

    private void update_left_() {
        left.getItems().clear();
        for (var item : cur_file_left_.listFiles()) {
            String prefix;
            if (item.isDirectory()) {
                prefix = "(D)";
            } else {
                prefix = "(F)";
            }
            left.getItems().add(prefix + " " + item.getName());
        }
    }

    private void update_right_() {
        right.getItems().clear();
        for (var item : cur_file_right_.listFiles()) {
            String prefix;
            if (item.isDirectory()) {
                prefix = "(D)";
            } else {
                prefix = "(F)";
            }
            right.getItems().add(prefix + " " + item.getName());
        }
    }

    @FXML
    public Button move;

    public void initialize() {
        move.setOnMouseClicked(event -> {
            if (left.getSelectionModel().getSelectedItem() != null) {
                var filename = left.getSelectionModel().getSelectedItem().substring(4);
                var from = cur_file_left_.toPath().resolve(filename);
                var to = cur_file_right_.toPath().resolve(filename);
                try {
                    Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
                }
                catch (IOException e) {
                    e.printStackTrace();
                };
                update_right_();
            }
        });
//        System.out.println(System.getProperty("user.home"));
//       left.getItems().add("Kek");

        cur_file_left_ = new File(System.getProperty("user.home"));
        cur_file_right_ = new File(System.getProperty("user.home"));


        update_left_();
        update_right_();


       left.setOnMouseClicked(event -> {
           if (event.getClickCount() == 2) {
               int index = left.getSelectionModel().getSelectedIndex();
               var text = left.getSelectionModel().getSelectedItem();
               var newPath =  cur_file_left_.toPath().resolve(text.substring(4));
               if (newPath.toFile().isDirectory()) {
                   cur_file_left_ = newPath.toFile();
                   update_left_();
               }
           }
       });

       left.setOnKeyPressed(keyEvent -> {
           if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
               cur_file_left_ = cur_file_left_.toPath().getParent().toFile();
               update_left_();
           }
       });

        right.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int index = right.getSelectionModel().getSelectedIndex();
                var text = right.getSelectionModel().getSelectedItem();
                var newPath =  cur_file_right_.toPath().resolve(text.substring(4));
                if (newPath.toFile().isDirectory()) {
                    cur_file_right_ = newPath.toFile();
                    update_right_();
                }
            }
        });

        right.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                cur_file_right_ = cur_file_right_.toPath().getParent().toFile();
                update_right_();
            }
        });
   }

    @FXML
    public ListView<String> left;

    @FXML
    public ListView<String> right;


}
