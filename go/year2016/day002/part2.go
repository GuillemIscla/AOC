package main

import "strings"

func part2() string {
	instructions, _ := parseInput(nil)

	var code []string

	position := Position{X: 0, Y: 2}

	for _, instructionLine := range instructions {
		for _, ins := range instructionLine {
			position = position.Process(ins, keypad_2)
		}
		code = append(code, keypad_2[position])
	}

	return strings.Join(code, "")
}
