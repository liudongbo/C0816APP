package com.xt.mijkplayer.ui.dashboard;

import static com.xt.mijkplayer.util.GlobalDefine.CODEC_TYPE;
import static com.xt.mijkplayer.util.GlobalDefine.NET4G_TYPE;
import static com.xt.mijkplayer.util.GlobalDefine.NET_CHANG_TYPE;
import static com.xt.mijkplayer.util.GlobalDefine.PRIORITY_NETWORK_TYPE;
import static com.xt.mijkplayer.util.GlobalDefine.RESOLUTION_TYPE;
import static com.xt.mijkplayer.util.GlobalDefine.WIFI_MODE_TYPE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.isapanah.awesomespinner.AwesomeSpinner;
import com.xt.mijkplayer.R;
import com.xt.mijkplayer.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment{
    private FragmentDashboardBinding binding;
    public String gCodecType = "h264";
    public String gResolution = "1920x1080";
    public String gWidth = "1920";
    public String gHeight = "1080";
    public String gPriorityNetwork = "wifi";
    public String gNet4g = "800MHz";
    public String gNetChange = "wifi";

    public String gWifiSSID = "ssid";
    public String gWifiPassword = "123456";
    public String gWifiMode = "AP";
    public String gServerID = "31000000002000000001";
    public String gServerIP = "101.200.134.6";
    public String gServerPort = "5061";
    public String gDeviceID = "31000000001310000199";
    public String gPassword = "123456";


    private EditText gWifiSSIDEditText;
    private EditText gWifiPasswordEditText;
    private EditText gWifiModeEditText;
    private EditText gServerIDEditText;
    private EditText gServerIPEditText;
    private EditText gServerPortEditText;
    private EditText gDeviceIDEditText;
    private EditText gPasswordEditText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        binding.applySettingButton.setOnClickListener(view -> {
            gWifiSSID = gWifiSSIDEditText.getText().toString();
            gWifiPassword = gWifiPasswordEditText.getText().toString();
            gServerID = gServerIDEditText.getText().toString();
            gServerIP = gServerIPEditText.getText().toString();
            gServerPort = gServerPortEditText.getText().toString();
            gDeviceID = gDeviceIDEditText.getText().toString();
            gPassword = gPasswordEditText.getText().toString();

            String buffer = String.format("%s:%s:%s:%s:%s:%s:%s:%s:%s:%s:%s:%s:",
                    gCodecType,
                    gResolution,
                    gNetChange,
                    gNet4g,
                    gWifiMode,
                    gWifiSSID,
                    gWifiPassword,
                    gServerID,
                    gServerIP,
                    gServerPort,
                    gDeviceID,
                    gPassword);
            Toast.makeText(getActivity(), buffer, Toast.LENGTH_SHORT).show();
            //                new MainActivity.udpBroadCast(buffer).start();
        });
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        gWifiSSIDEditText = view.findViewById(R.id.ssid);
        gWifiPasswordEditText = view.findViewById(R.id.wifiPassword);
        gServerIDEditText = view.findViewById(R.id.serverID);
        gServerIPEditText = view.findViewById(R.id.serverIP);
        gServerPortEditText = view.findViewById(R.id.serverPort);
        gDeviceIDEditText = view.findViewById(R.id.deviceID);
        gPasswordEditText = view.findViewById(R.id.password);

        initOneSpinner(view, R.id.codecTypeSpinner, R.array.codecTypeArray, CODEC_TYPE);
        initOneSpinner(view, R.id.resolutionSpinner, R.array.resolutionArray, RESOLUTION_TYPE);
        initOneSpinner(view, R.id.priorityNetworkSpinner, R.array.priorityNetworkArray, PRIORITY_NETWORK_TYPE);
        initOneSpinner(view, R.id.net4gSpinner, R.array.net4GArray, NET4G_TYPE);
        initOneSpinner(view, R.id.netChangeSpinner, R.array.netChangeArray, NET_CHANG_TYPE);
        initOneSpinner(view, R.id.wifiModeSpinner, R.array.wifiModeArray, WIFI_MODE_TYPE);
    }

    private void initOneSpinner(View view, int id, int array, String tag){
        // init resolution type spinner ui
        AwesomeSpinner spinner = view.findViewById(id);
        ArrayAdapter<CharSequence> provincesAdapter = ArrayAdapter.createFromResource(getActivity(),
                array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(provincesAdapter, 0);
        spinner.setTag(tag);
        spinner.setOnSpinnerItemClickListener((position, itemAtPosition) -> {
            //TODO YOUR ACTIONS
            Log.i("TAG", "onItemSelected " +  spinner.getTag() + ": " + itemAtPosition);
            updatePara(position, itemAtPosition, tag);
        });
        spinner.setSelection(0);
    }

    private void updatePara(int pos, String item, String tag){
        switch (tag){
            case CODEC_TYPE:
                gCodecType = item;
                break;
            case RESOLUTION_TYPE:
                gResolution = item;
                break;
            case PRIORITY_NETWORK_TYPE:
                gPriorityNetwork = item;
                break;
            case NET4G_TYPE:
                gNet4g = item;
                break;
            case NET_CHANG_TYPE:
                gNetChange = item;
                break;
            case WIFI_MODE_TYPE:
                gWifiMode = item;
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}