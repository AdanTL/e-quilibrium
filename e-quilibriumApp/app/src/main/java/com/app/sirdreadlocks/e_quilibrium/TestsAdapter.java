package com.app.sirdreadlocks.e_quilibrium;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Adán on 20/11/2016.
 */

public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.CardViewHolder> implements Filterable {

    private ArrayList<Test> tests;
    private ArrayList<Test> testsFilter;
    private TestsAdapter.CustomFilter mFilter;

    public TestsAdapter(HashMap<String,Test> tests){
        this.tests = new ArrayList<>();
        this.tests.addAll(tests.values());
        this.testsFilter = new ArrayList<>();
        this.testsFilter.addAll(tests.values());
        this.mFilter = new TestsAdapter.CustomFilter(TestsAdapter.this);
    }

    public TestsAdapter.CardViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_list, viewGroup, false);

        TestsAdapter.CardViewHolder cvh = new TestsAdapter.CardViewHolder(itemView);

        return cvh;
    }

    public void onBindViewHolder(TestsAdapter.CardViewHolder viewHolder, final int pos){
        viewHolder.holderBinder(testsFilter.get(pos));
    }

    public int getItemCount(){
        return testsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder{
        private Test test;
        private View mView;

        public CardViewHolder(View v){
            super(v);
            mView = v;
/*            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =
                            new Intent(v.getContext(), Measures.class);
                    intent.putExtra("TEST",test);
                    v.getContext().startActivity(intent);
                }
            });*/
        }

        public void holderBinder(Test test) {
            this.test = test;
            TextView date = (TextView) mView.findViewById(R.id.txtCard1);
            TextView type = (TextView) mView.findViewById(R.id.txtCard2);
            date.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(test.getDate()));
            type.setText(test.getType());
        }

    }

    public class CustomFilter extends Filter {
        private TestsAdapter testsAdapter;

        private CustomFilter(TestsAdapter testsAdapter) {
            super();
            this.testsAdapter = testsAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            testsFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                testsFilter.addAll(tests);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Test test : tests) {
                    if (test.getType().toLowerCase().contains(filterPattern)) {
                        testsFilter.add(test);
                    }
                }
            }
            results.values = testsFilter;
            results.count = testsFilter.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.testsAdapter.notifyDataSetChanged();
        }
    }

}
