package main

import (
	"bufio"
	"os"
	"strings"
)

func parseInput(value *int) ([]InputInstruction, []BotInstruction, error) {
	if value == nil {
		filename := "../../inputs/year2016/day010.txt"
		file, err := os.Open(filename)
		if err != nil {
			return []InputInstruction{}, []BotInstruction{}, err
		}
		defer file.Close()
		scanner := bufio.NewScanner(file)
		return ParseAll(scanner)
	} else if *value == 1 {
		scanner := bufio.NewScanner(strings.NewReader(`value 5 goes to bot 2
			bot 2 gives low to bot 1 and high to bot 0
			value 3 goes to bot 1
			bot 1 gives low to output 1 and high to bot 0
			bot 0 gives low to output 2 and high to output 0
			value 2 goes to bot 2`))
		return ParseAll(scanner)
	}

	return []InputInstruction{}, []BotInstruction{}, nil
}

func ParseAll(scanner *bufio.Scanner) ([]InputInstruction, []BotInstruction, error) {
	var InputInstructions []InputInstruction
	var BotInstructions []BotInstruction

	for scanner.Scan() {
		line := strings.TrimSpace(scanner.Text())
		if line == "" {
			continue
		}

		instr, err := ParseInstruction(line)
		if err != nil {
			return nil, nil, err
		}

		switch v := instr.(type) {
		case InputInstruction:
			InputInstructions = append(InputInstructions, v)
		case BotInstruction:
			BotInstructions = append(BotInstructions, v)
		}

	}

	if err := scanner.Err(); err != nil {
		return nil, nil, err
	}

	return InputInstructions, BotInstructions, nil
}
