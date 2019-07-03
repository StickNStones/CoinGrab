package com.mygdx.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle


import java.util.Random
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class CoinGrab : Game() {

    lateinit var scoreScreen: ScoreScreen

    companion object {
        lateinit var batch: SpriteBatch
        var screen2 = true
        var score = 0
        var jumps = 0
        var startTime = System.currentTimeMillis()
        var timeElapsed = System.currentTimeMillis() - startTime
        lateinit var character: Array<Texture>
    }

    lateinit var background: Texture
    lateinit var font: BitmapFont
    lateinit var keyScreen: BitmapFont
    var pause = 0
    var gravity = 0.2f
    var velocity = 0f

    var gameState = 0

    lateinit var random: Random

    lateinit var coin: Texture
    var coinCount: Int = 0

    var coinArray = ArrayList<FloatingItem>()

    var chestCount: Int = 0
    var chestArray = ArrayList<FloatingItem>()

    var keyCount: Int = 0
    var keyArray = ArrayList<FloatingItem>()

    var bombArray = ArrayList<FloatingItem>()

    lateinit var bomb: Texture
    var bombCount: Int = 0
    lateinit var dizzy: Texture

    // Audio
    lateinit var deathSound: Music

    var keyBool = false

    lateinit var preferences: Preferences

    lateinit var man: Character

    override fun create() {
        batch = SpriteBatch()
        background = Texture("bg.png")
        character = arrayOf(Texture("frame-1.png"), Texture("frame-2.png"), Texture("frame-3.png"), Texture("frame-4.png"))
        dizzy = Texture("dizzy-1.png")
        coin = Texture("coin.png")
        bomb = Texture("bomb.png")

        man = Character()

        deathSound = Gdx.audio.newMusic(Gdx.files.internal("ohnolaugh.mp3"))

        random = Random()

        preferences = Gdx.app.getPreferences("My Preferences")

        font = BitmapFont()
        font.color = Color.WHITE
        font.data.setScale(10f)
        keyScreen = BitmapFont()
        keyScreen.color = Color.RED
        keyScreen.data.setScale(5f)
        startTime = System.currentTimeMillis()


        fun setScoreScreen() {
            scoreScreen = ScoreScreen()
            setScreen(scoreScreen)
        }
        if (screen2) {
            setScoreScreen()
        }
    }

    private fun makeItem(item: String) {
        var i = 0
        when(item) {
            "Key1.png" -> {
                if (keyCount < 188) {
                    keyCount++
                } else {
                    keyCount = 0
                    keyArray.add(FloatingItem(item))
                }
                val size = keyArray.size
                while (i < size && i != keyArray.size) {
                    if (keyArray[i].locX < -40) {
                        keyArray.removeAt(i)
                    } else {
                        batch.draw(keyArray[i].texture, keyArray[i].locX.toFloat(), keyArray[i].locY)
                        keyArray[i].locX -= 4
                        keyArray[i].update()
                        i++
                    }
                }
            }
            "Chest_Locked1.png" -> {
                if (chestCount < 233) {
                    chestCount++
                } else {
                    chestCount = 0
                    chestArray.add(FloatingItem(item))
                }

                var i = 0
                val size = chestArray.size
                while (i < size && i != chestArray.size) {
                    if (chestArray[i].locX < -40) {
                        chestArray.removeAt(i)
                    } else {
                        batch.draw(chestArray[i].texture, chestArray[i].locX.toFloat(), chestArray[i].locY)
                        chestArray[i].locX -= 4
                        chestArray[i].update()
                        i++
                    }
                }
            }
            "bomb.png" -> {
                if (bombCount < 77) {
                    bombCount++
                } else {
                    bombCount = 0
                    bombArray.add(FloatingItem(item))
                    bombArray[bombArray.size-1].update((0.80).toFloat(), (0.70).toFloat())
                }

                for (i in bombArray.indices) {
                    batch.draw(bombArray[i].texture, bombArray[i].locX.toFloat(), bombArray[i].locY)
                    bombArray[i].locX -= 4
                    bombArray[i].update()
                }
            }
            "coin.png" -> {
                if (coinCount < 100) {
                    coinCount++
                } else {
                    coinCount = 0
                    coinArray.add(FloatingItem(item))
                }
                for (i in coinArray.indices) {
                    batch.draw(coinArray[i].texture, coinArray[i].locX.toFloat(), coinArray[i].locY)
                    coinArray[i].locX -= 4
                    coinArray[i].update()
                }
            }
        }

    }
    private fun reset() {
        man.characterY = Gdx.graphics.height/2

        velocity = 0f

        chestCount = 0
        chestArray.clear()
        keyCount = 0
        keyArray.clear()
        coinCount = 0
        coinArray.clear()
        bombCount = 0
        bombArray.clear()

        jumps = 0
        score = 0
        keyBool = false
    }

    override fun render() {
        super.render()

        if (!screen2) {
            Gdx.graphics.isContinuousRendering = true
            batch.begin()
            batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
            if (gameState == 1) {
                deathSound.stop()

                makeItem("Key1.png")
                makeItem("Chest_Locked1.png")
                makeItem("bomb.png")
                makeItem("coin.png")

                // Jump
                if (Gdx.input.justTouched()) {
                    velocity += -10f
                    jumps++
                } else {
                    velocity += gravity
                    if (man.characterY <= 0) {
                        man.characterY = 0
                        velocity = 0f
                    }
                }

                val accelX = Gdx.input.accelerometerX
                man.characterX -= accelX.toInt()
                if (man.characterX < 0) {
                    man.characterX = 0
                } else if (man.characterX > Gdx.graphics.width) {
                    man.characterX = Gdx.graphics.width
                }

                if (pause < 8) {
                    pause++
                } else {
                    pause = 0
                    if (man.characterState < 3) {
                        man.characterState++
                    } else {
                        man.characterState = 0
                    }
                }
                man.characterY -= velocity.toInt()

            } else if (gameState == 0) {
                // Wait to start
                if (Gdx.input.justTouched()) {
                    startTime = System.currentTimeMillis()
                    gameState = 1
                }

            } else if (gameState == 2) {
                // Game over
                if (Gdx.input.justTouched()) {
                    startTime = System.currentTimeMillis()
                    gameState = 1
                }
            }

            if (gameState == 2) {
                batch.draw(dizzy, man.characterX.toFloat(), man.characterY.toFloat())
            } else {
                batch.draw(character[man.characterState], man.characterX.toFloat(), man.characterY.toFloat())
                man.update()
                intersection(coinArray)
                intersection(keyArray)
                intersection(chestArray)
                intersection(bombArray)
            }

            font.draw(batch, score.toString(), 100f, 220f)

            if (keyBool) {
                keyScreen.draw(batch, "KEY", 100f, 100f)
            }
            batch.end()
        } else {
            Gdx.graphics.isContinuousRendering = false
            screen2 = false
            reset()
        }
    }

    private fun intersection(itemArray: ArrayList<FloatingItem> ) {
        var i = 0
        when(itemArray) {
            keyArray -> {
                val size = itemArray.size
                while (i < size && i != itemArray.size) {
                    if (Intersector.overlaps(man.characterRectangle, itemArray[i].itemRectangle)) {
                        keyBool = true
                        itemArray.removeAt(i)
                    } else {
                        i++
                    }
                }
            }
            chestArray -> {
                val size = itemArray.size
                while (i < size && i != itemArray.size) {
                    if (Intersector.overlaps(man.characterRectangle, itemArray[i].itemRectangle) && keyBool) {
                        score += 5
                        keyBool = false
                        itemArray.removeAt(i)
                        break
                    } else {
                        i++
                    }
                }
            }
            bombArray -> {
                val size = itemArray.size
                while (i < size && i != itemArray.size) {
                    if (Intersector.overlaps(man.characterRectangle, bombArray[i].itemRectangle)) {
                        Gdx.app.log("coinbomb", "COLLISION BOMB")
                        timeElapsed = (System.currentTimeMillis() - startTime)
                        Gdx.app.log("time1", timeElapsed.toString())
                        Gdx.app.log("time2", TimeUnit.MILLISECONDS.toSeconds(timeElapsed).toString())
                        for (r in 1..3) {
                            if (preferences.getString("Score$r", "0").toInt() <= score) {
                                for (index in (r+1) until 4) {
                                    val past = index-1
                                    preferences.putString("Score$index", preferences.getString("Score$past"))
                                    preferences.putString("Jumps$index", preferences.getString("Jumps$past"))
                                    preferences.putLong("Time$index",preferences.getLong("Time$past"))
                                }

                                preferences.putString("Score$r", score.toString())
                                preferences.putString("Jumps$r", jumps.toString())
                                preferences.putLong("Time$r", timeElapsed)
                                preferences.flush()
                                break
                            }
                        }
                        gameState = 2
                        deathSound.play()
                        screen2 = true
                        break
                    }
                    i++
                }
            }
            coinArray -> {
                var size = itemArray.size
                while (i < size && i != itemArray.size) {
                    if (Intersector.overlaps(man.characterRectangle, coinArray[i].itemRectangle)) {
                        score++
                        coinArray.removeAt(i)
                        break
                    } else {
                        i++
                    }
                }
            }
        }
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
    }
}
