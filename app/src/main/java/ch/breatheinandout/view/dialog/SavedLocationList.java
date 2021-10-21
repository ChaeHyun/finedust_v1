package ch.breatheinandout.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import ch.breatheinandout.R;
import ch.breatheinandout.model.Addresses;
import ch.breatheinandout.utils.AppSharedPreferences;
import ch.breatheinandout.view.Views;

import java.util.ArrayList;

public class SavedLocationList extends Dialog {

    public SavedLocationList(@NonNull final Context context , final Views.DialogButtonClick connectFragment) {
        super(context);

        setTitle("지역선택");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_listview);

        final ArrayList<String> savedLocation = getListOfSavedLocation(context);

        ArrayAdapter<String> Adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, savedLocation);

        ListView listView = findViewById(R.id.dialog_address_listview);
        listView.setAdapter(Adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(context, "지역설정 : " + savedLocation.get(position) , Toast.LENGTH_SHORT).show();
                connectFragment.dialogListViewItemClick(position);
                dismiss();
            }
        });

        ImageView image = findViewById(R.id.imageFinish);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    private ArrayList<String> getListOfSavedLocation(Context context) {
        AppSharedPreferences pref = new AppSharedPreferences(context);
        ArrayList<String> data = new ArrayList<>();

        data.add("현재위치 [GPS]");
        for (int i = 1; i < 4; i++) {
            Addresses saved = (Addresses) pref.getObject(AppSharedPreferences.MEMORIZED_LOCATIONS[i], "", new Addresses());
            if (saved != null) {
                data.add(saved.getAddr());
            }
        }

        return data;
    }
}
