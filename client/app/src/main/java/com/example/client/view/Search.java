package com.example.client.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.client.adapters.RecAdapterHomePage;
import com.example.client.adapters.RecAdapterSearchList;
import com.example.client.databinding.FragmentSearchBinding;
import com.example.client.viewmodel.HomePageViewModel;
import com.example.client.viewmodel.SearchListViewModel;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {

    private FragmentSearchBinding searchBinding;
    private SearchListViewModel searchListViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        searchBinding = FragmentSearchBinding.inflate(getLayoutInflater());

        searchListViewModel = new ViewModelProvider(Search.this).get(SearchListViewModel.class);

        searchListViewModel.getSearchResult("");
        RecAdapterSearchList adapter = new RecAdapterSearchList(new ArrayList<>(), ((MainActivity) getActivity()));
        searchBinding.list.setAdapter(adapter);
        searchListViewModel.getUsersList().observe(getViewLifecycleOwner(), s -> {
            Log.d("logdev", s.toString());
            adapter.setList(s);
            adapter.notifyDataSetChanged();
        });

        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search( charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}


        };

        searchBinding.search.addTextChangedListener(textWatcher);


        return searchBinding.getRoot();
    }
    private void search(String q){
        searchListViewModel.getSearchResult(q);
    }

}