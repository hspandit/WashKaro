package inspire2connect.inspire2connect.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import inspire2connect.inspire2connect.R;
import inspire2connect.inspire2connect.utils.BaseActivity;
import inspire2connect.inspire2connect.utils.LocaleHelper;


@SuppressWarnings("SpellCheckingInspection")
public class aboutActivity extends BaseActivity implements View.OnClickListener {

    private ImageView tavlab;
    private ImageView precog;
    private ImageView iiitd;

    aboutElem[] elems;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        elems = new aboutElem[]{
                new aboutElem(this, R.string.bhavika, R.string.bhavika_tag, R.string.bhavikaURL),
                new aboutElem(this, R.string.chirag, R.string.chirag_tag, R.string.chiragURL),
                new aboutElem(this, R.string.harsh, R.string.harsh_tag, R.string.harshURL),
                new aboutElem(this, R.string.himanshu, R.string.himanshu_tag, R.string.himanshuURL),
                new aboutElem(this, R.string.kanav, R.string.kanav_tag, R.string.kanavURL),
                new aboutElem(this, R.string.priyanka, R.string.priyanka_tag, R.string.priyankaURL),
                new aboutElem(this, R.string.rohan, R.string.rohan_tag, R.string.rohanURL),
                new aboutElem(this, R.string.tanuj, R.string.tanuj_tag, R.string.tanujURL),
                new aboutElem(this, R.string.vaibhav, R.string.vaibhav_tag, R.string.vaibhavURL),
                new aboutElem(this, R.string.vrinda, R.string.vrinda_tag, R.string.vrindaURL),
                new aboutElem(this, R.string.tav, R.string.tav_description, R.string.tavURL),
                new aboutElem(this, R.string.pk, R.string.pk_description, R.string.pkURL)
        };

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ca_activity_about);


        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setTitle(getString(R.string.about_us));
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.about_us));
        }

        GridView gridView = findViewById(R.id.gridview);
        about_adapter aboutAdapter = new about_adapter(this, elems);
        gridView.setAdapter(aboutAdapter);

        tavlab = findViewById(R.id.tavlabLogo);
        precog = findViewById(R.id.iiitdLogo);
        iiitd = findViewById(R.id.precogLogo);

        tavlab.setOnClickListener(this);
        precog.setOnClickListener(this);
        iiitd.setOnClickListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                String url = elems[position].url;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }


    @Override
    public void onClick(View view) {
        String url = getString(R.string.iiitdURL);
        switch (view.getId()) {
            case R.id.iiitdLogo:
                url = getString(R.string.iiitdURL);
                break;
            case R.id.precogLogo:
                url = getString(R.string.precogURL);
                break;
            case R.id.tavlabLogo:
                url = getString(R.string.tavlabURL);
                break;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
