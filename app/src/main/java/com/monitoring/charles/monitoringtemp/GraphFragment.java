package com.monitoring.charles.monitoringtemp;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GraphFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> dataSet;
    private LineGraphSeries<DataPoint> graphSeries;
    private LineGraphSeries<DataPoint> graphSeries2;
    private int xToAdd = 0;
    private int yToAdd = 0;


    private OnFragmentInteractionListener mListener;
    private Runnable mTimer1;
    private final Handler mHandler = new Handler();
    private Runnable mTimer2;
    private double graph2LastXvalue = 5d;
    private boolean breakOut = false;
    private ArrayList<String> dataSet2;
    private ArrayList<String> dataSet3;
    private LineGraphSeries<DataPoint> graphSeries3;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param data3
     * @return A new instance of fragment GraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance() {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        //  args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        //args.putStringArrayList("First Data", data);
        //args.putStringArrayList("Second Data", data2);
        //args.putStringArrayList("Third Data", data3);


        //fragment.setArguments(args);
        return fragment;
    }

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSet = new ArrayList<String>(200);

        if (getArguments() != null) {
            //  mParam1 = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
            //dataSet2 = getArguments().getStringArrayList("Second Data");
            //dataSet3 = getArguments().getStringArrayList("Third Data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        graphSeries = new LineGraphSeries<DataPoint>();
        graphSeries2 = new LineGraphSeries<DataPoint>();
        graphSeries3 = new LineGraphSeries<DataPoint>();
        graph.addSeries(graphSeries);
        //graph.addSeries(graphSeries2);
        //graph.addSeries(graphSeries3);
        graphSeries.setColor(Color.RED);
        graphSeries2.setColor(Color.BLUE);
        graphSeries3.setColor(Color.GREEN);
        graphSeries.setTitle("First Data");
        graphSeries2.setTitle("Second Data");
        graphSeries3.setTitle("Third Data");
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        //graph.getViewport().setYAxisBoundsManual(true);
        //graph.getViewport().setMinY(15);
        //graph.getViewport().setMaxY(40);
        //graph.getGridLabelRenderer()
        graph.getGridLabelRenderer().setNumVerticalLabels(20);
        graph.getGridLabelRenderer().setNumHorizontalLabels(6);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        //graph.getLegendRenderer().s

        return view;
    }

    public DataPoint[] makeDataFromList() {

        DataPoint[] values = new DataPoint[dataSet.size()];
        for (int i = 0; i < dataSet.size(); i++) {
            double time = i;
            double y = Double.parseDouble(dataSet.get(i));
            DataPoint v = new DataPoint(time, y);
            values[i] = v;


        }

        return values;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void addData() {
        //System.out.println("Add" + dataSet.get(yToAdd));
        TempActivity ck = (TempActivity)getActivity();
        boolean checkedPlot = ck.getCheckedPlot();

        if(checkedPlot == true) {
            graphSeries.appendData(new DataPoint(xToAdd, Double.parseDouble(dataSet.get(yToAdd))), true, 200);
        }
        else if(checkedPlot == false){
            System.out.println("I am at false");
            graphSeries.appendData(new DataPoint(xToAdd, Double.parseDouble(dataSet.get(yToAdd))), false, 200);

        }
        //graphSeries2.appendData(new DataPoint(xToAdd, Double.parseDouble(dataSet2.get(yToAdd))), true, dataSet2.size());
        //graphSeries3.appendData(new DataPoint(xToAdd, Double.parseDouble(dataSet3.get(yToAdd))), true, dataSet3.size());

        xToAdd++;
        yToAdd++;
    }

    public void setDataSet() {
        TempActivity act = (TempActivity) getActivity();
        Double temp = act.getTemperature();
        System.out.println(temp);
        this.dataSet.add(String.valueOf(temp));
    }

    /*
    public void startPlotting(){
        new Thread() {
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setDataSet();
                            System.out.println("hello");
                            /*
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            addData();
                        }

                    });
                    Thread.sleep(1000);
                    if (breakOut) {
                       // System.out.println("im breaking");
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
            */
    public void onResume() {
        super.onResume();

        final TempActivity act = (TempActivity) getActivity();
        int flag = act.getFlag();
            setDataSet();
            addData();
            //startPlotting();
            /*
            new Thread() {
                public void run() {
                    try {
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDataSet();
                                System.out.println("hello");
                            /*
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                                addData();
                                onPause();

                            }

                        });
                        Thread.sleep(1000);
                        if (breakOut) {
                          //  System.out.println("im breaking");
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        */

        }





    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onPause() {
        mHandler.removeCallbacks(mTimer1);
        breakOut = true;
        //  mHandler.removeCallbacks(mTimer2);
        super.onPause();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
