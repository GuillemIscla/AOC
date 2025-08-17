package main

func part1() int {
	instructions, _ := parseInput(nil)

	var screen [50][6]bool

	for _, instruction := range instructions {
		instruction.Process(&screen)
	}

	pixelsOn := 0
	for _, row := range screen {
		for _, pixel := range row {
			if pixel {
				pixelsOn++
			}
		}
	}
	return pixelsOn
}
