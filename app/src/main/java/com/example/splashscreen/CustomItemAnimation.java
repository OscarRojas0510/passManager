package com.example.splashscreen;

import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class CustomItemAnimation extends DefaultItemAnimator
{
    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder)
    {
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(
                holder.itemView.getContext(),
                R.anim.do_not_move
        ));

        return super.animateRemove(holder);
    }
}
