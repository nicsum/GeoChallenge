package ru.geochallengegame.app.ui.menu


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fr_menu_maps.*
import ru.geochallengegame.R
import ru.geochallengegame.app.ui.menu.rv.GameMapAdapter
import ru.geochallengegame.app.ui.menu.vm.MenuMapsViewModel
import javax.inject.Inject

abstract class BaseMapsFragment : Fragment() {

    abstract fun getLayout(): Int

    @Inject
    lateinit var adapterView: GameMapAdapter

    @Inject
    lateinit var viewModel: MenuMapsViewModel

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        refreshLayout.setOnRefreshListener { viewModel.loadMaps() }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.modeInfo) {
            viewModel.info()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.maps_menu, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (context as MenuActivity).menuComponent?.inject(this)

        maps_rv.layoutManager = linearLayoutManager
        maps_rv.adapter = adapterView

        viewModel.maps.observe(viewLifecycleOwner, Observer {
            adapterView.add(it, true)
        })
        viewModel.loadingIsVisible.observe(
            viewLifecycleOwner,
            Observer {
                refreshLayout.isRefreshing = it
            })
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadMaps()
    }

}