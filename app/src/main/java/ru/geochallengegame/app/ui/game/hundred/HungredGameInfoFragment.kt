package ru.geochallengegame.app.ui.game.hundred


import ru.geochallengegame.app.ui.game.BaseGameViewModel
import ru.geochallengegame.app.ui.game.classic.ClassicGameInfoFragment
import javax.inject.Inject

class HungredGameInfoFragment @Inject constructor() : ClassicGameInfoFragment() {
    override fun getViewModel(): BaseGameViewModel {
        return (activity as HungredGameActivity).viewModel
    }
}