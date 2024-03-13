package com.xt.mijkplayer.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.xt.mijkplayer.databinding.FragmentHomeBinding;
import com.xt.mijkplayer.ui.video.VideoActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.rtspStartBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), VideoActivity.class);
            startActivity(intent);
        });
        binding.resetButton.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "reset", Toast.LENGTH_SHORT).show();
//            new MainActivity.udpBroadCast("reset").start();
        });
        binding.formatButton.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "format", Toast.LENGTH_SHORT).show();
//            new MainActivity.udpBroadCast("reset").start();
        });
        binding.rebootButton.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "reboot", Toast.LENGTH_SHORT).show();
//            new MainActivity.udpBroadCast("reset").start();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}