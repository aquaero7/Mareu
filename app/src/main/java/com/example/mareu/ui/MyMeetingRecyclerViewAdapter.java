package com.example.mareu.ui;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mareu.R;
import com.example.mareu.databinding.FragmentMeetingBinding;
import com.example.mareu.model.Meeting;
import com.example.mareu.ui.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.   A supprimer
 */
public class MyMeetingRecyclerViewAdapter extends RecyclerView.Adapter<MyMeetingRecyclerViewAdapter.ViewHolder> {

    /* // Initialement TODO : A supprimer ---------------------------------------------------------
    private final List<PlaceholderItem> mValues;

    public MyMeetingRecyclerViewAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }
    */ // Fin -------------------------------------------------------------------------------------

    // // Init variables et constructeur RV Adapter -----------------------------------------------
    private final List<Meeting> mMeetings;
    public MyMeetingRecyclerViewAdapter(List<Meeting> meetings) {
        this.mMeetings = meetings;
    }
    // // Fin -------------------------------------------------------------------------------------


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // // Initialement & option A2 OK ---------------------------------------------------------
        return new ViewHolder(FragmentMeetingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        // // Fin ---------------------------------------------------------------------------------

        /* // Option A1 OK ------------------------------------------------------------------------
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_meeting, parent, false);
        return new ViewHolder(view);
        */ // Fin ---------------------------------------------------------------------------------

        /* // Option A2 OK TODO : A supprimer -----------------------------------------------------
        return new ViewHolder(FragmentMeetingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        */ // Fin ---------------------------------------------------------------------------------


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        // Appelé autant de fois qu'on a d'éléments dans la liste, quand l'élément arrive à l'écran.

        /* // Initialement TODO : A supprimer -----------------------------------------------------
        holder.mItem = mValues.get(position);
        holder.mLine1.setText(mValues.get(position).id);
        holder.mLine2.setText(mValues.get(position).content);
        */ // Fin ---------------------------------------------------------------------------------

        // // Affichage de l'item -----------------------------------------------------------------
        holder.displayMeeting(mMeetings.get(position)); // Méthode displayMeeting créée dans ViewHolder.
        // // Fin ---------------------------------------------------------------------------------
    }

    @Override
    public int getItemCount() {

        /* // Initialement TODO : A supprimer  ----------------------------------------------------
        return mValues.size();
        */ // Fin ---------------------------------------------------------------------------------

        // // Décompte des items de la liste à afficher -------------------------------------------
        return mMeetings.size();
        // // Fin ---------------------------------------------------------------------------------
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        /* // Option B1 avec ImageView d'un shape ou d'un VectorAsset -----------------------------
        public final ImageView mColorMeeting;
        */ // Fin ---------------------------------------------------------------------------------

        // // Option B2 avec CardView -------------------------------------------------------------
        public final CardView mColorMeeting;
        // // Fin ---------------------------------------------------------------------------------

        public final TextView mLine1;
        public final TextView mLine2;
        public final ImageButton mDeleteButton;
        // public PlaceholderItem mItem;    // TODO : A supprimer

        // // Constructeur ViewHolder et liens avec layout ----------------------------------------

        // // Initialement & option A2 OK ---------------------------------------------------------
        public ViewHolder(FragmentMeetingBinding meetingBinding) {
            super(meetingBinding.getRoot());
            mColorMeeting = meetingBinding.colorMeeting;
            mLine1 = meetingBinding.line1;
            mLine2 = meetingBinding.line2;
            mDeleteButton = meetingBinding.deleteButton;
        }
        // // Fin ---------------------------------------------------------------------------------

        /* // Option A1  OK -----------------------------------------------------------------------
        public ViewHolder(View view) {
            super(view);
            mColorMeeting = view.findViewById(R.id.colorMeeting);
            mLine1 = view.findViewById(R.id.line1);
            mLine2 = view.findViewById(R.id.line2);
            mDeleteButton = view.findViewById(R.id.deleteButton);
        }
        */ // Fin ---------------------------------------------------------------------------------

        /* // Option A2 OK TODO : A supprimer -----------------------------------------------------
        public ViewHolder(FragmentMeetingBinding meetingBinding) {
            super(meetingBinding.getRoot());
            mColorMeeting = meetingBinding.colorMeeting;
            mLine1 = meetingBinding.line1;
            mLine2 = meetingBinding.line2;
            mDeleteButton = meetingBinding.deleteButton;
        }
        */ // Fin ---------------------------------------------------------------------------------

        // Création méthode appelée dans onBindViewHolder pour afficher l'élément à la position courante.
        public void displayMeeting(Meeting meeting) {
            // L'argument étant un id de couleur ou une couleur au format "#xxxxxxxx"...

            /* // Option B1 OK avec ImageView d'un shape ou d'un VectorAsset ---------------------
            mColorMeeting.setColorFilter(mColorMeeting.getResources().getColor(meeting.getRoom().getColor()));
            ... ou bien ...
            mColorMeeting.setImageTintList(ColorStateList.valueOf(mColorMeeting.getResources().getColor(meeting.getRoom().getColor())));
            */ // Fin -----------------------------------------------------------------------------

            // // Option B2 OK avec CardView ------------------------------------------------------
            mColorMeeting.setCardBackgroundColor(mColorMeeting.getResources().getColor(meeting.getRoom().getColor()));
            // // Fin -----------------------------------------------------------------------------

            mLine1.setText(meeting.getName() + " - " + meeting.getStart().replace(":", "h") + " - " + meeting.getRoom().getName());
            mLine2.setText(meeting.getParticipants());
        }

        // TODO : A supprimer
        @Override
        public String toString() {
            return super.toString() + " '" + mLine2.getText() + "'";
        }
    }
}