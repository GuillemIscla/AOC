package main

import (
	"bufio"
	"os"
)

func parseInput(value *int) ([]Instruction, error) {
	if value == nil {
		filename := "../../inputs/year2016/day008.txt"
		file, err := os.Open(filename)
		if err != nil {
			return []Instruction{}, err
		}
		defer file.Close()
		scanner := bufio.NewScanner(file)
		array := make([]Instruction, 0)
		for scanner.Scan() {
			line := scanner.Text()
			instruction, err := NewInstruction(line)
			if err == nil {
				array = append(array, instruction)
			} else {
				panic("Invalid instruction: " + line + " - " + err.Error())
			}
		}
		return array, nil
	} else if *value == 1 {
		instr1, _ := NewInstruction("rect 3x2")
		instr2, _ := NewInstruction("rotate column x=1 by 1")
		instr3, _ := NewInstruction("rotate row y=0 by 4")
		instr4, _ := NewInstruction("rotate column x=1 by 1")
		return []Instruction{
			instr1,
			instr2,
			instr3,
			instr4,
		}, nil
	}
	return []Instruction{}, nil
}
