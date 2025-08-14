package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func parseInput(value *int) ([]Triangle, error) {
	if value == nil {
		filename := "../../inputs/year2016/day003.txt"
		file, err := os.Open(filename)
		if err != nil {
			return nil, err
		}
		defer file.Close()
		var triangles []Triangle
		scanner := bufio.NewScanner(file)
		lineNumber := 0
		for scanner.Scan() {
			line := strings.TrimSpace(scanner.Text())
			parts := strings.Fields(line)

			triangle := Triangle{}
			for i := 0; i < 3; i++ {
				value, err := strconv.Atoi(parts[i])
				if err != nil {
					return nil, fmt.Errorf("invalid triangle side length at line %d: %q", lineNumber, parts[i])
				}
				switch i {
				case 0:
					triangle.A = value
				case 1:
					triangle.B = value
				case 2:
					triangle.C = value
				}
			}
			triangles = append(triangles, triangle)
			lineNumber++
		}
		return triangles, nil
	} else if *value == 1 {
		return []Triangle{{5, 10, 25}}, nil
	}
	return nil, nil
}
