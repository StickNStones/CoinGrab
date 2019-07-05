# CoinGrab

Based on Super Mario Run Clone JumpMan from https://www.udemy.com/the-complete-android-oreo-developer-course


Simple tap to jump, tilt screen to more in X-axis, avoid bombs or score is reset to 0

Collect coins for +1 score, collect key to open a chest for 5+ score



Main Changes
* Converted Java to Kotlin
* Refactored code. No longer single class, improved readability
* Added more interactive objects to game
  * Chest and key from https://jan-schneider.itch.io/chest-and-coins
  * Cloud from https://thedarkbear.itch.io/simple-clouds-set
* Added tilt controls for the X-axis
* Added score screen to show the users past top 3 scores persistently, and most recent score, with number of jumps and length of run mm:ss
* Added audio when user hits a bomb
