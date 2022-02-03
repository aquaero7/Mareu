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
import com.example.mareu.di.DI;
import com.example.mareu.events.DeleteMeetingEvent;
import com.example.mareu.model.Meeting;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class MyMeetingRecyclerViewAdapter extends RecyclerView.Adapter<MyMeetingRecyclerViewAdapter.ViewHolder> {

    // Init variables et constructeur RV Adapter --------------------------------------------------
    private final List<Meeting> mMeetings;
    public MyMeetingRecyclerViewAdapter(List<Meeting> meetings) {
        this.mMeetings = meetings;
    }
    // Fin ----------------------------------------------------------------------------------------


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // // Initialement & option A2 OK ---------------------------------------------------------
        return new ViewHolder(FragmentMeetingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        // // Fin ---------------------------------------------------------------------------------

        /* // Option A1 OK ------------------------------------------------------------------------
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_meeting, parent, false);
        return new ViewHolder(view);
        */ // Fin ---------------------------------------------------------------------------------
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Appelé autant de fois qu'on a d'éléments dans la liste, quand l'élément arrive à l'écran

        // Affichage de l'item --------------------------------------------------------------------
        holder.displayMeeting(mMeetings.get(position));     // Méthode displayMeeting créée dans ViewHolder
        // Fin ------------------------------------------------------------------------------------

        // Mise en place d'un listener sur le bouton de suppression -------------------------------
        holder.setDeleteButton(mMeetings.get(position));    // Méthode setDeleteButton créée dans ViewHolder
        // Fin ------------------------------------------------------------------------------------
    }

    @Override
    public int getItemCount() {
        // Décompte des items de la liste à afficher ----------------------------------------------
        return mMeetings.size();
        // Fin ------------------------------------------------------------------------------------
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

        // Constructeur ViewHolder et liens avec layout -------------------------------------------

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


        // Création de la méthode appelée dans onBindViewHolder pour le listener à la position courante
        public void setDeleteButton(Meeting meeting) {
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new DeleteMeetingEvent(meeting));
                }
            });
        }
        // Fin ------------------------------------------------------------------------------------

        // Création de la méthode appelée dans onBindViewHolder pour afficher l'élément à la position courante
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
        // Fin ------------------------------------------------------------------------------------


    }
}