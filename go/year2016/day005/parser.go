package main

import (
	"bufio"
	"os"
)

func parseInput(value *int) (string, error) {
	if value == nil {
		filename := "../../inputs/year2016/day005.txt"
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
		return "abc", nil
	}
	return "", nil
}
