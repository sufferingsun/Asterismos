package com.example.asterismos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ConstellationDetailActivity extends AppCompatActivity {

    private ConstellationRepository repository;

    public int getGoal = 0;
    public int maxGetGoal = 20;
    public int constellation_group_id;
    private boolean isFinalTest = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TEST", "Карточка");

        repository = new ConstellationRepository(this);

        int from_where_we_get = getIntent().getIntExtra("where_point", 0);
        int constellationId = getIntent().getIntExtra("constellation_id", -1);
        constellation_group_id = getIntent().getIntExtra("constellation_group_id", -1);

        if (from_where_we_get == 0){
            setContentView(R.layout.activity_constellation_detail);
            if (constellationId != -1) {
                loadConstellationDataForEncyclopedia(constellationId);
            }
        } else if (from_where_we_get == 1){
            setContentView(R.layout.lessons_detail);
            loadConstellationDataForTestMenu(constellationId);
            repository.setConstellationLearned(constellationId);
        } else if (from_where_we_get == 2){
            setContentView(R.layout.test_doing_eeey);
            // перекинутые группы созвездий
            int[] groupArray = getIntent().getIntArrayExtra("group_array_list");
            isFinalTest = getIntent().getBooleanExtra("is_final_test", false);
            loadConstellationDataForTest(constellationId, groupArray);
        }
    }

    private void loadConstellationDataForEncyclopedia(int id) {
        TextView nameView = findViewById(R.id.name_constellation);
        TextView commonInfoView = findViewById(R.id.common_information);
        TextView storyView = findViewById(R.id.story_constellation);
        ImageView imageConstellationView = findViewById(R.id.constellation_image);
        ImageView imageSymbolView = findViewById(R.id.symbol_image);

        nameView.setText(repository.getNames()[id]);
        commonInfoView.setText(repository.getCommonInfo()[id]);
        storyView.setText(repository.getStory()[id]);
        imageConstellationView.setImageResource(repository.getConstellationImages()[id]);
        imageSymbolView.setImageResource(repository.getSymbolImages()[id]);
    }

    private void loadConstellationDataForTestMenu(int id) {
        TextView nameView = findViewById(R.id.name_constellation);
        ImageView imageConstellationView = findViewById(R.id.constellation_image);
        ImageView imageSymbolView = findViewById(R.id.symbol_image);

        nameView.setText(repository.getNames()[id]);
        imageConstellationView.setImageResource(repository.getConstellationImages()[id]);
        imageSymbolView.setImageResource(repository.getSymbolImages()[id]);
    }

    private void loadConstellationDataForTest(int id, int[] groupArray) {
        // счётчик правильных ответов
        ImageView imageConstellationView = findViewById(R.id.constellation_image);
        imageConstellationView.setImageResource(repository.getConstellationImages()[id]);

        Button firstChoise = findViewById(R.id.first_choise);
        Button secondChoise = findViewById(R.id.second_choise);
        Button thirdChoise = findViewById(R.id.third_choise);
        Button fourthChoise = findViewById(R.id.fourth_choise);

        List<Button> buttons = Arrays.asList(firstChoise, secondChoise, thirdChoise, fourthChoise);

        String correctName = repository.getNames()[id];
        List<Integer> randomIndices = getRandomDistinctIndices(id, 3, groupArray);

        List<String> options = new ArrayList<>();
        options.add(correctName);
        for (int idx : randomIndices) {
            options.add(repository.getNames()[idx]);
        }

        // смешной рандом, который случайно берет наши варианты ответов и мешает их
        Collections.shuffle(options);

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setText(options.get(i));
        }

        for (Button button : buttons){
            button.setOnClickListener(v -> {
                if (button.getText().equals(correctName)){
                    // Ай какая молодца, теперь дальше
                    getGoal++;
                    if (getGoal!=maxGetGoal){
                        moveToNextTest(groupArray);
                    } else{
                        // сбитие счётчика
                        getGoal = 0;
                        // статус группы меняется на пройденный
                        if (isFinalTest) {
                            repository.setFinalTestPassed(true);
                        } else {
                            repository.setGroupTestPassed(constellation_group_id);
                        }
                        finish();
                    }
                } else {
                    // Пользователь идёт в пень со своими ошибками и возвращается на экран блока
                    getGoal = 0;
                    setContentView(R.layout.lessons_detail);
                    loadConstellationDataForTestMenu(id);
                }
            });
        }

    }

    // Рандомим остальные индексы, которые мы не выбрали как правильные
    private List<Integer> getRandomDistinctIndices(int excludeId, int count, int[] groupArray) {
        List<Integer> needIndices = new ArrayList<>();
        for (int i : groupArray) {
            if (i != excludeId) {
                needIndices.add(i);
            }
        }
        // смешной рандом, который выбирает случайные значения неправильных ответов из группы
        Collections.shuffle(needIndices);
        return needIndices.subList(0, count);
    }

    // Обработка верного ответа
    private void moveToNextTest(int[] groupArray) {
        Random random = new Random();
        int randomIndex = random.nextInt(groupArray.length);
        int realIndex = groupArray[randomIndex];

        // Создание нового теста
        loadConstellationDataForTest(realIndex, groupArray);
    }
}