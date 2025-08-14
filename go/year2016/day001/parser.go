package main

import (
	"bufio"
	"os"
	"strings"
)

func parseInput(value *int) ([]Instruction, error) {
	if value == nil {
		filename := "../../inputs/year2016/day001.txt"
		file, err := os.Open(filename)
		if err != nil {
			return nil, err
		}
		defer file.Close()

		var instructions []Instruction
		scanner := bufio.NewScanner(file)
		for scanner.Scan() {
			line := scanner.Text()
			tokens := strings.Split(line, ",")
			for _, token := range tokens {
				inst, err := NewInstruction(token)
				if err != nil {
					return nil, err
				}
				instructions = append(instructions, inst)
			}
		}

		if err := scanner.Err(); err != nil {
			return nil, err
		}

		return instructions, nil
	} else if *value == 1 {
		return ([]Instruction{{Direction: Right, Steps: 3}, {Direction: Left, Steps: 2}}), nil
	} else if *value == 2 {
		return ([]Instruction{{Direction: Right, Steps: 2}, {Direction: Right, Steps: 2}, {Direction: Right, Steps: 2}}), nil
	} else if *value == 3 {
		return ([]Instruction{{Direction: Right, Steps: 5}, {Direction: Left, Steps: 5}, {Direction: Right, Steps: 5}, {Direction: Right, Steps: 3}}), nil
	} else if *value == 4 {
		return ([]Instruction{{Direction: Right, Steps: 8}, {Direction: Right, Steps: 4}, {Direction: Right, Steps: 4}, {Direction: Right, Steps: 8}}), nil
	}
	return nil, nil
}
