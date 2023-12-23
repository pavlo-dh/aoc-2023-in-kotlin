fun main() {
    fun part1(input: List<String>): Long {
        val seeds = numberRegex.findAll(input.first()).map { it.value.toLong() }

        val soilToFertilizerStart = input.indexOf("soil-to-fertilizer map:")
        val fertilizerToWaterStart = input.indexOf("fertilizer-to-water map:")
        val waterToLightStart = input.indexOf("water-to-light map:")
        val lightToTemperatureStart = input.indexOf("light-to-temperature map:")
        val temperatureToHumidityStart = input.indexOf("temperature-to-humidity map:")
        val humidityToLocationStart = input.indexOf("humidity-to-location map:")

        val seedToSoilMap = input.constructMap(3, soilToFertilizerStart - 2)
        val soilToFertilizerMap = input.constructMap(soilToFertilizerStart + 1, fertilizerToWaterStart - 2)
        val fertilizerToWaterMap = input.constructMap(fertilizerToWaterStart + 1, waterToLightStart - 2)
        val waterToLightMap = input.constructMap(waterToLightStart + 1, lightToTemperatureStart - 2)
        val lightToTemperatureMap = input.constructMap(lightToTemperatureStart + 1, temperatureToHumidityStart - 2)
        val temperatureToHumidityMap = input.constructMap(temperatureToHumidityStart + 1, humidityToLocationStart - 2)
        val humidityToLocationMap = input.constructMap(humidityToLocationStart + 1, input.lastIndex)

        return seeds.map { findInMap(seedToSoilMap, it) }.map { findInMap(soilToFertilizerMap, it) }
            .map { findInMap(fertilizerToWaterMap, it) }.map { findInMap(waterToLightMap, it) }
            .map { findInMap(lightToTemperatureMap, it) }.map { findInMap(temperatureToHumidityMap, it) }
            .map { findInMap(humidityToLocationMap, it) }.min()
    }

    fun part2(input: List<String>): Long {
        val seedRanges = numberRegex.findAll(input.first()).map { it.value.toLong() }
            .chunked(2) { (start, length) -> start..<start + length }

        val soilToFertilizerStart = input.indexOf("soil-to-fertilizer map:")
        val fertilizerToWaterStart = input.indexOf("fertilizer-to-water map:")
        val waterToLightStart = input.indexOf("water-to-light map:")
        val lightToTemperatureStart = input.indexOf("light-to-temperature map:")
        val temperatureToHumidityStart = input.indexOf("temperature-to-humidity map:")
        val humidityToLocationStart = input.indexOf("humidity-to-location map:")

        val seedToSoilMap = input.constructMap(3, soilToFertilizerStart - 2)
        val soilToFertilizerMap = input.constructMap(soilToFertilizerStart + 1, fertilizerToWaterStart - 2)
        val fertilizerToWaterMap = input.constructMap(fertilizerToWaterStart + 1, waterToLightStart - 2)
        val waterToLightMap = input.constructMap(waterToLightStart + 1, lightToTemperatureStart - 2)
        val lightToTemperatureMap = input.constructMap(lightToTemperatureStart + 1, temperatureToHumidityStart - 2)
        val temperatureToHumidityMap = input.constructMap(temperatureToHumidityStart + 1, humidityToLocationStart - 2)
        val humidityToLocationMap = input.constructMap(humidityToLocationStart + 1, input.lastIndex)

        return seedRanges.minOf { range ->
            range.minOf {
                findInMap(
                    humidityToLocationMap, findInMap(
                        temperatureToHumidityMap, findInMap(
                            lightToTemperatureMap, findInMap(
                                waterToLightMap, findInMap(
                                    fertilizerToWaterMap, findInMap(
                                        soilToFertilizerMap, findInMap(seedToSoilMap, it)
                                    )
                                )
                            )
                        )
                    )
                )
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

fun List<String>.constructMap(start: Int, end: Int): Map<LongRange, LongRange> {
    val map = mutableMapOf<LongRange, LongRange>()
    for (rowIndex in start..end) {
        val (destinationStart, sourceStart, length) = get(rowIndex).split(' ').map(String::toLong)
        val sourceRange = sourceStart..<sourceStart + length
        val destinationRange = destinationStart..<destinationStart + length
        map[sourceRange] = destinationRange
    }
    return map
}

fun findInMap(map: Map<LongRange, LongRange>, x: Long): Long {
    return map.firstNotNullOfOrNull { (sourceRange, destinationRange) ->
        if (x in sourceRange) destinationRange.first + (x - sourceRange.first) else null
    } ?: x
}
