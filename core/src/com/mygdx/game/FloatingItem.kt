package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils

class FloatingItem(item: String) {
    var locX: Int = Gdx.graphics.width
    var locY = MathUtils.random.nextFloat() * Gdx.graphics.height
    var texture = Texture(item)
    var itemRectangle = com.badlogic.gdx.math.Rectangle(locX.toFloat(), locY, texture.width.toFloat(), texture.height.toFloat())

    fun update() {
        itemRectangle = com.badlogic.gdx.math.Rectangle(locX.toFloat(), locY, texture.width.toFloat(), texture.height.toFloat())
    }

    fun update(modifierX: Float, modifierY: Float) {
        itemRectangle = com.badlogic.gdx.math.Rectangle(locX.toFloat(), locY, texture.width.toFloat()*modifierX, texture.height.toFloat()*modifierY)
    }

}