package com.grok.akm.movie

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.grok.akm.movie.Utils.SortType
import com.grok.akm.movie.ViewModel.FragmentViewModel
import com.grok.akm.movie.ViewModel.ViewModelFactory
import com.grok.akm.movie.di.MyApplication
import com.grok.akm.movie.di.SortPreferences
import javax.inject.Inject

class SortingDialogFragment : DialogFragment(), RadioGroup.OnCheckedChangeListener {

    private var mostPopular: RadioButton? = null
    private var highestRated: RadioButton? = null
    private var favorites: RadioButton? = null
    private var newest: RadioButton? = null

    private var sortingOptionsGroup: RadioGroup? = null

    @Inject
    lateinit var pref: SortPreferences

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var fragmentViewModel: FragmentViewModel? = null

    companion object {

        fun newInstance(): SortingDialogFragment {
            return SortingDialogFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        (activity?.application as MyApplication).appComponent!!.doInjection(this)

        fragmentViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(FragmentViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.sorting_options, null)

        mostPopular = dialogView.findViewById<View>(R.id.most_popular) as RadioButton
        highestRated = dialogView.findViewById<View>(R.id.highest_rated) as RadioButton
        favorites = dialogView.findViewById<View>(R.id.favorites) as RadioButton
        newest = dialogView.findViewById<View>(R.id.newest) as RadioButton
        sortingOptionsGroup = dialogView.findViewById<View>(R.id.sorting_group) as RadioGroup
        setChecked()
        sortingOptionsGroup?.setOnCheckedChangeListener(this)

        val dialog = Dialog(activity!!)
        dialog.setContentView(dialogView)
        dialog.setTitle(R.string.sort_by)
        dialog.show()
        return dialog
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {

        when (checkedId) {
            R.id.most_popular -> {
                fragmentViewModel?.setStatusLiveData(SortType.MOST_POPULAR)
                pref.setSelectedOption(SortType.MOST_POPULAR)
                dismiss()
            }

            R.id.highest_rated -> {
                fragmentViewModel?.setStatusLiveData(SortType.HIGHEST_RATED)
                pref.setSelectedOption(SortType.HIGHEST_RATED)
                dismiss()
            }

            R.id.favorites -> {
                fragmentViewModel?.setStatusLiveData(SortType.FAVORITES)
                pref.setSelectedOption(SortType.FAVORITES)
                dismiss()
            }
            R.id.newest -> {
                fragmentViewModel?.setStatusLiveData(SortType.NEWEST)
                pref.setSelectedOption(SortType.NEWEST)
                dismiss()
            }
        }

    }

    private fun setChecked() {
        val selectedOption = pref.selectedOption
        if (selectedOption == SortType.MOST_POPULAR.value) {
            mostPopular?.isChecked = true

        } else if (selectedOption == SortType.HIGHEST_RATED.value) {
            highestRated?.isChecked = true

        } else if (selectedOption == SortType.NEWEST.value) {
            newest?.isChecked = true

        } else {
            favorites?.isChecked = true

        }
    }
}