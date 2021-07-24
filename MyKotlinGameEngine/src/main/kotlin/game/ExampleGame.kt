package com.rk.mykotlingameengine.game

import com.rk.mykotlingameengine.core.FpsCounter
import com.rk.mykotlingameengine.core.GameTime
import com.rk.mykotlingameengine.core.IGame

class ExampleGame : IGame {

    private var lastFpsTimestamp = GameTime.elapsedTime

    override fun onTick() {
        if (GameTime.elapsedTime - lastFpsTimestamp >= 1.0f) {
            println("${FpsCounter.currentFps} FPS")
            lastFpsTimestamp = GameTime.elapsedTime
        }
    }
}