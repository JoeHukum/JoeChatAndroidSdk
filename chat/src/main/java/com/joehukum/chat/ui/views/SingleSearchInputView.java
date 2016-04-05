//package com.joehukum.chat.ui.views;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.design.widget.FloatingActionButton;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AutoCompleteTextView;
//import android.widget.FrameLayout;
//
//import com.joehukum.chat.R;
//import com.joehukum.chat.messages.objects.Option;
//import com.joehukum.chat.ui.adapters.AutoCompleteAdapter;
//
//import java.util.List;
//
///**
// * Created by pulkitkumar on 05/04/16.
// */
//public class SingleSearchInputView extends FrameLayout
//{
//    public interface SingleSearchInputCallback
//    {
//        public void onSingleItemSelected();
//    }
//
//    private SingleSearchInputCallback mListener;
//    private List<Option> mOptions;
//    private AutoCompleteAdapter mAdapter;
//
//    public SingleSearchInputView(Context context, SingleSearchInputCallback listener)
//    {
//        super(context);
//        mListener = listener;
//        addView(getSingleAddView(context));
//    }
//
//    private View getSingleAddView(Context context)
//    {
//        View view = LayoutInflater.from(context).inflate(R.layout.single_search_input, null, false);
//        final AutoCompleteTextView autoComplete = (AutoCompleteTextView) view.findViewById(R.id.auto_complete);
//        FloatingActionButton send = (FloatingActionButton) view.findViewById(R.id.send);
//        mAdapter = new AutoCompleteAdapter(context, mOptions);
//        autoComplete.setAdapter(mAdapter);
//        send.setOnClickListener(new OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                mListener.onSingleItemSelected();
//            }
//        });
//        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                onItemClick();
//            }
//        });
//        return view;
//    }
//
//    public void setOptions(@NonNull List<Option> options)
//    {
//        mOptions = options;
//    }
//}
