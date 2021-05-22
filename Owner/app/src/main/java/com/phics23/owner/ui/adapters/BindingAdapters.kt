package com.phics23.owner.ui.adapters

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.phics23.owner.R
import com.squareup.picasso.Picasso
import java.io.File
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.processNextEventInCurrentThread
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class BindingAdapters {
    companion object {

        //load image into imageview
        @BindingAdapter("imgFile")
        @JvmStatic
        fun setImageFromFile(imageView: ImageView, img: File) {

                Picasso.get().load(img).error(R.drawable.ic_broken_image_24).into(imageView)

        }

        @BindingAdapter("profileImg")
        @JvmStatic
        fun setProfileimg(shapeableImageView: ShapeableImageView, imgUrl: String?) {

            if (imgUrl!=null)
            {
                Picasso.get().load(imgUrl).error(R.drawable.ic_broken_image_24).into(shapeableImageView)
            }else
                shapeableImageView.setImageResource(R.drawable.ic_person_24)


        }
        fun ImageView.loadImage(imgView : ImageView, resource : File)
        {
            Picasso.get().load(resource).into(imgView);
        }

        @BindingAdapter("imgUrl")
        @JvmStatic
        fun setImageFromUrl(imageView: ImageView, imgUrl: String) {

            Picasso.get().load(imgUrl).error(R.drawable.ic_broken_image_24).into(imageView)

        }

        @BindingAdapter("app:errorText")
        @JvmStatic
        fun setErrorMessage(view: TextInputLayout, errorMessage: String) {
            view.error = errorMessage
        }

        @BindingAdapter(value = ["currentOccupants", "totalOccupants"], requireAll = true)
        @JvmStatic
        fun setOccupants(textView: TextView, currentOccupants: String, totalOccupants: String) {

            val occupants = currentOccupants+"/"+totalOccupants
            textView.text= occupants

        }

        @BindingAdapter("facilityAvailable")
        @JvmStatic
        fun isFacilityAvailable(imageView: ImageView, facilities: List<String>) {

            if(imageView.contentDescription in facilities)
            {
                imageView.visibility = View.VISIBLE

            }else
            {
                imageView.visibility = View.GONE
            }
        }

        @BindingAdapter("setRating")
        @JvmStatic
        fun setRating(view: View, rating: Float?) {

            if (view is TextView)
            {   if(rating!=null)
            {
                view.text = rating.toString()
            }else
            {
                view.visibility=View.GONE
            }

            }else
                if (view is RatingBar)
                {
                    if(rating!=null)
                    {
                        view.rating=rating
                    }else
                    {
                        view.visibility=View.GONE
                    }

                }else return


        }

        @BindingAdapter(value = ["fromDate", "toDate"], requireAll = true)
        @JvmStatic
        fun setFromToDate(textView: TextView, from: Long, to: Long) {

            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/LL/yy")
            val fromDate = LocalDateTime.ofEpochSecond(from,0, ZoneOffset.of("+05:30"))
            val toDate = LocalDateTime.ofEpochSecond(to,0, ZoneOffset.UTC)
            val FromToDate = fromDate.format(formatter)+" to "+toDate.format(formatter)
            textView.text= FromToDate

        }



    }
}