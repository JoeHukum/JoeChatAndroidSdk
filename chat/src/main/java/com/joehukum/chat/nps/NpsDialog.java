package com.joehukum.chat.nps;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.joehukum.chat.R;

/**
 * Created by pulkitkumar on 22/09/16.
 */
public class NpsDialog extends DialogFragment implements View.OnClickListener
{
    public interface NpsDialogListener
    {
        void onClickOk(float rating, String comments);
        void onClickCancel();
    }

    private NpsDialogListener listener;
    private EditText comments;
    private RatingBar ratingBar;
    private Button okButton;
    private Button cancelButton;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        listener = (NpsDialogListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_nps, container, false);
        comments = (EditText) v.findViewById(R.id.comments);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        okButton = (Button) v.findViewById(R.id.btnPositive);
        cancelButton = (Button) v.findViewById(R.id.btnNegative);

        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnPositive)
        {
            onOkClick();
        } else if (view.getId() == R.id.btnNegative)
        {
            listener.onClickCancel();
        } else
        {
            return;
        }
    }

    private void onOkClick()
    {
        if (ratingBar.getRating() > 0)
        {
            listener.onClickOk(ratingBar.getRating(), comments.getText().toString());
        } else
        {
            showError();
        }
    }

    private void showError()
    {
        Toast.makeText(getActivity(), getString(R.string.rating_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach()
    {
        listener = null;
        super.onDetach();
    }
}
