package com.codeerow.sandbox

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        generateCurtainMatrix(1, listOf(5, 7, 9, 13, 3, 5, 9, 13), 13).mapIndexed { x, list ->
            list.mapIndexed { y, i ->
                //                "\\\"$i\\\":[$x, $y],"
                "\"$i\":[$x, $y],"
            }
        }.flatten().forEach { print(it) }

//        generateGrid (20, 10).mapIndexed { x, list ->
//            list.mapIndexed { y, i ->
//                "\"$i\":[$y, $x],"
//            }
//        }.flatten().forEach { print(it) }

    }

    fun generateGrid(width: Int, height: Int): List<List<Int>> {
        var address = 1
        val matrix = mutableListOf<List<Int>>()

        for (i in 0 until height) {
            val list = List(width) { address++ }
            val resList = if (i % 2 == 0) {
                list
            } else {
                list.reversed()
            }

            matrix.add(resList)
        }

        return matrix
    }

    fun generateCurtainMatrix(
        startAddress: Int,
        curtainList: List<Int>,
        height: Int
    ): List<List<Int?>> {
        val matrix = mutableListOf<List<Int?>>()

        var startIndex = startAddress
        curtainList.forEach { lineSize ->
            val line = createCurtainLine(startIndex, lineSize)
                .map { it }
                .toMutableList()

            while (line.size < height) {
                line.add(null)
            }

            matrix.add(line)
            startIndex += lineSize
        }

        return matrix.map { it.filter { it != null } }
    }


    private fun createCurtainLine(startAddress: Int, size: Int): List<Int?> {
        var start = startAddress
        var end = startAddress + size - 1

        val resList = mutableListOf<Int>()
        while (start <= end) {
            if (start == end) {
                resList.add(start)
            } else {
                resList.add(start)
                resList.add(end)
            }
            start++
            end--
        }

        return resList
    }
}
