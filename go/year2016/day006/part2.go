package main

func part2() string {
	messages, _ := parseInput(nil)

	frequencies := make([]map[rune]int, len(messages[0]))
	for i := range frequencies {
		frequencies[i] = make(map[rune]int)
	}

	for _, message := range messages {
		for i, char := range message {
			frequencies[i][char]++
		}
	}

	var result string
	for _, freq := range frequencies {
		var minChar rune
		var minCount int = int(^uint(0) >> 1) // Max int value
		for char, count := range freq {
			if count < minCount {
				minCount = count
				minChar = char
			}
		}
		result += string(minChar)
	}

	return result
}
