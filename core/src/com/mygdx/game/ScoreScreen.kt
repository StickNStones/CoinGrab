package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import java.util.concurrent.TimeUnit
import kotlin.text.Typography.times

class ScoreScreen : Screen {
    lateinit var background: Texture
    lateinit var game: CoinGrab

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {

    }

    override fun dispose() {

    }

    override fun hide() {

    }

    override fun show() {
    }

    override fun render(delta: Float) {
        if (CoinGrab.screen2) {
            background = Texture("scoreScreen.png")
            CoinGrab.batch.begin()
            CoinGrab.batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
            Gdx.app.log("SCORE", "Scorescreen")

            val preferences = Gdx.app.getPreferences("My Preferences")

            val font = BitmapFont()
            font.color = Color.WHITE
            font.data.setScale(10f)
            for (r in 1..3) {
                val height = (Gdx.graphics.height - 100 - 150*r).toFloat()
                font.draw(CoinGrab.batch, preferences.getString("Score$r", "0").padStart(3, '0'), (Gdx.graphics.width / 5) - 200f, height)
                font.draw(CoinGrab.batch, preferences.getString("Jumps$r", "0").padStart(3, '0'), (Gdx.graphics.width / 2) - 200f, height)
                font.draw(CoinGrab.batch,
                        TimeUnit.MILLISECONDS.toMinutes(preferences.getLong("Time$r")).toString().padStart(2,'0')
                        + ":" +
                        (TimeUnit.MILLISECONDS.toSeconds(preferences.getLong("Time$r"))%60).toString().padStart(2,'0')
                        , ((Gdx.graphics.width / 1.25) - 200f).toFloat(), height)
            }

            val height = (Gdx.graphics.height - 250).toFloat()
            font.draw(CoinGrab.batch, CoinGrab.score.toString(), (Gdx.graphics.width/5)-200f, height-700)
            font.draw(CoinGrab.batch, CoinGrab.jumps.toString(), (Gdx.graphics.width/2)-200f, height-700)
            font.draw(CoinGrab.batch, TimeUnit.MILLISECONDS.toMinutes(CoinGrab.timeElapsed).toString().padStart(2,'0')
                    + ":" +
                    (TimeUnit.MILLISECONDS.toSeconds(CoinGrab.timeElapsed)%60).toString().padStart(2,'0'), ((Gdx.graphics.width/1.25)-200f).toFloat(), height-700)

            CoinGrab.batch.end()

        }

    }
}