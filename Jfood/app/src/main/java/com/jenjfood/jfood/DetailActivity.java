package com.jenjfood.jfood;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jenjfood.jfood.adapters.ImageAdapter;
import com.jenjfood.jfood.adapters.IngredientsAdapter;
import com.jenjfood.jfood.objects.Ingredient;
import com.jenjfood.jfood.objects.Recipe;
import com.jenjfood.jfood.utils.Utils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    List<Ingredient> items = new ArrayList<>();

    CollapsingToolbarLayout collapsing;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final String[] URLIMAGES = {"http://pbs.twimg.com/media/CeQMoSvUYAAp4Pt.jpg","http://pbs.twimg.com/media/CeQHajRVAAA8P17.jpg","http://pbs.twimg.com/media/CeQGK0rVAAAN8rH.jpg","http://pbs.twimg.com/media/CeQF4eLUYAAIxZk.jpg"};
    private ArrayList<String> ImagesUrlArray = new ArrayList<String>();
    private String RecipeImagesGallery[];

    FloatingActionButton fab;
    String RecipeName;
    Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadein);

        setContentView(R.layout.activity_detail);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Bundle bundle = this.getIntent().getExtras();

        RecipeName = bundle.getString("NAME");
        final String RecipeDate = bundle.getString("DATE");
        final String RecipeType = bundle.getString("TYPE");
        final String RecipeMainPicture = bundle.getString("MAIN_PICTURE");
        final String RecipePortions = bundle.getString("PORTIONS");
        final String RecipePTime = bundle.getString("P_TIME");
        final String RecipeSteps[] = bundle.getStringArray("STEPS");
        String RecipeStepsKey = "";
        final String RecipeCategory = bundle.getString("CATEGORY");
        final String Recipeingredients[] = bundle.getStringArray("INGREDIENTS");
        RecipeImagesGallery = bundle.getStringArray("IMAGES_GALLERY");

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final TextView name = (TextView) findViewById(R.id.name_recipe);
        assert name != null;
        name.setText(RecipeName);

        assert Recipeingredients != null;
        for (String s : Recipeingredients) {
            items.add(new Ingredient(s));
        }

        RecyclerView recycler = (RecyclerView) findViewById(R.id.ingredients);
        assert recycler != null;
        recycler.setHasFixedSize(true);

        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        IngredientsAdapter adapter = new IngredientsAdapter(items, this);
        recycler.setAdapter(adapter);

        //IMAGEN
        final ImageView MainPicture = (ImageView) findViewById(R.id.main_picture);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://jfood-d66a8.appspot.com/recipes/type/");

        String typeImage = RecipeType+"/"+RecipeMainPicture;

        storageRef.child(typeImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(DetailActivity.this)
                        .load(uri)
                        .centerCrop()
                        .crossFade()
                        .into(MainPicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Glide.with(DetailActivity.this)
                        .load(R.drawable.ic_action_file_cloud_off)
                        .centerCrop()
                        .crossFade()
                        .into(MainPicture);

            }
        });


        assert MainPicture != null;
        MainPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Glide.with(this)
                .load(RecipeMainPicture)
                .centerCrop()
                .crossFade()
                .into(MainPicture);

        RelativeLayout main_image_view = (RelativeLayout) findViewById(R.id.main_image_view);

        assert main_image_view != null;
        main_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DetailActivity.this,ViewImage.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(DetailActivity.this, MainPicture, MainPicture.getTransitionName());

                Bundle bundle =new Bundle();
                bundle.putString("URLIMAGE", RecipeMainPicture);
                intent.putExtras(bundle);
                startActivity(intent, options.toBundle());

            }
        });

        // CATEGORIA
        TextView Category = (TextView) findViewById(R.id.category);
        assert Category != null;
        switch (RecipeCategory){
            case "1":
                Category.setText("Platillo principal");
                break;
            case "2":
                Category.setText("Sopas");
                break;
            case "3":
                Category.setText("Salsas y Aderezos");
                break;
            case "4":
                Category.setText("Guarniciones");
                break;
            case "5":
                Category.setText("Cremas");
                break;
            case "6":
                Category.setText("Frutas");
                break;
            case "7":
                Category.setText("Pastas");
                break;
            case "8":
                Category.setText("Almuerzo");
                break;
            case "9":
                Category.setText("Ensalada");
                break;
            case "10":
                Category.setText("Desayuno");
                break;
            case "11":
                Category.setText("Pastel o Pay");
                break;
            case "12":
                Category.setText("Malteada/Smoothie");
                break;
            case "13":
                Category.setText("Jugo");
                break;
            case "14":
                Category.setText("Aperitivos y Entremeses");
                break;
            case "15":
                Category.setText("Arroces");
                break;
            case "16":
                Category.setText("Café");
                break;
            case "17":
                Category.setText("Huevos");
                break;
            case "18":
                Category.setText("Recetas para bebés y niños");
                break;
            case "19":
                Category.setText("Sushi");
                break;
            case "20":
                Category.setText("Tacos/Quesadillas");
                break;
            case "21":
                Category.setText("Mariscos");
                break;
            case "22":
                Category.setText("Coctelería");
                break;
            case "23":
                Category.setText("Helados");
                break;
            case "24":
                Category.setText("Pan/Panqués");
                break;
            case "25":
                Category.setText("Mexicana");
                break;
        }
        // FECHA
        TextView Dater = (TextView) findViewById(R.id.date);
        assert Dater != null;

        Dater.setText(DateUtils.getRelativeTimeSpanString(Integer.valueOf(RecipeDate)));

        // TIEMPO DE PREPARACIÓN
        TextView Ptime = (TextView) findViewById(R.id.Ptime);
        assert Ptime != null;
        Ptime.setText("Tiempo de preparación "+RecipePTime+" minutos");

        // PORCIONES
        TextView Portions = (TextView) findViewById(R.id.portions);
        assert Portions != null;
        Portions.setText("Para " +RecipePortions + " porciones");

        // PASOS O PROCEDIMIENTO
        TextView Steps = (TextView) findViewById(R.id.steps);

        int i = 1;
        for(String s : RecipeSteps){
            String stepN = "<h3>Paso " + i + ":</h3>";
            RecipeStepsKey = RecipeStepsKey + stepN + "<p>"  + Html.fromHtml(s) + "</p>";
            i++;
        }
        Steps.setText(Html.fromHtml(RecipeStepsKey));
        Steps.setMovementMethod(LinkMovementMethod.getInstance());

        if (RecipeImagesGallery.length < 0){
            init();
        }else{
            RelativeLayout galleryL = (RelativeLayout) findViewById(R.id.galleryL);
            assert galleryL != null;
            galleryL.setVisibility(View.GONE);
        }

        fab = (FloatingActionButton) findViewById(R.id.add_fav);
        assert fab != null;
        ArrayList<String> favoritesList;
        favoritesList = new ArrayList<>(Arrays.asList(Utils.loadArray("favorites", getApplicationContext())));
        String[] favorites = new String[favoritesList.size()];
        favorites = favoritesList.toArray(favorites);
        for(int item =0;item<favorites.length;item++){
            if(Objects.equals(RecipeName, favorites[item])){
                isFavorite = true;
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_action_favorite, getTheme()));
            }else{
                isFavorite = false;
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_action_favorite_outline, getTheme()));
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> favoritesList;
                favoritesList = new ArrayList<>(Arrays.asList(Utils.loadArray("favorites", getApplicationContext())));
                if(!isFavorite){
                    favoritesList.add(RecipeName);
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_action_favorite, getTheme()));
                    Toast.makeText(DetailActivity.this, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
                    isFavorite = true;
                }else{
                    for(int item =0;item<favoritesList.size();item++){
                        if(Objects.equals(RecipeName, favoritesList.get(item))){
                            favoritesList.remove(item);
                            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_action_favorite_outline, getTheme()));
                            Toast.makeText(DetailActivity.this, "Borrado de favoritos", Toast.LENGTH_SHORT).show();
                        }
                    }
                    isFavorite = false;
                }
                String[] favorites = new String[favoritesList.size()];
                favorites = favoritesList.toArray(favorites);
                Utils.saveArray(favorites,"favorites",getApplicationContext());
            }
        });

        RelativeLayout Container = (RelativeLayout) findViewById(R.id.container);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        supportFinishAfterTransition();
    }

    private void init() {

        /*  ADD STATIC RESOURCE DRAWABLE
        for(int i=0;i<IMAGES.length;i++){
            ImagesArray.add(IMAGES[i]);
        }*/

        for(int i=0;i<RecipeImagesGallery.length;i++){
            ImagesUrlArray.add(RecipeImagesGallery[i]);
        }

        mPager = (ViewPager) findViewById(R.id.pager);

        mPager.setAdapter(new ImageAdapter(DetailActivity.this,ImagesUrlArray));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);

        NUM_PAGES = URLIMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_download) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.add_fav){
            ArrayList<String> favoritesList;
            favoritesList = new ArrayList<>(Arrays.asList(Utils.loadArray("favorites", getApplicationContext())));
            favoritesList.add(RecipeName);
            String[] favorites = new String[favoritesList.size()];
            favorites = favoritesList.toArray(favorites);
            Utils.saveArray(favorites,"favorites",getApplicationContext());
            fab.setVisibility(View.GONE);
            Toast.makeText(DetailActivity.this, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
        }
    }
}
