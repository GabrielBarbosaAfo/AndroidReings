package br.edu.ifsudestemg.throne.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import br.edu.ifsudestemg.throne.R;

public class TwistDialog extends androidx.fragment.app.DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }


    public interface OnTwistFinished {
        void onFinished();
    }

    private static final String ARG_TEXT = "text";

    private OnTwistFinished callback;

    public static TwistDialog newInstance(String text, OnTwistFinished cb) {
        TwistDialog dialog = new TwistDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        dialog.setArguments(args);
        dialog.callback = cb;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(false);
            if (getDialog().getWindow() != null) {
                getDialog().getWindow()
                        .setBackgroundDrawableResource(android.R.color.transparent);
            }
        }

        View view = inflater.inflate(R.layout.dialog_twist, container, false);
        TextView twistText = view.findViewById(R.id.twist_text);

        twistText.setText(getArguments() != null
                ? getArguments().getString(ARG_TEXT, "")
                : "");

        twistText.setOnClickListener(v -> {
            dismiss();
            if (callback != null) callback.onFinished();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        callback = null;
    }
}