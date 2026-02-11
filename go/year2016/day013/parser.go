package main

import (
	"bufio"
	"os"
	"strconv"
	"strings"
)

func parseInput(value *int) (int, error) {
	if value == nil {
		filename := "../../inputs/year2016/day013.txt"
		file, err1 := os.Open(filename)
		if err1 != nil {
			return -1, err1
		}
		defer file.Close()
		scanner := bufio.NewScanner(file)
		scanner.Scan()
		line := strings.TrimSpace(scanner.Text())
		myInt, err2 := strconv.Atoi(line)
		if err2 != nil {
			return -1, err2
		}
		return myInt, nil
	} else if *value == 1 {
		return 10, nil
	}

	return -1, nil
}
