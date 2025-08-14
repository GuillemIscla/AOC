package main

import (
	"bufio"
	"fmt"
	"os"
)

func parseInput(value *int) ([][]Direction, error) {
	if value == nil {
		filename := "../../inputs/year2016/day002.txt"
		file, err := os.Open(filename)
		if err != nil {
			return nil, err
		}
		defer file.Close()
		var directions [][]Direction
		scanner := bufio.NewScanner(file)
		lineNumber := 0
		for scanner.Scan() {
			line := scanner.Text()
			var directionsLine []Direction
			for i, ch := range line {
				var dir Direction
				switch ch {
				case 'R':
					dir = Right
				case 'L':
					dir = Left
				case 'U':
					dir = Up
				case 'D':
					dir = Down
				default:
					return nil, fmt.Errorf("invalid direction at position %d in line %d: %q", i, lineNumber, ch)
				}
				directionsLine = append(directionsLine, dir)
			}
			directions = append(directions, directionsLine)
			lineNumber++
		}

		return directions, nil

	} else if *value == 1 {
		return ([][]Direction{[]Direction{Up, Left, Left}, []Direction{Right, Right, Down, Down}, []Direction{Left, Up, Right, Down, Left}, []Direction{Up, Up, Up, Up, Down}}), nil
	} else {
		return nil, nil
	}
}
