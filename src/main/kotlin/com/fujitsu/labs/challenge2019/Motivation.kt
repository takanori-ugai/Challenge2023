package com.fujitsu.labs.challenge2019

import com.mayabot.mynlp.fasttext.FastText

fun main(args: Array<String>) {
    val motivations = arrayOf("rancor", "love", "drug", "self-defence", "domination", "belief", "greed", "money", "pleasure", "money", "pleasure")
    val motivation = Motivation()
    for (mo in motivations) {
        motivation.relatedWords(mo)
    }
}

class Motivation {
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
