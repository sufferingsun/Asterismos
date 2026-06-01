package com.example.asterismos;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Tests extends AppCompatActivity {
    private ConstellationRepository repository;
    // Списочек наименований
    private String[] constellationNames;
    // Чек на конкретику экрана. Список тестов или список созвездий для изучения
    private boolean isShowingTestButtons = true;
    // Сохраняем контейнер
    private LinearLayout containerForTest;
    //Сохраняем текущую тестовую группу
    private List<Integer> currentTestGroup;
    private int currentGroupId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_first_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        repository = new ConstellationRepository(this);

        containerForTest = findViewById(R.id.buttons_container);
        constellationNames = getResources().getStringArray(R.array.constellation_names);

        showTestButtons();

        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // Обработка системной кнопки назад. Я НЕНАВИЖУ эту штуку
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (!isShowingTestButtons) {
                // если мы на созвездиях, возвращаемся на тесты
                showTestButtons();
            } else {
                // если на тестах, то выходим на главный экран
                finish();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        // Существует для обновления кнопок созвездий без отката на меню тестов
        if (!isShowingTestButtons) {
            if (currentTestGroup != null) {
                showConstellationButtons(currentTestGroup, containerForTest, currentGroupId);
            }
        }else {
            showTestButtons();
        }
    }

    private void showTestButtons(){
        isShowingTestButtons = true;
        constellationNames = getResources().getStringArray(R.array.constellation_names);
        // Cписок (изменяемый!)
        List<Integer> testGroup;
        containerForTest.removeAllViews();
        // счётчик для итогового теста
        int countLearnGroups = 0;

        for (int j = 0; j < 8; j++){
            Button TestButton = new Button(this);

            final int constellationGroupId = j;

            if (repository.isGroupTestPassed(constellationGroupId)) {
                countLearnGroups++;
            }

            if (j == 0){
                TestButton.setText("Мифы");
                testGroup = Arrays.asList(0, 6, 8, 9, 11, 12, 17, 18, 28, 50, 53, 54, 75, 76, 81, 82);
            } else if (j==1){
                TestButton.setText("Зодиакальные созвездия");
                testGroup = Arrays.asList(1, 4, 5, 15, 31, 35, 47, 57, 59, 64, 68, 70);
            } else if (j==2){
                TestButton.setText("Водные созвездия");
                testGroup = Arrays.asList(16, 26, 30, 36, 84);
            } else if (j==3){
                TestButton.setText("Птицы");
                testGroup = Arrays.asList(10, 13, 22, 34, 49, 51, 56, 72, 73);
            } else if (j==4){
                TestButton.setText("Животные");
                testGroup = Arrays.asList(2, 3, 7, 14, 21, 23, 25, 38, 39, 40, 41, 42, 44, 60, 74, 87);
            } else if (j==5){
                TestButton.setText("Инструменты и техника");
                testGroup = Arrays.asList(19, 37, 43, 45, 46, 48, 55, 58, 61, 62, 63, 66, 67, 69, 71, 77, 78, 79, 80, 83, 85, 86);
            } else if (j==6){
                TestButton.setText("Корабль Арго");
                testGroup = Arrays.asList(29, 32, 33, 52);
            } else {
                TestButton.setText("Люди");
                testGroup = Arrays.asList(20, 24, 27, 65);
            }

            ButtonMaker(TestButton, -1);
            if (repository.isGroupTestPassed(constellationGroupId)) {
                TestButton.setBackgroundColor(getResources().getColor(R.color.dark_gray_for_constellations, null));
                TestButton.setTextColor(getResources().getColor(R.color.light_text_color, null));
            }

            // Создаём кнопки на занятия, удаляя всё, что было до этого
            List<Integer> finalTestGroup = testGroup;
            TestButton.setOnClickListener(v -> {
                showConstellationButtons(finalTestGroup, containerForTest, constellationGroupId);
            });

            if (UserAdministration.getInstance().isAdmin()){
                // При долгом нажатии закрываем группу
                TestButton.setOnLongClickListener(v -> {
                    // Вкусная, вкусная архитектура!
                    repository.setGroupTestPassed(constellationGroupId);
                    onResume();
                    return true;
                });
            }

            containerForTest.addView(TestButton);
        }

        // Создание кнопки итогового теста
        Button GroupTestButtonLAST = new Button(this);

        int[] arrayAll = new int[88];
        for (int i = 0; i < arrayAll.length; i++) {
            arrayAll[i] = i;
        }

        if (countLearnGroups == 8){
            GroupTestButtonLAST.setText("Итоговый тест!");
            ButtonMaker(GroupTestButtonLAST, -1);

            // Проверка итогового теста
            if (repository.isFinalTestPassed()){
                GroupTestButtonLAST.setBackgroundColor(getResources().getColor(R.color.dark_gray_for_constellations, null));
                GroupTestButtonLAST.setTextColor(getResources().getColor(R.color.light_text_color, null));
            }

            containerForTest.addView(GroupTestButtonLAST);

            GroupTestButtonLAST.setOnClickListener(v -> {
                Random random = new Random();
                int randomIndex = random.nextInt(arrayAll.length);
                int realIndex = arrayAll[randomIndex];
                Intent intent = new Intent(Tests.this, ConstellationDetailActivity.class);
                intent.putExtra("where_point", 2);
                intent.putExtra("group_array_list", arrayAll);
                intent.putExtra("constellation_id", realIndex);
                intent.putExtra("is_final_test", true);
                startActivity(intent);
            });

            if (UserAdministration.getInstance().isAdmin()){
                // При долгом нажатии закрываем финальный тест
                GroupTestButtonLAST.setOnLongClickListener(v -> {
                    // Вкусная, вкусная архитектура!
                    repository.setFinalTestPassed(true);
                    onResume();
                    return true;
                });
            }
        }
    }

    private void showConstellationButtons(List<Integer> finalTestGroup, LinearLayout containerForTest, int constellationGroupId){
        this.currentTestGroup = finalTestGroup;
        this.currentGroupId = constellationGroupId;
        containerForTest.removeAllViews();
        isShowingTestButtons = false;
        // подсчёт количества изученных карточек
        int countLearnConst = 0;
        // перевод списка в удобоваримый формат
        int[] groupArray = finalTestGroup.stream().mapToInt(Integer::intValue).toArray();
        for (int i = 0; i < finalTestGroup.size(); i++) {
            // Создание кнопок созвездий
            Button LessonButton = new Button(this);
            int realIndex = finalTestGroup.get(i);
            LessonButton.setText(constellationNames[realIndex]);
            ButtonMaker(LessonButton, realIndex);
            containerForTest.addView(LessonButton);

            if (repository.isConstellationLearned(realIndex)) {
                countLearnConst++;
            }

            LessonButton.setOnClickListener(v -> {
                Intent intent = new Intent(Tests.this, ConstellationDetailActivity.class);
                // Передаваемая переменная 1 - это обозначение какой экран будет создан.
                // В данном случае это экран изучения созвездия
                intent.putExtra("where_point", 1);
                // Передаём именно индекс карточки, а не текущий индекс
                intent.putExtra("constellation_id", realIndex);
                // Передаём индекс текущей группы
                startActivity(intent);
            });

            if (UserAdministration.getInstance().isAdmin()){
                // При долгом нажатии закрываем занятие
                int finalI = i;
                LessonButton.setOnLongClickListener(v -> {
                    // Вкусная, вкусная архитектура!
                    repository.setConstellationLearned(finalTestGroup.get(finalI));
                    onResume();
                    return true;
                });
            }
        }


        // Создание кнопки теста блока
        Button GroupTestButton = new Button(this);
        if (countLearnConst == finalTestGroup.size()){
            GroupTestButton.setText("Тест по блоку!");
            ButtonMaker(GroupTestButton, -1);
            containerForTest.addView(GroupTestButton);
            if (repository.isGroupTestPassed(constellationGroupId)){
                GroupTestButton.setBackgroundColor(getResources().getColor(R.color.dark_gray_for_constellations, null));
                GroupTestButton.setTextColor(getResources().getColor(R.color.light_text_color, null));
            }

            // Эта хреноборина была во внешней части, поэтому работала постоянно и ломала мне логику
            // построения экранчиков. Гххххх
            GroupTestButton.setOnClickListener(v -> {
                Random random = new Random();
                int randomIndex = random.nextInt(groupArray.length);
                int realIndex = groupArray[randomIndex];
                Intent intent = new Intent(Tests.this, ConstellationDetailActivity.class);
                intent.putExtra("where_point", 2);
                intent.putExtra("group_array_list", groupArray);
                intent.putExtra("constellation_id", realIndex);
                intent.putExtra("constellation_group_id", constellationGroupId);
                startActivity(intent);
            });

            if (UserAdministration.getInstance().isAdmin()){
                // При долгом нажатии закрываем группу
                GroupTestButton.setOnLongClickListener(v -> {
                    // Вкусная, вкусная архитектура!
                    repository.setGroupTestPassed(constellationGroupId);
                    onResume();
                    return true;
                });
            }
        }
    }

    private void ButtonMaker(Button TestButton, int realIndex){
        if (!repository.isConstellationLearned(realIndex)) {
            // Если мы НЕ смотрели на эту карточку
            TestButton.setBackgroundResource(R.drawable.paper);
            TestButton.setTextColor(getResources().getColor(R.color.dark_gray_for_constellations, null));
        } else{
            // Если мы смотрели на эту карточку
            TestButton.setBackgroundColor(getResources().getColor(R.color.dark_gray_for_constellations, null));
            TestButton.setTextColor(getResources().getColor(R.color.light_text_color, null));
        }
        TestButton.setTextSize(20);

        // Параметры отображения (Layout params)
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                200
        );
        params.setMargins(60, 0, 60, 40);
        TestButton.setLayoutParams(params);
    }

}
