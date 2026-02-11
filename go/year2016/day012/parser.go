package main

import (
	"bufio"
	"os"
	"regexp"
	"strings"
)

func parseInput(value *int) (Program, error) {
	if value == nil {
		filename := "../../inputs/year2016/day012.txt"
		file, err := os.Open(filename)
		if err != nil {
			return Program{}, err
		}
		defer file.Close()
		scanner := bufio.NewScanner(file)
		return ParseAll(scanner)
	} else if *value == 1 {
		scanner := bufio.NewScanner(strings.NewReader(`cpy 41 a
inc a
inc a
dec a
jnz a 2
dec a`))
		return ParseAll(scanner)
	}

	return Program{}, nil
}

func ParseAll(scanner *bufio.Scanner) (Program, error) {
	var instructions []Instruction

	for scanner.Scan() {
		line := strings.TrimSpace(scanner.Text())
		if line == "" {
			continue
		}

		newInstruction := ParseInstruction(line)
		instructions = append(instructions, newInstruction)
	}

	return NewProgram(instructions), nil
}

func ParseInstruction(line string) Instruction {
	regex := regexp.MustCompile(`(cpy|inc|dec|jnz) (-?\d+|[a-z]) ?(-?\d+|[a-z]){0,1}`)
	match := regex.FindAllStringSubmatch(line, -1)[0]
	switch match[1] {
	case "cpy":
		return cpy{
			one: NewSlot(match[2]),
			two: match[3],
		}
	case "inc":
		return inc{
			one: match[2],
		}
	case "dec":
		return dec{
			one: match[2],
		}
	case "jnz":
		return jnz{
			one: NewSlot(match[2]),
			two: NewSlot(match[3]),
		}
	}
	return dec{one: "a"}
}
