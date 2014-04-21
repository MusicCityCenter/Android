package org.magnum.mcc.nav;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.magnum.mccmap.R;
/**
 * Created by yaopan on 4/20/14.
 */
public class FloorplanFragment extends Fragment {

    private int floorplanId;

    private static final String ZOOM_EXTRA = "ZOOM_LEVEL";
    private static final int ZOOM_1 = 21;
    private static final int ZOOM_1M = 20;
    private static final int ZOOM_2 = 19;
    private static final int ZOOM_3 = 18;
    private static final int ZOOM_4 = 17;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.activity_floorplan, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();
        if(args !=null){
            floorplanId = args.getInt(ZOOM_EXTRA);

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);


        ImageView floorplanView = (ImageView)getView().findViewById(R.id.imageView_floorplan);


        switch (floorplanId) {
            case ZOOM_1:
                floorplanView.setImageDrawable(getResources().getDrawable(R.drawable.level1));
                break;
            case ZOOM_1M:
                floorplanView.setImageDrawable(getResources().getDrawable(R.drawable.level1m));
                break;
            case ZOOM_2:
                floorplanView.setImageDrawable(getResources().getDrawable(R.drawable.level2));
                break;
            case ZOOM_3:
                floorplanView.setImageDrawable(getResources().getDrawable(R.drawable.level3));
                break;
            case ZOOM_4:
                floorplanView.setImageDrawable(getResources().getDrawable(R.drawable.level4));
                break;
        }



    }
}
