package com.example.asterismos;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ConstellationEncyclopedia extends AppCompatActivity {

    private ConstellationRepository repository;

    private LinearLayout buttonsContainer;
    private String[] constellationNames;
    private int[] imagesConstellation;
    //Переменная для выбора формата, по которому создаётся ряд.
    private int orientationScreen;
    //Переменная для сохранения введённого текста поиска.
    private String currentSearchQuery = "";

    private final int IsFromTest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TEST", "Энциклопедия");
        setContentView(R.layout.activity_encyclopedia);

        // Определяем текущую ориентацию и ставим значение.
        orientationScreen = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;

        repository = new ConstellationRepository(this);

        //Создаётся вся та канитель с рядами кнопок для перехода на каточки созвездий.
        ButtonsMaker();
        //Работа поисковой строки
        SearchBarMaker();
    }

    private void ButtonsMaker(){
        buttonsContainer = findViewById(R.id.buttons_container);
        buttonsContainer.removeAllViews();
        buttonsContainer.setOrientation(LinearLayout.VERTICAL);

        //Для наименований.
        constellationNames = repository.getNames();

        //Для картинок.
        imagesConstellation = repository.getConstellationImages();

        //Для рядов.
        LinearLayout currentRow = null;
        int cardIndex = 0;

        //Создаёт кнопку проходя по массиву наименований.
        for (int i = 0; i < constellationNames.length; i++) {

            //А раньше был метод отдельный для обновления карточек в связи с поиском.
            if (!currentSearchQuery.isEmpty()) {
                // Я не могу, я не могу, проблема оказалась в моём устройстве.
                String NameReplacement = constellationNames[i].toLowerCase();
                // Тупая замена всратой ё на ё нормальную. Помните, дети, о диакритических знаках
                String SearchReplacement = currentSearchQuery.toLowerCase().replace("ë","ё");
                if (!NameReplacement.contains(SearchReplacement)) {
                    //Принципиально пропускает карточку и не вкидывает её в создание ряда.
                    continue;
                }
            }

            //Чек на оринтацию, и поимка ошибок с невозможностью создать ряд.
            if (currentRow == null || cardIndex % orientationScreen == 0) {
                currentRow = RowMaker();
            }

            LinearLayout cardLayout = ContainerMaker(i);
            currentRow.addView(cardLayout);
            cardIndex++;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Обновляем количество карточек в ряду.
        orientationScreen = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;

        ButtonsMaker();

        setupSearchViewSize();

        SearchView searchView = findViewById(R.id.searchView);
        if (!currentSearchQuery.isEmpty()) {
            searchView.setQuery(currentSearchQuery, false);
        }
    }

    private LinearLayout RowMaker(){
        LinearLayout currentRow = new LinearLayout(this);
        currentRow.setOrientation(LinearLayout.HORIZONTAL);
        currentRow.setGravity(Gravity.CENTER);
        currentRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        currentRow.setPadding(8, 20, 8, 4);
        buttonsContainer.addView(currentRow);
        return currentRow;
    }

    private LinearLayout ContainerMaker(int i){
        LinearLayout cardLayout = new LinearLayout(this);
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setGravity(Gravity.CENTER);
        cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        cardLayout.setPadding(20, 8, 20, 8);

        //Прописывание картинки.
        View constellationImage = PictureMaker(i);

        //Прописывание названия.
        View constellationName = NameMaker(i);

        //Кидаем картинку и название в контейнер.
        cardLayout.addView(constellationImage);
        cardLayout.addView(constellationName);

        final int constellationId = i;
        //По индексу фиксирует передачу шаблончика карточек кнопке.
        //Блин, надеюсь я пойму, что это значило.
        cardLayout.setOnClickListener(v -> {
            Intent intent = new Intent(ConstellationEncyclopedia.this, ConstellationDetailActivity.class);
            intent.putExtra("where_point", IsFromTest);
            intent.putExtra("constellation_id", constellationId);
            startActivity(intent);
        });

        return cardLayout;
    }

    private View PictureMaker(int i){
        int newWidth = 400;
        int newHeight = 400;
        ImageView constellationImage = new ImageView(this);
        constellationImage.setImageResource(imagesConstellation[i]);
        constellationImage.setBackgroundResource(R.drawable.paper);
        constellationImage.setPadding(10, 10, 10, 10);
        //Для того, чтоб картика не обрезалась.
        constellationImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        constellationImage.setLayoutParams(new LinearLayout.LayoutParams(
                newWidth,
                newHeight
        ));
        return constellationImage;
    }

    private View NameMaker(int i){
        TextView constellationName = new TextView(this);
        constellationName.setText(constellationNames[i]);
        constellationName.setTextSize(18);
        constellationName.setBackgroundResource(R.drawable.paper);
        constellationName.setGravity(Gravity.CENTER);
        constellationName.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                200
        ));
        constellationName.setPadding(0, 8, 0, 0);
        return constellationName;
    }

    //Метод для поисковой строки. Раньше их было два, но ButtonMaker съел один.
    private void SearchBarMaker(){
        SearchView searchView = findViewById(R.id.searchView);

        //Чек на наличие чего-либо в поисковой строке после переворота экрана.
        if (!currentSearchQuery.isEmpty()) {
            searchView.setQuery(currentSearchQuery, false);
        }

        //Моя дорогая - прекрасная женщина. Своим примером пользования показала мне, где нужно исправить штуку
        //Метод ниже переписывает считку клика на всю строку поиска, а не только на знак лупы
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Нахрен не нужно. Мы всё равно всё обрабатываем в методе ниже по ходу написания текста.
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText;
                //Пересоздаются кнопочки исходя из работы поиска
                ButtonsMaker();
                return true;
            }
        });
    }

    //Существует чисто для того, чтобы установить отступы в вертикальной и горизонтальной ориентациях
    private void setupSearchViewSize() {
        View searchView = findViewById(R.id.searchView);

        int heightInDp;
        int marginHorizontal;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            heightInDp = 36;
            marginHorizontal = 120;
        } else {
            heightInDp = 48;
            marginHorizontal = 40;
        }

        int heightInPx = (int) (heightInDp * getResources().getDisplayMetrics().density);
        int marginInPx = (int) (marginHorizontal * getResources().getDisplayMetrics().density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                heightInPx
        );
        params.setMargins(marginInPx, 16, marginInPx, 16);
        searchView.setLayoutParams(params);
    }

}