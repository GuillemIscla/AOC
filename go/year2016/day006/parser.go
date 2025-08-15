package main

import (
	"bufio"
	"os"
)

func parseInput(value *int) ([]string, error) {
	if value == nil {
		filename := "../../inputs/year2016/day006.txt"
		file, err := os.Open(filename)
		if err != nil {
			return []string{}, err
		}
		defer file.Close()
		scanner := bufio.NewScanner(file)
		array := make([]string, 0)
		for scanner.Scan() {
			array = append(array, scanner.Text())
		}
		return array, nil
	} else if *value == 1 {
		return []string{
			"eedadn",
			"drvtee",
			"eandsr",
			"raavrd",
			"atevrs",
			"tsrnev",
			"sdttsa",
			"rasrtv",
			"nssdts",
			"ntnada",
			"svetve",
			"tesnvt",
			"vntsnd",
			"vrdear",
			"dvrsen",
			"enarar",
		}, nil
	}
	return []string{}, nil
}
