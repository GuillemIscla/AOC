package main

import (
	"bufio"
	"os"
)

func parseInput(value *int) (string, error) {
	if value == nil {
		filename := "../../inputs/year2016/day009.txt"
		file, err := os.Open(filename)
		if err != nil {
			return "", err
		}
		defer file.Close()
		scanner := bufio.NewScanner(file)
		for scanner.Scan() {
			return scanner.Text(), nil
		}
	} else if *value == 1 {
		return "ADVENT", nil
	} else if *value == 2 {
		return "A(1x5)BC", nil
	} else if *value == 3 {
		return "(3x3)XYZ", nil
	} else if *value == 4 {
		return "A(2x2)BCD(2x2)EFG", nil
	} else if *value == 5 {
		return "(6x1)(1x3)A", nil
	} else if *value == 6 {
		return "X(8x2)(3x3)ABCY", nil
	} else if *value == 7 {
		return "(27x12)(20x12)(13x14)(7x10)(1x12)A", nil
	} else if *value == 8 {
		return "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN", nil
	}
	return "", nil
}
