package com.example.myapplication.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.utils.Utility;

public class SlideUpDialog extends Dialog {

    public interface OnDeleteClickListener {
        void onDelete();
    }

    private OnDeleteClickListener onDeleteClickListener;



    public SlideUpDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public SlideUpDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected SlideUpDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.slide_up_dialog, null);
        setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = Utility.getScreenWidth(getContext());
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);

        Button cancelButton, deleteButton;
        cancelButton = findViewById(R.id.dialog_cancel);
        deleteButton = findViewById(R.id.dialog_delete);

        cancelButton.setOnClickListener(v -> {
            dismiss();
        });

        deleteButton.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDelete();
            }
            dismiss();
        });
    }

    public OnDeleteClickListener getOnDeleteClickListener() {
        return onDeleteClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }
}
