package main

import (
	"bufio"
	"os"
	"regexp"
	"strings"
)

func parseInput(value *int) (State, error) {
	if value == nil {
		filename := "../../inputs/year2016/day011.txt"
		file, err := os.Open(filename)
		if err != nil {
			return State{}, err
		}
		defer file.Close()
		scanner := bufio.NewScanner(file)
		return ParseAll(scanner)
	} else if *value == 1 {
		scanner := bufio.NewScanner(strings.NewReader(`The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
The second floor contains a hydrogen generator.
The third floor contains a lithium generator.
The fourth floor contains nothing relevant.`))
		return ParseAll(scanner)
	}

	return State{}, nil
}

func ParseAll(scanner *bufio.Scanner) (State, error) {
	var cargos []Cargo

	offset := 0

	for scanner.Scan() {
		line := strings.TrimSpace(scanner.Text())
		if line == "" {
			continue
		}

		floor, innerRaw := ParseOuter(line)
		newCargos := ParseInner(floor, innerRaw, offset)
		offset += len(newCargos)
		cargos = append(cargos, newCargos...)
	}

	return NewState(1, cargos), nil
}

func ParseOuter(line string) (int, string) {
	outerRegex := regexp.MustCompile(`The (first|second|third|fourth) floor contains (.*).`)
	matches := outerRegex.FindAllStringSubmatch(line, -1)
	floorRaw, innerRaw := matches[0][1], matches[0][2]
	var floorMap = map[string]int{
		"first":  1,
		"second": 2,
		"third":  3,
		"fourth": 4,
	}
	return floorMap[floorRaw], innerRaw
}

func ParseInner(floor int, line string, offset int) []Cargo {
	innerRegex := regexp.MustCompile(`a ([a-z]+)( generator|-compatible microchip)`)
	var cargos []Cargo
	matches := innerRegex.FindAllStringSubmatch(line, -1)
	for _, m := range matches {
		cargos = append(cargos, NewCargo(floor, offset, m[1], m[2] == " generator"))
		offset++
	}
	return cargos
}
