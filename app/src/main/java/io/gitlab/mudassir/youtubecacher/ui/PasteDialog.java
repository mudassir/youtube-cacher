package io.gitlab.mudassir.youtubecacher.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import io.gitlab.mudassir.youtubecacher.R;

/**
 * Dialog to prompt user to enter the URL for the video
 */
public class PasteDialog extends DialogFragment {

	public interface Listener {
		void onPasteReceived(String url);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Need a reference to the EditText
		View root = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_paste, null);
		final EditText editText = (EditText) root.findViewById(R.id.paste_text);

		final AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.setView(root)
				.setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
//						android.widget.Toast.makeText(getActivity(), editText.getText(), android.widget.Toast.LENGTH_SHORT).show();
						((Listener) getActivity()).onPasteReceived(editText.getText().toString());
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();

		// Need to change default color of dialog buttons, this was the only way I could find to do it
		dialog.setOnShowListener( new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface arg0) {
				int color = ContextCompat.getColor(getActivity(), R.color.colorAccent);
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
				dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
			}
		});

		return dialog;
	}
}
