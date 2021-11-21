package com.spitzer.igdbgames.ui.gamedetails

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.fragment.app.DialogFragment
import com.spitzer.igdbgames.databinding.MyRatingLayoutBinding

class RatingDialog(
    var gameRating: Float?,
    val onPositiveClickFunction: (Float?) -> Unit
) : DialogFragment() {
    private var _binding: MyRatingLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = MyRatingLayoutBinding.inflate(LayoutInflater.from(context))

        val dialog = activity?.let {
            Dialog(it)
        }

        if (dialog != null) {

            dialog.setContentView(binding.root)

            if (gameRating != null) {
                binding.dialogRatingBar.rating = gameRating!!
            } else {
                binding.dialogRatingBar.rating = 0.0f
            }

            binding.saveRating.setOnClickListener {
                onPositiveClickFunction(gameRating)
                dismiss()
            }
            binding.cancel.setOnClickListener {
                dismiss()
            }

            binding.dialogRatingBar.onRatingBarChangeListener =
                OnRatingBarChangeListener { _, rating, _ ->
                    gameRating = rating
                }

        }
        return dialog!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
