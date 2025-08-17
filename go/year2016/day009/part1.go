package main

import "fmt"

func part1() int {
	compressed, _ := parseInput(nil)

	var compressedSlice []rune
	compressedSlice = []rune(compressed)
	var uncompressed string

	for {
		start, end, err := FindNextMarker(compressedSlice)
		if err != nil {
			uncompressed += string(compressedSlice[:])
			break
		}

		marker := compressedSlice[start+1 : end]
		var length, repeatCount int
		fmt.Sscanf(string(marker), "%dx%d", &length, &repeatCount)

		uncompressed += string(compressedSlice[:start])
		for i := 0; i < repeatCount; i++ {
			uncompressed += string(compressedSlice[end+1 : end+1+length])
		}
		compressedSlice = compressedSlice[end+1+length:]
	}

	return len(uncompressed)
}

func FindNextMarker(compressed []rune) (int, int, error) {
	start, end := -1, -1
	for i := 0; i < len(compressed); i++ {
		if compressed[i] == '(' {
			start = i
		} else if compressed[i] == ')' {
			end = i
			break
		}
	}
	if start == -1 || end == -1 {
		return -1, -1, fmt.Errorf("Next Marker not found")
	}
	return start, end, nil
}
