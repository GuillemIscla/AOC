package main

import (
	"bufio"
	"os"
	"strings"
)

func parseInput(value *int) (string, error) {
	if value == nil {
		filename := "../../inputs/year2016/day014.txt"
		file, err1 := os.Open(filename)
		if err1 != nil {
			return "", err1
		}
		defer file.Close()
		scanner := bufio.NewScanner(file)
		scanner.Scan()
		line := strings.TrimSpace(scanner.Text())
		return line, nil
	} else if *value == 1 {
		return "abc", nil
	}

	return "", nil
}
