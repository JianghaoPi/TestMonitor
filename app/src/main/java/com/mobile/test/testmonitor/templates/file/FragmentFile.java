package com.mobile.test.testmonitor.templates.file;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.test.testmonitor.R;

import java.io.File;

/**
 * Created by Administrator on 2017/8/16.
 */

public class FragmentFile extends Fragment {
    private LinearLayout fileShow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showFiles();
    }

    @Override
    public void onResume() {
        super.onResume();
        showFiles();
    }

    //显示文件列表
    public void showFiles(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TestMonitor";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] fileList = file.listFiles();
        fileShow = (LinearLayout) getView().findViewById(R.id.file_show);
        fileShow.removeAllViews();
        if (fileList != null) {
            for (final File _file : fileList) {
                TextView oneFile = new TextView(this.getActivity());
                oneFile.setBackground(getResources().getDrawable(R.drawable.style_file_item));
                oneFile.setWidth(oneFile.getMaxWidth());
                oneFile.setText(_file.getName());
                oneFile.setTextSize(20);
                oneFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.mobile.test.testmonitor.ACTION_START");
                        intent.addCategory("com.mobile.test.testmonitor.File_DETAIL_CATEGORY");
                        intent.putExtra("path", _file.getAbsolutePath());
                        startActivity(intent);
                    }
                });
                fileShow.addView(oneFile);
            }
        }
    }
}
