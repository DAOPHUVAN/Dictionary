package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dictionary.Adaptes.MeaningAdapter;
import com.example.dictionary.Adaptes.PhoneticAdapter;
import com.example.dictionary.Models.APIResponse;
import com.example.dictionary.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ProgressDialog progressDialog;
    PhoneticAdapter phoneticAdapter;
    MeaningAdapter meaningAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(this);

        /*progressDialog.setTitle("Loading...");
        progressDialog.show();
        RequestManager manager = new RequestManager(MainActivity.this);
        manager.getWorkMeaning(listener, "hello");*/

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog.setTitle("Fetching response for " + query);
                progressDialog.show();
                RequestManager manager = new RequestManager(MainActivity.this);
                manager.getWorkMeaning(listener, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private final OnFetchDataListener listener = new OnFetchDataListener() {
        @Override
        public void onFetchData(APIResponse apiResponse, String message) {
            progressDialog.dismiss();
            if (apiResponse == null){
                Toast.makeText(MainActivity.this, "No data Found!!", Toast.LENGTH_SHORT).show();
                return;
            }
            showData(apiResponse);
        }

        @Override
        public void onError(String message) {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private void showData(APIResponse apiResponse) {
        binding.textViewWord.setText("Word: " + apiResponse.getWord());
        binding.recyclerPhonetics.setHasFixedSize(true);
        binding.recyclerPhonetics.setLayoutManager(new GridLayoutManager(this,1));
        phoneticAdapter = new PhoneticAdapter(this,apiResponse.getPhonetics());
        binding.recyclerPhonetics.setAdapter(phoneticAdapter);

        binding.recyclerMeanings.setHasFixedSize(true);
        binding.recyclerMeanings.setLayoutManager(new GridLayoutManager(this,1));
        meaningAdapter = new MeaningAdapter(this, apiResponse.getMeanings());
        binding.recyclerMeanings.setAdapter(meaningAdapter);
    }
}