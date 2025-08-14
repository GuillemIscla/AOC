package main

import "strings"

func part1() string {
	instructions, _ := parseInput(nil)

	var code []string

	position := Position{X: 1, Y: 1}

	for _, instructionLine := range instructions {
		for _, ins := range instructionLine {
			position = position.Process(ins, keypad_1)
		}
		code = append(code, keypad_1[position])
	}

	return strings.Join(code, "")
}
