package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle

class Character {
    var characterState = 0
    var characterY = Gdx.graphics.height / 2 - CoinGrab.character[characterState].height / 2
    var characterX = Gdx.graphics.width / 2 - CoinGrab.character[characterState].width / 2
    lateinit var characterRectangle: Rectangle

    fun update() {
        characterRectangle = Rectangle(characterX.toFloat() + 90, characterY.toFloat(), (CoinGrab.character[characterState].width * 0.60).toFloat(), (CoinGrab.character[characterState].height * 0.90).toFloat())

    }
}