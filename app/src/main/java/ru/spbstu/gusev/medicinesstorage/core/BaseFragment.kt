package ru.spbstu.gusev.medicinesstorage.core

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ru.spbstu.gusev.medicinesstorage.utils.launchAndCollectIn

abstract class BaseFragment : Fragment() {

    inline fun <T> Flow<T>.launchAndCollectIn(
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        crossinline action: suspend CoroutineScope.(T) -> Unit, // no `= {}` because https://youtrack.jetbrains.com/issue/KT-45505/IAE-suspend-default-lambda-X-cannot-be-inlined-use-a-function-re
    ) = launchAndCollectIn(
        viewLifecycleOwner,
        minActiveState,
        action,
    )
}