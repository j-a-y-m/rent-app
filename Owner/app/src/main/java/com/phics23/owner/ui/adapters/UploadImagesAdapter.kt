package com.phics23.owner.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.phics23.owner.databinding.ItemNewlistingUploadimagesBinding
import com.phics23.owner.databinding.LayoutNewlistingUploadimagesBinding
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import java.io.File

class UploadImagesAdapter(private val fragment: Fragment,private val easyImage: EasyImage) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "UploadImagesAdapter"
     private val _imageFileList = mutableListOf<File>()
     val imageFileList = _imageFileList


//    var easyImage : EasyImage = EasyImage.Builder(context).setChooserType(ChooserType.CAMERA_AND_GALLERY).allowMultiple(true).build()

    class ViewHolderImage(var binding: ItemNewlistingUploadimagesBinding, val viewType: Int) : RecyclerView.ViewHolder(binding.root)
    {

        private  val TAG = "UploadImagesAdapterViewHolder"
        fun log(position: Int)
        {
            //Log.e("TAG", "VH log: "+binding.toString()+position )
        }

        fun bind(image: File) {
            if (viewType==1)
            {
                //Log.e(TAG, "bind: "+image.path )
               var itemBinding = binding
                binding.image=image
            }
        }
    }

    class ViewHolderButton(binding: LayoutNewlistingUploadimagesBinding,val viewType: Int) : RecyclerView.ViewHolder(binding.root) {

    }


    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
        if (_imageFileList.isEmpty() || position == _imageFileList.size )
        {
            return 0
        }
        else
            return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //Log.e(TAG, "onCreateViewHolder: "+viewType )
        if(viewType==1)
        {
            //Image
            var binding = ItemNewlistingUploadimagesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return  ViewHolderImage(binding,viewType)
        }else
        {
            //Button
            var binding = LayoutNewlistingUploadimagesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            binding.root.setOnClickListener {
                easyImage.openGallery(fragment)

            }
            return  ViewHolderButton(binding, viewType)
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Log.e(TAG, "onBindViewHolder: "+position )
            if(holder.itemViewType==1) {
               var holder= holder as ViewHolderImage
                holder.bind(_imageFileList[position])
            }
    }

    override fun getItemCount(): Int {
        if (_imageFileList.isEmpty())
        {
            return 1
        }else
            return _imageFileList.size+1
    }

    fun addImages(imageMediaFiles: Array<MediaFile>)
    {
//        //Log.e(TAG, "addImages: $imageFiles", )
        val imageFiles = imageMediaFiles.map { imageMediaFile-> imageMediaFile.file }
        _imageFileList.addAll(imageFiles)
        //Log.e(TAG, "addImages: ${_imageFileList.size}" )
        notifyItemRangeChanged(_imageFileList.size,imageFiles.size)
    }


}