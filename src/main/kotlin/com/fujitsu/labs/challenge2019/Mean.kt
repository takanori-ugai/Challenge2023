package com.fujitsu.labs.challenge2019

import com.mayabot.mynlp.fasttext.FastText

fun main(args: Array<String>) {
  val motivations = arrayOf("eradication", "strangulation", "sting",
    "stabbing", "beating", "poisoning", "bombing", "burning", "shooting")
  val motivation = Mean()
  for (mo in motivations) {
    motivation.relatedWords(mo)
  }
}

class Mean {
  val fastText = FastText.loadFasttextBinModel("dict/cc.en.300.bin")

  fun relatedWords(arg: String) {
    val neighbor = fastText.nearestNeighbor(arg, 300)
    print("\"$arg\"")
    for (i in neighbor) {
      print(",\"${i.second}\"")
    }
    println()
  }
}
