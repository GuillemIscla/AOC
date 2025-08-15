package main

func part1() string {
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
		var maxChar rune
		var maxCount int
		for char, count := range freq {
			if count > maxCount {
				maxCount = count
				maxChar = char
			}
		}
		result += string(maxChar)
	}

	return result
}
